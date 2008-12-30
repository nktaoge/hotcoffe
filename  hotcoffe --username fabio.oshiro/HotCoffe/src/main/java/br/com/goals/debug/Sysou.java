package br.com.goals.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * Classe para debug e println "estruturado"
 * @author Fabio Issamu Oshiro
 *
 */
public class Sysou {
	private static int ident=0;
	private static HashMap<String,Integer> mapVerbose = new HashMap<String,Integer>();
	/**
	 * Guarda comandos não atribuidos,
	 * isso acontece quando é chamado um setVerboseLevelBySimpleName
	 * e ainda não existe uma instância para o simpleName no mapVerbose.
	 */
	private static ArrayList<Comando> bufferComandoVerboseLevel = new ArrayList<Comando>();
	private String nome; 
	private String simpleName;
	private static String lastPrintSimpleName;
	private static boolean primeiroPrint = true;
	
	public Sysou(Object o,int v){
		nome = o.getClass().getName();
		simpleName = o.getClass().getSimpleName();
		mapVerbose.put(nome,v);
		atribuirComandosNoBuffer();
		
	}
	static void testar(){
		//TODO testar dentro do print e println
		try{
			throw new Exception();
		}catch(Exception e){
			StackTraceElement[] st = e.getStackTrace();
			st[0].getMethodName();
			//st[0].
		}
	}
	private static void atribuirComandosNoBuffer(){
		for(Comando c:bufferComandoVerboseLevel){
			boolean b = setVL(c.simpleName,c.level);
			if(b){
				bufferComandoVerboseLevel.remove(c);
				break;
			}
		}
	}
	/**
	 * set verbose level
	 * @param simpleName nome simples da classe
	 * @param level valor da verborracidade
	 * @return true caso seja atribuido, ou seja, a classe está instanciada
	 */
	private static boolean setVL(String simpleName,int level){
		boolean achou=false;
		Set<String> keys = mapVerbose.keySet();
		for(String classe:keys){
			if(classe.endsWith("."+simpleName)){
				mapVerbose.put(classe, level);
				achou = true;
				break;
			}
		}
		return achou;
	}
	/**
	 * Atribui a volume verborrágico do Sysou
	 * @param simpleName nome simples da classe, ex.: CAL
	 * @param level valor de 0 a N
	 * @throws Exception caso um erro ocorra
	 */
	public static void setVerboseLevelBySimpleName(String simpleName,int level) throws Exception{
		boolean achou = setVL(simpleName, level);
		if(!achou){
			bufferComandoVerboseLevel.add(new Comando(simpleName,level));
		}
	}
	private void printString(String str){
		String idents = "";
		for(int i=0;i<ident;i++){
			idents+="\t";
		}
		if(!simpleName.equals(lastPrintSimpleName)){
			System.out.println(idents+simpleName+":");
			lastPrintSimpleName = simpleName;
		}
		System.out.println(idents+str.replace("\n", "\n"+idents));
		primeiroPrint = true;
	}
	public synchronized void println(int level,Object obj){
		int v=mapVerbose.get(nome);
		if(level<=v){
			printString(obj.toString());
		}
	}
	public synchronized void println(int level,String mensagem){
		int v=mapVerbose.get(nome);
		if(level<=v){
			printString(mensagem);
		}
	}
	public void onEnterFunction(int level,String mensagem) {
		int v=mapVerbose.get(nome);
		if(level<=v){
			printString(simpleName+"."+mensagem+"(){");
		}
		ident++;
	}
	public void onExitFunction(int level,String mensagem){
		ident--;
		int v=mapVerbose.get(nome);
		if(level<=v){
			printString("}//"+simpleName+"."+mensagem);
		}
	}
	public void print(int level, String mensagem) {
		int v=mapVerbose.get(nome);
		if(level<=v){
			String idents = "";
			for(int i=0;i<ident;i++){
				idents+="\t";
			}
			if(!simpleName.equals(lastPrintSimpleName)){
				System.out.println(idents+simpleName+":");
				lastPrintSimpleName = simpleName;
			}
			if(primeiroPrint){
				primeiroPrint=false;
				System.out.print(idents);
			}
			System.out.print(mensagem.replace("\n", "\n"+idents));
		}
	}
}
/**
 * Classe para guardar um comando não atribuido no verbose level
 * @author Fabio Issamu Oshiro
 *
 */
class Comando{
	String simpleName;
	int level;
	Comando(String simpleName,int level){
		this.simpleName = simpleName;
		this.level = level;
	}
}
