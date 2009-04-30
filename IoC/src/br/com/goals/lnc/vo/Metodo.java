package br.com.goals.lnc.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.goals.lnc.bo.Programador;

public class Metodo {
	private String nome;
	private List<String> parametros = new ArrayList<String>();
	private String retorno;
	private String body;
	private String comentario;
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<String> getParametros() {
		return parametros;
	}
	public void setParametros(List<String> parametros) {
		this.parametros = parametros;
	}
	public String getRetorno() {
		return retorno;
	}
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		String ret = "/**\n" +
				" * " + comentario + "\n" +
				" */\n" +
				"public " + retorno + " " + nome+"( ";
		for (String param :parametros) {
			ret+=param + " " + Programador.escreverNomeVariavel(param) + ",";
		}
		ret = ret.substring(0,ret.length()-1);
		ret+="){\n\t";
		ret+=body.replace("\n","\n\t") + "\n";
		ret+="}\n";
		return ret;
	}
}
