package il.ac.technion.logic.ServerCommunication;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.AsyncTasker.AsyncRequest;
import il.ac.technion.logic.AsyncTasker.ConnectionStatus;
import il.ac.technion.logic.AsyncTasker.Priority;

import java.io.IOException;
import java.net.ConnectException;

/**
 * 
 * @author Sonya (Sofia) Gendelman
 * 
 * this is a function-object.
 * It is for activating a-sync requests to the server
 * the method will activate serverRequest.Invoke();
 * upon completion, UI modification will be executed
 * (UiOnDone if server request was successful and UiOnError if not)
 *
 */


public class ServerInvocation {
	
	void Invoke(final IServerRequest serverRequest, final UiOnDone onRes, final UiOnError onErr){
		
		new AsyncRequest(Priority.IMMEDIATELY) {

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				
				serverRequest.Invoke();		
				return null;
			}
			
			@Override
			public Void onResult(ConnectionStatus status) {
				if(status == ConnectionStatus.RESULT_OK){
					if(onRes != null) onRes.execute();	
				}else{
					if(onErr != null) onErr.execute();
				}
				return null;
			}
		
		}.run();
	}
}
