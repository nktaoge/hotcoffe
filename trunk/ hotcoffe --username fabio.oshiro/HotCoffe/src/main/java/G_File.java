

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 * Classe genérica para IO leitura e escrita simples de arquivos como na maioria
 * das linguagens
 * 
 * Change Log:
 * v1.2 14/11/2007: Método para limpar o diretório clearDir()
 * v1.1 22/08/2007: Colocada uma caixa de diálogo para abrir e salvar arquivos
 * v1: Carregador genérico para ler e gravar arquivos
 * 
 * @version 1.1
 * @author Fabio Issamu Oshiro
 * 
 */
public class G_File {

	private String path;

	private StringBuilder conteudo;

	private boolean lazyLoad = true;

	public String erroDesc;

	/**
	 * Indica o caminho do arquivo manualmente
	 * 
	 * @param path
	 *            :parametro para indicar o caminho do arquivo
	 * 
	 */
	public G_File(String path) {
		this.path = path;
	}

	/**
	 * Indica o caminho do o arquivo através de componente Swing
	 */
	public G_File() {
		JFileChooser fc = new JFileChooser();
		int opt = fc.showOpenDialog(null);
		if (opt == JFileChooser.APPROVE_OPTION) {
			path = fc.getSelectedFile().getPath();
		}
	}

	/**
	 * Limitar o tipo de extensão e indicar o caminho mais recente
	 * 
	 * @param recentPath
	 *            passa o Path recente para que ele não necessite navegar
	 *            novamente até o último folder aberto
	 * @param filtro
	 *            Array com os tipos de extensões aceitas Ex: ".css"
	 */
	public G_File(String recentPath, String filtro[]) {
		JFileChooser fc = new JFileChooser();

		G_FileFilter ff = new G_FileFilter(filtro);
		fc.addChoosableFileFilter(ff);

		fc.setSelectedFile(new File(recentPath + " "));
		int opt = fc.showOpenDialog(null);
		if (opt == JFileChooser.APPROVE_OPTION) {
			path = fc.getSelectedFile().getPath();
		}
	}

	/**
	 * Limitar o tipo de extensão e indicar o caminho mais recente
	 */
	public G_File(String recentPath, String filtro) {
		JFileChooser fc = new JFileChooser();
		G_FileFilter ff = new G_FileFilter(filtro, "Arquivos *" + filtro);
		fc.addChoosableFileFilter(ff);
		fc.setSelectedFile(new File(recentPath + " "));
		int opt = fc.showOpenDialog(null);
		if (opt == JFileChooser.APPROVE_OPTION) {
			path = fc.getSelectedFile().getPath();
		}
	}

	/**
	 * Abre a caixa de diálogo para salvar como
	 * 
	 * @param conteudo
	 *            Texto a ser salvo
	 * @param filtro
	 *            Tipo de extensão permitida
	 * @return
	 */
	public boolean openDialogSaveAs(String conteudo, String filtro[]) throws Exception {
		JFileChooser fc = new JFileChooser();
		G_FileFilter ff = new G_FileFilter(filtro);
		fc.addChoosableFileFilter(ff);
		if (this.path != null && !this.path.equals("")) {
			fc.setSelectedFile(new File(this.path + " "));
		}
		fc.setDialogTitle("Salvar");
		int salvar = fc.showSaveDialog(null);
		boolean retorno = false;
		if (salvar == JFileChooser.APPROVE_OPTION) {
			String fullPath = fc.getSelectedFile().getPath();
			File file = new File(fullPath);
			if (file.exists()) {
				int confirmar = JOptionPane.showConfirmDialog(fc, "Sobreescrever?", "Salvar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(fc, TokenLang.DIALOG_7);
				} else {
					this.path = fullPath;
					this.write(conteudo);
					retorno = true;
				}
			} else {
				this.path = fullPath;
				this.write(conteudo);
				retorno = true;
			}
		}
		return retorno;
	}

	/**
	 * Abre a caixa de diálogo salvar como
	 * 
	 */
	public boolean openDialogSaveAs(String filtro[]) throws Exception {
		JFileChooser fc = new JFileChooser();
		G_FileFilter ff = new G_FileFilter(filtro);
		fc.addChoosableFileFilter(ff);
		if (this.path != null && !this.path.equals("")) {
			fc.setSelectedFile(new File(this.path + " "));
		}
		fc.setDialogTitle("Salvar");
		int salvar = fc.showSaveDialog(null);
		boolean retorno = false;
		if (salvar == JFileChooser.APPROVE_OPTION) {
			String fullPath = fc.getSelectedFile().getPath();
			File file = new File(fullPath);
			if (file.exists()) {
				int confirmar = JOptionPane.showConfirmDialog(fc, "Sobreescrever", "Salvar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(fc, TokenLang.DIALOG_7);
				} else {
					String conteudo = this.read();
					this.path = fullPath;
					this.write(conteudo);
					retorno = true;
				}
			} else {
				String conteudo = this.read();
				this.path = fullPath;
				this.write(conteudo);
				retorno = true;
			}
		}
		return retorno;
	}

