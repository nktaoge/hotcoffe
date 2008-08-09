package br.com.goals.ui.programas;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.id.jericho.lib.html.CharacterReference;
import br.com.goals.tts.TTS;
import br.com.goals.utils.Http;

public class Navegar extends CommandLineClient {
	Pattern patLimpaScript = Pattern.compile("<script.*?</script>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	Pattern patLimpaStyle = Pattern.compile("<style.*?</style>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	Pattern patLimpaComments = Pattern.compile("<!--.*?-->",Pattern.DOTALL);
	Pattern patLimpaImg = Pattern.compile("<img[^>]*alt=(\"|')(.*?)\\1[^>]*>",Pattern.DOTALL);
	Pattern patTag = Pattern.compile("<[^>]*>",Pattern.DOTALL);
	Pattern patLink = Pattern.compile("href=(\"|')(.*?)\\1");
	int currentTag = 0;
	String currentPage=null;
	String nextPage=null;
	Http http = new Http();
	ArrayList<Tag> tags;
	Tag currentTagLink;
	boolean espera=true;
	class Tag{
		String text;
		String help;
		String href;
	}
	public String limparHtml(String codHtml){
		codHtml = patLimpaScript.matcher(codHtml).replaceAll("");
		codHtml = patLimpaStyle.matcher(codHtml).replaceAll("");
		Matcher mat = patLimpaImg.matcher(codHtml);
		while(mat.find()){
			codHtml = mat.replaceFirst(mat.group(2));
			mat = patLimpaImg.matcher(codHtml);
		}
		codHtml = patLimpaComments.matcher(codHtml).replaceAll("");
		codHtml = CharacterReference.decode(codHtml);
		return codHtml;
	}
	@Override
	public void execute() throws Exception { 
		TTS.speak("Digite o nome da página");
		nextPage =getNextLine();
		do{
			abrirPagina(nextPage);
		}while(nextPage!=null);
	}
	private void abrirPagina(String url){
		try{
			nextPage=null;
			currentPage = url;
			String codHtml = http.getContent(currentPage);
			codHtml = this.limparHtml(codHtml);
			Matcher mat = patTag.matcher(codHtml);
			int start=0;
			tags = new ArrayList<Tag>();
			int i=0;
			String href=null;
			while(mat.find()){
				Tag tag = new Tag();
				Matcher matHref = patLink.matcher(mat.group());
				if(matHref.find()){	
					href = matHref.group(2);
				}else if(href!=null){
					tag.href=href;
					href=null;
				}
				tag.text=codHtml.substring(start,mat.start());
				tags.add(tag);
				start = mat.end();
				i++;
			}
			do{
				System.out.println("tags.size() = "+tags.size());
				for(currentTag=0;currentTag<tags.size();currentTag++){
					try{
						if(tags.get(currentTag).href!=null){
							speak("link,");
							currentTagLink = tags.get(currentTag);
						}
						speak(tags.get(currentTag).text);
					}catch(Exception e){
						
					}
				}
				if(nextPage!=null) return;
				speak("Fim da página");
				while(espera && currentTag!=-1){
					Thread.sleep(200);
				}
			}while(currentTag==-1);
		}catch(Exception e){
			TTS.speak("Erro navegando: "+e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void setNextLine(String nextLine) {
		System.out.print("setNextLine called...");
		for(int i=0;i<nextLine.length();i++){
			System.out.print((int)nextLine.charAt(i)+" ");
		}
		System.out.println();
		if(nextLine.trim().equals("v")){
			currentTag-=2;
		}else if(nextLine.trim().equals("ini")){
			System.out.println("ini");
			currentTag=-1;
		}else if(nextLine.equals("\t")){
			System.out.println("Tab");
			speak("");
			for(;currentTag<tags.size();currentTag++){
				if(tags.get(currentTag).href!=null){
					currentTag--;
					break;
				}
			}
		}else if(nextLine.equals("\n")){
			if(tags!=null){
				System.out.println("Enter");
				//System.out.println("-->"+tags.get(currentTag).text);
				//System.out.println("+-->"+currentTagLink.href);
				abrirLinkAtual();
			}
		}
	}
	private void abrirLinkAtual(){
		speak("Abrindo link "+currentTagLink.text);
		try{
			if(currentTagLink.href.startsWith("http")){
				nextPage = currentTagLink.href;
			}else{
				URL url = new URL(new URL(currentPage),currentTagLink.href);
				nextPage = url.toExternalForm();
			}
		}catch(Exception e){
			
		}
		currentTag = tags.size();
	}
	@Override
	protected void exit() {
		try{
			currentTag=tags.size();
			espera=false;
		}catch(Exception e){
			//
		}
	}
}
