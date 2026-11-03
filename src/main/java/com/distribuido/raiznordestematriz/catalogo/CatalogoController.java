package com.distribuido.raiznordestematriz.catalogo;

import com.distribuido.raiznordestematriz.catalogo.dto.InputProdutoDTO;
import com.distribuido.raiznordestematriz.catalogo.dto.ProdutoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class CatalogoController {

    private final CatalogoService service;

    public CatalogoController(CatalogoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(@Valid @RequestBody InputProdutoDTO inputDto) {
        return ProdutoResponseDTO.de(service.criar(inputDto));
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody InputProdutoDTO inputDto) {
        return ProdutoResponseDTO.de(service.atualizar(id, inputDto));
    }

    @GetMapping
    public List<ProdutoResponseDTO> listar() {
        return service.listar().stream().map(ProdutoResponseDTO::de).toList();
    }
}
