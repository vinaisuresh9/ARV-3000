package com.ais.arv_3000;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button startBtn = (Button) findViewById(R.id.startButton);
		startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startIntent = new Intent(MainActivity.this, MapTextActivity.class);
				startActivity(startIntent);
				finish();
			}
		});
	}
}
