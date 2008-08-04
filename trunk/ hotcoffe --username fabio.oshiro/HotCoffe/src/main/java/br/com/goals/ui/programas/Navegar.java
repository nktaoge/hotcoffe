package br.com.goals.ui.programas;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.id.jericho.lib.html.CharacterReference;
import br.com.goals.tts.TTS;
import br.com.goals.utils.Http;

public class Navegar extends CommandLineClient {
	Pattern patLimpaScript = Pattern.compile("<script.*?</script>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	Pattern patLimpaComments = Pattern.compile("<!--.*?-->",Pattern.DOTALL);
	Pattern patLimpaImg = Pattern.compile("<img[^>]*alt=(\"|')(.*?)\\1[^>]*>",Pattern.DOTALL);
	Pattern patTag = Pattern.compile("<[^>]*>",Pattern.DOTALL);
	Pattern patLink = Pattern.compile("href=(\"|')(.*?)\\1");
	int currentTag = 0;
	String currentLink=null;
	String currentPage=null;
	Http http = new Http();
	ArrayList<Tag> tags;
	class Tag{
		String text;
		String help;
		String href;
	}
	public String limparHtml(String codHtml){
		codHtml = patLimpaScript.matcher(codHtml).replaceAll("");
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
		abrirPagina(getNextLine());
	}
	private void abrirPagina(String url){
		try{
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
			for(currentTag=0;currentTag<tags.size();currentTag++){
				if(tags.get(currentTag).href!=null){
					speak("link,");
					currentLink = tags.get(currentTag).href;
				}
				speak(tags.get(currentTag).text);
			}
			speak("Fim da página");
		}catch(Exception e){
			TTS.speak("Erro navegando: "+e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void setNextLine(String nextLine) {
		System.out.println("setNextLine called...");
		if(tags!=null){
			System.out.println("-->"+tags.get(currentTag).text);
			System.out.println("+-->"+currentLink);
			abrirPagina(currentLink);
		}
	}
}
