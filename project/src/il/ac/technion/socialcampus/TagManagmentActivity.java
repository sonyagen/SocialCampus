package il.ac.technion.socialcampus;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.Tag;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class TagManagmentActivity extends FragmentActivity 
	implements TagSearchFragment.OnFragmentInteractionListener {

	private TagListAdapter adapter;
	private ArrayList<Tag> mTagList = new ArrayList<Tag>();
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_managment);
		if (savedInstanceState == null)
			setAutoCompleteSerchBox();
		SetTagListView();
	}

	private void setAutoCompleteSerchBox() {
			TagSearchFragment f = (TagSearchFragment)TagSearchFragment.newInstance();
			getSupportFragmentManager().beginTransaction().add(R.id.frameBoxForTagSearch, f).commit();
	}

	private void SetTagListView() {
		String callerType = (String) getIntent().getExtras().get("type");
		Long hotSpotId = callerType.equals("hotspot") ? (Long)getIntent().getExtras().get("id") : -1L;
		
		adapter = new TagListAdapter(this, 
				R.layout.tags_view_row, getTagsListFromCaller(),callerType.equals("hotspot"), hotSpotId);

		ListView lv = (ListView) findViewById(R.id.tagListView );         
		if (lv != null){  lv.setAdapter(adapter) ;}
		
		//set listeners
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adp, View v, int pos, long id) {
				//TODO implement
			}});
	}
	
	private ArrayList<Tag> getTagsListFromCaller(){
		String type = (String) getIntent().getExtras().get("type");
		
		List<Long> Ids;
		
		if(type.equals("hotspot")){
			Long hId = (Long)getIntent().getExtras().get("id");
			Ids = LocalDBManager.INSTANCE.TagHotSpot.getTagsFromHotSpot(hId);
		}
			
		else if(type.equals("user")){
			String uId = (String)getIntent().getExtras().get("id");
			Ids = LocalDBManager.INSTANCE.UserTag.getTagsFromUser(uId);
		}
		
		else
			return null;
		
		
		mTagList.clear();
		mTagList.addAll(LocalDBManager.INSTANCE.TagDB.getItemsbyIds(Ids));
		return mTagList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_managment, menu);
		return true;
	}

	@Override
	public void addNewTag(String tagName, final Long tagId) {
		// tag dosen't exist in the system
		if (tagId.equals(-1L)){
			
			//fixString
			tagName = tagName.trim();
			tagName = WordUtils.capitalize(tagName);
			tagName = tagName.replaceAll("\\s+","");
			
			final String editedTagName = tagName;
			
			//dialog to approve new tagging
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
			 builder.setTitle("New Tag")
			 .setMessage("Creating new Tag:  #"+ tagName + "\n")
             .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                	 final Tag toAdd = new Tag(0L,editedTagName);
                	 ServerRequestManager.INSTANCE.addTag(toAdd, new UiOnDone() {
						@Override
						public void execute() {
							addTagToObj(toAdd.getmId());
						}
					}, new UiOnError(mContext));
                 }
             })
             .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                     // User cancelled the dialog
                 }
             });
			 // Create the AlertDialog object and return it
			 AlertDialog d = builder.create();
			 d.show();
		}
		else{
			addTagToObj(tagId);
		}
		
	}
	
	void addTagToObj(final Long tagId){
		//add tag id to user/hotSpot and refresh the view
		String callerType = (String) getIntent().getExtras().get("type");
		Long hotSpotId = callerType.equals("hotspot") ? (Long)getIntent().getExtras().get("id") : -1L;
		
		//if dealing with hotspot tags
		if (callerType.equals("hotspot")){
			if(LocalDBManager.INSTANCE.TagHotSpot.isCombined(tagId, hotSpotId)){
				Toast.makeText(mContext, "Tag already attached.", Toast.LENGTH_SHORT).show();
				return;
			}
			ServerRequestManager.INSTANCE.JoinHotSpotTag(hotSpotId, tagId, new UiOnDone() {
				@Override
				public void execute() {
					mTagList.add(LocalDBManager.INSTANCE.TagDB.getItemById(tagId));
					adapter.notifyDataSetChanged();
				}
			}, new UiOnError(this));
			
		}
		// dealing with user tags
		else{
			if(LocalDBManager.INSTANCE.UserTag.isCombined(UserManager.INSTANCE.getMyID(), tagId)){
				Toast.makeText(mContext, "Tag already attached.", Toast.LENGTH_SHORT).show();
				return;
			}
			ServerRequestManager.INSTANCE.JoinUserTag(UserManager.INSTANCE.getMyID(), tagId, new UiOnDone() {
				@Override
				public void execute() {
					mTagList.add(LocalDBManager.INSTANCE.TagDB.getItemById(tagId));
					adapter.notifyDataSetChanged();
				}
			}, new UiOnError(this));
		}
	}

}
