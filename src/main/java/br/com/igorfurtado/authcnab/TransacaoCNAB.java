package br.com.igorfurtado.authcnab;

import java.math.BigDecimal;

public record TransacaoCNAB(
    Integer tipo,
    String tipotran,
    String valor,
    String contaorigem,
    String contadestino,
    String reservado) {

}
