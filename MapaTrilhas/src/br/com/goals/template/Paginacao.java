package br.com.goals.template;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Paginacao {
	int resultadosPorPagina = 5;

	int totalRows;

	int pagina = 0;

	int maxLinksDePaginas = 10;

	int totalPagina;

	StringBuilder links = null;

	ArrayList<Link> arrLinks;
	
	public Paginacao() {
		arrLinks = new ArrayList<Link>();
	}

	public List<Link> getLinks() {
		if (links == null) {
			makeLinks();
		}
		//return links.toString();
		return arrLinks;
	}

	private void makeLinks() {
		links = new StringBuilder();
		totalPagina = totalRows / resultadosPorPagina;
		if (totalRows % resultadosPorPagina != 0) {
			totalPagina++;
		}
		int i, metade = (maxLinksDePaginas / 2) - 1;
		int startAt =pagina - metade < 0 ? 0 : pagina - metade;
		if((totalPagina-pagina)<maxLinksDePaginas && totalPagina>maxLinksDePaginas){
			startAt=totalPagina-maxLinksDePaginas;
		}
		for (i = startAt;	i < startAt+maxLinksDePaginas && i < totalPagina; i++) {
			arrLinks.add(new Link(i+1,i));
		}
	}

	public int getFirstResultNumber() {
		return pagina * resultadosPorPagina;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(Integer paginaAtual) {
		if (paginaAtual == null){
			this.pagina = 0;
		}else{
			this.pagina = paginaAtual;
		}
	}

	

	/**
	 * Se houver muitos resultados troque tudo para bigInteger
	 * 
	 * @param bigInteger
	 */
	public void setTotalRows(BigInteger bigInteger) {
		this.totalRows = Integer.valueOf(bigInteger.toString());
	}
	/**
	 * Resultados por pagina
	 */
	public void setMaxResults(int v){
		resultadosPorPagina = v;
	}
	/**
	 * Resultados por pagina
	 */
	public int getMaxResults() {
		return resultadosPorPagina;
	}

	public int getLastPage() {
		return totalPagina-1;
	}

}
