
load('datasQuentes.js');
load('datasFrias.js');

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

var palavrasDatasFrias = {};
var palavrasDatasQuentes = {};
var BADWORDS = [",",".","'","´"];

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		var jornal = doc.subFonte;

		if(["ESTADAO","FOLHASP","G1"].indexOf(subFonte) > -1){

			var date = new Date(doc.timestamp * 1000);        
			var dateKey = if((date.getMonth()+1))+"/"+date.getDate()+"/"+date.getFullYear();

			titulo = doc.titulo;

			if(datasFrias.indexOf(dateKey) > -1){
				
				contaPalavras(titulo,palavrasDatasFrias);
			
			}else if(datasQuentes.indexOf(dateKey) > -1){

				contaPalavras(titulo,palavrasDatasQuentes);

			}
		}
	
	}

);

print("*********TITULO***********")
for (var a in palavrasDatasQuentes){
	
	print(a+":"+palavrasDatasQuentes[a]);
}

print("*******CONTEUDO***********")
for (var a in palavrasDatasFrias){
	
	print(a+":"+palavrasDatasFrias[a]);	
}
