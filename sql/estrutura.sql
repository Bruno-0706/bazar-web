CREATE DATABASE IF NOT EXISTS bazar
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bazar;

CREATE TABLE IF NOT EXISTS orgao_fiscalizador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS orgao_donatario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(255),
    telefone VARCHAR(30),
    descricao VARCHAR(500),
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS lote (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_entrega DATE,
    observacao VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
    fiscalizador_id INT NOT NULL,
    donatario_id INT NULL,

    CONSTRAINT fk_lote_fiscalizador
        FOREIGN KEY (fiscalizador_id)
        REFERENCES orgao_fiscalizador(id),

    CONSTRAINT fk_lote_donatario
        FOREIGN KEY (donatario_id)
        REFERENCES orgao_donatario(id)
);

CREATE TABLE IF NOT EXISTS produto (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),
    lote_id INT NOT NULL,

    CONSTRAINT fk_produto_lote
        FOREIGN KEY (lote_id)
        REFERENCES lote(id)
);