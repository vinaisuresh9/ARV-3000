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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quests);
		addItems();
		setListAdapter(adapter);
	}
	
	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems() {
    	//Query to backend for the quests
		adapter.add("Talk to shopkeeper.");
		adapter.add("go to dorm.");
		adapter.add("go to library");
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Intent goOnQuest = new Intent(QuestsActivity.this, MapTextActivity.class);
        goOnQuest.putExtra("Quest_choice", (String)getListView().getItemAtPosition(position));
        float[] newLoc = {17, 10};
        goOnQuest.putExtra("LocValue", newLoc);
        startActivity(goOnQuest);
        finish();
    }
}
