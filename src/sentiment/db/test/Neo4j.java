package sentiment.db.test;

import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class Neo4j {

	public static void main(String args[]){

		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase("/Users/zegildo/neo4j");

		//		 Transaction tx = db.beginTx();
		//		 
		//		 try{
		//			 Node noProduto = db.createNode();
		//			 noProduto.setProperty("nome", "Play3");
		//			 noProduto.setProperty("preco", 850.99);
		//			 System.out.println("Id do produto eh: "+noProduto.getId());
		//			 
		//			 Node noCategoria = db.createNode();
		//			 noCategoria.setProperty("nome", "Video Game");
		//			 
		//			 Relationship r = noProduto.createRelationshipTo(noCategoria, Relacionamento.E_UM);
		//			 System.out.println(r.getId());
		//			 tx.success();
		//			 
		//			 
		//		 }catch(Exception e){
		//			 tx.failure();
		//		 }finally{
		//			 tx.finish();
		//		 }

		//START bom=node:node_auto_index(value = 'bom') RETURN bom
		//START n=node(301) MATCH n-->b RETURN n,b limit 500
		//TART n=node(583676) MATCH n-[r]->b RETURN n,r,b
		//457868,332739,263138,170650,411645
		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result = engine.execute("START bom=node(332739) MATCH (bom)-[r]-(x) return bom,r,x limit 1000");

		System.out.println(result.dumpToString());

		db.shutdown();




	}

}
