package br.com.igorfurtado.authcnab;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TransacaoService {
    private final TransacaoRepository repository;
    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public Iterable<Transacao> ListallTransacao(){
        var transacoes = repository.findAll();
        return transacoes;
    }
}

