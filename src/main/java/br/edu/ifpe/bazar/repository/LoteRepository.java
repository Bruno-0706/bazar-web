
package br.edu.ifpe.bazar.repository;

import br.edu.ifpe.bazar.config.Conexao;
import br.edu.ifpe.bazar.model.Lote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LoteRepository {

    public void criar(Lote lote) throws SQLException {

        String sql = """
            INSERT INTO lote
                (data_entrega, observacao, fiscalizador_id)
            VALUES (?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            if (lote.getDataEntrega() == null) {
                comando.setDate(1, null);
            } else {
                comando.setDate(
                    1, Date.valueOf(lote.getDataEntrega()));
            }

            comando.setString(2, lote.getObservacao());
            comando.setInt(3, lote.getFiscalizadorId());

            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    lote.setId(chaves.getInt(1));
                }
            }

            lote.setStatus("DISPONIVEL");
            lote.setDonatarioId(null);
        }
    }

    public List<Lote> listarPorFiscalizador(int fiscalizadorId)
            throws SQLException {

        String sql = """
            SELECT id, data_entrega, observacao, status,
                   fiscalizador_id, donatario_id
            FROM lote
            WHERE fiscalizador_id = ?
            ORDER BY id DESC
            """;

        List<Lote> lotes = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql)) {

            comando.setInt(1, fiscalizadorId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lotes.add(mapearLote(resultado));
                }
            }
        }

        return lotes;
    }

    public List<Lote> listarDisponiveis() throws SQLException {

        String sql = """
            SELECT id, data_entrega, observacao, status,
                   fiscalizador_id, donatario_id
            FROM lote
            WHERE status = 'DISPONIVEL'
            ORDER BY id DESC
            """;

        List<Lote> lotes = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                lotes.add(mapearLote(resultado));
            }
        }

        return lotes;
    }

    public List<Lote> listarRecebidos(int donatarioId)
            throws SQLException {

        String sql = """
            SELECT id, data_entrega, observacao, status,
                   fiscalizador_id, donatario_id
            FROM lote
            WHERE donatario_id = ?
              AND status = 'DOADO'
            ORDER BY data_entrega DESC, id DESC
            """;

        List<Lote> lotes = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql)) {

            comando.setInt(1, donatarioId);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lotes.add(mapearLote(resultado));
                }
            }
        }

        return lotes;
    }

    public boolean registrarDoacao(int loteId, int donatarioId)
            throws SQLException {

        String sql = """
            UPDATE lote
            SET donatario_id = ?,
                data_entrega = CURRENT_DATE,
                status = 'DOADO'
            WHERE id = ?
              AND status = 'DISPONIVEL'
              AND donatario_id IS NULL
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando =
                 conexao.prepareStatement(sql)) {

            comando.setInt(1, donatarioId);
            comando.setInt(2, loteId);

            return comando.executeUpdate() == 1;
        }
    }

    private Lote mapearLote(ResultSet resultado)
            throws SQLException {

        Lote lote = new Lote();

        lote.setId(resultado.getInt("id"));

        Date data = resultado.getDate("data_entrega");

        if (data != null) {
            lote.setDataEntrega(data.toLocalDate());
        }

        lote.setObservacao(resultado.getString("observacao"));
        lote.setStatus(resultado.getString("status"));
        lote.setFiscalizadorId(
            resultado.getInt("fiscalizador_id"));

        int donatarioId = resultado.getInt("donatario_id");

        if (resultado.wasNull()) {
            lote.setDonatarioId(null);
        } else {
            lote.setDonatarioId(donatarioId);
        }

        return lote;
    }
}
