import pandas as pd
import numpy as np

ibovespa = pd.read_csv('/home/zegildo/workspace/PhD/src/scriptsPython/correlacao/indices/IndiceIbovespa.csv') 
variacoes = [float(x.replace(",",".")) for x in ibovespa.variacao.values]

print "Max:", np.amax(variacoes)
print "Min:",np.amin(variacoes)
print "Media:",np.mean(variacoes)
print "Mediana:", np.median(variacoes)
print "Desvio Padrao:",np.std(variacoes)

classes = []
for c in variacoes:
	if c < -1.99:
		classes.append("Pessimo")
	elif (c<=-1 and c>=-1.99):
		classes.append("Ruim")
	elif (c<=-0.01 and c>=-0.99):
		classes.append("Levemente Ruim")
	elif (c>=0.0 and c<=0.99):
		classes.append("Levemente Bom")
	elif (c>=1 and c<=1.99):
		classes.append("Bom")
	elif (c>1.99):
		classes.append("Otimo")

ibovespa['classes'] = classes
#copia as informacoes do dataframe ibovespa sem o index_label
ibovespa.to_csv('ibovespa.csv', sep=',', index=False)