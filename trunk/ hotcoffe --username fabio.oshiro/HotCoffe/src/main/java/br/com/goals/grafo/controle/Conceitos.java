package br.com.goals.grafo.controle;

import java.util.HashSet;

import br.com.goals.grafo.modelo.Ponto;
import br.com.goals.grafo.persistencia.PontoDao;

/**
 * Responsavel por carregar conceitos iniciais<br>
 * Criado pois nao aguentava mais trocar as descricoes<br>
 * Agora as descrições podem ser comentadas :-)
 * 
 * @author Fabio Issamu Oshiro
 *
 */
public class Conceitos {
	private static PontoDao pontoDao = PontoDao.getInstance();
	public static final String DUVIDA = "Dúvida, algo desconhecido";
	public static final String MENSAGEM_AO_CAL = "Uma mensagem de algo emitida ao CAL";
	public static final String ALGO_MENSAGEIRO = "Algo não identificado que emite mensagem";
	public static final String INSTANCIA_ALGO = "Algo que falou";
	public static final String INSTANCIA_MENSAGEM = "Instância de uma mensagem";
	public static final String ATRIBUTO = "Atributo de algo";
	public static final String VALOR_ATRIBUTO = "Valor de um atributo";
	public static final String VERBO = "Verbo";
	public static final String VERBO_SER_PRESENTE = "Verbo 'ser' no presente (é)";
	public static final String ARTIGO = "Artigo o, a, the, etc";
	public static final String QUEM = "Para articular questões";
	public static final String PONTO_INTERROGACAO = "Simples ponto de interrogação";
	public static final String INSTANCIA_PREDICADO = "Grupo predicado";
	public static final String THE_CAL = "Ele mesmo, o EU, ego, etc.";
	public static final String SIGNIFICA =	"Siginifica";
	public static final String GRUPO = "Simples agrupamento";
	public static final String A_OPOSTO_B = "Oposicao de algo, A é oposto a B e o tipo é 'nao'";
	
	public static Ponto ponto_interrogacao;
	public static Ponto duvida;
	public static Ponto verbo;
	public static Ponto grupo;
	public static Ponto artigo;
	public static Ponto artigo_o;
	public static Ponto verboSerPresente;
	public static Ponto quem;
	public static Ponto a_oposto_b;
	public static Ponto nao;
	public static Ponto sim;
	private static HashSet<Long> conceitosBasicosID = new HashSet<Long>();
	public static Ponto p_extends;
	public static Ponto significa;
	public static Ponto algo_mensageiro;
	/*
	 * Programacao e Orientacao a Objetos
	 * P para significar Programacao
	 */
	/**
	 * é um tipo de, é como uma "MAS" não exatamente
	 */
	public static final String P_EXTENDS = "p Extends";
	public static final String P_INSTANCIA = "instancia, ocorrencia";
	/**
	 * é um, é uma
	 */
	public static final String P_CLASS = "p Class";
	public static Ponto p_instancia;
	/*
	 * Programacao estruturada
	 */
	public static Ponto p_if;
	public static Ponto p_else;
	
	/**
	 * Carrega no banco alguns conceitos iniciais
	 */
	public static void carregarConceitos(){
		duvida = criar(DUVIDA);
		verbo = criar(VERBO);
		artigo = criar(ARTIGO);
		
		/*
		 * Parte de sim e nao
		 */
		a_oposto_b = criar(A_OPOSTO_B);
		nao = new Ponto("Palavra nao");
		nao.setNome("não");
		nao = criarOuAcharPorNome(nao);
		sim = new Ponto("Palavra sim");
		sim.setNome("sim");
		sim = criarOuAcharPorNome(sim);
		
		criar(MENSAGEM_AO_CAL);
		algo_mensageiro = criar(ALGO_MENSAGEIRO);
		criar(ATRIBUTO);
		criar(VALOR_ATRIBUTO);
		p_extends = criar(P_EXTENDS);
		significa = criar(SIGNIFICA);
		grupo = criar(GRUPO);
		p_instancia = criar(P_INSTANCIA);
		Ponto pararSom = new Ponto("Comando para parar som");
		pararSom.setNome("silêncio");
		pararSom.setClasse("PararSom");
		criarOuAcharPorDescricao(pararSom);
		
		criarVerboSer();
		
		artigo_o = new Ponto("Primeiramente como artigo do sistema, mas pode ter outro sentido.");
		artigo_o.setNome("o");
		artigo_o=criarOuAcharPorNome(artigo_o);
		pontoDao.ligarSeDesligado(artigo, artigo_o,p_extends);
		
		ponto_interrogacao = new Ponto(PONTO_INTERROGACAO);
		ponto_interrogacao.setNome("?");
		ponto_interrogacao = criarOuAcharPorNome(ponto_interrogacao);
		
		quem = new Ponto(QUEM);
		quem.setNome("quem");
		quem = criarOuAcharPorNome(quem);
		
		criar(THE_CAL);
	}
	private static void criarVerboSer(){
		verboSerPresente = new Ponto(VERBO_SER_PRESENTE);
		verboSerPresente.setClasse("SerPresente");
		verboSerPresente = criarOuAcharPorDescricao(verboSerPresente);
		pontoDao.ligarSeDesligado(verbo, verboSerPresente,p_extends);
		
		Ponto palavraEh = new Ponto();
		palavraEh.setNome("é");
		palavraEh = criarOuAcharPorNome(palavraEh);
		pontoDao.ligarSeDesligado(verboSerPresente, palavraEh,significa);
	}
	/**
	 * Facilitador
	 * @param descricao descrição do ponto
	 * @return ponto criado
	 */
	private static Ponto criar(String descricao){
		Ponto ponto =pontoDao.acharOuCriarPorDescricao(descricao);
		conceitosBasicosID.add(ponto.getPontoId());
		return ponto;
	}
	private static Ponto criarOuAcharPorNome(Ponto ponto){
		ponto = pontoDao.acharOuCriarPorNome(ponto);
		conceitosBasicosID.add(ponto.getPontoId());
		return ponto;
	}
	private static Ponto criarOuAcharPorDescricao(Ponto ponto){
		ponto = pontoDao.acharOuCriarPorDescricao(ponto);
		conceitosBasicosID.add(ponto.getPontoId());
		return ponto;
	}
	public static boolean ehConceitoBasico(Ponto ponto){
		return conceitosBasicosID.contains(ponto.getPontoId());
	}
	
	public static void programming(){
		//quem.getClass().
	}
}
