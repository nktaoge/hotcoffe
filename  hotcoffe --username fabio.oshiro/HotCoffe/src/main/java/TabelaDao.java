import javax.swing.table.DefaultTableModel;


public class TabelaDao {
	public static final String path = "tabela.txt";
	public static void create(DefaultTableModel dtm) throws Exception{
		int m = dtm.getRowCount();
		int n=dtm.getColumnCount();
		String val="";
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				if(dtm.getValueAt(i,j)!=null)
					val+=dtm.getValueAt(i,j).toString()+"\t";
			}
			val+="\n";
		}
		G_File arq=new G_File(path);
		arq.write(val);
	}
	public static void read(DefaultTableModel dtm){
		G_File arq=new G_File(path);
		if(!arq.exists()) return;
		String val = arq.read();
		String linha[]= val.split("\n");
		for(int i =0;i<linha.length;i++){
			String coluna[]=linha[i].split("\t");
			for(int j=0;j<coluna.length;j++){
				dtm.setValueAt(coluna[j], i,j);
			}
		}
	}
}
