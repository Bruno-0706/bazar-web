package br.edu.ifpe.bazar.model;

public class UsuarioAutenticado {

	
	private final int id;
	private final String nome;
	private final String email;
	private final String tipo;
	
	public UsuarioAutenticado(
			int id, String nome, String email, String tipo) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getTipo() {
		return tipo;
	}
	
	public boolean isFiscalizador() {
		return "FISCALIZADOR".equals(tipo);
	}
	
	public boolean isDonatario() {
		return "DONATARIO".equals(tipo);
	}
}
