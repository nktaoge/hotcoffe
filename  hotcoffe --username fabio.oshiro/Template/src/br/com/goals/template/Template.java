package br.com.goals.template;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Ações comuns ao template<br>
 * Depende do commons io da apache
 * 
 * @author Fabio Issamu Oshiro
 *
 */
public class Template extends BaseTemplate{
	private static Logger logger = Logger.getLogger(Template.class);
	private static Pattern patRs = Pattern.compile("<!-- ini rs -->(.*?)<!-- fim rs -->", Pattern.DOTALL);
	private static Pattern patRsItens = Pattern.compile("<!-- ini rs\\((.*?)\\) -->(.*?)<!-- fim rs\\(\\1\\) -->", Pattern.DOTALL);
	private static Pattern patRsImagens = Pattern.compile("<!-- ini rs imagem\\((.*?)\\) -->(.*?)<!-- fim rs imagem\\(\\1\\) -->", Pattern.DOTALL);
	private static Pattern patAnterior = Pattern.compile("<!-- ini anterior -->(.*?)<!-- fim anterior -->", Pattern.DOTALL);
	private static Pattern patProxima = Pattern.compile("<!-- ini proxima -->(.*?)<!-- fim proxima -->", Pattern.DOTALL);
	private static Pattern patLimpaLink = Pattern.compile("(<a\\s.*?>|</a>)", Pattern.DOTALL);

	private int semNomeNum = 1;
	
	/**
	 * paginacao
	 */
	private static Pattern patPag = Pattern.compile("<!-- ini paginacao -->(.*?)<!-- fim paginacao -->", Pattern.DOTALL);
	private static Pattern patPags = Pattern.compile("<!-- ini paginas -->(.*?)<!-- fim paginas -->", Pattern.DOTALL);
	private String template;
	private String templateOriginal;

	private HashMap<String,String> substituir=new HashMap<String,String>();
	private HashMap<String,Template> subTemplate = new HashMap<String,Template>();
	private RsItemCustomizado rsItemCustomizado;
	
	/**
	 * Atribui um menu conforme o pacote
	 * @param strPackage ex.: br.com.goals
	 * @see SysMenu#package2ul(String, String)
	 */
	public void setMenuPackage(String strPackage){
		try{
			String str = SysMenu.package2ul(strPackage, strPackage);
			setArea("menu", str);
		}catch (Exception e) {
			logger.warn("Nao foi possivel criar o menu.\nErro: '" + e.getMessage() + "'");
		}
	}
	
	/**
	 * Substitui um valor quando este for igual
	 * no ResultSet
	 * @param igual
	 * @param novoValor
	 */
	public void addReplace(String igual,String novoValor){
		substituir.put(igual, novoValor);
	}
	
	/**
	 * Serve para retornar a pasta dos template(s)<br>
	 * Ex.: /home/fabio/tomcat6/webapps/aplicacao/template<br>
	 * Se a pasta nao existir sera criada uma nova pasta
	 * @return retorna o path real da pasta template dentro da aplicacao
	 */
	public File getTemplatePath(){
		String path = this.getClass().getClassLoader().getResource("Configuracao.properties").getPath();
		File file = new File(path);
		File template = new File(file.getParentFile().getParentFile().getParentFile(),"template");
		if(!template.exists()){
			template.mkdirs();
		}
		return template;
	}

