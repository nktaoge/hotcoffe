
public class TestaScanner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			int i;
			while((i=System.in.read())!=-1){
				char a=(char)i;
				System.out.print(a);
			}
		}catch(Exception e){
			
		}
	}

}
