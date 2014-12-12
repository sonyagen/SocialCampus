package il.ac.technion.logic.ServerCommunication;

import il.ac.technion.socialcampus.SociaCampusApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class Communicator {

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

	public static String execute(RequestDetails req)  throws IOException {

		String $ = "";

		InputStream in;


		URL url = req.getRequestUrl();

		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();		
		urlConnection.setRequestMethod(req.getRequestType().value());
		if(req.getRequestType()== RequestType.POST || req.getRequestType()== RequestType.PUT){
				
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(15000);
//			urlConnection.setRequestMethod("POST");
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
		if(status/100 != 2){
			Log.e("server response", String.valueOf(status));
			throw new IOException();
		}
		InputStream ins= urlConnection.getInputStream();
		in = new BufferedInputStream(ins);

		$ = readStream(in);
		in.close();

		return $;
	}

	///////////////////////////////////////////////////////////////////////////////
	
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}

	
	static public String uploadFile(Uri uri) {
		int serverResponseCode = 0;

		String s = getRealPathFromURI(SociaCampusApplication.getAppContext(),uri);
		
		String sourceFileUri = s;
		
		String fileName = sourceFileUri;
		
		HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
      
         
        if (!sourceFile.isFile()) { 
             Log.e("uploadFile", "Source File not exist ");
             return "";
        }
       
        try { 
        	String upLoadServerUri = "http://api.social-campus.org/v1.0/event/image";
             
			// open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
  
			// Open a HTTP  connection to  the URL
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("image", fileName); 
  
			dos = new DataOutputStream(conn.getOutputStream());
    
            dos.writeBytes(twoHyphens + boundary + lineEnd); 
            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                                       + fileName + "\"" + lineEnd);
              
            dos.writeBytes(lineEnd);
    
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available(); 
    
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
    
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                
            while (bytesRead > 0) {
            	dos.write(buffer, 0, bufferSize);
            	bytesAvailable = fileInputStream.available();
            	bufferSize = Math.min(bytesAvailable, maxBufferSize);
            	bytesRead = fileInputStream.read(buffer, 0, bufferSize);           
            }
    
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                 // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
               
            Log.i("uploadFile", "HTTP Response is : "
                     + serverResponseMessage + ": " + serverResponseCode);
              
            if(serverResponseCode == 200){
                
//            Toast.makeText(MyApplication.getAppContext(), "File Upload Complete", 
//                         Toast.LENGTH_SHORT).show();
                           
            }    
              
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            
            InputStream in;

            InputStream ins = conn.getInputStream();
    		in = new BufferedInputStream(ins);

    		String resSTR = readStream(in);
    		in.close();

    		return resSTR;
    		
              
        } catch (MalformedURLException ex) {
        	Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
        } catch (Exception e) {
            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
        }     
        return "";
	} 
	
}
