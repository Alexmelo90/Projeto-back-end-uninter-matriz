package com.distribuido.raiznordestematriz.cliente;

import com.distribuido.raiznordestematriz.cliente.model.ClienteModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria o registro mínimo (só uuid) se ainda não existir. Em corrida, a unique constraint
     * em uuid barra a segunda inserção e a exceção cai no caminho de idempotência do consumidor.
     */
    public ClienteModel buscarOuCriarPorUuid(UUID uuid) {
        return repository.findByUuid(uuid).orElseGet(() -> {
            ClienteModel novo = new ClienteModel();
            novo.setUuid(uuid);
            return repository.save(novo);
        });
    }
}
