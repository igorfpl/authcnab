CREATE TABLE IF NOT EXISTS transacao (
  id SERIAL primary key,
  tipo int,
  tipotran varchar(255),
  valor decimal,
  conta_origem varchar(255),
  conta_destino varchar(255),
  reservado varchar(255)
);