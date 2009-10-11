package br.com.goals.template;

public class Link {
	private String label;
	private String href;
	public Link(int label,String href){
		this.label=String.valueOf(label);
		this.href=href;
	}
	public Link(int i, int j) {
		label=String.valueOf(i);
		href=String.valueOf(j);
	}
	public Link(String label, String href) {
		this.label=label;
		this.href=href;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
