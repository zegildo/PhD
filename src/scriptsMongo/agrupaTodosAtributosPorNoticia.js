
print("Data,DiadaSemana,Mes,Dia,Ano,Jornal,qtComentarios,qtTweets,qtFacebook,qtLinkedIn,qtGooglePlus,polaridade");


db.informacoesGerais.find({}).forEach(

	function (doc){
		
		var subFonte = doc.subFonte;
		var polaridade = doc.polarity;

		if(["ESTADAO","FOLHASP","G1"].indexOf(subFonte) > -1){
			var result = "";
			var date = new Date(doc.timestamp * 1000);        
			var diaSemana = (date.getDay()+1);
			var mes = (date.getMonth()+1)+"";
			var dia = date.getDate()+"";	
			var ano = date.getFullYear();
			var data = ((dia.length<2) ? "0"+dia : dia)+"/"+((mes.length<2) ? "0"+mes : mes)+"/"+ano;
			
			result += data+","+diaSemana+","+dia+","+mes+","+ano+","+subFonte+",";
			var repercussoes = doc.repercussao.split(",");
			var engajamento = {"c":0,"t":0,"f":0,"l":0,"g":0};
			for (var rep in repercussoes){
				siglaValor = repercussoes[rep].split(":");
				sigla = siglaValor[0];
				if(sigla != "total"){
					valor = siglaValor[1];
					engajamento[sigla] = valor;
				}

			}
			for (var i in engajamento){
				result+=engajamento[i]+",";
			}
			result+=polaridade;
			print(result);

		}
	}

);
