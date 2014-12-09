package il.ac.technion.socialcampus;

import il.ac.technion.logic.Objects.User;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserListAdapter extends ArrayAdapter<User> {
	
	private ArrayList<User> mUsers = new ArrayList<User>();
	private Context mContext;
	Long mHotSpotId;
	UserListAdapter mThis = this;
	
	
	public UserListAdapter(Context context, int resource, ArrayList<User> objects) {
		super(context, resource, objects);
		mUsers = objects;
		mContext = context;
	}
	
	static class ViewHolder {
	    public TextView UserName;
	    public ImageView UserImage;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null){
			 LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			 row = inflater.inflate(R.layout.users_view_row, null);
			 
			 ViewHolder viewHolder = new ViewHolder();
			 viewHolder.UserName = (TextView) row.findViewById(R.id.UserName);
			 viewHolder.UserImage = (ImageView) row.findViewById(R.id.UserImage);

			 row.setTag(viewHolder);
		}
		
		ViewHolder hv = (ViewHolder) row.getTag();
		
		//TextView tagView = (TextView) row.findViewById(R.id.tagName);
		TextView UserName = hv.UserName;
		ImageView UserImage = hv.UserImage;
		final User u = mUsers.get(position);
		
		UserName.setText(u.getmName());
		u.setUserPhoto(UserImage);
			
//		((ImageButton) row.findViewById(R.id.chatBtn)).setOnClickListener(
//				new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(mContext, "send user a message", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		((ImageButton) row.findViewById(R.id.addBtn)).setOnClickListener(
//				new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(mContext, "send user freind request", Toast.LENGTH_SHORT).show();
//			}
//		});
		
		return row;
	}
	
	

}
