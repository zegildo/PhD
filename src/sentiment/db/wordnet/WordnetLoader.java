package sentiment.db.wordnet;


//to download RDF dumps of WordNet 3.0:
//git clone git://eculture.cs.vu.nl/home/git/wordnet

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class WordnetLoader {
	//Formato em Portugues...
	//private static final String SOURCE = "src/sentiment/db/wordnet";
	private static final String SOURCE = "/Users/zegildo/Desktop/WordNet-ttl-gz";
	private static final String DEST = "//Users/zegildo/neo4j";
	private static final long BUFFER_SIZE = 1000;

	public static void main(final String[] args) {
		try {
			new WordnetLoader().load();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@SuppressWarnings("rawtypes")
	private void load() throws Exception {
		Neo4jGraph g = new Neo4jGraph(DEST);

		/*@SuppressWarnings("unchecked")
		Sail sail = new GraphSail(g);
		sail.initialize();
		try {
			SailConnection c = sail.getConnection();
			try {
				long startTime = System.currentTimeMillis();

				File dir = new File(SOURCE);
				for (File f : dir.listFiles()) {
					long before = System.currentTimeMillis();
					System.out.println("loading file: " + f);
					String n = f.getName();
					InputStream is;
					if (n.endsWith(".ttl.gz")) {
						is = new GZIPInputStream(new FileInputStream(f));
					} else if (n.endsWith(".ttl")) {
						is = new FileInputStream(f);
					} else {
						continue;
					}
					//Formato para o Wordnet-PT
					//RDFParser p = Rio.createParser(RDFFormat.NTRIPLES);
					RDFParser p = Rio.createParser(RDFFormat.TURTLE);
					p.setStopAtFirstError(false);
					p.setRDFHandler(new SailConnectionAdder(c));

					try {
						p.parse(is, "http://example.org/wordnet/");
					} catch (Throwable t) {
						// Attempt to recover.
						t.printStackTrace();
					} finally {
						is.close();
					}

					long after = System.currentTimeMillis();
					System.out.println("\tfinished in " + (after - before) + "ms");
				}

				long endTime = System.currentTimeMillis();
				System.out.println("resulting triple store has " + c.size() + " statements");
				System.out.println("total load time: " + (endTime - startTime) + "ms");
			} finally {
				c.close();
			}
		} finally {
			sail.shutDown();
		}*/
	}

	private class SailConnectionAdder implements RDFHandler {
		private final SailConnection c;
		private long count = 0;

		private SailConnectionAdder(SailConnection c) {
			this.c = c;
		}

		public void startRDF() throws RDFHandlerException {
		}

		public void endRDF() throws RDFHandlerException {
			try {
				c.commit();
			} catch (SailException e) {
				throw new RDFHandlerException(e);
			}
		}

		public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
			try {
				c.setNamespace(prefix, uri);
			} catch (SailException e) {
				throw new RDFHandlerException(e);
			}

			incrementCount();
		}

		public void handleStatement(Statement statement) throws RDFHandlerException {
			try {
				c.addStatement(statement.getSubject(), statement.getPredicate(), statement.getObject(), statement.getContext());
			} catch (SailException e) {
				throw new RDFHandlerException(e);
			}

			incrementCount();
		}

		public void handleComment(String s) throws RDFHandlerException {
		}

		private void incrementCount() throws RDFHandlerException {
			count++;
			if (0 == count % BUFFER_SIZE) {
				try {
					c.commit();
				} catch (SailException e) {
					throw new RDFHandlerException(e);
				}
			}
		}
	}
}

