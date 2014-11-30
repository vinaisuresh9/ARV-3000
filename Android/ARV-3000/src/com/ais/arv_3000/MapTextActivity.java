package com.ais.arv_3000;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
	private int storyId;
	private float distance = 15;
	
	private Marker current;
	private double lat=0, lon=0;
	private LatLngBounds.Builder builder = new LatLngBounds.Builder();
	private int questArrIndex = 0;
    private JSONArray arr = null;
    private boolean demo;
    private boolean done = true;
	
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
        
        map.clear();
        
        if(map != null) {
        	map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location arg0) {
					LatLng point = new LatLng(arg0.getLatitude(), arg0.getLongitude());
					current = map.addMarker(new MarkerOptions()
			        .position(point)
			        .title("You are here!"));
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
					map.setOnCameraChangeListener(new OnCameraChangeListener() {
						
						@Override
						public void onCameraChange(CameraPosition arg0) {
							map.moveCamera(CameraUpdateFactory.newLatLngZoom(arg0.target, arg0.zoom));
						}
					});
				}
			});
        }
        
        if(this.getIntent().hasExtra("storyId")) {
        	storyId = this.getIntent().getIntExtra("storyId", 1);
        }
        
        if(this.getIntent().hasExtra("demo")) {
        	demo = this.getIntent().getBooleanExtra("demo", false);
        }
        
        final TextView storyline = (TextView) findViewById(R.id.storyTextView);
        AsyncTask<String, String, String> request;
        JSONObject questJson;
        String s="";
        JSONObject obj = null;
        if(this.getIntent().hasExtra("Quest_choice")) {
        	//get story info from quest path chosen, below assuming shopkeeper path chosen
        	try {
				obj = new JSONObject(this.getIntent().getStringExtra("Quest_choice"));
				s = obj.getString("result");
				int questId = obj.getInt("id");
				String locName="ERROR";
				try {
					lat = obj.getJSONObject("location").getDouble("lat");
					lon = obj.getJSONObject("location").getDouble("lon");
					locName = obj.getJSONObject("location").getString("desc");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LatLng point = new LatLng(lat, lon);
				current = map.addMarker(new MarkerOptions()
		        .position(point)
		        .title(locName));
				request = new RequestTask().execute(MainActivity.hostApi+"get_available_quests/"+storyId+"/"+questId);
				questJson = new JSONObject(request.get().toString());
				arr = questJson.getJSONArray("quests");
				questArrIndex = 0;
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
//        	saveCoordinatesInPreferences(Float.parseFloat(loc[0]),Float.parseFloat(loc[1]));
//        	LatLng newLoc = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
//        	locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        	locMan.requestLocationUpdates(
//        			LocationManager.GPS_PROVIDER,
//        			MINIMUM_TIME_BETWEEN_UPDATE,
//        			MINIMUM_DISTANCECHANGE_FOR_UPDATE,
//        			new MyLocationListener());
        	
        } else {
        	try {
        		request = new RequestTask().execute(MainActivity.hostApi+"get_available_quests/0/"+storyId);
    			questJson = new JSONObject(request.get().toString());
    			arr = questJson.getJSONArray("quests");
    			obj = arr.getJSONObject(questArrIndex);
    			s = obj.getString("desc");
    			questArrIndex += 1;
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			Log.e("JSONException", "Could not retrieve from database");
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ExecutionException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        storyline.setText(s);
        
        Button textQuestButton = (Button) findViewById(R.id.nextQuestButton);
        textQuestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject obj = null;
				try {
					obj = arr.getJSONObject(questArrIndex);
					if(obj.getString("result").equals("null")) {
						quest = false; 
					} else {
						quest = true;
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.d("quest?", ""+quest);
				Log.d("demo?",""+demo);
				if(quest && demo) {
					Intent questList = new Intent(MapTextActivity.this, QuestsActivity.class);
					ArrayList<String> quests = new ArrayList<String>();
					while(questArrIndex < arr.length()) {
						try {
							if(!arr.getJSONObject(questArrIndex).getString("result").equals("null")) {
								quests.add(arr.getJSONObject(questArrIndex).toString());
								questArrIndex++;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					questList.putStringArrayListExtra("quests", quests);
					startActivity(questList);
					finish();
				}  else if(quest && !demo) {
					ArrayList<JSONObject> locs = new ArrayList<JSONObject>();
					v.setEnabled(false);
					done = false;
					while(questArrIndex < arr.length()) {
						try {
							if(!arr.getJSONObject(questArrIndex).getString("result").equals("null")) {
								locs.add(arr.getJSONObject(questArrIndex).getJSONObject("location"));
								questArrIndex++;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					int k = 0;
					for(int i=0; i<locs.size(); i++) {
						String locName="ERROR";
						try {
							JSONObject lo = locs.get(i);
							lat = locs.get(i).getDouble("lat");
							lon = locs.get(i).getDouble("lon");
							locName = locs.get(i).getString("desc");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							done = true;
						}
						LatLng point = new LatLng(lat, lon);
						storyline.setText("Choose a location and head over, when you get within 6 feet, you will meet someone/see something new.");
						Marker newMarker = map.addMarker(new MarkerOptions()
				        .position(point)
				        .title(locName));
						builder.include(newMarker.getPosition());
					}
					while(!done) {
						for(int i=0; i<locs.size(); i++) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									AsyncTask<Double,String,Boolean> task = new CheckDistance().execute(lat,lon);
									try {
										done = task.get();
										Log.d("done?", ""+done);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ExecutionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}
						Log.d("done?", ""+done);
					}
				} else if(!quest) {
					String s = "";
					try {
						obj = arr.getJSONObject(questArrIndex);
						questArrIndex++;
						s = obj.getString("desc");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					storyline.setText(s);
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
			distance = arg0.distanceTo(pointLocation);
			if(distance <= 6) {
				Toast.makeText(MapTextActivity.this,
					"Distance from Point:"+distance, Toast.LENGTH_LONG).show();
				done = true;
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onProviderDisabled(String provider) {}
	}
	
	private class CheckDistance extends AsyncTask<Double, String, Boolean> {
		@Override
		protected Boolean doInBackground(Double... params) {
			saveCoordinatesInPreferences((float)params[0].floatValue(),(float)params[1].floatValue());
			locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	locMan.requestLocationUpdates(
        			LocationManager.GPS_PROVIDER,
        			MINIMUM_TIME_BETWEEN_UPDATE,
        			MINIMUM_DISTANCECHANGE_FOR_UPDATE,
        			new MyLocationListener());
			return true;
		}
	 }
}