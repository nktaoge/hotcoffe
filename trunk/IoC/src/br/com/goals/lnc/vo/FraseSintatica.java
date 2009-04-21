package br.com.goals.lnc.vo;

/**
 * SVP
 * @author fabio
 *
 */
public class FraseSintatica {
	private Sujeito sujeito;
	private Verbo verbo;
	private Predicado predicado;
	private int certeza = 0;
	public int getCerteza() {
		return certeza;
	}
	public void setCerteza(int certeza) {
		this.certeza = certeza;
	}
	public Sujeito getSujeito() {
		return sujeito;
	}
	public void setSujeito(Sujeito sujeito) {
		this.sujeito = sujeito;
	}
	public Verbo getVerbo() {
		return verbo;
	}
	public void setVerbo(Verbo verbo) {
		this.verbo = verbo;
	}
	public Predicado getPredicado() {
		return predicado;
	}
	public void setPredicado(Predicado predicado) {
		this.predicado = predicado;
	}
	@Override
	public String toString() {
		return "FraseSintatica:\n\t" + sujeito + "\n\t" + verbo + "\n\t" + predicado;
	}
}
