package il.ac.technion.logic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class Communicator {

	public static String execute(APIRequest req) throws IOException {

		String $ = "";

		InputStream in;


		URL url = req.getRequestUrl();//new URL(arg0.getStringExtra(URL));

		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(req.getRequestType().value());
		in = new BufferedInputStream(urlConnection.getInputStream());

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


}
