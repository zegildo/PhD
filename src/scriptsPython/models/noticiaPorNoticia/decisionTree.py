from sklearn.metrics import confusion_matrix
from sklearn.externals.six import StringIO  
from sklearn import tree
import pydot 

def main():
 	CURR_PATH = path.dirname(path.realpath(__file__))
    dataset = pickle.load(open(CURR_PATH+'/arquivoTreinoNumericoEstruturado.p', "rb" ) )
    train, target = dataset['train'], dataset['target']


def noCrossValidation(train,target,numeroArvores):
    X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(train, target, test_size=0.25,random_state=0)
    rf = tree.DecisionTreeClassifier()(n_estimators=numeroArvores, n_jobs=2, max_features=None)
    scores = rf.fit(X_train, Y_train)
    print "\tNO-CrossValidation:", scores.score(X_test, Y_test) 

def crossValidation(train,target,numeroArvores):
    rf = tree.DecisionTreeClassifier()(n_estimators=numeroArvores, n_jobs=2, max_features=None)
    scores = cross_validation.cross_val_score(rf,train,target, cv=25)
    print("\tCrossValidation: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))

def 
#matriz confusao
y_true = [0, 1, 2, 0, 1, 2, 0, 1, 2]
y_pred = [0, 0, 0, 0, 1, 1, 0, 2, 2]
confusion_matrix(y_true, y_pred)

def exibeArvoreGerada():
	dot_data = StringIO() 
	tree.export_graphviz(clf, out_file=dot_data) 
	graph = pydot.graph_from_dot_data(dot_data.getvalue()) 
	graph.write_pdf("iboveTree.pdf") 