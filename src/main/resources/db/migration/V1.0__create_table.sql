CREATE TABLE IF NOT EXISTS conta (
    id SERIAL PRIMARY KEY,
    data_vencimento DATE,
    data_pagamento DATE,
    valor NUMERIC(5,1),
    descricao VARCHAR(255) NOT NULL,
    situacao VARCHAR(255) NULL
);