	/**
	 * Salva uma BufferedImage como JPG
	 * 
	 * @param bufferedImageAtual
	 * @return
	 * @throws IOException
	 */
	public boolean openDialogSaveAs(BufferedImage bufferedImageAtual) throws IOException {
		JFileChooser fc = new JFileChooser();
		G_FileFilter ff = new G_FileFilter(new String[] { ".jpg" });
		fc.addChoosableFileFilter(ff);
		if (this.path != null && !this.path.equals("")) {
			fc.setSelectedFile(new File(this.path + " "));
		}
		fc.setDialogTitle("Salvar");
		int salvar = fc.showSaveDialog(null);
		boolean retorno = false;
		if (salvar == JFileChooser.APPROVE_OPTION) {
			String fullPath = fc.getSelectedFile().getPath();
			File file = new File(fullPath);
			if (file.exists()) {
				int confirmar = JOptionPane.showConfirmDialog(fc, "Sobreescrever", "Salvar", JOptionPane.YES_NO_OPTION);
				if (confirmar == JOptionPane.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(fc, TokenLang.DIALOG_7);
				} else {
					this.path = fullPath;
					ImageIO.write(bufferedImageAtual, "jpg", new File(fullPath));
					retorno = true;
				}
			} else {
				this.path = fullPath;
				ImageIO.write(bufferedImageAtual, "jpg", new File(fullPath));
				retorno = true;
			}
		}
		return retorno;
	
	}

	/**
	 * Parametro para ver se deixa ou não na memória. caso colocado como false a
	 * cada read() ele lê novamente do HD
	 * 
	 * @param na_memoria
	 *            se true (default) deixa o arquivo na memória
	 */
	public void setLazyLoad(boolean na_memoria) {
		lazyLoad = na_memoria;
	}

	/**
	 * Na primeira execução coloca o conteudo do arquivo no atributo
	 * StringBuilder conteudo em outras execuções se na_memoria é falso, se for
	 * le novamente do disco caso seja verdadeiro este metodo não faz nada. caso
	 * colocado como false a cada read() ele lê novamente do HD
	 */

