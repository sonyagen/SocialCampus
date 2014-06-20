package il.ac.technion.logic;

import android.content.Context;
import android.widget.Toast;

public class UiOnError{
	Context c;
	String msg = "Bad connection. Try again later";
	
	public UiOnError(Context c){
		this.c = c;
	}
	
	public UiOnError(Context c, String msg){
		this.c = c;
		this.msg = msg;
	}
	public void execute(){
		Toast.makeText(c, msg , Toast.LENGTH_SHORT).show();
	}
}