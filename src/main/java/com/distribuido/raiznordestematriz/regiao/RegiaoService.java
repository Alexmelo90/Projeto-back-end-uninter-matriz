package com.distribuido.raiznordestematriz.regiao;

import com.distribuido.raiznordestematriz.comum.exception.RecursoNaoEncontradoException;
import com.distribuido.raiznordestematriz.mensageria.CatalogoPublisher;
import com.distribuido.raiznordestematriz.regiao.dto.InputRegiaoDTO;
import com.distribuido.raiznordestematriz.regiao.dto.InputUnidadeDTO;
import com.distribuido.raiznordestematriz.regiao.model.RegiaoModel;
import com.distribuido.raiznordestematriz.regiao.model.UnidadeModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final CatalogoPublisher publisher;

    public RegiaoService(RegiaoRepository regiaoRepository, UnidadeRepository unidadeRepository,
                         CatalogoPublisher publisher) {
        this.regiaoRepository = regiaoRepository;
        this.unidadeRepository = unidadeRepository;
        this.publisher = publisher;
    }

    @Transactional
    public RegiaoModel criarRegiao(InputRegiaoDTO inputDto) {
        return regiaoRepository.save(new RegiaoModel(null, inputDto.nome()));
    }

    public List<RegiaoModel> listarRegioes() {
        return regiaoRepository.findAll();
    }

    public RegiaoModel buscarRegiao(Long id) {
        return regiaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Região não encontrada: " + id));
    }

    @Transactional
    public UnidadeModel criarUnidade(Long regiaoId, InputUnidadeDTO inputDto) {
        RegiaoModel regiao = buscarRegiao(regiaoId);
        UnidadeModel unidade = unidadeRepository.save(
                new UnidadeModel(null, regiaoId, inputDto.nome(), inputDto.tipoCozinha()));
        publisher.publicarUnidade(unidade, regiao);
        return unidade;
    }

    public List<UnidadeModel> listarUnidades() {
        return unidadeRepository.findAll();
    }
}
