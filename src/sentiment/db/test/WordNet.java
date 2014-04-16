package sentiment.db.test;

import java.util.List;
import java.util.Set;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import rita.wordnet.RiWordnet;
import rita.wordnet.WordnetPos;

public class WordNet {

	private static boolean ehFim(Word[] lemas, String fim){
		for (Word word : lemas) {
			String lema = word.getLemma();
			if(lema.equals(fim)){
				return true;
			}
		}	
		return false;
	}

	public static List<String> encontraCaminhoProfundidade(String inicio, String fim, String caminho, List<String> expandidos, Dictionary dic, List<String> paths) throws JWNLException{

		IndexWordSet WORDSET1 = dic.lookupAllIndexWords(inicio);
		POS p = WordnetPos.getWordnetPOSForKey(RiWordnet.ADJ);
		IndexWord start = WORDSET1.getIndexWord(p);
		expandidos.add(inicio);
		caminho+=inicio+"-";
		int senseCount1 = start.getSenseCount();

		for (int i = 1; i <= senseCount1; i++) {
			Synset s = start.getSense(i);
			Word w[] = s.getWords();
			for (Word word : w) {
				String lema = word.getLemma();
				if(expandidos.contains(lema)){
					continue;
				}else if(ehFim(w, fim)){
					paths.add(caminho+fim);
					continue;
				}else{
					encontraCaminhoProfundidade(lema, fim,caminho,expandidos, dic, paths);
				}
			}
		}
		return paths;
	}

	private static Synset[] buscarSentidos(String lema, Dictionary dic) throws JWNLException{

		IndexWordSet WORDSET1 = dic.lookupAllIndexWords(lema);
		POS p = WordnetPos.getWordnetPOSForKey(RiWordnet.ADJ);
		IndexWord start = WORDSET1.getIndexWord(p);
		if(start != null){
			return start.getSenses();
		}		 
		return null;
	}
	public static String encontraCaminho(String inicio, String fim, Dictionary dic) throws JWNLException{
		boolean encontrado = false;
		Synset sense[] = buscarSentidos(inicio, dic);
		Set<String> expansaoAtual = new java.util.HashSet<String>();
		Set<String> novaExpansao = new java.util.HashSet<String>();

		for (Synset sen : sense) {
			for (Word w : sen.getWords()) {
				String lema = w.getLemma();
				if(lema.equals(fim)){
					return inicio+"-"+fim;
				}else if(!inicio.equals(lema)){
					expansaoAtual.add(inicio+"-"+lema);
				}
			}		
		}	

		while(!encontrado){
			for (String caminho : expansaoAtual) {
				String termo = caminho.substring(caminho.lastIndexOf("-")+1, caminho.length());
				sense = buscarSentidos(termo, dic);
				if(sense != null){
					for (Synset synset : sense) {
						for (Word w : synset.getWords()) {
							String lema = w.getLemma();
							if(lema.equals(fim)){
								return caminho+"-"+fim;
							}else if(!caminho.contains(lema)){
								novaExpansao.add(caminho+"-"+lema);
							}
						}	
					}
				}
				
			}
			expansaoAtual.clear();
			expansaoAtual.addAll(novaExpansao);
			novaExpansao.clear();
		}


		return null;

	}

	public static void main(String args[]) throws JWNLException{
		RiWordnet wordnet = new RiWordnet();
		Dictionary dic = wordnet.getDictionary();
//		List<String> s = WordNet.encontraCaminhoProfundidade("good", "bad", "", new ArrayList<String>(), dic, new ArrayList<String>());
//		for (String string : s) {
//			System.out.println(string);
//		}
		
		System.out.println(encontraCaminho("good", "bad", dic));

		//		RelationshipList relList;
		//
		//		for (int i = 1; i <= senseCount1; i++)
		//		{
		//			
		//			for (int j = 1; j <= senseCount2; j++)
		//			{
		//
		//				try
		//				{
		//					System.out.println(start.getSense(i)+"-"+end.getSense(j));
		//
		//					relList = RelationshipFinder.getInstance().findRelationships(
		//							start.getSense(i), end.getSense(j), PointerType.MEMBER_MERONYM);
		//				}
		//				catch (Exception e)
		//				{
		//					continue;
		//				}
		//
		//				for (Iterator relListItr = relList.iterator(); relListItr.hasNext(); )
		//				{
		//					Relationship rel = (Relationship)relListItr.next();
		//					int relLength = rel.getDepth();
		//					if(relLength< menorDistancia){
		//						menorDistancia = relLength;
		//					}
		//
		//				}
		//			}
		//		}
	}
}
