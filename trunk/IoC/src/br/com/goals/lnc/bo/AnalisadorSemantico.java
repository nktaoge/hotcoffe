package br.com.goals.lnc.bo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.goals.lnc.vo.FraseSintatica;
import br.com.goals.lnc.vo.UmaPalavra;

public class AnalisadorSemantico {
	private static Logger logger = Logger.getLogger(AnalisadorSemantico.class);
	public static final String SIG_PACK="br.com.goals.lnc.sig";
	public static final String SIG_SRC="br/com/goals/lnc/sig/";
	public synchronized static String analisar(FraseSintatica fraseSintatica) {
		String retorno = null;
		List<String> adicionarSig = new ArrayList<String>();
		{
			//Pegar o sig(s) da palavra do sujeito
			for(UmaPalavra umaPalavra:fraseSintatica.getSujeito().getPalavras()){
				boolean temSignificado = false;
				if(umaPalavra.getSignificados()!=null){
					if(umaPalavra.getSignificados().size()==1){
						try {
							Class.forName(SIG_PACK + '.' + umaPalavra.getSignificados().get(0));
							temSignificado = true;
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				if(!temSignificado){
					String className = 'S' + umaPalavra.getClass().getSimpleName().substring(1);
					adicionarSig.add(umaPalavra.getClass().getSimpleName());
					try{
						Compilador.criarSig(SIG_PACK, className, SIG_SRC);
					}catch(Exception e){
						logger.error("Erro ao criar sig " + className,e);
					}
				}
			}
		}
		//Compilar os significados
		Programador programador = new Programador();
		for (String string : adicionarSig) {
			String className = 'S' + string.substring(1);
			try{
				programador.colocarSignificadoEmPalavra(string, className);
			}catch(Exception e){
				logger.error("Erro ao recompilar a " + className,e);
			}
		}
		
		String metodoVerbo = "exz";
		String comentario = "Verbo(s): ";
		//Pegar o sig(s) da palavra do sujeito
		for(UmaPalavra umaPalavra:fraseSintatica.getVerbo().getPalavras()){
			metodoVerbo+=umaPalavra.getClass().getSimpleName();
			comentario+=umaPalavra.getEscrita() + ", ";
		}
		
		//Pegar o sig(s) da palavra do sujeito
		for(UmaPalavra umaPalavra:fraseSintatica.getSujeito().getPalavras()){
			if(umaPalavra.getSignificados()!=null && umaPalavra.getSignificados().size()==1){
				try {
					String sigName = umaPalavra.getSignificados().get(0);
					Class cls = Class.forName(SIG_PACK + '.' + sigName);
					Method[] metodos = cls.getDeclaredMethods();
					boolean achouMetodo = false;
					for (int i = 0; i < metodos.length; i++) {
						if(metodos[i].getName().equals(metodoVerbo)){
							achouMetodo = true;
						}
					}
					if(!achouMetodo){
						programador.criarMetodo(sigName,metodoVerbo,comentario);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		
		
		return retorno;
	}

}
