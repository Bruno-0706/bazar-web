# Sistema Bazar

Sistema web para gerenciamento de lotes de produtos destinados à doação, desenvolvido como projeto acadêmico.

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Thymeleaf
- JDBC com SQL puro
- MySQL
- Maven
- BCrypt para hash de senhas

O projeto utiliza JDBC diretamente, sem JPA e sem `JdbcTemplate`.

## Funcionalidades

### Fiscalizador
- Login e logout.
- Cadastro e consulta dos próprios lotes.
- Cadastro de produtos em lotes disponíveis.
- Visualização dos produtos de cada lote.
- Doação de lotes a instituições donatárias.
- Restrição de acesso aos lotes de outros fiscalizadores.

### Donatário
- Login e logout.
- Consulta dos lotes recebidos pela instituição.
- Visualização dos produtos de cada lote recebido.
- Restrição de acesso a lotes destinados a outras instituições.

## Pré-requisitos

- JDK 17.
- MySQL Server.
- Eclipse IDE com suporte a Maven.
- Acesso à internet na primeira resolução das dependências Maven.

## Banco de dados

O banco utilizado pela aplicação chama-se `bazar`. Ele contém as tabelas:

- `orgao_fiscalizador`
- `orgao_donatario`
- `lote`
- `produto`

A estrutura do banco e os dados de demonstração devem ser preparados pelo script SQL disponibilizado no repositório.

## Configuração da conexão

A aplicação utiliza três variáveis de ambiente:

| Variável | Valor de exemplo |
|---|---|
| `BAZAR_DB_URL` | `jdbc:mysql://localhost:3306/bazar` |
| `BAZAR_DB_USUARIO` | `root` |
| `BAZAR_DB_SENHA` | Senha local do MySQL |

Configure as variáveis no ambiente de execução do Eclipse. Utilize as credenciais da sua instalação e não publique senhas reais.

## Como executar

1. Clone o repositório ou importe o projeto Maven no Eclipse.
2. Inicie o servidor MySQL.
3. Execute o script SQL para criar e preparar o banco de dados.
4. Configure as três variáveis de ambiente da conexão no Eclipse.
5. Aguarde a resolução das dependências Maven.
6. Execute a classe principal da aplicação Spring Boot.
7. Acesse `http://localhost:8080/login`, caso a aplicação esteja utilizando a porta padrão.

## Segurança e autorização

As senhas são verificadas utilizando BCrypt. A aplicação mantém a sessão do usuário autenticado e diferencia as permissões dos perfis fiscalizador e donatário.

## Testes realizados

Foram testados login válido, rejeição de senha incorreta, logout, isolamento dos lotes por fiscalizador, restrição de cadastro de produtos, doação de lotes, bloqueio de doações repetidas e isolamento dos lotes e produtos por donatário.

## Demonstração

O link do vídeo demonstrativo será incluído após sua publicação.

## Autoria

Projeto acadêmico. Preencha com os nomes dos integrantes e as informações solicitadas pela disciplina.