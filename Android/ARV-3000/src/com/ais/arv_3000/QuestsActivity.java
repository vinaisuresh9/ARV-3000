package com.ais.arv_3000;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuestsActivity extends ListActivity {
	private ArrayList<String> quests = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests);
		
		if(this.getIntent().hasExtra("QuestCount"))
			this.count = this.getIntent().getIntExtra("QuestCount", 0);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quests);
		addItems();
		setListAdapter(adapter);
	}
	
	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems() {
    	//Query to backend for the quests
    	if(count == 0) {
    		adapter.add("Go to Taco Bell");
    	} else if(count == 1) {
    		adapter.clear();
    		adapter.add("Go to Brown");
    		adapter.add("Go to Pi Kappa Theta");
    		adapter.notifyDataSetChanged();
    	} else {
    		adapter.clear();
    		adapter.add("Examine wallet");
    		adapter.add("Go to Waffle House");
    		adapter.notifyDataSetChanged();
    	}
		//adapter.add("Talk to shopkeeper.");
		//adapter.add("go to dorm.");
		//adapter.add("go to library");
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Intent goOnQuest = new Intent(QuestsActivity.this, MapTextActivity.class);
        goOnQuest.putExtra("Quest_choice", (String)getListView().getItemAtPosition(position));
        goOnQuest.putExtra("StoryCount", this.getIntent().getExtras().getString("StoryCount"));
        String[] newLoc = new String[2];
        if(count == 0) {
        	newLoc[0] = "33.772626";
        	newLoc[1] = "-84.373187";
        }
        else if(count == 1) {
	        if(((String)getListView().getItemAtPosition(position)).equals("Go to Brown")) {
	        	newLoc[0] = "33.771846";
	        	newLoc[1] = "-84.391845";
	    	} else {
	        	newLoc[0] = "33.776595";
	        	newLoc[1] = "-84.393851";
	        }
        }
        goOnQuest.putExtra("LocValue", newLoc);
        startActivity(goOnQuest);
        finish();
    }
}
