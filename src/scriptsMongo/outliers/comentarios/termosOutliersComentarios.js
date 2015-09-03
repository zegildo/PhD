/*
Comentarios:
Noticias que geraram mais de 100 comentarios para o ESTADAO
*/

function eliminaResquicios(palavras){

	for(var p in palavras){
		
		palavraAtual = palavras[p];
		palavraAtual = palavraAtual.toLowerCase();

		for(var b in BADWORDS){
			palavraAtual = palavraAtual.replace(BADWORDS[b],"");
		}
		palavras[p] = palavraAtual;
	}
}

function contaPalavras(texto, map){
	
	listPalavras = texto.split(" ");
	eliminaResquicios(listPalavras);

	for(var p in listPalavras){
		palavra = listPalavras[p];
		if(palavra in map){
			map[palavra]++;
		}else{
			map[palavra]=1;
		}
	}
}
	
	
/*
Datas importantes para análise de comentarios

*/
var mapaDeDatas = ["10-12-2015"];


var palavras_titulo = {};
var palavras_conteudo = {};
var BADWORDS = [",",".","'","´"];

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		var jornal = doc.subFonte;
		var OUTLIER = 100;
		//o elemento "c" é o que representa a quantidade de comentários
		var comentario = "c";

		if("ESTADAO" == jornal){

		var date = new Date(doc.timestamp * 1000);        
		var dateKey = (date.getMonth()+1)+"-"+date.getDate()+"-"+date.getFullYear();

			if(mapaDeDatas.indexOf(dataKey) > -1){
			
				repercussoes = doc.repercussao.split(",");
				
				for (var rep in repercussoes){

					siglaValor = repercussoes[rep].split(":");
					sigla = siglaValor[0];
					valor = siglaValor[1];
					
					if (sigla == comentario && valor == OUTLIER){
						
						print("url: "+doc.url);
						titulo = doc.titulo;
						contaPalavras(titulo,palavras_titulo);
						
						conteudo = doc.conteudo;
						contaPalavras(titulo,palavras_conteudo);
						break;

					}
				}

			}else{
				print("Deu errado aqui: "+dateKey);
			}

		}
		
		
	}

);

print("Quantidades: "+quantidadeOutliers);

print("*********TITULO***********")
for (var a in palavras_titulo){
	
	print(a+":"+palavras_titulo[a]);
}

print("*******CONTEUDO***********")
for (var a in palavras_conteudo){
	
	print(a+":"+palavras_conteudo[a]);	
}
