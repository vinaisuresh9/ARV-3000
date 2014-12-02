package com.ais.arv_3000;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity{
	public static final String hostApi = "http://ec2-54-69-143-63.us-west-2.compute.amazonaws.com:5000/api/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		final Button startBtn = (Button) findViewById(R.id.startButton);
		startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean checked = ((CheckBox) findViewById(R.id.demo)).isChecked();
				Intent startIntent = new Intent(MainActivity.this, MapTextActivity.class);
				AsyncTask<String, String, String> request = new RequestTask().execute(hostApi + "generate_story/0");
				JSONObject result = null;
				try {
					result = new JSONObject(request.get());
					startIntent.putExtra("storyId", result.getInt("story_id"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startIntent.putExtra("demo", checked);
				startActivity(startIntent);
				finish();
			}
		});
	}
}
