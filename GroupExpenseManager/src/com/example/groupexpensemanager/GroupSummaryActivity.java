package com.example.groupexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class GroupSummaryActivity extends Activity {

	public String grpName = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);
        
        setContentView(R.layout.activity_group_summary);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        TextView header = (TextView) findViewById(R.id.textView1);
        header.setText(grpName);
        
        MemberListWithBalance(grpName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group_summary, menu);
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
    
    public int GroupNameToDatabaseId(String GroupName){
    	int databaseId=0;
    	SQLiteDatabase commonDb=null;
    	try{
    		commonDb = this.openOrCreateDatabase(MainActivity.CommonDatabase, MODE_PRIVATE, null);
	        Cursor idquery = commonDb.rawQuery("SELECT ID FROM " + MainActivity.GroupTable +" WHERE Name = '"+GroupName+"';", null);
	        idquery.moveToFirst();
        	databaseId = idquery.getInt(0);;
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(commonDb!=null)
        		commonDb.close();
        }
    	return databaseId;
    }
    
    public void MemberListWithBalance(String GroupName){
    	int databaseId=GroupNameToDatabaseId(GroupName);
    	String gdName="Database_"+databaseId;
    	SQLiteDatabase groupDb=null;
    	int count=0;
        
        String[] name = new String[5];
        float[] balance = new float[5];
        
    	try{
	        groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
	        Cursor mquery = groupDb.rawQuery("SELECT Name,Balance FROM " + MainActivity.MemberTable+";",null);
	        //int count=0;
	        //count = mquery.getCount();
	        
	        mquery.moveToFirst();
		    do{
		    	name[count] = mquery.getString(0);
		    	balance[count] = mquery.getFloat(1);
		    	count++;
				}while(mquery.moveToNext());
	        
	        
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(groupDb!=null)
        		groupDb.close();
        }
		
		for(int j=0;j<count;j++){
			TextView n;
			TextView b;
			if (j==0) { n = (TextView) findViewById(R.id.nameText1); b = (TextView) findViewById(R.id.balanceText1); }
			else if (j==1) { n = (TextView) findViewById(R.id.nameText2); b = (TextView) findViewById(R.id.balanceText2); }
			else if (j==2) { n = (TextView) findViewById(R.id.nameText3); b = (TextView) findViewById(R.id.balanceText3); }
			else if (j==3) { n = (TextView) findViewById(R.id.nameText4); b = (TextView) findViewById(R.id.balanceText4); }
			else { n = (TextView) findViewById(R.id.nameText5); b = (TextView) findViewById(R.id.balanceText5); }
			n.setText(name[j]);
			b.setText(String.valueOf(balance[j]));			
		}
    	for (int j=count; j<5; j++) {
    		View nameV;
    		View balanceV;
			if (j==0) { nameV = findViewById(R.id.nameText1); balanceV = findViewById(R.id.balanceText1); }
			else if (j==1) { nameV = findViewById(R.id.nameText2); balanceV = findViewById(R.id.balanceText2); }
			else if (j==2) { nameV = findViewById(R.id.nameText3); balanceV = findViewById(R.id.balanceText3); }
			else if (j==3) { nameV = findViewById(R.id.nameText4); balanceV = findViewById(R.id.balanceText4); }
			else { nameV = findViewById(R.id.nameText5); balanceV = findViewById(R.id.balanceText5); }
			nameV.setVisibility(View.GONE);
			balanceV.setVisibility(View.GONE);
    	}
    }
    
    public void cashTransfer(View v) {
    	Intent intent = new Intent(this, CashTransferActivity.class);
    	intent.putExtra(GroupsActivity.GROUP_NAME, grpName);
    	startActivity(intent);
    }

}
