COPY empresas FROM '/Users/zegildo/Desktop/DadosEmpresas.csv' CSV;
COPY empresas_isin FROM '/Users/zegildo/Desktop/DadosEmpresasISINs.csv' CSV;
COPY contato_investidores FROM '/Users/zegildo/Desktop/DadosContatosInvestidores.csv' CSV;
COPY links_noticias_empresa FROM '/Users/zegildo/Desktop/DadosContatosInvestidores.csv' CSV;

COPY cotacoes( data_pregao,
    cod_bdi,
    cod_negociacao,
    tipo_mercado, 
    nome_resumido,
    especificacao_papel,
    prazo_termo,
    moeda_referencia,
    preco_abertura        ,
    preco_maximo          , 
    preco_minimo          , 
    preco_medio           , 
    preco_ultimo          , 
    preco_melhor_compra   , 
    preco_melhor_venda    , 
    total_negocios        ,
    qtd_titulos           ,
    volume_titulos        , 
    preco_exercicio       , 
    ind_mercado_opcoes    , 
    data_vencimento       ,
    fator_cotacao         , 
    pontos_exercicio      ,
    cod_isin              ,
    num_distribuicao      ) FROM '/Users/zegildo/Desktop/teste.csv' CSV;

select * from cotacoes;

select * from empresas;

insert into empresas_isin values ('12345678909876','ACESACON')
insert into empresas values ('teste','','','','12345678909876','','','','','','','','','','','','','','');