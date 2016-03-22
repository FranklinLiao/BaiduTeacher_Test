package com.franklin.voice;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class VoiceDeal {
	private static final String serverURL = "http://vop.baidu.com/server_api";
	private static final String tokenURL =  "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials";
    private static final String apiKey = "1oblhCsLVVGsALrCx9iBL6Vb";
    private static final String secretKey = "08360a6aeeecde587556d87802379973";
    private static final String cuid = "FranklinLiao";
    private static final String testFileName = "C:\\Users\\Franklin\\Desktop\\73999749wav\\1-9\\9.wav";//"D:/test.pcm";
    private static VoiceDeal voiceDeal = null;
    //private constructor
    private VoiceDeal() {}
    public static synchronized VoiceDeal getInstance() {
		if(voiceDeal == null) {
			voiceDeal = new VoiceDeal();
		}
    	return voiceDeal;
    }
    
    private static String getToken() throws IOException{
    	String token = "";
        String getTokenURL = tokenURL + "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        JSONObject json = getJsonResponse(conn);
        System.out.println("token:" + json);
        if(json != null) {
        	token = json.getString("access_token");
        }
        return token;
    }
    
    private static JSONObject getJsonResponse(HttpURLConnection conn) throws IOException {
    	 InputStream is = conn.getInputStream();
         BufferedReader rd = new BufferedReader(new InputStreamReader(is));
         StringBuffer response = new StringBuffer();
         if (conn.getResponseCode() != 200) {
             return null;
         }
         String line;
         while ((line = rd.readLine()) != null) {
             response.append(line);
             response.append("\r");
         }
         is.close();
         rd.close();
         JSONObject json =JSON.parseObject(response.toString());
         return json;
    }
    
    public String getVoice(File file) throws MalformedURLException, IOException   {
       HttpURLConnection conn = getPostConnection(file);
       JSONObject json = getJsonResponse(conn);
       System.out.println("voiceResult: " + json);
       String result = "";
       if(json != null) {
	       int errCode = json.getInteger("err_no");
	       if(errCode == 0) { // no err
	    	   JSONArray jsons = json.getJSONArray("result");
	    	   result = jsons.get(0).toString(); //get the first result
	       } else {
	    	   result = json.getString("err_msg");
	       }	
       } 
       return result;
    }
    
    private static HttpURLConnection getPostConnection(File file) throws MalformedURLException, IOException {
    	HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();
		byte[] fileBytes = getFileBytes(file);
        JSONObject params = new JSONObject();
        params.put("format", "wav");//pcm
        params.put("rate", 16000);//8000
        params.put("channel", "1");
        params.put("token", getToken());
        params.put("cuid", cuid);
        params.put("len", file.length());
        params.put("speech", DatatypeConverter.printBase64Binary(fileBytes)); //base64 code
        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();
        return conn;
    }
    
    private static byte[] getFileBytes(File file) throws IOException {
    	byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
}
