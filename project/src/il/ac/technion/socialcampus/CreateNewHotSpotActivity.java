package il.ac.technion.socialcampus;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateNewHotSpotActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_hot_spot);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_hot_spot, menu);
		return true;
	}

}
