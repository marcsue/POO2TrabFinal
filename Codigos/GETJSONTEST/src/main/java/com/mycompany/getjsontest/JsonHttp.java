package com.mycompany.getjsontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import static org.apache.http.HttpVersion.HTTP;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public class JsonHttp {
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JSONObject readJsonFromURL (String URL) throws IOException, JSONException {
        InputStream is = new URL(URL).openStream();
        try {
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
            
        } finally {
            is.close();
        }
        
        
    }
    
    public static JSONObject postJsonToURL (JSONObject json, String URL) throws UnsupportedEncodingException, IOException {
        try {
            List<NameValuePair> params;
            params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("json", json.toString()));
            
            HttpClient client = HttpClientBuilder.create().build();            
            HttpPost p = new HttpPost(URL);
            
            p.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse response = client.execute(p);
            
            JSONObject js = null;
            
            if (response != null) {
                InputStream in = response.getEntity().getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                js = new JSONObject(jsonText);
            }
            
            return js;
            
        } finally {
                
        }
    }
    
    public static void main(String[] args) throws IOException , JSONException {
        JSONObject json = readJsonFromURL("http://echo.jsontest.com/key/value/one/two");
        System.out.println(json.toString());
        System.out.println(json.get("one"));
        
        JSONObject js2 = postJsonToURL(json, "http://validate.jsontest.com/");
        System.out.println(js2);
        System.out.println(js2.get("validate"));
    }
    
}
