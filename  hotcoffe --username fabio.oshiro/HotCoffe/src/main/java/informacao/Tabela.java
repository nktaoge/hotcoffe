package informacao;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Tabela extends JFrame{
	private static final long serialVersionUID = -4419216375530458240L;
	public static final int TOTG = 2;
	javax.swing.table.DefaultTableModel dtm;
	public Tabela(){
		//super(new BorderLayout());
		
		JPanel central = new JPanel(new BorderLayout());
		
		JTable tabela = new JTable();
		JScrollPane scrollpane = new JScrollPane(tabela); 
		tabela.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] { },
				new String [] {
				//aqui adiciona-se as colunas e seus respectivos nomes
				" ","1","2","3","4","5","6","E","P","C","Q","E","P","C","Q"
				}
				));
		dtm =(javax.swing.table.DefaultTableModel)tabela.getModel();
		//lembre-se um "" para cada coluna na tabela
		for(int i=1;i<=TOTG;i++){
			dtm.addRow(new Object[]{"E "+i,"","","","","","","","","","","","","",""});
		}
		
		Thread t = new Thread(){
			public void run(){
				while(true){
					try{
						Thread.sleep(1000);
						recalcular();
					}catch(Exception e){
						
					}
				}
			}
		};
		t.start();
		central.add(scrollpane,BorderLayout.CENTER);
		this.add(central,BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public final int vRi = 1;
	public final int vAg = 2;
	public final int vFl = 3;
	public final int vRe = 4;
	
	protected void recalcular() {
		
		int E,P,C,Q,Ri,Ag,Fl,Re;
		for(int i=0;i<=TOTG;i++){
			
			E=P=C=Q=100;
			Ri=Ag=Fl=Re=0;
			for(int j=1;j<=7;j++){
				try{
					int a = Integer.parseInt(dtm.getValueAt(i,j).toString());
					switch(a){
					case vRi:
						Ri++;
						break;
					case vAg:
						Ag++;
						break;
					case vFl:
						Fl++;
						break;
					case vRe:
						Re++;
						break;
					}
				}catch(Exception e){
					
				}
			}
			Re = natural(Re - Ag);
			Ag = natural(Ag - Re);
			Ri = natural(Ri - Fl);
			Fl = natural(Fl - Ri);
			if(Ri>=0 && Ag>0){
				E+=Ri*5;
				Q-=Ag*5;
			}
			if(Re>=0 && Ri>0){
				P+=Ri*5;
				C-=Re*5;
			}
			if(Fl>=0 && Re>0){
				E-=Re*5;
				C+=Fl*5;
			}
			if(Ag>=0 && Fl>0){
				P-=Ag*5;
				Q+=Fl*5;
			}
			dtm.setValueAt("E"+E, i, 11);
			dtm.setValueAt("P"+P, i, 12);
			dtm.setValueAt("C"+C, i, 13);
			dtm.setValueAt("Q"+Q, i, 14);
			System.out.println("E="+E + " P="+P+" C="+C +" Q="+Q);
		}
		
		
	}
	int natural(int i){
		return i<0?0:i;
	}
	public static void main(String args[]){
		new Tabela();
	}
}
