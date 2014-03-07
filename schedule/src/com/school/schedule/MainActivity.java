package com.school.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

public class MainActivity extends Activity  {
	RegisterAdapter register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		register = new RegisterAdapter(this);
		register = register.open();
		if(register.userInfoExists())
		{
			
		
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		int today = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DAY_OF_WEEK, -today+Calendar.MONDAY);
		String date = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());
		Log.i("asd",date);
		setTitle(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Hashtable info = register.getUserInfo();
		info.put("date", date);
		new scheduleDownloader().execute(info);
		}
		else
		{
			
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	class scheduleDownloader extends AsyncTask<Hashtable,Void,Hashtable>
	{

		public Hashtable getSchedule(Hashtable info)
		{
		GetSchedule schedule = new GetSchedule();
		Hashtable classes = schedule.GetSchedule(info);

		Log.i("asd", classes.toString());
		//ArrayList classesToday = (ArrayList) classes.get(date);
		
		return classes;
		
		}	


		@Override
		protected Hashtable doInBackground(Hashtable... info) {
			Hashtable classesToday = getSchedule(info[0]);
			return classesToday;
		}
		protected void onPostExecute(Hashtable classes)
	    {
			TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
			tabHost.setup();
			
			Map<String,String> map = classes;
			List<String> sortedKeys = new ArrayList<String>(map.size());
			sortedKeys.addAll(map.keySet());
			Collections.sort(sortedKeys);
			for (String key :  sortedKeys)
			{
	            final ArrayList classesToday = (ArrayList) classes.get(key);

				TabHost.TabSpec Day = tabHost.newTabSpec(key).setIndicator(key)
					    .setContent(new TabHost.TabContentFactory(){
					        public View createTabContent(String tag) {
					            Context context = MainActivity.this;
					            ListView list = new ListView(MainActivity.this); 
					            //populate m_data from the database
					            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,classesToday,android.R.layout.simple_list_item_2, 
					                    new String[] {"className", "timeSpan" }, 
					                    new int[] {android.R.id.text1, android.R.id.text2 });
								list.setAdapter(adapter);
								return list;   
					            }});
				tabHost.addTab(Day);

				

			}
	    }
	}
	}
