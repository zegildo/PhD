$(document).ready(function(){
	var my_posts = $("[rel=tooltip]");
	var size = $(window).width();
	for(i=0;i<my_posts.length;i++){
		the_post = $(my_posts[i]);
		if(the_post.hasClass('invert') && size >=767 ){
			the_post.tooltip({ placement: 'left'});
			the_post.css("cursor","pointer");
		}else{
			the_post.tooltip({ placement: 'rigth'});
			the_post.css("cursor","pointer");
		}
	}
	new Autocomplete("busca", {
	    srcType : "array", 
	    onInput : function(newValue, oldValue) {
	        var autocomplete = this;
	        $.ajax({
		 			type: 'GET',
		 			url: '/PhD/produtos/busca.json?busca='+newValue,
		 			async:false,
		 			dataType: 'json',
		 			success: function(data) {
			            autocomplete.addValues(data);
		 			},
		 			error : function(jqXHR, textStatus, errorThrown) {
		    			alert("Error status :"+textStatus);  
		      			alert("Error type :"+errorThrown);  
		    		}
				});
	     
	    }
	});

	$('#searchButton').click(function(){
		 $.ajax({
	 			type: 'GET',
	 			url: '/PhD/acoes?q='+$('#busca').val(),
	 			async:false,
	 			dataType: 'json',
	 			success: function(data) {
		            montaColuna(data);
	 			},
	 			error : function(jqXHR, textStatus, errorThrown) {
	    			alert("Error status :"+textStatus);  
	      			alert("Error type :"+errorThrown);  
	    		}
			});
	
	});
	
	$('.selectpicker').selectpicker();

	$('#confMenu').click(
		function() {
			$( "#search" ).fadeOut( "slow",  function complete(){
				$( "#menu" ).fadeIn( "slow" );
			});
	});

	$('#confSearch').click(function() {
		$( "#menu" ).fadeOut( "slow",  function complete(){
			$( "#search" ).fadeIn( "slow" );
		});
	});

	var dow_prices = [16154.39
	                  ,16130.40
	                  ,16040.56
	                  ,16133.23
	                  ,16103.30
	                  ,16207.14
	                  ,16179.66
	                  ,16198.41
	                  ,16272.65
	                  ,16321.71
	                  ,16168.03
	                  ,16395.88
	                  ,16360.18
	                  ,16421.89
	                  ,16452.72
	                  ,16418.68
	                  ,16351.25
	                  ,16340.08
	                  ,16108.89
	                  ,16065.67
	                  ,16247.22
	                  ,16336.19
	                  ,16222.17
	                  ,16331.05
	                  ,16302.70
	                  ,16276.69
	                  ,16367.88
	                  ,16268.99
	                  ,16264.23
	                  ,16323.06
	                  ];
	dow_volume = [84064837
	              ,91252875
	              ,80564723
	              ,77717931
	              ,126582640
	              ,244559949
	              ,99462277
	              ,93984551
	              ,97625540
	              ,122110544
	              ,92760132
	              ,96119258
	              ,73975341
	              ,75901024
	              ,80685020
	              ,68208475
	              ,78150836
	              ,77898533
	              ,86165214
	              ,85660847
	              ,88926593
	              ,79145434
	              ,90113882
	              ,91530690
	              ,353667405
	              ,110620603
	              ,89111351
	              ,92767094
	              ,93625717
	              ,86374508
	              ]; 

	$('#dow').sparkline(dow_volume, {
		height: '1.5em', 
		type: 'bar', 
		barSpacing: 0, 
		barWidth: 3,
		barColor: '#ddd', 
		tooltipPrefix: 'Volume: '
	});

	$('#dow').sparkline(dow_prices, {
		composite: true, 
		height: '1.5em', 
		fillColor: false,
		lineColor:'#0000bf',
		spotColor: '#ff0000',
		minSpotColor: '#ff0000',
		maxSpotColor: '#ff0000',
		tooltipPrefix: 'Index: '

	});

	$('#nasdaq').sparkline(dow_volume, {
		height: '1.5em', 
		type: 'bar', 
		barSpacing: 0, 
		barWidth: 3,
		barColor: '#ddd', 
		tooltipPrefix: 'Volume: '
	});

	$('#nasdaq').sparkline(dow_prices, {
		composite: true, 
		height: '1.5em', 
		fillColor: false,
		lineColor:'#0000bf',
		spotColor: '#ff0000',
		minSpotColor: '#ff0000',
		maxSpotColor: '#ff0000',
		tooltipPrefix: 'Index: '

	});


});

