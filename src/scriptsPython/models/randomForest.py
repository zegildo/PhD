from sklearn.ensemble import RandomForestClassifier
from sklearn import cross_validation, preprocessing
from os import path
import pandas as pd
import pickle

def main():
    CURR_PATH = path.dirname(path.realpath(__file__))
    dataset = pickle.load(open(CURR_PATH+'/arquivoTreinoNumericoEstruturado.p', "rb" ) )
    train, target = dataset['train'], dataset['target']
    noCrossValidation(train,target)
    crossValidation(train,target)

def noCrossValidation(train,target):
    #create and train the random forest
    #multi-core CPUs can use: rf = RandomForestClassifier(n_estimators=100, n_jobs=2)
    X_train, X_test, Y_train, Y_test = cross_validation.train_test_split(train, target, test_size=0.25,random_state=0)
    rf = RandomForestClassifier(n_estimators=200, n_jobs=2, max_features=None)
    scores = rf.fit(X_train, Y_train)
    print "scores noCrossValidation:", scores.score(X_test, Y_test) 

def crossValidation(train,target):
    rf = RandomForestClassifier(n_estimators=200, n_jobs=2, max_features=None)
    scores = cross_validation.cross_val_score(rf,train,target, cv=25)
    print "scores CrossValidation:", scores
    print("Accuracy: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))

    #savetxt('Data/submission2.csv', rf.predict(test), delimiter=',', fmt='%f')

if __name__=="__main__":
    main()