package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.cliente.ClienteRepository;
import com.distribuido.raiznordestematriz.cliente.model.ClienteModel;
import com.distribuido.raiznordestematriz.consolidacao.model.VendaConsolidadaModel;
import com.distribuido.raiznordestematriz.fidelidade.dto.InputResgateDTO;
import com.distribuido.raiznordestematriz.fidelidade.dto.ResgateResponseDTO;
import com.distribuido.raiznordestematriz.fidelidade.dto.SaldoResponseDTO;
import com.distribuido.raiznordestematriz.fidelidade.model.ContaFidelidadeModel;
import com.distribuido.raiznordestematriz.fidelidade.model.MovimentoPontosModel;
import com.distribuido.raiznordestematriz.fidelidade.model.TipoMovimento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FidelidadeService {

    private final ClienteRepository clienteRepository;
    private final ContaFidelidadeRepository contaRepository;
    private final MovimentoPontosRepository movimentoRepository;

    public FidelidadeService(ClienteRepository clienteRepository, ContaFidelidadeRepository contaRepository,
                             MovimentoPontosRepository movimentoRepository) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
        this.movimentoRepository = movimentoRepository;
    }

    public static int calcularPontos(BigDecimal total) {
        if (total == null || total.signum() <= 0) {
            return 0;
        }
        return total.setScale(0, RoundingMode.DOWN).intValue();
    }

    /** Deve ser chamado dentro da transação da consolidação. Venda anônima não pontua. */
    @Transactional
    public void creditarPontos(VendaConsolidadaModel venda) {
        if (venda.getClienteId() == null) {
            return;
        }

        int pontos = calcularPontos(venda.getTotal());

        if (pontos == 0) {return;}

        ContaFidelidadeModel conta = contaRepository.buscarParaAtualizar(venda.getClienteId())
                .orElseGet(() -> contaRepository.save(new ContaFidelidadeModel(null, venda.getClienteId(), 0)));

        conta.setSaldoPontos(conta.getSaldoPontos() + pontos);

        contaRepository.save(conta);

        movimentoRepository.save(new MovimentoPontosModel(null, conta.getId(), venda.getId(), pontos,
                TipoMovimento.CREDITO, LocalDateTime.now(), null, null));
    }

    /**
     * Debita pontos da conta. Idempotente por {@code resgateId}: a repetição devolve o resgate já feito.
     * A conta fica travada (FOR UPDATE) durante a operação, então dois resgates simultâneos não deixam o saldo negativo.
     */
    @Transactional
    public ResgateResponseDTO resgatar(UUID clienteUuid, InputResgateDTO inputDto) {
        ClienteModel cliente = clienteRepository.findByUuid(clienteUuid)
                .orElseThrow(() -> new IllegalArgumentException("Saldo insuficiente: o cliente não tem pontos"));
        ContaFidelidadeModel conta = contaRepository.buscarParaAtualizar(cliente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Saldo insuficiente: o cliente não tem pontos"));

        // Conferido depois do lock: uma repetição concorrente espera o primeiro resgate terminar e o encontra aqui.
        MovimentoPontosModel existente = movimentoRepository.findByResgateId(inputDto.resgateId()).orElse(null);
        if (existente != null) {
            if (!existente.getContaId().equals(conta.getId()) || existente.getPontos() != inputDto.pontos()) {
                throw new IllegalArgumentException("resgateId " + inputDto.resgateId() + " já foi usado em outro resgate");
            }
            return ResgateResponseDTO.de(existente, clienteUuid, conta.getSaldoPontos());
        }

        if (conta.getSaldoPontos() < inputDto.pontos()) {
            throw new IllegalArgumentException("Saldo insuficiente: " + conta.getSaldoPontos()
                    + " pontos disponíveis, " + inputDto.pontos() + " pedidos");
        }

        conta.setSaldoPontos(conta.getSaldoPontos() - inputDto.pontos());
        contaRepository.save(conta);

        MovimentoPontosModel debito = movimentoRepository.save(new MovimentoPontosModel(null, conta.getId(), null,
                inputDto.pontos(), TipoMovimento.DEBITO, LocalDateTime.now(), inputDto.resgateId(),
                inputDto.descricao()));
        return ResgateResponseDTO.de(debito, clienteUuid, conta.getSaldoPontos());
    }

    /** Cliente ou conta inexistente devolve saldo 0 (o nó trata qualquer falha como matriz indisponível). */
    public SaldoResponseDTO saldo(UUID clienteUuid) {
        ClienteModel cliente = clienteRepository.findByUuid(clienteUuid).orElse(null);
        if (cliente == null) {
            return new SaldoResponseDTO(clienteUuid, 0);
        }
        ContaFidelidadeModel conta = contaRepository.findByClienteId(cliente.getId()).orElse(null);
        int saldo = conta == null ? 0 : conta.getSaldoPontos();
        return new SaldoResponseDTO(clienteUuid, saldo);
    }
}
