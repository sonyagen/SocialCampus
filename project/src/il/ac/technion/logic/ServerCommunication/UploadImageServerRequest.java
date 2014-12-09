package il.ac.technion.logic.ServerCommunication;

import java.io.IOException;

import android.net.Uri;

import com.google.gson.JsonSyntaxException;

public abstract class UploadImageServerRequest implements IServerRequest{

	Uri mUri;
	UploadImageServerRequest(Uri uri){
		mUri = uri;
	}
	
	@Override
	public void Invoke() throws IOException, JsonSyntaxException {
		String serverRes = Communicator.uploadFile(mUri);
		postAction(serverRes);
		
	}
	abstract void postAction(String serverRes);

}
