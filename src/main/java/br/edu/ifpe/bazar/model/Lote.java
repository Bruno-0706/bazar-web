package br.edu.ifpe.bazar.model;

import java.time.LocalDate;

public class Lote {

	private int id;
    private LocalDate dataEntrega;
    private String observacao;
    private String status;

    private int fiscalizadorId;
    private Integer donatarioId;

    public Lote() {
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFiscalizadorId() {
		return fiscalizadorId;
	}

	public void setFiscalizadorId(int fiscalizadorId) {
		this.fiscalizadorId = fiscalizadorId;
	}

	public Integer getDonatarioId() {
		return donatarioId;
	}

	public void setDonatarioId(Integer donatarioId) {
		this.donatarioId = donatarioId;
	}
    
	
}
