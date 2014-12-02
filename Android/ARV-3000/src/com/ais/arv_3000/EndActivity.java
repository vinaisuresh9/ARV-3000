package com.ais.arv_3000;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		
		TextView completionTV = (TextView) findViewById(R.id.completionTextView);
		String s = completionTV.getText().toString();
		completionTV.setText(s+" 0%"); 
		
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
