package com.distribuido.raiznordestematriz.campanha;

import com.distribuido.raiznordestematriz.campanha.dto.InputCampanhaDTO;
import com.distribuido.raiznordestematriz.campanha.dto.InputCampanhaRegiaoDTO;
import com.distribuido.raiznordestematriz.campanha.model.CampanhaModel;
import com.distribuido.raiznordestematriz.campanha.model.CampanhaRegiaoModel;
import com.distribuido.raiznordestematriz.comum.exception.RecursoNaoEncontradoException;
import com.distribuido.raiznordestematriz.regiao.RegiaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Só persiste: o CatalogoEvento ainda não tem tipo CAMPANHA (ver guia, seção 11.4). */
@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final CampanhaRegiaoRepository campanhaRegiaoRepository;
    private final RegiaoRepository regiaoRepository;

    public CampanhaService(CampanhaRepository campanhaRepository,
                           CampanhaRegiaoRepository campanhaRegiaoRepository,
                           RegiaoRepository regiaoRepository) {
        this.campanhaRepository = campanhaRepository;
        this.campanhaRegiaoRepository = campanhaRegiaoRepository;
        this.regiaoRepository = regiaoRepository;
    }

    @Transactional
    public CampanhaModel criar(InputCampanhaDTO inputDto) {
        return campanhaRepository.save(new CampanhaModel(null, inputDto.nome(), inputDto.regra(),
                inputDto.desconto(), inputDto.periodo(), inputDto.publicoAlvo()));
    }

    @Transactional
    public CampanhaRegiaoModel vincularRegiao(Long campanhaId, InputCampanhaRegiaoDTO inputDto) {
        if (!campanhaRepository.existsById(campanhaId)) {
            throw new RecursoNaoEncontradoException("Campanha não encontrada: " + campanhaId);
        }
        if (!regiaoRepository.existsById(inputDto.regiaoId())) {
            throw new RecursoNaoEncontradoException("Região não encontrada: " + inputDto.regiaoId());
        }
        if (inputDto.dataInicio() != null && inputDto.dataFim() != null
                && inputDto.dataFim().isBefore(inputDto.dataInicio())) {
            throw new IllegalArgumentException("dataFim não pode ser anterior a dataInicio");
        }
        return campanhaRegiaoRepository.save(new CampanhaRegiaoModel(null, campanhaId, inputDto.regiaoId(),
                inputDto.ativa(), inputDto.dataInicio(), inputDto.dataFim()));
    }
}
