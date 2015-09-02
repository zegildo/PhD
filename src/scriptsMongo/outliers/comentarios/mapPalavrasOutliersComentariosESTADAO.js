/*

Comentarios:
Noticias que geraram mais de 100 comentarios para o ESTADAO

*/


var palavras_titulo = {};
var palavras_conteudo = {};
var BADWORDS = [",",".","'","´"];
var CORTE = 100;
var quantidadeOutliers = 0;
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

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		if("ESTADAO" == doc.subFonte){
			repercussoes = doc.repercussao.split(",");
			for (var rep in repercussoes){
				
				siglaValor = repercussoes[rep].split(":");
				sigla = siglaValor[0];
				valor = siglaValor[1];
				valor = parseInt(valor);
				//Comentarios sao "c:6,t:71,f:608,l:0,total:685"
				if (sigla == "c" && valor >= CORTE){
					print("url: "+doc.url);
					quantidadeOutliers++;

					titulo = doc.titulo;
					contaPalavras(titulo,palavras_titulo);
					
					conteudo = doc.conteudo;
					contaPalavras(titulo,palavras_conteudo);
					break;
				}

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
