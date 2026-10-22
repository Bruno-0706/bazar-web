package br.edu.ifpe.bazar.service;

import br.edu.ifpe.bazar.model.Lote;
import br.edu.ifpe.bazar.model.OrgaoDonatario;
import br.edu.ifpe.bazar.model.Produto;
import br.edu.ifpe.bazar.repository.LoteRepository;
import br.edu.ifpe.bazar.repository.OrgaoDonatarioRepository;
import br.edu.ifpe.bazar.repository.ProdutoRepository;

import java.sql.SQLException;
import java.util.List;

public class LoteService {

	private final LoteRepository loteRepository =
			new LoteRepository();
	
	private final ProdutoRepository produtoRepository =
			new ProdutoRepository();
	
	private final OrgaoDonatarioRepository donatarioRepository =
			new OrgaoDonatarioRepository();
	
	public List<Lote> listarLotesDoFiscalizador(int fiscalizadorId)
			throws SQLException {
		
		return loteRepository.listarPorFiscalizador(fiscalizadorId);
	}
	
	public List<Lote> listarLotesDisponiveis()
			throws SQLException {
		
		return loteRepository.listarDisponiveis();
	}
	
	public List<Lote> listarLotesRecebidos(int donatarioId)
			throws SQLException {
		
		return loteRepository.listarRecebidos(donatarioId);
	}
	
	public List<Produto> listarProdutos(int loteId)
			throws SQLException {
		
		return produtoRepository.listarPorLote(loteId);
	}
	
	public void cadastrarLote(Lote lote)
			throws SQLException {
		
		if (lote.getFiscalizadorId() <= 0) {
			throw new IllegalArgumentException(
					"Fiscalizador inválido.");
		}
		
		loteRepository.criar(lote);
	}
	
	public void cadastrarProduto(Produto produto)
			throws SQLException {
		
		if (produto.getNome() == null
				|| produto.getNome().isBlank() ) {
			throw new IllegalArgumentException(
					"O nome do produto é obrigatório.");
		}
		
		if (produto.getLoteId() <= 0 ) {
			throw new IllegalArgumentException(
					"Lote inválido.");
		}
		
		boolean loteDisponivel = loteRepository
				.listarDisponiveis()
				.stream()
				.anyMatch(lote -> lote.getId() == produto.getLoteId());
		
		if (!loteDisponivel) {
			throw new IllegalArgumentException(
					"O lote não existe ou já foi doado.");
		}
		
		produtoRepository.criar(produto);
	}
	
	public boolean doarLote(int loteId, int donatarioId)
			throws SQLException {
		
		if (loteId <= 0 || donatarioId <= 0) {
			throw new IllegalArgumentException(
					"Lote ou donatário inválido.");
		}
		
		boolean donatarioExiste = donatarioRepository
				.listarTodos()
				.stream()
				.anyMatch(orgao -> orgao.getId() == donatarioId);
		
		if (!donatarioExiste) {
			throw new IllegalArgumentException(
					"Órgão donatário não encontrado.");
		}
		
		return loteRepository.registrarDoacao(
				loteId, donatarioId);
	}
	
	private final OrgaoDonatarioRepository orgaoDonatarioRepository =
			new OrgaoDonatarioRepository();
	
	public List<OrgaoDonatario> listarDonatarios() throws SQLException {
		return orgaoDonatarioRepository.listarTodos();
	}
}