	/**
	 * Atribui a string na area.<br>
	 * Se houver bloco, o bloco sera retirado.<br>
	 * Se na marcacao conter dd/MM/yyyy a data sera convertida de yyyy-mm-dd para BR
	 * @param template
	 * @param valor
	 * @param chave
	 * @return
	 */
	private String atribuirRsString(String template, String valor,String chave,String fieldName){		
		if(valor==null || valor.equals("")){
			Pattern patRs = Pattern.compile("<!-- ini bloco " + chave + " -->(.*?)<!-- fim bloco " + chave + " -->", Pattern.DOTALL);
			Matcher mat = patRs.matcher(template);
			if (mat.find()) {
				template = mat.replaceAll("");
			}
		}
		Pattern patRs = Pattern.compile("<!-- ini rs\\(" + chave + "\\) -->(.*?)<!-- fim rs\\(" + chave + "\\) -->", Pattern.DOTALL);
		Matcher mat = patRs.matcher(template);
		if (mat.find()) {
			if(valor==null) valor="";
			if(substituir.get(valor)!=null){
				valor=substituir.get(valor);
			}
			String tmp=mat.group(1).trim();
			if(tmp.equals("dd/MM/yyyy")){
				valor = Conversor.deYYYYMMDDparaDDMMYYYY(valor);
			}else if(tmp.startsWith("<input ")){
				String aux =tmp.replaceAll("value=\"[^\"]*\"","value=\"" + valor.replace("\"", "&quot;") + "\""); 
				valor = aux.replaceAll("name=\"[^\"]*\"","name=\"" + fieldName + "\"");
			}
			template = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(valor));
		}
		return template;
	}
	
	public void encaixaPaginacao(Link voltar){
		Matcher mat = patPag.matcher(template);
		if (mat.find()) {
			template = mat.replaceAll("<a href=\""+voltar.getHref()+"\">"+voltar.getLabel()+"</a>");
		}
	}
	public void encaixaPaginacao(String string) {
		Matcher mat = patPag.matcher(template);
		if (mat.find()) {
			template = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(string));
		}
	}
	/**
	 * Coloca a paginação de acordo com o template
	 * @param lstLinks lista de links
	 * @param lnkAnterior link da página anterior
	 * @param lnkProxima link da próxima página
	 * @param atual link da página atual
	 */
	public void encaixaPaginacao(List<Link> lstLinks,Link lnkAnterior,Link lnkProxima,Link atual){
		Matcher mat = patPag.matcher(template);
		if (mat.find()) {
			String paginas = mat.group(1);
			Matcher matPags = patPags.matcher(paginas);
			if (matPags.find()) {
				String strLink = matPags.group(1);
				String strSeparador = " - ";
				String strA = " - ";
				Pattern patLink = Pattern.compile("<a\\s(.*?)>", Pattern.DOTALL);
				Matcher matLink = patLink.matcher(strLink);
				if (matLink.find()) {
					strA = matLink.group();
				}
				Pattern patSeparador = Pattern.compile("<!-- ini separador -->(.*?)<!-- fim separador -->", Pattern.DOTALL);
				Matcher matSeparador = patSeparador.matcher(strLink);
				if (matSeparador.find()) {
					strSeparador = matSeparador.group(1);
				}
				StringBuilder sb = new StringBuilder();

				if (lstLinks.size() > 0) {
					Link link = lstLinks.get(0);
					if (link.equals(atual)) {
						sb.append(link.getLabel());
					} else {
						sb.append(HtmlUtils.substituiAtributoTag(strA, "a", "href", link.getHref()));
						sb.append(link.getLabel());
						sb.append("</a>");
					}
					if (lstLinks.size() > 1)
						sb.append(strSeparador);
					for (int i = 1; i < lstLinks.size(); i++) {
						link = lstLinks.get(i);
						if (link.equals(atual)) {
							sb.append(link.getLabel());
						} else {
							sb.append(HtmlUtils.substituiAtributoTag(strA, "a", "href", link.getHref()));
							sb.append(link.getLabel());
							sb.append("</a>");
						}
						sb.append(strSeparador);
					}
				}
				paginas = matPags.replaceAll(sb.toString());
			}
			// colocar prox e anterior

			Matcher matAntProx = patAnterior.matcher(paginas);
			if (matAntProx.find()) {
				String anterior = matAntProx.group(1);
				if (lnkAnterior!=null) {
					paginas = matAntProx.replaceAll(HtmlUtils.substituiAtributoTag(anterior, "a", "href", lnkAnterior.getHref()));
				} else {
					Matcher matAs = patLimpaLink.matcher(anterior);
					if (matAs.find()) {
						anterior = matAs.replaceAll("");
					}
					paginas = matAntProx.replaceAll(anterior);
				}
			}
			matAntProx = patProxima.matcher(paginas);
			if (matAntProx.find()) {
				String anterior = matAntProx.group(1);
				if (lnkProxima!=null) {
					paginas = matAntProx.replaceAll(HtmlUtils.substituiAtributoTag(anterior, "a", "href",lnkProxima.getHref()));
				} else {
					Matcher matAs = patLimpaLink.matcher(anterior);
					if (matAs.find()) {
						anterior = matAs.replaceAll("");
					}
					paginas = matAntProx.replaceAll(anterior);
				}
			}

			template = mat.replaceAll(paginas);
		}
	}
	/**
	 * 
	 * @param rowCount total de resultados
	 * @param pagina numero da pagina atual
	 * @param max resultados por pagina
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void encaixaPaginacao(int rowCount, int pagina, int max, HttpServletRequest request) {

		Matcher mat = patPag.matcher(template);
		if (mat.find()) {
			String qString = "";
			String paginas = mat.group(1);
			Paginacao paginacao = new Paginacao();
			// recuperar os postados e colocar no get
			Enumeration<String> e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String parm = e.nextElement();
				if (!parm.equals("pagina"))
					qString += parm + "=" + request.getParameter(parm) + "&";
			}

			Matcher matPags = patPags.matcher(paginas);
			if (matPags.find()) {
				String strLink = matPags.group(1);
				String strSeparador = " - ";
				String strA = " - ";
				Pattern patLink = Pattern.compile("<a\\s(.*?)>", Pattern.DOTALL);
				Matcher matLink = patLink.matcher(strLink);
				if (matLink.find()) {
					strA = matLink.group();
				}
				Pattern patSeparador = Pattern.compile("<!-- ini separador -->(.*?)<!-- fim separador -->", Pattern.DOTALL);
				Matcher matSeparador = patSeparador.matcher(strLink);
				if (matSeparador.find()) {
					strSeparador = matSeparador.group(1);
				}
				StringBuilder sb = new StringBuilder();

				paginacao.setTotalRows(rowCount);
				paginacao.setPagina(pagina);
				paginacao.setMaxResults(max);
				List<Link> lstLinks = paginacao.getLinks();

				if (lstLinks.size() > 0) {
					Link link = lstLinks.get(0);
					if (link.getHref().equals(String.valueOf(pagina))) {
						sb.append(link.getLabel());
					} else {
						sb.append(HtmlUtils.substituiAtributoTag(strA, "a", "href", "index.jsp?" + qString + "pagina=" + link.getHref()));
						sb.append(link.getLabel());
						sb.append("</a>");
					}
					if (lstLinks.size() > 1)
						sb.append(strSeparador);
					for (int i = 1; i < lstLinks.size(); i++) {
						link = lstLinks.get(i);
						if (link.getHref().equals(String.valueOf(pagina))) {
							sb.append(link.getLabel());
						} else {
							sb.append(HtmlUtils.substituiAtributoTag(strA, "a", "href", "index.jsp?" + qString + "pagina=" + link.getHref()));
							sb.append(link.getLabel());
							sb.append("</a>");
						}
						sb.append(strSeparador);
					}
				}
				paginas = matPags.replaceAll(sb.toString());
			}
			// colocar prox e anterior

			Matcher matAntProx = patAnterior.matcher(paginas);
			if (matAntProx.find()) {
				String anterior = matAntProx.group(1);
				if (pagina > 0) {
					paginas = matAntProx.replaceAll(HtmlUtils.substituiAtributoTag(anterior, "a", "href", "index.jsp?" + qString + "pagina=" + (pagina - 1)));
				} else {
					Matcher matAs = patLimpaLink.matcher(anterior);
					if (matAs.find()) {
						anterior = matAs.replaceAll("");
					}
					paginas = matAntProx.replaceAll(anterior);
				}
			}
			matAntProx = patProxima.matcher(paginas);
			if (matAntProx.find()) {
				String anterior = matAntProx.group(1);
				if (pagina < paginacao.getLastPage()) {
					paginas = matAntProx.replaceAll(HtmlUtils.substituiAtributoTag(anterior, "a", "href", "index.jsp?" + qString + "pagina=" + (pagina + 1)));
				} else {
					Matcher matAs = patLimpaLink.matcher(anterior);
					if (matAs.find()) {
						anterior = matAs.replaceAll("");
					}
					paginas = matAntProx.replaceAll(anterior);
				}
			}

			template = mat.replaceAll(paginas);
		}
	}
	/**
	 * Encaixa o resultSet no template
	 * @param rs resultado de uma busca
	 * @param maximo valor máximo para ser mostrado no navegador
	 */
	public void encaixaResultSet(ResultSet rs,int maximo) {
		try {
			String templateRs = "";
			Matcher mat = patRs.matcher(template);
			int i=0;
			if (mat.find()) {
				templateRs = mat.group(1);
				StringBuilder sb = new StringBuilder();
				String item;
				if(rs.getRow()==1){
					do {
						item = templateRs;
						Matcher matCol = patRsItens.matcher(templateRs);
						while (matCol.find()) {
							String g1=matCol.group(1);
							item = atribuirRsString(item, rs.getString(g1), g1,g1+"_"+i);
						}
						if(rsItemCustomizado!=null){
							item=rsItemCustomizado.tratar(rs, item);
						}
						sb.append(item);
					}while (rs.next() && maximo>i++);
				}else{
					while (rs.next() && maximo>i++) {
						item = templateRs;
						Matcher matCol = patRsItens.matcher(templateRs);
						while (matCol.find()) {
							String g1=matCol.group(1);
							item = atribuirRsString(item, rs.getString(g1), g1,g1+"_"+i);
						}
						if(rsItemCustomizado!=null){
							item=rsItemCustomizado.tratar(rs, item);
						}
						sb.append(item);
					}
				}
				template = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(sb.toString()));
			}
		} catch (Exception e) {
			logger.error("Erro", e);
		}
	}
	public void encaixaResultSet(ResultSet rs) {
		encaixaResultSet(rs,Integer.MAX_VALUE);
	}

	public void replace(String procurar,String substituir){
		template = template.replace(procurar, substituir);
	}
	
	/**
	 * Retorna o resultado e reinicia
	 * @return o template alterado com as informações
	 */
	public String getResultado() {
		String retorno = template;
		template = templateOriginal;
		return retorno;
	}
	
	
	
	/**
	 * Retorna o objeto template de uma área
	 * @param area nome da área
	 * @return Template da área
	 */
	public Template getTemplate(String area){
		Template temp = subTemplate.get(area);
		if(temp==null){
			temp = new Template();
			temp.setTemplate(getArea(area));
			subTemplate.put(area,temp);
		}
		return temp;
	}
	/**
	 * Coloca o HTML do template no objeto
	 * @param template HTML
	 */
	public void setTemplate(String template) {
		this.templateOriginal = template;
		this.template = template;
	}
	/**
	 * Encaixa um html no espaço para resultset
	 * @param html código HTML
	 */
	public void encaixaResultSet(String html){
		Matcher mat = patRs.matcher(template);
		if (mat.find()) {
			template = mat.replaceAll(html);
		}
	}
	/**
	 * Limpa a área para resultset
	 */
	public void limpaResultSet(){
		Matcher mat = patRs.matcher(template);
		if (mat.find()) {
			template = mat.replaceAll("");
		}
	}
	/**
	 * Coloca a lista de resultados no template
	 * @param resultado lista de objetos do resultado
	 * @param max numero maximo na pagina
	 */
	@SuppressWarnings("unchecked")
	public void encaixaResultSet(List resultado,int ini, int max) {
		try {
			String templateRs = "";
			Matcher mat = patRs.matcher(template);
			if (mat.find()) {
				templateRs = mat.group(1);
				StringBuilder sb = new StringBuilder();
				String item;
				int tot = resultado.size();
				for(int i=ini;i<tot && i<max;i++) {
					Object obj = resultado.get(i);
					Class cls = obj.getClass();
					String baseName = cls.getSimpleName();
					String id = i+"";
					try{
						id = cls.getMethod("getId").invoke(obj).toString();
					}catch(Exception e){
						
					}
					item = templateRs;
					Matcher matCol = patRsItens.matcher(templateRs);
					while (matCol.find()) {
						String chave = matCol.group(1);
						try{
							Method meth = cls.getMethod(chave);
							Object retobj = meth.invoke(obj);
							item = atribuirRsString(item,retobj==null?"":retobj.toString(),chave,baseName+"_"+id+"."+meth.getName());
						} catch(NoSuchMethodException e){
							logger.warn("o metodo nao existe "+e.getMessage());
							try{
								Method meth = cls.getMethod("get" + Character.toUpperCase(chave.charAt(0)) + chave.substring(1));
								Object retobj = meth.invoke(obj);
								item = atribuirRsString(item,retobj==null?"":retobj.toString(),chave,baseName+"_"+id+"."+chave);
							} catch(NoSuchMethodException e2){
								logger.warn("também nao existe "+e2.getMessage());
							}	
						}
					}
					//procurar pelas imagens
					matCol = patRsImagens.matcher(item);
					while(matCol.find()){
						try{
							String chave = matCol.group(1);
							Object retobj = cls.getMethod(chave).invoke(obj);
							logger.info("imagem " + retobj.toString());
							item = matCol.replaceAll(substituiSrcImagem(matCol.group(2), retobj.toString()));
						} catch(NoSuchMethodException e){
							logger.warn("o metodo nao existe "+e.getMessage());
						}
					}
					if(rsItemCustomizado!=null){
						item=rsItemCustomizado.tratar(obj, item);
					}
					sb.append(item);
				}
				template = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(sb.toString()));
			}
		} catch (Exception e) {
			logger.error("Erro ",e);
		}
		
	}
	/**
	 * Coloca a lista de resultados no template<br>
	 * <code>
	 * <pre>
	 * &lt;!-- ini rs --&gt;
	 * 	&lt;!-- ini rs imagem(getUrlRelativaJpg) --&gt;
	 * 		&lt;img src="#" /&gt;
	 * 	&lt;!-- fim rs imagem(getUrlRelativaJpg) --&gt;
	 * &lt;!-- fim rs --&gt;
	 * </pre>
	 * </code>
	 * @param resultado lista de objetos do resultado
	 */
	@SuppressWarnings("unchecked")
	public void encaixaResultSet(List resultado) {
		encaixaResultSet(resultado,0,resultado.size());
	}
	
	/**
	 * 
	 * @param nome
	 * @param href
	 * @throws AreaNaoEncontradaException 
	 */
	public void setLink(String nome,String href) throws AreaNaoEncontradaException{
		if(href==null){
			logger.info("retirando o link "+nome);
			setArea("link("+nome+")","");
		}else{
			String link = getArea("link("+nome+")");
			String novo = Template.substituiHrefA(link,href);
			template = template.replace(link, novo);
		}
	}
	public String getArea(String area) {
		area = area.replace("(","\\(");
		area = area.replace(")","\\)");
		area = area.replace(".","\\.");
		Pattern patArea = Pattern.compile("<!-- ini "+area+" -->(.*?)<!-- fim "+area+" -->",Pattern.DOTALL);
		Matcher mat = patArea.matcher(template);
		if(mat.find()){
			return mat.group(1);
		}
		return "";
	}
	/**
	 * A area é no seguinte formato
	 * &lt;!-- ini NomeDaArea --&gt;
	 * @param area
	 */
	public void retirarArea(String area){
		try{
			setArea(area,"");
		}catch(Exception e){
			
		}
	}
	
	/**
	 * Metodo para conveniencia<br>
	 * igual a setArea("mensagem", mensagem);
	 * @param mensagem
	 */
	public void setMensagem(String mensagem){
		try{
			setArea("mensagem", mensagem);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Se houver uma marcacao &lt;-- ini box "area" --> e o valor for nul ou "" o box sera retirado.
	 * @param area
	 * @param valor
	 * @throws AreaNaoEncontradaException
	 */
	public void setArea(String area, String valor) throws AreaNaoEncontradaException {
		area = area.replace("(","\\(");
		area = area.replace(")","\\)");
		area = area.replace(".","\\.");
		boolean boxApagado = false;
		if(valor==null || valor.equals("")){
			Pattern patBoxArea = Pattern.compile("<!-- ini box "+area+" -->(.*?)<!-- fim box "+area+" -->",Pattern.DOTALL);
			Matcher mat = patBoxArea.matcher(template);
			if(mat.find()){
				template = mat.replaceAll("");
				boxApagado = true;
			}
		}
		if(!boxApagado){
			if(valor==null) valor="";
			Pattern patArea = Pattern.compile("<!-- ini "+area+" -->(.*?)<!-- fim "+area+" -->",Pattern.DOTALL);
			Matcher mat = patArea.matcher(template);
			if(mat.find()){
				template = mat.replaceAll(StringUtils.tratarCaracteresEspeciaisRegex(valor));
			}else{
				throw new AreaNaoEncontradaException(area); 
			}
		}
	}
	public static String substituiSrcImagem(String html, String novoSrc) {
		return substituiAtributoTag(html, "img", "src", novoSrc);
	}
	/**
	 * Substitui todos os atributos href de tags a
	 * @param html código HTML
	 * @param novoHref novo href
	 * @return html alterado
	 */
	public static String substituiHrefA(String html, String novoHref) {
		return substituiAtributoTag(html, "a", "href", novoHref);
	}
	public static String substituiAtributoTag(String html, String tag, String atributo, String novoValor) {
		String retorno = "";
		String reg = "<" + tag + "(\\s|\\s[^<]*?\\s)" + atributo + "=\".*?\"(.*?)>";
		Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
		Matcher mat = pat.matcher(html);
		if (mat.find()) {
			String ini = mat.group(1);
			String fim = mat.group(2);
			retorno = mat.replaceAll("<" + tag + ini + atributo + "=\"" + novoValor + "\" " + fim + ">");
		}
		return retorno;
	}
	public void substituirAtributoTagId(String tagName,String id, String atributo, String novoValor){
		String reg = "<"+tagName+"(\\s|\\s([^<]*?)\\s)id=\"" + id + "\".*?>";
		Pattern pat = Pattern.compile(reg,Pattern.DOTALL);
		Matcher mat = pat.matcher(template);
		if (mat.find()) {
			String tag = mat.group();
			//limpar o atributo
			Pattern patAtr = Pattern.compile("\\s"+atributo + "=\".*?\"",Pattern.DOTALL);
			Matcher matAtr = patAtr.matcher(tag);
			if(matAtr.find()){
				tag = matAtr.replaceAll(" "+atributo+"=\""+novoValor+"\"");
			}else{
				if(tag.endsWith("/>")){
					tag = tag.substring(0,tag.length()-2)+" "+atributo+"=\""+novoValor+"\" />";
				}else{
					tag = tag.substring(0,tag.length()-1)+" "+atributo+"=\""+novoValor+"\" >";
				}
			}
			template = mat.replaceAll(tag);
		}
	}
	/**
	 * Substitui um atributo de uma tag
	 * @param html código html
	 * @param tag nome da tag Ex.: img
	 * @param name nome do atributo nome da tag Ex.: &ltimg name="NOME"
	 * @param atributo nome do atributo a ser alterado
	 * @param novoValor valor do atributo
	 * @return codigo html alterado
	 */
	public static String substituiAtributoTag(String html, String tag,String name,String type, String atributo, String novoValor) {
		String reg = "<" + tag + "(\\s|\\s[^<]*?\\s)" + atributo + "=\".*?\"(.*?)>";
		Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
		Matcher mat = pat.matcher(html);
		while (mat.find()) {
			if(mat.group().indexOf("name=\""+name+"\"")!=-1 && mat.group().indexOf("type=\""+type+"\"")!=-1){
				String ini = mat.group(1);
				String fim = mat.group(2);
				int st = mat.start();
				int en = mat.end();
				html = html.substring(0,st)+"<" + tag + ini + atributo + "=\"" + novoValor + "\" " + fim + ">"+html.substring(en);
				break;
			}
		}
		return html;
	}
	/**
	 * Substitui um atributo de uma tag
	 * @param html código html
	 * @param tag nome da tag Ex.: img
	 * @param name nome do atributo nome da tag Ex.: &ltimg name="NOME"
	 * @param atributo nome do atributo a ser alterado
	 * @param novoValor valor do atributo
	 * @return codigo html alterado
	 */
	public static String substituiAtributoTag(String html, String tag,String name, String atributo, String novoValor) {
		String reg = "<" + tag + "(\\s|\\s[^<]*?\\s)" + atributo + "=\".*?\"(.*?)>";
		Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
		Matcher mat = pat.matcher(html);
		while (mat.find()) {
			if(mat.group().indexOf("name=\""+name+"\"")!=-1){
				String ini = mat.group(1);
				String fim = mat.group(2);
				int st = mat.start();
				int en = mat.end();
				html = html.substring(0,st)+"<" + tag + ini + atributo + "=\"" + novoValor + "\" " + fim + ">"+html.substring(en);
				break;
			}
		}
		return html;
	}
	public static String substituiEntre(String procurarEm,String ini, String fim, String conteudo) {
		int i=procurarEm.indexOf(ini);
		if(i!=-1){
			int f = procurarEm.indexOf(fim,i);
			if(f!=-1){
				procurarEm=procurarEm.substring(0,i+ini.length())
				+conteudo+
				procurarEm.substring(f);
			}
		}
		return procurarEm;
	}
	public RsItemCustomizado getRsItemCustomizado() {
		return rsItemCustomizado;
	}
	/**
	 * Informa um objeto que customiza o item gerado da forma padrão pelo Template
	 * @param rsItemCustomizado
	 */
	public void setRsItemCustomizado(RsItemCustomizado rsItemCustomizado) {
		this.rsItemCustomizado = rsItemCustomizado;
	}
	public void substituirAtributoTagId(String tagName, String id, String atributo, int quantidade) {
		substituirAtributoTagId(tagName, id, atributo, quantidade+"");
	}
	public void setArea(String area, int inteiro) throws AreaNaoEncontradaException {
		this.setArea(area, String.valueOf(inteiro));
	}
	/**
	 * Não entende pai, filho, etc.
	 * @param codHmtl
	 * @param tag
	 * @param name
	 * @param value
	 * @return código alterado
	 */
	public static String substituiConteudoTag(String codHmtl, String tag, String name, String value) {
		final String reg = "<" + tag + "(\\s|\\s[^<]*?\\s)name=\""+name+"\"([^>]*)>";
		Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
		Matcher matcher = pat.matcher(codHmtl);
		if (matcher.find()){
			logger.info("iniTag = " + matcher.group());
			return substituiEntre(codHmtl, matcher.group(), "</"+tag+">",value);
		}
		return codHmtl;
	}
	
	/**
	 * 
	 * @param selectName
	 * @param valor
	 * @return this
	 */
	public Template setSelect(String selectName,String valor){
		template = marcarSelect(template, selectName, valor);
		return this; 
	}
	
	/**
	 * 
	 * @param selectName
	 * @param valor
	 * @param list
	 */
	public void setSelect(String selectName, String valor, String[] list) {
		String opts="";
		for(String aux:list){
			opts+="<option value=\""+aux.replace("\"", "&quot;")+"\">"+aux.replace("<","&lt;")+"</option>";
		}
		logger.info("opts = '" + opts + "'");
		template = substituiConteudoTag(template, "select", selectName, opts);
		template = marcarSelect(template, selectName, valor);
	}
	/**
	 * Marcar o item de um select
	 * @param String codHtml, codigo html a ser alterado
	 * @param String selectName, nome do select o qual deve ser alterado
	 * @param String valor, valor que devera ser selecionado
	 * @return String codHtml
	 */
	public static String marcarSelect(String codHtml,String selectName,String valor){
		//achar a tag de select de acordo com o nome
		Pattern patSelect = Pattern.compile("(<select[^>]*?(\\sname=\""+selectName+"\")[^>]*>)(.*?)</select>",Pattern.DOTALL);
		Matcher matSelect = patSelect.matcher(codHtml);
		if(matSelect.find()){
			String opts = matSelect.group(3);
			//retirar qualquer marca de selected="selected"
			opts = opts.replace(" selected=\"selected\"", "");
			//achar o valor nos options e marcar
			Pattern patOpt = Pattern.compile("<option[^>]*?value=\""+valor+"\"[^>]*?>");
			Matcher matOpt = patOpt.matcher(opts);
			if(matOpt.find()){
				String opt = matOpt.group();
				opt = opt.substring(0, opt.length()-1) + " selected=\"selected\">";
				opts = matOpt.replaceAll(opt);
			}
			codHtml = matSelect.replaceAll(matSelect.group(1)+opts+"</select>");
		}
		return codHtml;
	}
	/**
	 * Le o arquivo de template na pasta template<br>
	 * O encoding padrao sera ISO-8859-1<br>
	 * Subistutui os atributos href=img.gif para href=template/img.gif<br>
	 * Coloca o menu.html entre o &lt;!-- ini menu --&gt;
	 * @param string
	 * @throws IOException
	 */
	public void setTemplateFile(String string) throws IOException {
		String retorno = "";
		String template = FileUtils.readFileToString(new File(getTemplatePath(),string), "ISO-8859-1");
		Pattern patHref = Pattern.compile("\\shref=\"(.*?)\"");
		Matcher mat = patHref.matcher(template);
		int last = 0;
		int fim = 0;
		while(mat.find()){
			String hrefVal = mat.group(1);
			if(hrefVal.startsWith("/")
					|| hrefVal.startsWith("http:")
					|| hrefVal.startsWith("https:")){
				continue;
			}
			//ver se o arquivo existe
			if(new File(getTemplatePath(),hrefVal.replace('/', File.separatorChar)).exists()){
				//Existe entao substituimos...
				fim = mat.start();
				retorno += template.substring(last,fim) + " href=\"template/" + hrefVal + "\"";
				last = mat.end();
			}
		}
		//copiar o fim
		retorno += template.substring(last);
		setTemplate(retorno);
		
		//ler o menu
		try{
			String menuHtml = FileUtils.readFileToString(new File(getTemplatePath(),"menu.html"), "ISO-8859-1");
			this.setArea("menu", menuHtml);
		}catch(Exception e){
			logger.warn("ini menu nao atribuido com o conteudo de template/menu.html");
		}
	}
	
	@Override
	public String toString() {
		return getResultado();
	}
	
	/**
	 * Set area e tambem campos do tipo input<br>
	 * se o valor for null completa o campo com ""
	 * @param area
	 * @param id
	 */
	public void set(String area, Long valor) {
		try{
			String v = valor==null?"":valor.toString();
			setArea(area, v);
			template = Template.substituiAtributoTag(template, "input",area, "value", v);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param name &lt;textarea name="NAME"
	 * @param valor valor, substiruiremos o &lt; por &amp;lt;
	 */
	public void setTextArea(String name, String valor) {
		if(valor == null) valor = "";
		template = Template.substituiConteudoTag(template, "textarea", name, valor.replace("<", "&lt;"));
	}
	
	/**
	 * 
	 * @param name &lt;input name="NAME"
	 * @param valor substituiremos o " por &amp;quot;
	 */
	public void setInput(String name, String valor) {
		if(valor == null){
			valor = "";
		}
		template = Template.substituiAtributoTag(template, "input",name, "value", valor.replace("\"", "&quot;"));
	}
	
	/**
	 * 
	 * @param name &lt;input name="NAME"
	 * @param valor se for null sera substituido por ""
	 */
	public void setInput(String name, Long valor) {
		if(valor == null){
			setInput(name, "");
		}else{
			setInput(name, valor+"");
		}
	}
	
	/**
	 * &lt;form action="acao.do"><br>
	 * &lt;-- ini area --><br>
	 * Esta area sera substituida por campos criados automaticamente<br>
	 * Por enquanto nao coloque os campos que vc quer!
	 * por reflection<br>
	 * &lt;-- fim area --><br>
	 * &lt;/form>
	 * @param area
	 * @param obj
	 * @throws AreaNaoEncontradaException 
	 */
	public void setForm(String area, Object obj) throws AreaNaoEncontradaException {
		try{
			if(obj!=null){
				String campos = criarCampos(obj.getClass().getSimpleName()+".",obj);
				setArea(area, campos);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Cria a area com opcoes de radio,
	 * Padrão:
	 * label = nome<br>
	 * value = id<br>
	 * @param string
	 * @param camadas
	 * @throws AreaNaoEncontradaException 
	 */
	
	@SuppressWarnings("unchecked")
	public void setRadio(String string, List opcoes,Object checked) throws AreaNaoEncontradaException {
		String html = "";
		try{
			String fieldName = opcoes.get(0).getClass().getSimpleName()+".id";
			for (Object obj : opcoes) {
				//ver se o obj tem id e nome
				String id = obj.getClass().getMethod("getId").invoke(obj).toString();
				String label="";
				Object lbl = obj.getClass().getMethod("getNome").invoke(obj);
				if(lbl==null){
					label = "<span title=\""+fieldName+"="+id + "\">Sem nome ("+(semNomeNum++)+")</span>";
				}else{
					label = lbl.toString();
				}
				html+="<div><label for=\""+fieldName+"_"+id+"\">"+label+"</label><input type=\"radio\" name=\""+fieldName+"\" id=\""+fieldName+"_"+id+"\" value=\""+id+"\" ";
				if(checked!=null && checked.toString().equals(id)){
					html+="checked=\"checked\" ";
				}
				html+="/></div>";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		setArea(string, html);
	}

	

	
	
	
	
	
}
