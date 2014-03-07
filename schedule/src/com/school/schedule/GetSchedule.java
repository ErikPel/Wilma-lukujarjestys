package com.school.schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.gistlabs.mechanize.*;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.form.Form;

import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class GetSchedule {

	public Hashtable GetSchedule (Hashtable info) {
		String date = (String) info.get("date");
		String username = (String) info.get("username");
		String password = (String) info.get("password");
		String wilma_url = (String) info.get("wilma_url");
		Hashtable events = new Hashtable();
		MechanizeAgent agent = new MechanizeAgent();
        Document page = agent.get(wilma_url);
        Form form = page.form("login-form");
        form.get("Login").set(username);
        form.get("Password").set(password);
        form.submit();	
        page = agent.get(wilma_url + "/schedule?date="+date);
        org.jsoup.nodes.Document parsed = Jsoup.parse(page.asString());
        Elements schedule = parsed.select("table.schedule");
        for(Element tr : schedule.select("tr"))
        {
        	Log.i("asd",tr.select("td.event").toString());
        	for(Element td : tr.select("td.event"))
        	{
        		//System.out.println(td.toString());
        		if(!td.text().contains("Harrasteopinnot")){
        		Element link = td.select("a[href]").first();
        		String linkHref = link.attr("href");
        		String eventDate = linkHref.split("=")[1];
        		String timeSpan = td.select("small").first().text();
        		String className = td.text().replace(timeSpan, "");
    			Hashtable eventInfo = new Hashtable();
    			eventInfo.put("timeSpan", timeSpan);
    			//substring(0) removes space from className so " className" becomes
    			// "clasName"
    			eventInfo.put("className", className.substring(1));
        		if(events.get(eventDate) == null)
        		{

        			List<Hashtable> eventsToday = new ArrayList<Hashtable>();
        			eventsToday.add(eventInfo);
        			events.put(eventDate, eventsToday);
        		}
        		else
        		{
        			List<Hashtable> eventsToday = (List<Hashtable>) events.get(eventDate);
        			eventsToday.add(eventInfo);
        			events.put(eventDate, eventsToday);
        		}
        		}
        	}
        	
        }
        if(events.isEmpty())
    	{
    		Log.e("p‰‰stiin t‰nne", "asd");
    		for(int i = 0;i<5;i++)
    		{
    			try {
					Date parsedDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
					Calendar c = Calendar.getInstance();
					c.setFirstDayOfWeek(Calendar.MONDAY);
					c.setTime(parsedDate);
					c.add(Calendar.DATE, i);
	    			Hashtable eventInfo = new Hashtable();
	    			//Log.e("p‰‰stiin t‰nne", "joo elikk‰s");
	    			eventInfo.put("className", "Ei tunteja t‰n‰‰n");
	    			eventInfo.put("timeSpan","");
					List<Hashtable> eventsToday = new ArrayList<Hashtable>();
					eventsToday.add(eventInfo);
					events.put(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()), eventsToday);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
        
        return events;
    }
}
