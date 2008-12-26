package br.com.goals.debug;

import java.util.HashMap;

public class Sysou {
	private static int ident=0;
	private static HashMap<String,Integer> mapVerbose = new HashMap<String,Integer>();
	private String nome; 
	private String simpleName;
	private static String lastPrintSimpleName; 
	public Sysou(Object o,int v){
		nome = o.getClass().getName();
		simpleName = o.getClass().getSimpleName();
		mapVerbose.put(nome,v);
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
}
