package com.example.groupexpensemanager;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(11)
public class GroupsActivity extends Activity {

	 //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
	public ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;
    private ListView gl;
    public final static String GROUP_NAME = "";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent=getIntent();
        //s = intent.getStringExtra(NewGroupActivity.TEXT_SERVICES_MANAGER_SERVICE);
        
        setContentView(R.layout.activity_groups);
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        gl = (ListView) this.findViewById(R.id.GroupsList);
        groupList();
        
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
        gl.setAdapter(adapter);
        gl.setClickable(true);
        gl.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
        	@SuppressWarnings("rawtypes")
    		public void onItemClick(AdapterView parentView, View childView, int position, long id) {
        		sendName(position);
        	}
            @SuppressWarnings({ "rawtypes", "unused" })
    		public void onNothingClick(AdapterView parentView) {
            }
        });
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_groups, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void newGroup(View v) {
    	Intent intent = new Intent(this, NewGroupActivity.class);
    	startActivity(intent);
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(ListView v, String s) {
     listItems.add(s);
     //adapter.notifyDataSetChanged();
    }
    
    public void sendName(int pos) {
    	Intent intent = new Intent(this, GroupSummaryActivity.class);
    	intent.putExtra(GROUP_NAME, listItems.get(pos));
        startActivity(intent);
    }
    
    public void groupList(){
    	SQLiteDatabase groupDb=null;
    	try{
	        groupDb = this.openOrCreateDatabase(MainActivity.CommonDatabase, MODE_PRIVATE, null);
	        Cursor gquery = groupDb.rawQuery("SELECT Name FROM " + MainActivity.GroupTable+";",null);
	        if(gquery.moveToFirst()){
	    		do{
	        		addItems(gl, gquery.getString(0));
	        	} while(gquery.moveToNext());
	    	}
    	}catch(Exception e) {
    		e.getMessage();
        }
        finally{ 
        	if(groupDb!=null)
        		groupDb.close();
        }
    }
    
}
