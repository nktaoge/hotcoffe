package br.com.goals.lnc.cup;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.goals.hotcoffe.ioc.casosdeuso.UmCasoDeUso;
import br.com.goals.lnc.bo.AnalisadorSemantico;
import br.com.goals.lnc.bo.AnalisadorSintaticoFactory;
import br.com.goals.lnc.bo.Compilador;
import br.com.goals.lnc.vo.Comando;
import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public class CriarCodigo extends UmCasoDeUso{
	private static Logger logger = Logger.getLogger(CriarCodigo.class);
	protected void iniciar() throws Exception {
		if(Compilador.workspaceSrc==null){
			Compilador.workspaceSrc = getControlador().getInitParameter("workspaceSrc");
		}
		while (true) {
			Comando comando= new Comando();
			ator.preencher(comando);
			String resposta = tratar(comando);
			if(resposta!=null && !resposta.equals("")){
				sistema.mostrar(resposta);
			}else{
				sistema.mostrar("Comando processado :-)");
			}
		}
	}
	private String tratar(Comando comando) throws Exception {
		String s =comando.getComando();
		//TODO melhorar
		s = s.replace("?", " ?")
			.replace("!", " !")
			.replace(",", " ,")
			.replace(".", " .");
		String[] token = s.split("\\s+");
		//Lexica
		List<UmaPalavra> palavras = new ArrayList<UmaPalavra>();
		for (int i = 0; i < token.length; i++) {
			String className = formatar2ClassName(token[i]);
			boolean ok = false;
			while(!ok){
				String fullClassName = CriarToken.TOKEN_PACKAGE+'.'+className;
				try {
					Object c = Class.forName(fullClassName).newInstance();
					palavras.add((UmaPalavra) c);
					ok = true;
				} catch (ClassNotFoundException e) {
					logger.debug("Simbolo nao encontrado: identifier not found " + fullClassName);
					CriarToken criarToken = new CriarToken();
					criarToken.setDuvida(token[i]);
					criarToken.setClassName(className);
					usar(criarToken);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		//sintatica
		FraseSintatica fraseSintatica = null;
		{
			//sujeito
			logger.info("comando = " + comando.getComando());
			AnalisadorSintaticoFactory analisador = new AnalisadorSintaticoFactory();
			fraseSintatica = analisador.analisar(palavras);
		}
		logger.info("fraseSintatica " + fraseSintatica);
		
		String codIntermediario = "";
		//semantica
		{
			/*
			 * a semantica seria talvez o modelo
			 * hei de colocar eles em sig
			 */
			codIntermediario = AnalisadorSemantico.analisar(fraseSintatica);
		}
		
		
		if(codIntermediario!=null && !codIntermediario.equals("")){
			logger.debug("codIntermediario = " + codIntermediario);
		}
		return codIntermediario;
	}
	
	/**
	 * Cria o nome de classe
	 * trata os caracteres especiais
	 * @param s
	 * @return nome de classe
	 */
	private static String formatar2ClassName(String s){
		String retorno = s.toLowerCase();
		for (int i = 0; i < retorno.length(); i++) {
			char c = retorno.charAt(i);
			if(!Character.isDigit(c) && (c>'z' ||c<'a')){
				retorno = retorno.substring(0,i)+ "C" + (int)c + retorno.substring(i+1); 
			}
		}
		//P de palavra
		retorno = 'P'+retorno;
		logger.debug("formatar2ClassName("+s+") = " + retorno);
		return retorno;
	}
}
