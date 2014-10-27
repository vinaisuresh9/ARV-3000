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
import android.util.Log;
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
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
	
	private int count = 0;
	
	//For mock Locations
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private Marker current;
	
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
					current = map.addMarker(new MarkerOptions()
			        .position(point)
			        .title("Current Location"));
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
					current.remove();
				}
			});
        }
        
        final TextView storyline = (TextView) findViewById(R.id.storyTextView);
        String s = "You wake up in Van Leer, and you don’t recall the previous day. But you think something big happened."; //query here like 10-15
        if(this.getIntent().hasExtra("Quest_choice")) {
        	//get story info from quest path chosen, below assuming shopkeeper path chosen
        	String[] loc = this.getIntent().getExtras().getStringArray("LocValue");
        	saveCoordinatesInPreferences(Float.parseFloat(loc[0]),Float.parseFloat(loc[1]));
        	LatLng newLoc = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
        	//locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	//locMan.requestLocationUpdates(
        	//		LocationManager.GPS_PROVIDER,
        	//		MINIMUM_TIME_BETWEEN_UPDATE,
        	//		MINIMUM_DISTANCECHANGE_FOR_UPDATE,
        	//		new MyLocationListener());
        	
        	//For demo
        	current = map.addMarker(new MarkerOptions()
            	.position(newLoc)
            	.title("Current Location"));
        	map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 15));
        	
        	String choice = this.getIntent().getExtras().getString("Quest_choice");
        	if(choice.equals("Go to Taco Bell")) {
        		s = "You see a friend and ask them if they saw you yesterday. He tells you that he saw you briefly last night at Pi Kappa Theta with your roommate.";
        		count = this.getIntent().getExtras().getInt("StoryCount");
				count += 2;
        	} else if(choice.equals("Go to Brown")) {
        		s = "You go home and see your roommate on his bed. He tells you that he doesn’t remember much thinks you went to Waffle House.";
        		count = this.getIntent().getExtras().getInt("StoryCount");
        		count += 5;
        	} else if(choice.equals("Go to Pi Kappa Theta")) {
        		s = "You see a fraternity member at the front of the house. He waves to you and says that he enjoyed meeting you last night and wants to extend a bid to you.";
        		count = this.getIntent().getExtras().getInt("StoryCount");
        		count += 6;
        	}
        }
        storyline.setText(s);
        
        Button textQuestButton = (Button) findViewById(R.id.nextQuestButton);
        textQuestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Story Counts", ""+count);
				if(count == 0) {
					storyline.setText("You reach into your pocket and see a receipt for Taco Bell. What would you like to do?");
					count += 1;
				} else if(count == 1) {
					quest = true;
				} else if(count == 2) {
					quest = true;
				} else if(count == 5) {
					storyline.setText("He also hands you your wallet, which you had dropped last night.");
					count += 1;
				} else if(count == 6) {
					quest = true;
				}
				//quest = Boolean.parseBoolean("false");//Query again
				if(quest) {
					Intent questList = new Intent(MapTextActivity.this, QuestsActivity.class);
					if(count == 1) {
						questList.putExtra("QuestCount",0);
						questList.putExtra("StoryCount", count);
						quest = false;
					} else if(count == 2) {
						questList.putExtra("QuestCount",1);
						questList.putExtra("StoryCount", count);
						quest = false;
					} else if(count == 6) {
						questList.putExtra("QuestCount",2);
						questList.putExtra("StoryCount", count);
						quest = false;
					}
					startActivity(questList);
					finish();
				}
				else {
					//query next part of story
					//storyline.setText(s);
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