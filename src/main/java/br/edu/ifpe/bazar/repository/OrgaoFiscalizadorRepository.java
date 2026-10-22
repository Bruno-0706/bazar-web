
package br.edu.ifpe.bazar.repository;

import br.edu.ifpe.bazar.config.Conexao;
import br.edu.ifpe.bazar.model.OrgaoFiscalizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrgaoFiscalizadorRepository {

    public void criar(OrgaoFiscalizador orgao)
            throws SQLException {

        String sql = """
            INSERT INTO orgao_fiscalizador
                (nome, descricao, email, senha)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, orgao.getNome());
            comando.setString(2, orgao.getDescricao());
            comando.setString(3, orgao.getEmail());
            comando.setString(4, orgao.getSenha());

            comando.executeUpdate();

            try (ResultSet chaves =
                     comando.getGeneratedKeys()) {

                if (chaves.next()) {
                    orgao.setId(chaves.getInt(1));
                }
            }
        }
    }

    public List<OrgaoFiscalizador> listarTodos()
            throws SQLException {

        List<OrgaoFiscalizador> orgaos =
            new ArrayList<>();

        String sql = """
            SELECT id, nome, descricao, email, senha
            FROM orgao_fiscalizador
            ORDER BY id
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql);
             ResultSet resultado =
                 comando.executeQuery()) {

            while (resultado.next()) {
                OrgaoFiscalizador orgao =
                    new OrgaoFiscalizador();

                orgao.setId(resultado.getInt("id"));
                orgao.setNome(resultado.getString("nome"));
                orgao.setDescricao(
                    resultado.getString("descricao"));
                orgao.setEmail(resultado.getString("email"));
                orgao.setSenha(resultado.getString("senha"));

                orgaos.add(orgao);
            }
        }

        return orgaos;
    }
    
    public OrgaoFiscalizador buscarPorEmail(String email)
    		throws SQLException {
    	
    	String sql = """
    		SELECT id, nome, descricao, email, senha
    		FROM orgao_fiscalizador
    		WHERE email = ?
    		""";
    	
    	try (Connection conexao = Conexao.conectar();
    			PreparedStatement comando = conexao.prepareStatement(sql)) {
    				
    				comando.setString(1, email);
    				
    				try (ResultSet resultado = comando.executeQuery()) {
    					if (resultado.next()) {
    						OrgaoFiscalizador orgao = new OrgaoFiscalizador();
    						
    						orgao.setId(resultado.getInt("id"));
    						orgao.setNome(resultado.getString("nome"));
    		                orgao.setDescricao(
    		                    resultado.getString("descricao"));
    		                orgao.setEmail(resultado.getString("email"));
    		                orgao.setSenha(resultado.getString("senha"));

    		                return orgao;
    					}
    				}
    			}
    			
    			return null;    			
    }
}