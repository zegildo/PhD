package model;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import crawler.twitter.search.TwitterSearch;
import dao.InformacoesDAO;

@Resource
public class TwitterController {
	
	private final InformacoesDAO infDAO;
	
	public TwitterController(InformacoesDAO infDAO){
		this.infDAO = infDAO;
	}
	
	@Get("/twitter/inserir/")
	public void inserirTwitters(){
		
		String searchDateStart = "01/01/2010";
		String searchDateFinish = "31/12/2013";

		//for para todos os nomes acoes


		//for para todos os segmentos

		//for para todos os subsetores

		//for para todos os setores

		String consulta =  "petrobrás";
				
		TwitterSearch search = new TwitterSearch();

		search.insereInformacao(searchDateStart,searchDateFinish, "ação", consulta, infDAO);
	}
}
