var DIAS_DA_SEMANA = new Array("Sun", "Mon",
      "Tue", "Wed", "Thu",
      "Fri", "Sat");

function generenateDatesBetween(){
	
	var dateMap = {};
   	
   	for (var dia in DIAS_DA_SEMANA){
   		dateMap[DIAS_DA_SEMANA[dia]] = {"G1":0, "FOLHASP":0, "ESTADAO":0};
   	}
   	print("termonou....")
    return dateMap;
}	
	
var mapaDeDatas = generenateDatesBetween();

db.informacoesGerais.find({}).forEach(

	function (doc){
		
		if(["ESTADAO","FOLHASP","G1"].indexOf(doc.subFonte) > -1){
			
			var date = new Date(doc.timestamp * 1000);        
			var dateKey = DIAS_DA_SEMANA[date.getDay()];
			
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