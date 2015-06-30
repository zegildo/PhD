
function generenateDatesBetween(startAno, stopAno){
	
	var dateMap = {};
   
   	for (ano = startAno; ano <= stopAno; ano++){
   		dateMap[ano] = {"G1":0, "FOLHASP":0, "ESTADAO":0};
   	}
    return dateMap;
}	
	

var mapaDeDatas = generenateDatesBetween(2000,2015);

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		if(["ESTADAO","FOLHASP","G1"].indexOf(doc.subFonte) > -1){
			
			var date = new Date(doc.timestamp * 1000);        
			var dateKey = date.getFullYear();
			
			if(dateKey in mapaDeDatas){
				mapaDeDatas[dateKey][doc.subFonte]++;
			}else{
				print("Deu errado aqui: "+dateKey);
			}
		}
	}

);

print("data,G1,FOLHASP,ESTADAO");
for (var data in mapaDeDatas){
	print(data+","+mapaDeDatas[data]["G1"]+","+mapaDeDatas[data]["FOLHASP"]+","+mapaDeDatas[data]["ESTADAO"]);

}