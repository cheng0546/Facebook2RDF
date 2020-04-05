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
            //打开和url之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
 
            result = new StringBuffer();
            //读取URL的响应
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
    	String fields = "id,likes";
    	String token = "EAACh63bdojwBAFvoeQb6SzJL3mmwF7qgl5ZAt2WrHEr8ZA9g25NTaoRwzW5gd8V4ljFbgXJMfAHMDNV7EZCimqNeJwWBDuFglYUG4QxZBtfqCdk1Xz4XZAQFh49KuGk4b3LWIY4maSB4vAde3ZCnXO5IWlZCZAYVqL3PKUvrNC8V6tlI0BY2XnIZAOq0RChJvjf5snZAwwbPXl6AZDZD";
        
    	String path = "https://graph.facebook.com/" + id + "?fields=" + fields + "&access_token=" + token;
        JSONObject jsonObj = FacebookApi.getHttpInterface(path);
        System.out.println("Result from Facebook API : " + jsonObj);
    }

}
