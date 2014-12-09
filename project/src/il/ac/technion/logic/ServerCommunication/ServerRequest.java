package il.ac.technion.logic.ServerCommunication;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

public abstract class ServerRequest implements IServerRequest{
	
	protected RequestDetails mRequest;

	ServerRequest(RequestDetails request){
		mRequest = request;
	}
	
	public void Invoke() throws IOException, JsonSyntaxException {
		String str =  Communicator.execute(mRequest);
		postAction(str);
	}
	
	abstract void postAction(String serverRes);

}
