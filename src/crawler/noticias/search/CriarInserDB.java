package crawler.noticias.search;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class CriarInserDB {

	public static void main(String args[]) throws FileNotFoundException{
		
		String arquivoInicio = "ESTADAO-final.txt";
		String arquivoGerado = "ESTADAO-Insert.txt";
		//BufferedWriter gravarArq = new BufferedWriter(new FileWriter(arquivoGerado));

		Scanner scanner = new Scanner(new FileReader(arquivoInicio))
		.useDelimiter("\\||\\n");
		
		while (scanner.hasNext()) {
			
			String timestamp = scanner.next();
			String subFonte = scanner.next(); 
			String titulo = scanner.next();
			String subTitulo = scanner.next();
			String conteudo = scanner.next();
			String emissor = scanner.next();
			String url = scanner.next();
			String repercussao = scanner.next();
			
			//gravarArq.write(noticiaAtual+"\n");
			
		}
		//gravarArq.close();
	}
}