function montaTelefones(telefones){
	
	 var tels = telefones.split(","); 
	 var resultado = "";
	 
	 for(var i = 0; i < tels.length; i++){
		 
		 resultado+="<small>"+tels[i]+"</small><br>";
	 }
	 return resultado; 
}
function informacoesPapeis(codigosNegociados){
	var informacoesPapeis = "<div>"+
								"<p class='lead'>Informacoes de Papeis</p>"+
								"<table class='table table-condensed'>"+
									"<tbody>"+
										"<tr>"+
											"<td><span class='label label-primary'>PAPEIS:</span></td>"+
											"<td><span class='label label-danger'>"+codigosNegociados+"</span></td>"+
										"</tr>"+
									"</tbody>"+
								"</table>"+
							"</div>";
	return informacoesPapeis;
	
}
function informacoesGerais(cnpj,codigo_cvm, atividadePrincipal,setor,sub_setor, segmento){
	
	var informacoesGerais = "<div>"+
								"<p class='lead'>Informacoes Gerais</p>"+
									"<table class='table table-condensed'>"+
										"<tbody>"+
											"<tr>"+
												"<td><span class='label label-primary'>CNPJ:</span></td>"+
												"<td>"+cnpj+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-primary'>CVM:</span></td>"+
												"<td>"+codigo_cvm+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-primary'>ATIVIDADE:</span></td>"+
												"<td class='text-justify'><small>"+atividadePrincipal+"</small></td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-success'>SETOR:</span></td>"+
												"<td>"+setor+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-info'>SUB-SETOR:</span></td>"+
												"<td>"+sub_setor+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-warning'>SEGMENTO:</span></td>"+
												"<td>"+segmento+"</td>"+
											"</tr>"+
										"</tbody>"+
									"</table>"+
								"</div>";
	return informacoesGerais;
}

function informacoesDeEndereco(endereco, cidade, estado, cep, telefones, fax){
	
	var informacoesEndereco = "<div>"+
								"<p class='lead'>Endereço</p>"+
									"<table class='table table-condensed'>"+
										"<tbody>"+
											"<tr>"+
												"<td><span class='label label-primary'>Endereço</span></td>"+
												"<td>"+endereco+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-primary'>Cidade</span></td>"+
												"<td>"+cidade+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-primary'>Estado:</span></td>"+
												"<td>"+estado+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-primary'>CEP:</span></td>"+
												"<td>"+cep+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-default'><span class='glyphicon glyphicon-earphone'></span>TELEFONE:</span></td>"+
												"<td>"+telefones+"</td>"+
											"</tr>"+
											"<tr>"+
												"<td><span class='label label-default'><span class='glyphicon glyphicon-file'></span>FAX:</span></td>"+
												"<td>"+fax+"</td>"+
										"</tr>"+
										"</tbody>"+
									"</table>"+
								"</div>";
	return informacoesEndereco;
}
function divNoticias(){
	var noticias = "<div class='panel panel-info'>"+
						"<div class='panel-heading'>Notícias</div>"+
						"<div class='panel-body'>"+
								"<ul class='nav nav-pills nav-stacked'>"+
									"<li class='active'>"+
										"<a href='#'>"+ 
											"<span class='badge pull-right'>42</span>"+ 
											"Light" +
										"</a>"+
									"</li>"+
									"<li>" +
										"<a href='#'>Servicos</a>" +
									"</li>"+
									"<li>"+
										"<a href='#'>"+
											"<span class='badge pull-right'>3</span>"+
											"Energia" +
										"</a>"+
									"</li>"+
								"</ul>"+
						"</div>"+
					"</div>";
	return noticias;
	
}
function montaGrafico(){
	var grafico = 
		$("<div class='panel panel-default'>"+
			"<div class='panel-heading'>Cotacao"+
			"</div>"+
			"<div id='panelGrafico' class='panel-body'>"+
				"<div id='container' style='height: 500px; min-width: 500px; display: block;'></div>"+
			"</div>"+
		"</div>").hide().fadeIn(2000);
	$('#geral').append(grafico);
	$.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function(data) {
		// Create the chart
		$('#container').highcharts('StockChart', {
	
			rangeSelector : {
				selected : 1
			},
	
			title : {
				text : 'AAPL Stock Price'
			},
	
			series : [{
				name : 'AAPL',
				data : data,
				tooltip: {
					valueDecimals: 2
				}
			}]
		});
	});
	
}

function montaNoticias(){
	
	var noticias = 
		"<div class='panel panel-info'>"+
			"<div class='panel-heading'>Noticias</div>"+
			"<div id='panelNoticia' class='panel-body'>"+
				//Noticias
			"</div>"+
		"</div>";
	$('#geral').append(noticias);
}

