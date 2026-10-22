
package br.edu.ifpe.bazar.repository;

import br.edu.ifpe.bazar.config.Conexao;
import br.edu.ifpe.bazar.model.OrgaoDonatario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrgaoDonatarioRepository {

    public void criar(OrgaoDonatario orgao)
            throws SQLException {

        String sql = """
            INSERT INTO orgao_donatario
                (nome, endereco, telefone, descricao, email, senha)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, orgao.getNome());
            comando.setString(2, orgao.getEndereco());
            comando.setString(3, orgao.getTelefone());
            comando.setString(4, orgao.getDescricao());
            comando.setString(5, orgao.getEmail());
            comando.setString(6, orgao.getSenha());

            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    orgao.setId(chaves.getInt(1));
                }
            }
        }
    }

    public List<OrgaoDonatario> listarTodos()
            throws SQLException {

        List<OrgaoDonatario> orgaos = new ArrayList<>();

        String sql = """
            SELECT id, nome, endereco, telefone,
                   descricao, email, senha
            FROM orgao_donatario
            ORDER BY id
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                OrgaoDonatario orgao = new OrgaoDonatario();

                orgao.setId(resultado.getInt("id"));
                orgao.setNome(resultado.getString("nome"));
                orgao.setEndereco(resultado.getString("endereco"));
                orgao.setTelefone(resultado.getString("telefone"));
                orgao.setDescricao(resultado.getString("descricao"));
                orgao.setEmail(resultado.getString("email"));
                orgao.setSenha(resultado.getString("senha"));

                orgaos.add(orgao);
            }
        }

        return orgaos;
    }
    
    public OrgaoDonatario buscarPorEmail(String email)
    		throws SQLException {
    	
    	String sql = """
    			SELECT id, nome, endereco, telefone,
    				descricao, email, senha
    			FROM orgao_donatario
    			WHERE email = ?
    			""";
    	
    	try (Connection conexao = Conexao.conectar();
    			PreparedStatement comando =
    					conexao.prepareStatement(sql)) {
    		
    		comando.setString(1, email);
    		
    		try (ResultSet resultado = comando.executeQuery()) {
        		if (resultado.next()) {
        			OrgaoDonatario orgao =
        					new OrgaoDonatario();
        			
        			orgao.setId(resultado.getInt("id"));
                    orgao.setNome(resultado.getString("nome"));
                    orgao.setEndereco(
                        resultado.getString("endereco"));
                    orgao.setTelefone(
                        resultado.getString("telefone"));
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