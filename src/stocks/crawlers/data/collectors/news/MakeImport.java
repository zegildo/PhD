package stocks.crawlers.data.collectors.news;

import java.io.File;

public class MakeImport {

	public static void main(String args[]){
		
		String dir = "/Users/zegildo/Desktop/POS-GRADUACAO/DOUTORADO/Stocks/src/data_collection/collect_news/newsCSV";
		
		File diretorio = new File(dir);
		File fList[] = diretorio.listFiles();

		for (File file : fList) {
			System.out.println("COPY links_noticias_empresas FROM '"+"/Users/zegildo/Desktop/StocksData/"+file.getName()+"' CSV");
		}
		
	}
}
