CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL
);

CREATE TABLE categoria_bicicleta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    valor_por_hora NUMERIC(10,2) NOT NULL
);

CREATE TABLE bicicleta (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
    id_categoria BIGINT NOT NULL REFERENCES categoria_bicicleta(id),
    CHECK (status IN ('DISPONIVEL', 'ALUGADA', 'MANUTENCAO', 'INATIVA'))
);

CREATE TABLE aluguel (
    id BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES cliente(id),
    id_bicicleta BIGINT NOT NULL REFERENCES bicicleta(id),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    valor_hora_aplicado NUMERIC(10,2) NOT NULL,
    valor NUMERIC(10,2)
);

CREATE TABLE historico (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DEVOLUCAO', 'ALUGUEL', 'MANUTENCAO')),
    id_aluguel BIGINT NOT NULL REFERENCES aluguel (id),
    id_cliente BIGINT NOT NULL REFERENCES cliente (id),
    id_bicicleta BIGINT NOT NULL REFERENCES bicicleta (id),
    valor NUMERIC(10,2),
    data_hora TIMESTAMP NOT NULL
);
