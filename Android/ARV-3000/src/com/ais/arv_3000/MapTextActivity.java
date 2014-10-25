package com.ais.arv_3000;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTextActivity extends Activity {
	private GoogleMap map;
	private int SCREEN_HEIGHT;
	private boolean quest = false;
	private boolean moreStory = false;
	private LocationManager locMan;
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
	
	private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
	private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maptextview);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_HEIGHT = size.y;
        
        LinearLayout mLay = (LinearLayout) findViewById(R.id.mapLayout);
        LayoutParams mLayParams = (LayoutParams) mLay.getLayoutParams();
        mLayParams.height = 3*(SCREEN_HEIGHT/4); 
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        
        if(map != null) {
        	map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location arg0) {
					LatLng point = new LatLng(arg0.getLatitude(), arg0.getLongitude());
					map.addMarker(new MarkerOptions()
			        .position(point)
			        .title("Current Location"));
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
				}
			});
        }
        
        final TextView storyline = (TextView) findViewById(R.id.storyTextView);
        String s = "She went to the store to buy milk, and ran into a friend"; //query here like 10-15
        if(this.getIntent().hasExtra("Quest_choice")) {
        	//get story info from quest path chosen, below assuming shopkeeper path chosen
        	float[] loc = this.getIntent().getExtras().getFloatArray("LocValue");
        	saveCoordinatesInPreferences(loc[0],loc[1]);

        	locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	locMan.requestLocationUpdates(
        			LocationManager.GPS_PROVIDER,
        			MINIMUM_TIME_BETWEEN_UPDATE,
        			MINIMUM_DISTANCECHANGE_FOR_UPDATE,
        			new MyLocationListener());
        	s = "She and her friend talk to the shopkeeper who then spins them a tale of dragon riders.";
        }
        storyline.setText(s);
        
        Button textQuestButton = (Button) findViewById(R.id.nextQuestButton);
        textQuestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				quest = Boolean.parseBoolean("false");//Query again
				if(quest) {
					Intent questList = new Intent(MapTextActivity.this, QuestsActivity.class);
					startActivity(questList);
					finish();
				}
				else {
					//query next part of story
					storyline.setText("She spoke of days of old when dragons, elves, and all kinds of"
							+ " outlandish creatures roamed the earth.");
				}
			}
		});
    }

	private void saveCoordinatesInPreferences(float latitude, float longitude) {
		SharedPreferences prefs =
				this.getSharedPreferences(getClass().getSimpleName(),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
		prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
		prefsEditor.commit();
	}

	
	private Location retrievelocationFromPreferences() {
		SharedPreferences prefs =
			this.getSharedPreferences(getClass().getSimpleName(),
			Context.MODE_PRIVATE);
		Location location = new Location("POINT_LOCATION");
		location.setLatitude(prefs.getFloat(POINT_LATITUDE_KEY, 0));
		location.setLongitude(prefs.getFloat(POINT_LONGITUDE_KEY, 0));
        return location;
	}

	
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location arg0) {
			Location pointLocation = retrievelocationFromPreferences();
			float distance = arg0.distanceTo(pointLocation);
			Toast.makeText(MapTextActivity.this,
					"Distance from Point:"+distance, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onProviderDisabled(String provider) {}
		
	}
}