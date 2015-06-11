from itertools import islice
from pymongo import MongoClient
from pymongo import ReturnDocument
from bson.objectid import ObjectId
import sys

client = MongoClient()
db = client.stocks
collection = db.informacoesGerais

with open('urlsESTADAO-out-all.txt','r') as f:
	while True:
		group = list(islice(f,2))
		if not group:
			break
		url = group[0].rstrip()
		timestamp = group[1].rstrip()
		collection.find_one_and_update({'url':url},{'$set':{'timestamp':timestamp}})