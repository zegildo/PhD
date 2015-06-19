var mapaDeDatas = {};
db.informacoesGerais.find({}).forEach(

	function (doc){
		
		if(["FOLHASP"].indexOf(doc.subFonte) > -1){
			
			var date = new Date(doc.timestamp * 1000);        
			var dateKey = (date.getMonth()+1)+"/"+date.getDate()+"/"+date.getFullYear();
			
			//if(dateKey == "5/29/2014"){

			if(dateKey in mapaDeDatas){
				//mapaDeDatas[dateKey].push(doc._id);
				mapaDeDatas[dateKey]++;
			}else{
				//mapaDeDatas[dateKey] = [doc._id];
				mapaDeDatas[dateKey] = 1;
			}
			//}
			
		}
	}

);
print("data"+","+"quantidade");
for (var data in mapaDeDatas){
	//print(data+": ");
	//print("\t"+mapaDeDatas[data]);

	print(data+","+mapaDeDatas[data]);

}