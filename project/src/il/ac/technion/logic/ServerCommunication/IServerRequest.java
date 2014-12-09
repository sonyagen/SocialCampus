package il.ac.technion.logic.ServerCommunication;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

public interface IServerRequest {
	public void Invoke() throws IOException, JsonSyntaxException;
}
