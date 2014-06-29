package il.ac.technion.socialcampus;


import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.User;
import il.ac.technion.logic.UserManager;

import java.util.Set;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
//import com.google.android.gms.tagmanager.TagManager;

public class ProfileActivity extends FragmentActivity implements OnClickListener,
ConnectionCallbacks, OnConnectionFailedListener, Tag.onTagClickListener {

	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;
	private  ProgressDialog progressDialog;

	// We use mSignInProgress to track whether user has clicked sign in.
	// mSignInProgress can be one of three values:
	//
	//       STATE_DEFAULT: The default state of the application before the user
	//                      has clicked 'sign in', or after they have clicked
	//                      'sign out'.  In this state we will not attempt to
	//                      resolve sign in errors and so will display our
	//                      Activity in a signed out state.
	//       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
	//                      in', so resolve successive errors preventing sign in
	//                      until the user has successfully authorized an account
	//                      for our app.
	//   STATE_IN_PROGRESS: This state indicates that we have started an intent to
	//                      resolve an error, and so we should not start further
	//                      intents until the current intent completes.
	
	private int mSignInProgress;
	private static final String SAVED_PROGRESS = "sign_in_progress";
	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "MainActivity";

	Context mContext = this;
	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	private SignInButton btnSignIn;
	private Button btnSignOut, btnRevokeAccess;
	private ImageView imgProfilePic;
	private TextView txtName;
	private LinearLayout all;
	private TextView tagsView;
	private ImageButton editTags;

	// lifecycle 
	//=====================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressDialog = new ProgressDialog(this);
		setContentView(R.layout.activity_profile);
		//signed in view
		all = (LinearLayout) findViewById(R.id.all);
		//google btn
		btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
		btnSignOut = (Button) findViewById(R.id.btn_sign_out);
		btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
		//1st area: pic & name
		imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
		txtName = (TextView) findViewById(R.id.txtName);
		//2nd area: tags
		tagsView = (TextView) findViewById(R.id.tags);
		editTags = (ImageButton) findViewById(R.id.editBtn);
		
		//3nd area: pinned events
		
		

		// Button click listeners
		btnSignIn.setOnClickListener(this);
		btnSignOut.setOnClickListener(this);
		btnRevokeAccess.setOnClickListener(this);
		editTags.setOnClickListener(this);
		
		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState
					.getInt(SAVED_PROGRESS, STATE_DEFAULT);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	protected void onStart() {
		super.onStart();

		if(!UserManager.isLoggedIn(mContext)){
			mGoogleApiClient.connect();
		}else{
			updateUI(true);
		}
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}
	
	// handle connection 
	//=====================================================
	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {

				// If the error resolution was successful we should continue
				// processing errors.
				mSignInProgress = STATE_SIGN_IN;
				mSignInClicked = false;
			} else {
				// If the error resolution was not successful or the user canceled,
				// we should stop processing errors.
				mSignInProgress = STATE_DEFAULT;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;		
		// Get user's information
		final User u = getProfileInformation();
		//Get the currently logged in user

		if(UserManager.INSTANCE.isRegistered(u.getmId())){
			//Set the current logged user
			//in case of illegal ID the current user will stay the anonymous
			UserManager.INSTANCE.setCurrentUser(u.getmId());
			UserManager.setLoggedIn(mContext, u.getmId());
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			updateUI(true);
		}else {
			UserManager.INSTANCE.addNewUser(u,  new UiOnDone() {

				@Override
				public void execute() {

					// Update the UI after signin
					UserManager.INSTANCE.setCurrentUser(u.getmId());
					UserManager.setLoggedIn(mContext, u.getmId());
					updateUI(true);

					// Indicate that the sign in process is complete.
					mSignInProgress = STATE_DEFAULT;
					progressDialog.dismiss();
				}
			},new UiOnError(getApplicationContext()){
				@Override
				public void execute() {
					super.execute();
					signOutFromGplus();
					progressDialog.dismiss();
				}
			});
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {

		if (mConnectionResult!= null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mSignInProgress = STATE_IN_PROGRESS;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mSignInProgress = STATE_SIGN_IN;
				mGoogleApiClient.connect();
			}
		}
	}

	
	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if(this.progressDialog!=null){
			progressDialog.dismiss();
		}
		if (isSignedIn) {
			btnSignIn.setVisibility(View.GONE);
			all.setVisibility(View.VISIBLE);
			
			User currentU = UserManager.INSTANCE.getMyData();
	
			txtName.setText(currentU.getmName());
			currentU.setUserPhoto(imgProfilePic);
			buildTags();
			
			
			
		} else {
			btnSignIn.setVisibility(View.VISIBLE);
			all.setVisibility(View.GONE);

		}
	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private User getProfileInformation() {
		User u = null;
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();


				u = new User();
				u.setmId(currentPerson.getId());
				u.setmName(personName);
				u.setmImage(personPhotoUrl);

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

	
	// SpannableString - into a factory
	//=====================================================
	
	private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
	    SpannableString link = new SpannableString(text);
	    link.setSpan(new ClickableString(listener), 0, text.length(), 
	        SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    return link;
	}

	private void makeLinksFocusable(TextView tv) {
	    MovementMethod m = tv.getMovementMethod();  
	    if ((m == null) || !(m instanceof LinkMovementMethod)) {  
	        if (tv.getLinksClickable()) {  
	            tv.setMovementMethod(LinkMovementMethod.getInstance());  
	        }  
	    }  
	}
	
	private static class ClickableString extends ClickableSpan {  
	    private View.OnClickListener mListener;          
	    public ClickableString(View.OnClickListener listener) {              
	        mListener = listener;  
	    }          
	    @Override  
	    public void onClick(View v) {  
	        mListener.onClick(v);  
	    }        
	}
	
	// usin the SpannableString methods - can go to factory
	//=====================================================

	private void buildTags(){
		
		tagsView.setText("");
		
		Set<Long> tagIds = UserManager.INSTANCE.getMyData().getmTags();
		Set<Tag> Tags = TagManager.INSTANCE.getItemsbyIds(tagIds);
		for(Tag t:Tags){
			t.setListener(this);
			SpannableString link = makeLinkSpan(t.getmName(), t);
			tagsView.append("#");
			tagsView.append(link);
			tagsView.append("	");
		}
		
		makeLinksFocusable(tagsView);
		
	}

	@Override
	public void onTagClick(Long tid) {
		Tag t = TagManager.INSTANCE.getItemsbyId(tid);
		//TODO intent to View Tag activity
		Toast.makeText(mContext, t.getmName(), Toast.LENGTH_SHORT).show();
	}
	


	// google+ account: login/logout/revoke
	//=====================================================
	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		switch (v.getId()) {

		case R.id.editBtn:
			//TODO intent to manage Tags Activity/Dialog?
			break;
		case R.id.btn_sign_in:
			// Signin button clicked
			//			mGoogleApiClient.connect();
			progressDialog.setMessage(this.getResources().getString(R.string.log_in_msg));
			progressDialog.show();
			signInWithGplus();

			break;
		case R.id.btn_sign_out:
			// Signout button clicked
			progressDialog.setMessage(this.getResources().getString(R.string.log_out_msg));
			progressDialog.show();
			UserManager.INSTANCE.logout(mContext);
			signOutFromGplus();
			progressDialog.dismiss();
			break;
		case R.id.btn_revoke_access:
			// Revoke access button clicked
			progressDialog.setMessage(this.getResources().getString(R.string.revoke_access_msg));
			progressDialog.show();
			UserManager.INSTANCE.removeUser( 
					UserManager.INSTANCE.getMyData(),
					new UiOnDone() {
						@Override
						public void execute() {
							revokeGplusAccess();
							UserManager.INSTANCE.logout(mContext);

						}
					}, new UiOnError(getApplicationContext()){
						@Override
						public void execute() {

						}
					});

			break;
		}

	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			mSignInProgress = STATE_IN_PROGRESS;
			resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();


			//	updateUI(false);
		}
		updateUI(false);
	}

	/**
	 * Revoking access from google
	 * */
	private void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
			.setResultCallback(new ResultCallback<Status>() {
				@Override
				public void onResult(Status arg0) {
					Log.e(TAG, "User access revoked!");
					mGoogleApiClient.connect();
					updateUI(false);
				}

			});
		}
	}


}