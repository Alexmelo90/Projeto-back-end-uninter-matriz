package com.distribuido.raiznordestematriz.consolidacao;

import com.distribuido.raiznordestematriz.cliente.ClienteService;
import com.distribuido.raiznordestematriz.consolidacao.dto.ResumoVendasResponseDTO;
import com.distribuido.raiznordestematriz.consolidacao.model.Canal;
import com.distribuido.raiznordestematriz.consolidacao.model.ItemVendaConsolidadaModel;
import com.distribuido.raiznordestematriz.consolidacao.model.VendaConsolidadaModel;
import com.distribuido.raiznordestematriz.fidelidade.FidelidadeService;
import com.distribuido.raiznordestematriz.mensageria.evento.VendaEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ConsolidacaoService {

    private static final Logger log = LoggerFactory.getLogger(ConsolidacaoService.class);
    //inicio do ano atual
    private static final LocalDateTime INICIO = LocalDate.now().withDayOfYear(1).atStartOfDay();
    //fim do ano atual
    private static final LocalDateTime FIM = LocalDate.now().with(java.time.temporal.TemporalAdjusters.lastDayOfYear()).atTime(java.time.LocalTime.MAX);

    private final VendaConsolidadaRepository vendaRepository;
    private final ItemVendaConsolidadaRepository itemRepository;
    private final ClienteService clienteService;
    private final FidelidadeService fidelidadeService;

    public ConsolidacaoService(VendaConsolidadaRepository vendaRepository,
                               ItemVendaConsolidadaRepository itemRepository,
                               ClienteService clienteService, FidelidadeService fidelidadeService) {
        this.vendaRepository = vendaRepository;
        this.itemRepository = itemRepository;
        this.clienteService = clienteService;
        this.fidelidadeService = fidelidadeService;
    }

    @Transactional
    public void consolidar(VendaEvento evento) {
        VendaConsolidadaModel venda = new VendaConsolidadaModel();
        venda.setPedidoOrigemId(evento.pedidoOrigemId());
        venda.setUnidadeId(evento.unidadeId());
        venda.setClienteId(evento.clienteUuid() == null ? null
                : clienteService.buscarOuCriarPorUuid(evento.clienteUuid()).getId());
        venda.setTipoOrigem(Canal.valueOf(evento.tipoOrigem()));
        venda.setTotal(evento.total());
        venda.setData(evento.data());
        VendaConsolidadaModel salva = vendaRepository.save(venda);   // unique constraint barra a duplicata

        for (VendaEvento.Item itemAtual : evento.itens()) {
            ItemVendaConsolidadaModel item = new ItemVendaConsolidadaModel();
            item.setVendaId(salva.getId());
            item.setProdutoId(itemAtual.produtoId());
            item.setQtd(itemAtual.qtd());
            item.setValor(itemAtual.valor());
            itemRepository.save(item);
        }

        fidelidadeService.creditarPontos(salva);   // na MESMA transação
    }

    public List<VendaConsolidadaModel> listarVendas(LocalDateTime de, LocalDateTime ate, Long unidadeId) {
        LocalDateTime inicio = de == null ? INICIO : de;
        LocalDateTime fim = ate == null ? FIM : ate;
        return unidadeId == null
                ? vendaRepository.findByDataBetween(inicio, fim)
                : vendaRepository.findByUnidadeIdAndDataBetween(unidadeId, inicio, fim);
    }

    public ResumoVendasResponseDTO resumo(LocalDateTime de, LocalDateTime ate, Long unidadeId) {
        List<VendaConsolidadaModel> vendas = listarVendas(de, ate, unidadeId);

        BigDecimal valorTotal = vendas.stream().map(VendaConsolidadaModel::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ticketMedio = vendas.isEmpty() ? BigDecimal.ZERO
                : valorTotal.divide(BigDecimal.valueOf(vendas.size()), 2, RoundingMode.HALF_UP);

        long itens = vendas.isEmpty() ? 0 : itemRepository
                .findByVendaIdIn(vendas.stream().map(VendaConsolidadaModel::getId).toList())
                .stream().mapToLong(ItemVendaConsolidadaModel::getQtd).sum();

        Map<String, Long> porCanal = vendas.stream().collect(Collectors.groupingBy(
                v -> v.getTipoOrigem().name(), TreeMap::new, Collectors.counting()));

        return new ResumoVendasResponseDTO(de, ate, unidadeId, vendas.size(), itens, valorTotal,
                ticketMedio, porCanal);
    }

    /** A ingestão é contínua; o relatório gerencial fecha a cada ~2h. */
    @Scheduled(fixedRate = 7_200_000)
    public void fecharRelatorio() {
        LocalDateTime agora = LocalDateTime.now();

        ResumoVendasResponseDTO r = resumo(agora.minusHours(2), agora, null);

        log.info("Relatório das últimas 2h: {} vendas, total {}, ticket médio {}",
                r.quantidadeVendas(), r.valorTotal(), r.ticketMedio());
    }
}
