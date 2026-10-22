
package br.edu.ifpe.bazar.repository;

import br.edu.ifpe.bazar.config.Conexao;
import br.edu.ifpe.bazar.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void criar(Produto produto) throws SQLException {

        String sql = """
            INSERT INTO produto (nome, descricao, lote_id)
            VALUES (?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, produto.getNome());
            comando.setString(2, produto.getDescricao());
            comando.setInt(3, produto.getLoteId());

            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    produto.setCodigo(chaves.getInt(1));
                }
            }
        }
    }

    public List<Produto> listarPorLote(int loteId)
            throws SQLException {

        String sql = """
            SELECT codigo, nome, descricao, lote_id
            FROM produto
            WHERE lote_id = ?
            ORDER BY codigo
            """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql)) {

            comando.setInt(1, loteId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Produto produto = new Produto();

                    produto.setCodigo(
                        resultado.getInt("codigo"));
                    produto.setNome(
                        resultado.getString("nome"));
                    produto.setDescricao(
                        resultado.getString("descricao"));
                    produto.setLoteId(
                        resultado.getInt("lote_id"));

                    produtos.add(produto);
                }
            }
        }

        return produtos;
    }
}
