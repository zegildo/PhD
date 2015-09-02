/*

Comentarios:
Noticias que geram mais de 1500 comentarios para a FOLHA DE SAO PAULO

*/


var palavras_titulo = {};
var palavras_conteudo = {};
var BADWORDS = [",",".","'","´"];
var CORTE = 1500;
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
		
		if(["FOLHASP"].indexOf(doc.subFonte) > -1){
			repercussoes = doc.repercussao.split(",");
			for (var rep in repercussoes){
				
				siglaValor = repercussoes[rep].split(":");
				sigla = siglaValor[0];
				valor = siglaValor[1];
				valor = parseInt(valor);
				//Comentarios sao "c:6,t:71,f:608,l:0,total:685"
				if (sigla == "c" && valor > CORTE){
					
					quantidadeOutliers++;

					titulo = doc.titulo;
					contaPalavras(titulo,palavras_titulo);
					
					conteudo = doc.conteudo;
					contaPalavras(titulo,palavras_conteudo);
					
				}

			}
		}
	}
);

print("Qtd: "+quantidadeOutliers);

print("*********TITULO***********")
for (var a in palavras_titulo){
	
	print(a+":"+palavras_titulo[a]);
	
	
}

print("*******CONTEUDO***********")
for (var a in palavras_conteudo){
	
	print(a+":"+palavras_conteudo[a]);
	
}
