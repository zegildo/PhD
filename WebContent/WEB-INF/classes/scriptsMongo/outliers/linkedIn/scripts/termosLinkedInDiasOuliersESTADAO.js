
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
Datas em que o número de LinkedIn no dia foi maior que 500 para o ESTADAO
*/
var mapaDeDatas = ["6-17-2014","12-12-2014","1-19-2015","1-24-2015","1-30-2015","4-17-2012","2-1-2015","2-12-2015","2-18-2015","2-23-2015","2-26-2015","3-1-2015"];

var palavras_titulo = {};
var palavras_conteudo = {};
var BADWORDS = [",",".","'","´"];

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		var jornal = doc.subFonte;

		if("ESTADAO" == jornal){

			var date = new Date(doc.timestamp * 1000);        
			var dateKey = (date.getMonth()+1)+"-"+date.getDate()+"-"+date.getFullYear();

			if(mapaDeDatas.indexOf(dateKey) > -1){
				
				titulo = doc.titulo;
				contaPalavras(titulo,palavras_titulo);
				
				conteudo = doc.conteudo;
				contaPalavras(titulo,palavras_conteudo);
			}
		}
	
	}

);

print("*********TITULO***********")
for (var a in palavras_titulo){
	
	print(a+":"+palavras_titulo[a]);
}

print("*******CONTEUDO***********")
for (var a in palavras_conteudo){
	
	print(a+":"+palavras_conteudo[a]);	
}