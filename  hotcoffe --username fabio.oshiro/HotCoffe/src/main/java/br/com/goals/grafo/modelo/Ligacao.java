package br.com.goals.grafo.modelo;

import java.util.Date;

public class Ligacao {
	private Long ligacaoId;
	private long pontoIdA;
	private long pontoIdB;
	private Date dataHora;
	public long getPontoIdA() {
		return pontoIdA;
	}
	public void setPontoIdA(long pontoIdA) {
		this.pontoIdA = pontoIdA;
	}
	public long getPontoIdB() {
		return pontoIdB;
	}
	public void setPontoIdB(long pontoIdB) {
		this.pontoIdB = pontoIdB;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public Long getLigacaoId() {
		return ligacaoId;
	}
	public void setLigacaoId(Long ligacaoId) {
		this.ligacaoId = ligacaoId;
	}
}
