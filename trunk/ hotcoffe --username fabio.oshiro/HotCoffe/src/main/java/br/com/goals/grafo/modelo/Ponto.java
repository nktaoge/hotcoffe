package br.com.goals.grafo.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ponto {
	private Long pontoId = null;
	private String nome;
	private String classe;
	private String descricao;
	private Date dataHora;
	private List<Ponto> ligacaoA;
	private List<Ponto> ligacaoB;
	public Ponto(){
		dataHora = new Date();
	}
	public Ponto(String string) {
		this();
		this.descricao = string;
	}
	public Long getPontoId() {
		return pontoId;
	}
	public void setPontoId(Long pontoId) {
		this.pontoId = pontoId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String toString(){
		String retorno = "-->id="+pontoId+"\nnome='"+nome+"'\nclasse='"+classe + "'\ndataHora='"+dataHora+"'\ndescricao='"+descricao+"'";
		if(ligacaoA==null) return retorno;
		retorno+="\nligacaoA.size()=" + ligacaoA.size();
		for(Ponto ponto:ligacaoA){
			retorno+="\n\t"+ponto.toString().replace("\n","\n\t");
		}
		return retorno;
	}
	/**
	 * Sobreescrito, compara por ponto_id
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ponto){
			Ponto ponto = (Ponto) obj;
			if(ponto.getPontoId().equals(this.pontoId)){
				return true;
			}else{
				return false;
			}
		}
		return super.equals(obj);
	}
	public List<Ponto> getLigacaoA() {
		if(ligacaoA==null){
			ligacaoA = new ArrayList<Ponto>();
		}
		return ligacaoA;
	}
	public void setLigacaoA(List<Ponto> ligacaoA) {
		this.ligacaoA = ligacaoA;
	}
	public List<Ponto> getLigacaoB() {
		if(ligacaoB==null){
			ligacaoB = new ArrayList<Ponto>();
		}
		return ligacaoB;
	}
	public void setLigacaoB(List<Ponto> ligacaoB) {
		this.ligacaoB = ligacaoB;
	}
}
