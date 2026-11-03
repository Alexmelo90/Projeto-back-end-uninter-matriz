package com.distribuido.raiznordestematriz.catalogo;

import com.distribuido.raiznordestematriz.catalogo.dto.InputProdutoDTO;
import com.distribuido.raiznordestematriz.catalogo.model.ProdutoModel;
import com.distribuido.raiznordestematriz.comum.exception.RecursoNaoEncontradoException;
import com.distribuido.raiznordestematriz.mensageria.CatalogoPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class CatalogoService {

    private final ProdutoRepository repository;
    private final CatalogoPublisher publisher;

    public CatalogoService(ProdutoRepository repository, CatalogoPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public ProdutoModel criar(InputProdutoDTO inputDto) {
        return salvarEPublicar(new ProdutoModel(), inputDto);
    }

    @Transactional
    public ProdutoModel atualizar(Long id, InputProdutoDTO inputDto) {
        ProdutoModel produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        return salvarEPublicar(produto, inputDto);
    }

    public List<ProdutoModel> listar() {
        return repository.findAll();
    }

    private ProdutoModel salvarEPublicar(ProdutoModel produto, InputProdutoDTO inputDto) {
        produto.setNome(inputDto.nome());
        produto.setCategoria(inputDto.categoria());
        produto.setSazonal(inputDto.sazonal());
        produto.setJanelaDisponibilidade(inputDto.janelaDisponibilidade());
        produto.setPrecoBase(inputDto.precoBase());
        produto.setCamposTravados(inputDto.camposTravados() == null
                ? new HashSet<>() : new HashSet<>(inputDto.camposTravados()));
        ProdutoModel salvo = repository.save(produto);
        publisher.publicarProduto(salvo);
        return salvo;
    }
}
