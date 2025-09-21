package br.com.igorfurtado.authcnab;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("transacoes")
public class TransacaoController {
    private final TransacaoService service;
    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @GetMapping()
    Iterable<Transacao> listAll(){
        return service.ListallTransacao();
    }
    
}