	private void readConteudo() {

		if (conteudo != null && lazyLoad)
			return;
		conteudo = new StringBuilder();
		if (this.path == null)
			return;

		try {
			FileReader fr;
			fr = new FileReader(this.path);
			
			BufferedReader br = new BufferedReader(fr);
			//conteudo.append(br.readLine()+"\n");
			//conteudo.append('\n');
			String linha = br.readLine();
			while (linha != null) {
				conteudo.append(linha).append('\n');
				linha = br.readLine();
			}
			br.close();
			//Charset a = Charset();
			return;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final int mb = 1024;
		File file = new File(this.path);
		FileInputStream fis = null;
		try {
			if (file.exists()) {
				fis = new FileInputStream(file);
				
				byte[] dados = new byte[mb];
				int bytesLidos = 0;
				while ((bytesLidos = fis.read(dados)) > 0) {
					conteudo.append(new String(dados, 0, bytesLidos));
				}
				fis.close();
			} else {
				return;
			}
		} catch (Exception e) {
			erroDesc = e.getMessage();
			return;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		// return ;
		// conteudo = sb.toString().trim();
	}

	/**
	 * executa readConteudo e retorna o atributo conteudo como String
	 * 
	 * @return versão String do StringBuilder conteudo
	 */
	public String read() {
		// tentativa de lazy load
		readConteudo();
		if(conteudo.indexOf("Ã¡")!=-1){
//			Julgo que é UTF-8
			try {
				//System.out.println("Convertido para UTF8");
				return new String(conteudo.toString().getBytes(),"UTF8");
			} catch (UnsupportedEncodingException e) {
				//e.printStackTrace();
			}
		}
		return conteudo.toString();
		/*
		try {
			//System.out.println("Convertido ISO-8859-15");
			//return new String(conteudo.toString().getBytes(),"ISO-8859-15");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		return "";
		*/
	}

	/**
	 * Caso o arquivo exista apaga o seu conteudo e coloca a String, caso não
	 * exista este é criado e o conteudo da String é colocado no arquivo
	 * 
	 * @param String
	 *            contendo o novo conteudo do arquivo
	 */
	public void write(String texto) throws Exception{
		if (this.path == null)
			return;
		File file = new File(this.path);
		if(!file.exists()){
			if(file.getParentFile()!=null){
				file.getParentFile().mkdirs();
			}
		}
		texto +="";
		FileWriter arq;
			arq = new FileWriter(this.path);
			arq.write(texto);
			arq.flush();
			arq.close();

			if (lazyLoad) {
				conteudo = new StringBuilder();
				conteudo.append(texto);
			}

	}
	
	/**
	 * caso o path tenha algum arquivo e caso o arquivo exista
	 * este é deletado
	 * 
	 * @author Renato Tomaz Nati
	 */
	public void delete() {
		if (this.path == null)
			return;
	
		File arq = new File(this.path);
		if(arq.exists()){
		arq.delete();
		}
	}

	/**
	 * Caso o arquivo exista adiciona a String, caso não exista este é criado e
	 * o conteudo da String é colocado no arquivo
	 * 
	 * @param String
	 *            contendo conteudo a ser adicionado no arquivo
	 */
	public void append(String texto) {
		FileWriter arq;
		try {
			arq = new FileWriter(this.path, true);
			arq.write(texto);
			arq.close();
			if (lazyLoad) {
				if (conteudo == null)
					conteudo = new StringBuilder();
				conteudo.append(texto);
				// a linha abaixo é muuuuiiito lenta com o String conteudo
				// conteudo+=texto;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifica se o arquivo já existe
	 */
	public boolean exists() {
		File testa = new File(path);
		return testa.exists();
	}

	/**
	 * Cria um arquivo vazio
	 */
	public void newFile() {
		append("");
	}

	/**
	 * Retorna o arquivo de acordo com o path que foi informado
	 */
	public File getFile() {
		if (path != null) {
			return new File(path);
		} else {
			return null;
		}
	}

	/**
	 * Retorna o arquivo como InputStream
	 * @return InputStream referente ao arquivo
	 */
	public InputStream getInputStream() {
		InputStream fis = null;
		try {
			fis = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

	/**
	 * Atribui o caminho do arquivo
	 * @param string
	 */
	public void setPath(String string) {
		this.path = string;		
	}

	/**
	 * Retorna o caminho do arquivo de acordo com protocolo "file:"
	 * @return path do arquivo para ser usado em navegadores
	 */
	public String getUrlPath() {
		File arq =  new File(this.path);
		String url = arq.getAbsolutePath();
		url ="file:///" + url.replace("\\", "/");
		return url;
	}

	/**
	 * Limpa o diretório passado como parametro
	 * menos o .svn
	 * @param nomeDir nome do diretorio
	 */
	public static void clearDir(String nomeDir) {
		File dir = new File(nomeDir);
		if (dir.isDirectory()) {
			delFolderCont(dir);
		}
	}
	/**
	 * Função recursiva para limpar diretórios
	 * @param dir
	 */
	private static void delFolderCont(File dir){
		String path = dir.getAbsolutePath();
		for (String fileName : dir.list()) {
			File file = new File(path + "/" + fileName);
			if(file.getName().equals(".svn")) continue;
			if(file.isDirectory()){
				delFolderCont(file);
				file.delete();
			}else{
				file.delete();
			}
		}
	}
}

/**
 * SubClasse para criar os filtros de extensão
 * 
 * @author Fabio Issamu Oshiro
 * 
 */
class G_FileFilter extends javax.swing.filechooser.FileFilter {
	private String descricao;

	private HashSet<String> arrExtensao = new HashSet<String>();

	/**
	 * Construtor do filtro
	 * 
	 * @param extensao
	 *            extensão que será aceita
	 * @param descricao
	 *            descrição do tipo do arquivo
	 */
	public G_FileFilter(String extensao, String descricao) {
		arrExtensao.add(extensao);
		this.descricao = descricao;
	}

	/**
	 * Construtor do filtro
	 * 
	 * @param extensao
	 *            extensão que será aceita
	 */
	public G_FileFilter(String extensao[]) {
		this.descricao = "Arquivos ";
		for (int i = 0; i < extensao.length; i++) {
			arrExtensao.add(extensao[i]);
			this.descricao += "*" + extensao[i] + " ";
		}
	}

	public boolean accept(File f) {
		String arr[] = f.getName().toLowerCase().split("\\.");
		String extens;
		if (arr.length > 0) {
			extens = "." + arr[arr.length - 1];
			if (arrExtensao.contains(extens)) {
				return true;
			}
		}
		return f.isDirectory();
	}

	public String getDescription() {
		return descricao;
	}
}
