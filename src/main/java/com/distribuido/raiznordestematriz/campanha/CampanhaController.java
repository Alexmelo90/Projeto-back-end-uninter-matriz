package com.distribuido.raiznordestematriz.campanha;

import com.distribuido.raiznordestematriz.campanha.dto.InputCampanhaDTO;
import com.distribuido.raiznordestematriz.campanha.dto.InputCampanhaRegiaoDTO;
import com.distribuido.raiznordestematriz.campanha.model.CampanhaModel;
import com.distribuido.raiznordestematriz.campanha.model.CampanhaRegiaoModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campanhas")
public class CampanhaController {

    private final CampanhaService service;

    public CampanhaController(CampanhaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampanhaModel criar(@Valid @RequestBody InputCampanhaDTO inputDto) {
        return service.criar(inputDto);
    }

    @PostMapping("/{id}/regioes")
    @ResponseStatus(HttpStatus.CREATED)
    public CampanhaRegiaoModel vincularRegiao(@PathVariable Long id,
                                              @Valid @RequestBody InputCampanhaRegiaoDTO inputDto) {
        return service.vincularRegiao(id, inputDto);
    }
}
