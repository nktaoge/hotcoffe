package br.com.goals.template;

/**
 * Interface para criar um html de um item 
 */
public interface RsItemCustomizado {
	/**
	 * Customiza o render de um obj
	 * @param obj objeto atual na linha &lt;!-- ini rs -->
	 * @param item html do item tratado pelo template
	 * @return Html customizado com o obj
	 */
	public String tratar(Object obj,String item);
}
