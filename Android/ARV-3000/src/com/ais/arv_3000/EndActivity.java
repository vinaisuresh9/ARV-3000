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
import android.widget.TextView;

public class EndActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		int storyId = 0;
		if(this.getIntent().hasExtra("storyId")) {
			storyId = this.getIntent().getIntExtra("storyId", 0);
		}
		TextView completionTV = (TextView) findViewById(R.id.completionTextView);
		AsyncTask<String, String, String>request = new RequestTask().execute(MainActivity.hostApi+"get_story/"+storyId);
		JSONObject requestJson;
		try {
			requestJson = new JSONObject(request.get().toString());
			String s = requestJson.getString("story");
			completionTV.setText(s);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Button newGameBtn = (Button) findViewById(R.id.newGameButton);
		newGameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startIntent = new Intent(EndActivity.this, MainActivity.class);
				startActivity(startIntent);
				finish();
			}
		});
	}
}
