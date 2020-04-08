package facebook2RDF;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class SparqlTest {
	public static String inputFileName = "./src/assets/ontology.rdf";
	
	public SparqlTest() {
		this.sparqlQuery();
	}
    public void sparqlQuery() {
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        model.read(inputFileName);
            
        // an example: display the pages and their categories that liked by the user who has the userId "2468713943378827"
        String queryString = "SELECT DISTINCT ?user ?pages ?category WHERE { ?user <http://www.semanticweb.org/yanni/ontologies/2020/3/facebookApi#userId> '2468713943378827'; <http://www.semanticweb.org/yanni/ontologies/2020/3/facebookApi#likePage> ?pages. ?pages <http://www.semanticweb.org/yanni/ontologies/2020/3/facebookApi#isCategoryOf> ?category }";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        qe.close();
    }
}