package crawler.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDB {


	private static DBCollection mongoClient;

	private MongoDB() {
	}

	public static synchronized DBCollection getInstance() throws UnknownHostException {
		if (mongoClient == null){
			MongoClient client = new MongoClient();
			DB db = client.getDB( "stocks" );
			mongoClient = db.getCollection("informacoesGerais");
		}
		return mongoClient;
	}


}
