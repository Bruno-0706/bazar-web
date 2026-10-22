
package br.edu.ifpe.bazar.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = obterConfiguracao(
            "BAZAR_DB_URL",
            "jdbc:mysql://localhost:3306/bazar"
    );

    private static final String USUARIO = obterConfiguracao(
            "BAZAR_DB_USUARIO",
            "root"
    );

    private static final String SENHA = obterConfiguracao(
            "BAZAR_DB_SENHA",
            null
    );

    private static String obterConfiguracao(
            String nome,
            String valorPadrao) {

        String valor = System.getenv(nome);

        if (valor == null || valor.isBlank()) {
            valor = valorPadrao;
        }

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "A variável de ambiente " + nome
                    + " não foi configurada."
            );
        }

        return valor;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
