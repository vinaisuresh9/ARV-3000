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
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTextActivity extends Activity {
	private GoogleMap map;
	private int SCREEN_HEIGHT;
	private boolean quest = false;
	
	private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
	private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
	
	private int storyId;
	private float distance = 15;
	
	private Location currentLoc= null;
	private int questArrIndex = 0;
    private JSONArray arr = null;
    private boolean demo;
    private boolean done = true;
    private ArrayList<JSONObject> locs = new ArrayList<JSONObject>();
	
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
					currentLoc = new Location("My Loc");
					currentLoc.setLatitude(arg0.getLatitude());
					currentLoc.setLongitude(arg0.getLongitude());
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()), 15));
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
				storyline.setText(s);
				int questId = obj.getInt("id");
				if(this.getIntent().hasExtra("drawMarker")) {
					String locName="ERROR";
					double lat=0, lon=0;
					try {
						lat = obj.getJSONObject("location").getDouble("lat");
						lon = obj.getJSONObject("location").getDouble("lon");
						locName = obj.getJSONObject("location").getString("name");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					map.addMarker(new MarkerOptions()
							.position(new LatLng(lat, lon))
							.title(locName));
				}
				request = new RequestTask().execute(MainActivity.hostApi+"get_available_quests/"+storyId+"/"+questId);
				questJson = new JSONObject(request.get().toString());
				arr = questJson.getJSONArray("quests");
				if(arr.length() == 0) {
					Intent end = new Intent(MapTextActivity.this, EndActivity.class);
					end.putExtra("storyId", storyId);
					startActivity(end);
					finish();
				}
				questArrIndex = 0;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
        } else {
        	try {
        		request = new RequestTask().execute(MainActivity.hostApi+"get_available_quests/"+storyId+"/0");
    			questJson = new JSONObject(request.get().toString());
    			arr = questJson.getJSONArray("quests");
    			if(arr.length() == 0) {
					Intent end = new Intent(MapTextActivity.this, EndActivity.class);
					end.putExtra("storyId", storyId);
					startActivity(end);
					finish();
				}
    			obj = arr.getJSONObject(questArrIndex);
    			s = obj.getString("desc");
    			questArrIndex += 1;
    		} catch (JSONException e) {
    			Log.e("JSONException", "Could not retrieve from database");
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		} catch (ExecutionException e) {
    			e.printStackTrace();
    		}
        }
        storyline.setText(s);
        
        Button textQuestButton = (Button) findViewById(R.id.nextQuestButton);
        textQuestButton.setEnabled(true);
        textQuestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(arr.length() == 0) {
					Intent end = new Intent(MapTextActivity.this, EndActivity.class);
					startActivity(end);
					finish();
				}
				JSONObject obj = null;
				try {
					obj = arr.getJSONObject(questArrIndex);
					if(obj.getString("result").equals("null")) {
						quest = false; 
					} else {
						quest = true;
					}
				} catch (JSONException e1) {
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
							e.printStackTrace();
						}
					}
					questList.putStringArrayListExtra("quests", quests);
					startActivity(questList);
					finish();
				}  else if(quest && !demo) {
					Log.d("done?", ""+done);
					if(done) {
						done = false;
						while(questArrIndex < arr.length()) {
							try {
								if(!arr.getJSONObject(questArrIndex).getString("result").equals("null")) {
									Log.d("loc:",arr.getJSONObject(questArrIndex).getJSONObject("location").toString());
									//locs.add(arr.getJSONObject(questArrIndex).getJSONObject("location"));
									locs.add(arr.getJSONObject(questArrIndex));
									questArrIndex++;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
//						try {
//							locs.add(new JSONObject("{\"id\":3,\"radius\":5,\"lon\":-84.401513,\"desc\":null,\"lat\":33.779813,\"name\":\"Center Street\",\"result\":\"done! Congrats!\"}"));
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						}
						for(int i=0; i<locs.size(); i++) {
							String locName="ERROR";
							double lat = 0, lon = 0;
							try {
								lat = locs.get(i).getJSONObject("location").getDouble("lat");
								lon = locs.get(i).getJSONObject("location").getDouble("lon");
								locName = locs.get(i).getJSONObject("location").getString("name");
							} catch (JSONException e) {
								e.printStackTrace();
								done = true;
							}
							LatLng point = new LatLng(lat, lon);
							map.addMarker(new MarkerOptions()
					        .position(point)
					        .title(locName));
						}
						storyline.setText("Choose a location and head over, when you get within 30 "
								+ "feet of the location hit next.");
					} else {
						for(int i=0; i<locs.size(); i++) {
							try {
								saveCoordinatesInPreferences((float)locs.get(i).getJSONObject("location").getDouble("lat"),(float)locs.get(i).getJSONObject("location").getDouble("lon"));
								done = checkDistance(locs.get(i).getJSONObject("location").getString("name"),locs.get(i).getJSONObject("location").getDouble("lat"),locs.get(i).getJSONObject("location").getDouble("lon"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
				        	if(done == true) {
				        		Intent goOnQuest = new Intent(MapTextActivity.this, MapTextActivity.class);
				                goOnQuest.putExtra("Quest_choice", locs.get(i).toString());
				                startActivity(goOnQuest);
				                finish();
				        	}
						}
						if(done == false)
							storyline.setText("You are not at any of the locations");
						Log.d("done?", ""+done);
					}
				} else if(!quest) {
					String s = "";
					try {
						obj = arr.getJSONObject(questArrIndex);
						questArrIndex++;
						s = obj.getString("desc");
						storyline.setText(s);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(obj == null) {
						Intent end = new Intent(MapTextActivity.this, EndActivity.class);
						startActivity(end);
						finish();
					}
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
	
	public boolean checkDistance(String name, double lat, double lon) {
		Location dest = new Location(name);
		dest.setLatitude(lat);
		dest.setLongitude(lon);
		double dist = currentLoc.distanceTo(dest);
		if(dist <= 85) {
			return true;
		}
		return false;
	}
}