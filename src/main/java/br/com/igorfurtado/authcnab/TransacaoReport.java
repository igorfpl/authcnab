package br.com.igorfurtado.authcnab;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(
    String tipotran,
    BigDecimal totaltransacao,

    List<Transacao> transacoes
) {
}
