package br.edu.ifpe.bazar.model;

public class Produto {

    private int codigo;
    private String nome;
    private String descricao;
    private int loteId;

    public Produto() {
    }

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getLoteId() {
		return loteId;
	}

	public void setLoteId(int loteId) {
		this.loteId = loteId;
	}
    
}