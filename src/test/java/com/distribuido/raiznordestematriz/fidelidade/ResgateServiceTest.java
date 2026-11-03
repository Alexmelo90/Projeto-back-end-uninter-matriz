package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.cliente.ClienteRepository;
import com.distribuido.raiznordestematriz.cliente.model.ClienteModel;
import com.distribuido.raiznordestematriz.fidelidade.dto.InputResgateDTO;
import com.distribuido.raiznordestematriz.fidelidade.dto.ResgateResponseDTO;
import com.distribuido.raiznordestematriz.fidelidade.model.ContaFidelidadeModel;
import com.distribuido.raiznordestematriz.fidelidade.model.MovimentoPontosModel;
import com.distribuido.raiznordestematriz.fidelidade.model.TipoMovimento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResgateServiceTest {

    private final ClienteRepository clienteRepository = mock(ClienteRepository.class);
    private final ContaFidelidadeRepository contaRepository = mock(ContaFidelidadeRepository.class);
    private final MovimentoPontosRepository movimentoRepository = mock(MovimentoPontosRepository.class);
    private final FidelidadeService service = new FidelidadeService(clienteRepository, contaRepository, movimentoRepository);

    private final UUID clienteUuid = UUID.randomUUID();
    private ContaFidelidadeModel conta;

    @BeforeEach
    void setUp() {
        ClienteModel cliente = mock(ClienteModel.class);
        when(cliente.getId()).thenReturn(7L);
        when(clienteRepository.findByUuid(clienteUuid)).thenReturn(Optional.of(cliente));
        conta = new ContaFidelidadeModel(3L, 7L, 100);
        when(contaRepository.buscarParaAtualizar(7L)).thenReturn(Optional.of(conta));
        when(movimentoRepository.findByResgateId(any())).thenReturn(Optional.empty());
        when(movimentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void debitaOSaldoEGravaMovimentoDebito() {
        UUID resgateId = UUID.randomUUID();
        ResgateResponseDTO resposta = service.resgatar(clienteUuid, new InputResgateDTO(resgateId, 40, "Cuscuz grátis"));

        assertEquals(60, conta.getSaldoPontos());
        assertEquals(60, resposta.saldoPontos());
        assertEquals(40, resposta.pontosResgatados());
        ArgumentCaptor<MovimentoPontosModel> captor = ArgumentCaptor.forClass(MovimentoPontosModel.class);
        verify(movimentoRepository).save(captor.capture());
        assertEquals(TipoMovimento.DEBITO, captor.getValue().getTipo());
        assertEquals(resgateId, captor.getValue().getResgateId());
        assertNull(captor.getValue().getVendaId());
    }

    @Test
    void recusaQuandoSaldoNaoCobre() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.resgatar(clienteUuid, new InputResgateDTO(UUID.randomUUID(), 101, null)));
        assertTrue(e.getMessage().contains("Saldo insuficiente"));
        assertEquals(100, conta.getSaldoPontos());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void repeticaoComMesmoResgateIdNaoDebitaDeNovo() {
        UUID resgateId = UUID.randomUUID();
        conta.setSaldoPontos(60);
        when(movimentoRepository.findByResgateId(resgateId)).thenReturn(Optional.of(new MovimentoPontosModel(
                9L, 3L, null, 40, TipoMovimento.DEBITO, LocalDateTime.now(), resgateId, null)));

        ResgateResponseDTO resposta = service.resgatar(clienteUuid, new InputResgateDTO(resgateId, 40, null));

        assertEquals(60, resposta.saldoPontos());
        assertEquals(60, conta.getSaldoPontos());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void recusaResgateIdReaproveitadoComOutroValor() {
        UUID resgateId = UUID.randomUUID();
        when(movimentoRepository.findByResgateId(resgateId)).thenReturn(Optional.of(new MovimentoPontosModel(
                9L, 3L, null, 40, TipoMovimento.DEBITO, LocalDateTime.now(), resgateId, null)));

        assertThrows(IllegalArgumentException.class,
                () -> service.resgatar(clienteUuid, new InputResgateDTO(resgateId, 50, null)));
    }

    @Test
    void clienteSemContaRecebeSaldoInsuficiente() {
        when(clienteRepository.findByUuid(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.resgatar(UUID.randomUUID(), new InputResgateDTO(UUID.randomUUID(), 1, null)));
    }
}
