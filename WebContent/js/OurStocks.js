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

	$('#btnContainer').click(function() {
		$(this).addClass('active');
		$('#btnTimeline').removeClass('active');
		$( "#timeline" ).fadeOut( "slow",  function complete(){
			$( "#container" ).fadeIn( "slow" );
		});

	});

	$('#btnTimeline').click(function() {
		$(this).addClass('active');
		$('#btnContainer').removeClass('active');
		$( "#container" ).fadeOut( "slow",  function complete(){
			$( "#timeline" ).fadeIn( "slow" );
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
$(function() {

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

});

function montaColuna(data){
	
	for(var i = 0; i < data.length; i++){
		
		var empresa = data[i].empresa;
		
		var cnpj = empresa.cnpj.trim();
		var codigo_cvm = empresa.cod_cvm;
		var nomeDaEmpresa = empresa.nome_empresa;
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
		
		var resultado = 
			"<div id='botoesMain' class='btn-group'>"+
				"<button class='btn btn-primary' id='btnGrafico' type='button'>"+
					"<span class='glyphicon glyphicon-check'></span>"+
				"</button>"+
				"<button class='btn btn-primary' id='btnInfo' type='button'>"+
					"<span class='glyphicon glyphicon-home'></span>"+
				"</button>"+
				"<button class='btn btn-primary' id='btnSocialMedia' type='button'>"+
					"<span class='glyphicon glyphicon-user'></span>"+
				"</button>"+
			"</div>"+
			  "<h1 class='page-header'>"+nomeDaEmpresa+"</h1>"+
				"<p class='lead'>"+atividadePrincipal+"</p>"+
					"<div class='row placeholders'>"+
						"<div class='col-xs-6 col-sm-3 placeholder'>"+
							"<img class='img-circle' alt='100x100' src='"+$('#imgs').val()+'/'+cnpj+".jpg'/>"+
						"</div>"+
						
						"<div class='col-xs-6 col-sm-9 placeholder'>"+
							"<div class='panel panel-primary'>"+
			  					"<div class='panel-body'>"+
									"<address>"+
										  "<strong>"+nomePregao+"</strong><br>"+
										  endereco+"<br>"+
										  cidade+", "+estado+" "+cep+"<br>"+
										  "<abbr title='Phone'>P:</abbr>"+telefone+"<br>"+
										  "<abbr title='Fax'>F:</abbr>"+fax+
									"</address>"+
								"</div>"+
			  		"</div>"+
			  	"</div>"+
		  	"</div>";

			
		$("#resultado").append(resultado);

		
		//Informacoes para contato...
		var site = empresa.site;
		
		
		//Midias sociais..
		var emails = empresa.emails;
		var twitter_empresa = empresa.twitter_empresa;
		var facebook_empresa = empresa.twitter_empresa;
		
		
		alert(Data.parse("2013-12-30 00:00:00"));

		
		
		

		
	}
	
}

