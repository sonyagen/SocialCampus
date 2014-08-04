package il.ac.technion.logic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;



public class Communicator {

	public static String execute(APIRequest req) throws IOException {

		String $ = "";

		InputStream in;


		URL url = req.getRequestUrl();

		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(req.getRequestType().value());
		if(req.getRequestType()==RequestType.POST){
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(15000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			
			OutputStream os = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
			        new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(req.getRequestParametersMap()));
			writer.flush();
			writer.close();
			os.close();

			urlConnection.connect();
		
		}
		int status = urlConnection.getResponseCode();
		if(status%100 != 2){
			Log.e("server response", String.valueOf(status));
			return null;
		}
		InputStream ins= urlConnection.getInputStream();
		in = new BufferedInputStream(ins);

		$ = readStream(in);
		in.close();

		return $;
	}

	
	private static String readStream(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			//TODO: handle connection errorLog.e("something bad", "IOException", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				//TODO: handle connection error Log.e("something bad", "IOException", e);
			}
		}
		return sb.toString();
	}

	private static String getQuery(Map<String,String> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	   
		for(String k: params.keySet()){
			 if (first){
		            first = false;
		     } else {
		            result.append("&");
			 }
			 result.append(URLEncoder.encode(k, "UTF-8"));
		        result.append("=");
		        result.append(URLEncoder.encode( params.get(k), "UTF-8"));
		}

	    return result.toString();
	}

}