function trataPapeis(cotacoes){
	
	for(var i = 0; i < cotacoes.length; i++){
		
		var papel = cotacoes[i][0];
		var cotacs = cotacoes[i][1];

		alert(papel);
		alert(cotacs);
		trataCotacoes(cotacs);
	
	}	
}

function trataCotacoes(dadosCotacoes){
	
	for(var i = 0; i < dadosCotacoes.length; i++){
		
		var time = Date.parse(dadosCotacoes[i][0].replace(" ","T"));
		var valor = dadosCotacoes[i][2];

	}
	
}

function montaColuna(data){
	
	for(var i = 0; i < data.length; i++){
		
		var empresa = data[i].empresa;
		var cotacoes = data[i].cotacoes;
		trataPapeis(cotacoes);
		
		var cnpj = empresa.cnpj.trim();
		var codigo_cvm = empresa.cod_cvm;
		var nomeDaEmpresa = empresa.nome_empresa+" ";
		var nomePregao = empresa.nome_pregao;
		var codigosNegociados = empresa.cod_negociacao;
		var atividadePrincipal = empresa.atividade_principal;
		var setor = empresa.setor;
		var sub_setor = empresa.sub_setor;
		var segmento = empresa.segmento;
		var endereco = empresa.endereco;
		var cidade = empresa.cidade;
		var estado = empresa.estado;
		var cep = empresa.cep;
		var telefone = empresa.telefone;
		var fax = empresa.fax;
		var site = empresa.site;

		
		var teste1 = "danger";
			
		var head = 
			$("<div class='text-center'>"+
					"<h1>"+nomeDaEmpresa+
						"<span class='label label-"+teste1+"'>"+
						"-4%</span>"+
					"</h1>"+
				"</div>").hide().fadeIn(2000);
		
		$("#resultado").append(head);
		
		var  lateral = 
			
			$(divNoticias()+
						
			"<div class='panel panel-info'>"+
				"<div id='lateral' class='panel-body'>"+
					"<div class='text-center'>" +
						"<strong>"+nomePregao+"</strong>"+
						"<img class='img-circle' alt='100x100' src='"+$('#imgs').val()+'/'+cnpj+".jpg'/>"+		
						"</p>"+
					"</div>"+
				informacoesGerais(cnpj,codigo_cvm, atividadePrincipal,setor,sub_setor, segmento)+
				informacoesPapeis(codigosNegociados)+
				informacoesDeEndereco(endereco, cidade, estado, cep, montaTelefones(telefone), montaTelefones(fax))+	
				"</div>"+
				"<div class='panel-footer text-center'>"+
					"<a href="+site+" class='btn btn-primary btn-xs' role='button'>"+site+"</a>"+
				"</div>"+
			"</div>").hide().fadeIn(2000);
		
		$('#lateral').append(lateral);

		var geral = "<div class='panel panel-info'>"+
						"<div id='geral' class='panel-body'>"+
						"</div>"+
					"</div>";
		
		$('#resultado').append(geral);
		
		montaGrafico();
		montaNoticias();
		
		//Informacoes para contato...
		
		
		//Midias sociais..
		var emails = empresa.emails;
		var twitter_empresa = empresa.twitter_empresa;
		var facebook_empresa = empresa.twitter_empresa;
		
		

		
	}
	
	function geraTabela(codNegociacao,label, nomeDaEmpresa, volume, idGrafico, porcentagem){
		//Cabecalho
		 "<table class='table table-hover'>"+
		"<thead>"+
			"<tr>"+
				"<th>#</th>"+
				"<th>PregÃ£o</th>"+
				"<th>Name</th>"+
				"<th>Volume</th>"+
				"<th>Graph</th>"+
				"<th>Partial</th>"+
			"</tr>"+
		"</thead>"+
		

		//linha completa
		"<tr>"+
			"<td>"+
				"<div class='input-group'>"+
					"<input type='checkbox'>"+
				"</div>"+
			"</td>"+
			"<td><span class='label label-"+label+"'>"+codNegociacao+"</span></td>"+
			"<td>"+nomeDaEmpresa+"</td>"+
			"<td> R$"+volume+"</td>"+
			"<td>"+
				"<div id="+idGrafico+">"+
					"<canvas"+
						"style='display: inline-block; width: 90px; height: 40px; vertical-align: top;"+
						"width='90' height='40'></canvas>"+
				"</div>"+
			"</td>"+
			"<td><span class='text-"+label+"'>"+porcentagem+"%</span></td>"+
		"</tr>"+
	"</table>";
	}
	
}

