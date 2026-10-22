package br.edu.ifpe.bazar;

import br.edu.ifpe.bazar.model.Produto;
import br.edu.ifpe.bazar.repository.ProdutoRepository;

import java.sql.SQLException;

public class TesteProduto {

	public static void main(String[] args) throws SQLException {
		
		int loteId = 1;
		
		ProdutoRepository repositorio =
				new ProdutoRepository();
		
		Produto produto = new Produto();
		produto.setNome("Notebook");
		produto.setDescricao("Notebook usado para teste");
		produto.setLoteId(loteId);
		
		repositorio.criar(produto);
		
		System.out.println(
				"Produto cadastrado: " + produto.getCodigo());
		
		System.out.println(
				"Produtos do lote: "
				+ repositorio.listarPorLote(loteId).size());
	}
	
}
