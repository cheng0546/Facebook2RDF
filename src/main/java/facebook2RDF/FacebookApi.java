package facebook2RDF;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;

public class FacebookApi {
	
	public static JSONObject getHttpInterface(String path){
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
 
            result = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonObj = JSONObject.fromObject(result.toString());
            return jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
 
    public static void main(String[] args){
    	
    	String id = "2468713943378827";
    	String fields = "id,name,gender,email,likes{id,name,created_time,category}";
    	String token = "EAACh63bdojwBAGRUkHqPhTQ2qgAtATchpNWM7FAyhU4zXGOv6MZBUvcdemMqYIFEai9Y17Q325MMGoxBhZB2UzktZAYMDEhQZBCbJlgrtCBSxCnA1frvJbXRFHKOvZCGI7NopIHmebsxD07f7jtzioLScvKtZAdsQdg3u8Ob8wnaUp2DUWIZCte966j1SiZBiGZBLeImAC8TH3wZDZD";
        
    	String path = "https://graph.facebook.com/" + id + "?fields=" + fields + "&access_token=" + token;
        JSONObject jsonObj = FacebookApi.getHttpInterface(path);
        new JsonToRDF(jsonObj);
    }

}
