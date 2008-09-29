

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Tabela extends JFrame {
	private static final long serialVersionUID = -4419216375530458240L;
	public static final int TOTG = 20;
	public static final int COL_TOTPROGRAMA = 9;
	private static boolean areacritica = false;
	private static final String basePath = "";
	javax.swing.table.DefaultTableModel dtm;

	public Tabela() {
		super("Atitudes");
		JButton btnSalvar = new JButton("Salvar");
		JPanel central = new JPanel(new BorderLayout());

		JTable tabela = new JTable();
		JScrollPane scrollpane = new JScrollPane(tabela);
		tabela.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] {
						// aqui adiciona-se as colunas e seus respectivos nomes
						" ", "1", "2", "3", "4", "5", "6", "E", "P", "C", "Q",
						"E", "P", "C", "Q" }));
		dtm = (javax.swing.table.DefaultTableModel) tabela.getModel();
		// lembre-se um "" para cada coluna na tabela
		for (int i = 1; i <= TOTG; i++) {
			dtm.addRow(new Object[] { "E " + i, "", "", "", "", "", "", "", "",
					"", "", "", "", "", "" });
		}
		dtm.addRow(new Object[] { "Total Ri", "", "", "", "", "", "", "", "", "", "", "", "", "", "" });
		dtm.addRow(new Object[] { "Total Ag", "", "", "", "", "", "", "", "", "", "", "", "", "", "" });
		dtm.addRow(new Object[] { "Total Fl", "", "", "", "", "", "", "", "", "", "", "", "", "", "" });
		dtm.addRow(new Object[] { "Total Re", "", "", "", "", "", "", "", "", "", "", "", "", "", "" });
		TabelaDao.read(dtm);
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						recalcular();
					} catch (Exception e) {

					}
				}
			}
		};
		t.start();
		central.add(scrollpane, BorderLayout.CENTER);
		this.add(central, BorderLayout.CENTER);
		btnSalvar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				gravarVariaveis();
			}
		});
		this.add(btnSalvar,BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void gravarVariaveis(){
		areacritica = true;
		String opt = JOptionPane.showInputDialog("Digite a jogada");
		try{
			G_File file = new G_File(basePath+"variaveis.txt");
			if (!file.exists()){
				file = new G_File(basePath+"variaveis.txt");
			}
			int jog = Integer.parseInt(opt);
			calcularTotalJogada(jog);
			int Ri = Integer.parseInt(dtm.getValueAt(TOTG, COL_TOTPROGRAMA).toString().trim());
			int Ag = Integer.parseInt(dtm.getValueAt(TOTG+1, COL_TOTPROGRAMA).toString().trim());
			int Fl = Integer.parseInt(dtm.getValueAt(TOTG+2, COL_TOTPROGRAMA).toString().trim());
			int Re = Integer.parseInt(dtm.getValueAt(TOTG+3, COL_TOTPROGRAMA).toString().trim());
			int nRe = natural(Re - Ag);
			int nAg = natural(Ag - Re);
			int nRi = natural(Ri - Fl);
			int nFl = natural(Fl - Ri);
			int E, P, C, Q;
			E = P = C = Q = 100;
			if (nRi > 0 && nAg > 0) {
				E += nRi * 5;
				Q -= nAg * 5;
			}else if (nRe > 0 && nRi > 0) {
				P += nRi * 5;
				C -= nRe * 5;
			}else if (nFl > 0 && nRe > 0) {
				E -= nRe * 5;
				C += nFl * 5;
			}else if (nAg > 0 && nFl > 0) {
				P -= nAg * 5;
				Q += nFl * 5;
			}else{
				if(nAg!=0) P -= nAg * 5; else
				if(nFl!=0) C += nFl * 5; else
				if(nRe!=0) E -= nRe * 5; else
				if(nRi!=0) E += nRi * 5;
			}
			String vars = "agressivo="+Ag+"&reativo="+Re+"&rigido="+Ri+"&flexivel="+Fl+"&escala=10&ajustex=-4&ajustey=-8&vE="+E+"&vP="+P+"&vC="+C+"&vQ="+Q+"&";

			G_File jogada;
			jogada = new G_File(basePath + "j"+jog+".txt");
			if(!jogada.exists()){
				jogada = new G_File("C:/Atitudes/j"+jog+".txt");
			}
			jogada.write(vars);
			//System.out.println(vars);
			file.write(vars);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, e.getMessage());
		}		
		areacritica = false;
	}
	public static final int vRe = 1;
	public static final int vRi = 2;
	public static final int vAg = 3;
	public static final int vFl = 4;
	

	public int totRi = 0;
	public int totAg = 0;
	public int totFl = 0;
	public int totRe = 0;
	protected void recalcular() {
		if(areacritica) return;
		int E, P, C, Q, Ri, Ag, Fl, Re,j=1;
		for (int i = 0; i < TOTG; i++) {

			E = P = C = Q = 100;
			Ri = Ag = Fl = Re = 0;
			for (j = 1; j <= 6; j++) {
				try {
					int a = Integer.parseInt(dtm.getValueAt(i, j).toString().trim());
					switch (a) {
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
				} catch (Exception e) {
					//System.out.println(j);
					break;
				}
			}
			int tRe,tAg,tRi,tFl;
			tRe = natural(Re - Ag);
			tAg = natural(Ag - Re);
			tRi = natural(Ri - Fl);
			tFl = natural(Fl - Ri);
			if (tRi > 0 && tAg > 0) {
				E += tRi * 5;
				Q -= tAg * 5;
			}else if (tRe > 0 && tRi > 0) {
				P += tRi * 5;
				C -= tRe * 5;
			}else if (tFl > 0 && tRe > 0) {
				E -= tRe * 5;
				C += tFl * 5;
			}else if (tAg > 0 && tFl > 0) {
				P -= tAg * 5;
				Q += tFl * 5;
			}else{
				if(tAg!=0) P -= tAg * 5; else
				if(tFl!=0) C += tFl * 5; else
				if(tRe!=0) E -= tRe * 5; else
				if(tRi!=0) E += tRi * 5;
			}
			dtm.setValueAt("E" + E, i, 11);
			dtm.setValueAt("P" + P, i, 12);
			dtm.setValueAt("C" + C, i, 13);
			dtm.setValueAt("Q" + Q, i, 14);
			//
			System.out.println("-->'Equipe "+(i+1)+"' E=" + E + " P=" + P + " C=" + C + " Q=" + Q);
			System.out.println("--> Ag=" + tAg + " Ri=" + tRi + " Re=" + tRe + " Fl=" + tFl);
		}
		/*
		 * Calculando o total da jogada 
		 */
		for (j = 1; j <= 6; j++) {
			Ri = Ag = Fl = Re = 0;
			for (int i = 0; i < TOTG; i++) {
				try {
					int a = Integer.parseInt(dtm.getValueAt(i, j).toString().trim());
					switch (a) {
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
				} catch (Exception e) {
					//System.out.println(j);
					break;
				}
			}
			dtm.setValueAt(Ri,TOTG, j);
			dtm.setValueAt(Ag,TOTG+1, j);
			dtm.setValueAt(Fl,TOTG+2, j);
			dtm.setValueAt(Re,TOTG+3, j);
		}
		calcularTotalJogada(6);
		
		TabelaDao.create(dtm);
	}

	/**
	 * Calcula o valor até a jogada
	 * @param jogada
	 */
	public void calcularTotalJogada(int jogada){
		/*
		 * Calculando o total daS jogadaS
		 *  
		 */
		for(int i=0;i<4;i++){
			int tot=0;
			for (int j = 1; j <= jogada; j++) {
				try{
					int a = Integer.parseInt(dtm.getValueAt(TOTG+i, j).toString().trim());
					//System.out.print(a+" ");
					tot+=a;
				}catch(Exception e){
				
				}
			}
			//System.out.println(" = " + tot);
			dtm.setValueAt(tot, TOTG+i, COL_TOTPROGRAMA);
		}
	}
	int natural(int i) {
		return i < 0 ? 0 : i;
	}

	public static void main(String args[]) {
		new Tabela();
	}
}
