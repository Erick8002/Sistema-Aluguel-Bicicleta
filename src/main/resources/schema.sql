CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(15) NOT NULL
);

CREATE TABLE bicicleta (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL'
        CHECK (status IN ('DISPONIVEL', 'ALUGADA', 'MANUTENCAO', 'INATIVA'))
);

CREATE TABLE aluguel (
    id BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES cliente (id),
    id_bicicleta BIGINT NOT NULL REFERENCES bicicleta (id),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    valor DOUBLE PRECISION
);

CREATE TABLE historico (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    id_aluguel BIGINT NOT NULL REFERENCES aluguel (id),
    id_cliente BIGINT NOT NULL REFERENCES cliente (id),
    id_bicicleta BIGINT NOT NULL REFERENCES bicicleta (id),
    valor NUMERIC,
    data_hora TIMESTAMP NOT NULL
);
