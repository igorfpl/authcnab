package br.com.igorfurtado.authcnab;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Transacao(
    @Id Long id,
    Integer tipo,
    @Column("TIPOTRAN") String tipotran,
    BigDecimal valor,
    @Column("CONTA_ORIGEM") String contaorigem,
    @Column("CONTA_DESTINO") String contadestino,
    @Column("RESERVADO") String reservado){

  // Wither Pattern
  public Transacao withValor(BigDecimal valor) {
  return new Transacao(
    id, tipo, tipotran, valor, contaorigem, contadestino, reservado );
  }
}