package informacao;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Tabela extends JFrame{
	private static final long serialVersionUID = -4419216375530458240L;
	public Tabela(){
		//super(new BorderLayout());
		JPanel central = new JPanel(new BorderLayout());
		JTable tabela = new JTable();
		tabela.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] { },
				new String [] {
				//aqui adiciona-se as colunas e seus respectivos nomes
				" ","1","2","3","4","5","6","E","P","C","Q","E","P","C","Q"
				}
				));
		javax.swing.table.DefaultTableModel dtm =
			(javax.swing.table.DefaultTableModel)tabela.getModel();
			//lembre-se um "" para cada coluna na tabela
		dtm.addRow(new Object[]{"","J1","J2","J3","J4","J5","J6","E","P","C","Q","E","P","C","Q"});
		for(int i=1;i<21;i++){
			dtm.addRow(new Object[]{"E"+(i+1),"","","","","","","","","","","","","",""});
		}
		central.add(tabela,BorderLayout.CENTER);
		this.add(central,BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String args[]){
		new Tabela();
	}
}
