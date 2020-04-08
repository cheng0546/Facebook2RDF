package facebook2RDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;


public class JsonToRDF {
	JSONObject jsonObj;
	
	public JsonToRDF(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
		System.out.println("Result from Facebook API : " + this.jsonObj);
		try {
			this.createRDF();
		} catch(FileNotFoundException el) {
			System.out.println(el);
		}
	}
	
	public void createRDF() throws FileNotFoundException {
		//Model model = ModelFactory.createDefaultModel();
		OntModel base = ModelFactory.createOntologyModel();
		base.read("./src/assets/facebookApi.rdf", "RDF/XML");
		
		String ns = "http://www.semanticweb.org/yanni/ontologies/2020/3/facebookApi#";
		OntClass userClass = base.getOntClass(ns + "User"); 
		String userName = this.jsonObj.getString("name").replace(" ", "_");
		Individual user = base.createIndividual(ns + userName, userClass);
		String userId = this.jsonObj.getString("id");
		DatatypeProperty userIdP = base.getDatatypeProperty(ns + "userId");
		user.addProperty(userIdP, userId);
		String gender = this.jsonObj.getString("gender");
		OntClass genderClass = base.getOntClass(ns + "Gender");
		ObjectProperty hasGender = base.getObjectProperty(ns + "hasGender");
		Individual genderI = base.createIndividual(ns + gender, genderClass);
		user.addProperty(hasGender, genderI);
		if(gender.equals("male")) {
			OntClass manClass = base.getOntClass(ns + "Man");
			user.setOntClass(manClass);
		} else {
			OntClass womanClass = base.getOntClass(ns + "Woman");
			user.setOntClass(womanClass);
		}
		String email = this.jsonObj.getString("email");
		OntClass emailClass = base.getOntClass(ns + "Email");
		ObjectProperty hasEmail = base.getObjectProperty(ns + "hasEmail");
		ObjectProperty isEmailOf = base.getObjectProperty(ns + "isEmailOf");
		hasEmail.setInverseOf(isEmailOf);
		Individual emailI = base.createIndividual(ns + "emailOf" + userName, emailClass);
		DatatypeProperty emailAddress = base.getDatatypeProperty(ns + "emailAddress");
		emailI.addProperty(emailAddress, email);
		user.addProperty(hasEmail, emailI);
		emailI.addProperty(isEmailOf, user);
		
		JSONObject likes = this.jsonObj.getJSONObject("likes");
		JSONArray data = likes.getJSONArray("data");
		JSONObject row = null;
		for(int i = 0; i < data.size(); i++) {
			row = data.getJSONObject(i);
			String pageName = row.getString("name").replace(" ", "_");
			OntClass pageClass = base.getOntClass(ns + "Page");
			Individual page = base.createIndividual(ns + pageName, pageClass);
			ObjectProperty likePage = base.getObjectProperty(ns + "likePage");
			ObjectProperty likedBy = base.getObjectProperty(ns + "likedBy");
			likePage.setInverseOf(likedBy);
			page.addProperty(likedBy, user);
			user.addProperty(likePage, page);
			String pageId = row.getString("id");
			DatatypeProperty pageIdP = base.getDatatypeProperty(ns + "pageId");
			page.addProperty(pageIdP, pageId);
			String category = row.getString("category").replace(" ", "_");
			OntClass categoryClass = base.getOntClass(ns + "Category");
			Individual categoryI = base.createIndividual(ns + category, categoryClass);
			ObjectProperty hasPage = base.getObjectProperty(ns + "hasPage");
			ObjectProperty isCategoryOf = base.getObjectProperty(ns + "isCategoryOf");
			hasPage.setInverseOf(isCategoryOf);
			page.addProperty(isCategoryOf, categoryI);
			categoryI.addProperty(hasPage, page);
			String time = row.getString("created_time");
			DatatypeProperty likedTime = base.getDatatypeProperty(ns + "likedTime");
			page.addProperty(likedTime, time);
		}

		StmtIterator iter = base.listStatements();
		
		 while(iter.hasNext()){  
             Statement stmt = iter.nextStatement();  
             Resource subject = stmt.getSubject();  
             Property predicate = stmt.getPredicate();  
             RDFNode object = stmt.getObject();  

             System.out.print(subject.toString());  
             System.out.print(" "+predicate.toString());  
             if(object instanceof Resource){  
                 System.out.print(object.toString());  
             }else{  
                 System.out.print("\"" + object.toString() + "\"");  
             }  

             System.out.println(" .");  
         } 
  
         base.write(System.out);  
         System.out.println();  
         base.write(System.out, "RDF/XML-ABBREV");  
         System.out.println();  
         System.out.println("------------------------------------");
         File outfile = new File("./src/assets/ontology.rdf");
         FileOutputStream outputStream = new FileOutputStream(outfile);
         base.write(System.out, "N-TRIPLE");
         base.write(outputStream, "RDF/XML-ABBREV");
         
         new SparqlTest();
	}
	
	public String getStringReplaced (String str) {
		return str.replace(" ", "_");
	}
}
