package br.com.goals.hotcoffe.ioc;

public class TestQuickStart {
	public static void main(String[] args) {
		try {
			System.out.println(QuickStart.getClasses("br.com.goals.hotcoffe.ioc.casosdeuso"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
