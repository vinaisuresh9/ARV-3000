package com.ais.arv_3000;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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
	ArrayList<String> arr = null;
	private ArrayAdapter<String> adapter;
	private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests);
		
		if(this.getIntent().hasExtra("quests")) {
			arr = this.getIntent().getStringArrayListExtra("quests");
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quests);
		try {
			addItems();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setListAdapter(adapter);
	}
	
	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems() throws JSONException {
    	//Query to backend for the quests
    	for(int i=0; i<arr.size(); i++) {
    		adapter.add((new JSONObject(arr.get(i))).getString("desc"));
    	}
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Intent goOnQuest = new Intent(QuestsActivity.this, MapTextActivity.class);
        goOnQuest.putExtra("Quest_choice", arr.get(position));
        goOnQuest.putExtra("demo", true);
        startActivity(goOnQuest);
        finish();
    }
}
