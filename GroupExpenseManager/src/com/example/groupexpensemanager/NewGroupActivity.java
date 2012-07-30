package com.example.groupexpensemanager;

import android.annotation.TargetApi;
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
import android.widget.EditText;

@TargetApi(11)
public class NewGroupActivity extends Activity {

	public int numberMembers = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_group, menu);
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
    
    public void addMember(View v) {
    	View b = null;
    	View sign = null;
    	switch (numberMembers) {
    	case 1:
    		b = findViewById(R.id.tableRowNew1);
    		break;
    	case 2:
    		b = findViewById(R.id.tableRowNew2);
    		sign = findViewById(R.id.minusButton1);
    		break;
    	case 3:
    		b = findViewById(R.id.tableRowNew3);
    		sign = findViewById(R.id.minusButton2);
    		break;
    	case 4:
    		b = findViewById(R.id.tableRowNew4);
    		sign = findViewById(R.id.minusButton3);
    		break;
    	default:
    		break;
    	}
    	
    	b.setVisibility(View.VISIBLE);
    	if (sign != null) {
    		sign.setVisibility(View.GONE);
    	}
    	numberMembers++;
    	if (numberMembers == 5) {
    		View add = findViewById(R.id.addButton);
    		add.setVisibility(View.GONE);
    	}
    }
    
    public void removeMember(View v) {
    	int s = Integer.valueOf((String) v.getTag());
    	View b = null;
    	View sign = null;
    	switch (s) {
    	case 1:
    		b= findViewById(R.id.tableRowNew1);
    		break;
    	case 2:
    		b = findViewById(R.id.tableRowNew2);
    		sign = findViewById(R.id.minusButton1);
    		break;
    	case 3:
    		b = findViewById(R.id.tableRowNew3);
    		sign = findViewById(R.id.minusButton2);
			break;
    	case 4:
    		b = findViewById(R.id.tableRowNew4);
    		sign = findViewById(R.id.minusButton3);
    		break;
    	default:
    		break;
    	}
    	
    	b.setVisibility(View.GONE);
    	if (sign != null) {
    		sign.setVisibility(View.VISIBLE);
    	}
    	if (numberMembers == 5) {
    		View add = findViewById(R.id.addButton);
    		add.setVisibility(View.VISIBLE);
    	}
    	numberMembers--;
    }
    
    public void done(View v) {
    	Intent intent = new Intent(this, GroupsActivity.class);

    	EditText editText = (EditText) findViewById(R.id.grpText);
    	String message = editText.getText().toString();
    	//intent.putExtra(MainActivity.GroupName, message);
    	
    	String[] members = new String[numberMembers];
    	int k;
    	for (k=0; k<numberMembers; k++) {
    		EditText editTextMember;
    		if (k==0) editTextMember = (EditText) findViewById(R.id.EditText00);
    		else if (k==1) editTextMember = (EditText) findViewById(R.id.EditText01);
    		else if (k==2) editTextMember = (EditText) findViewById(R.id.EditText02);
    		else if (k==3) editTextMember = (EditText) findViewById(R.id.EditText03);
    		else editTextMember = (EditText) findViewById(R.id.EditText04);
    		String temp = editTextMember.getText().toString();
    		if (temp == null) {
    			//TODO ErrorPopUp
    		}
    		else {
    			members[k] = editTextMember.getText().toString();
    		}
    	}
    	//intent.putExtra(MainActivity.MEMBERS, members);
    	
    	insertToDatabase(message, members);
    	
    	startActivity(intent);
    }
    
    public void insertToDatabase(String groupName, String[] members) {
    	SQLiteDatabase myDB=null;
        String TableName=MainActivity.GroupTable;
        String CommonDatabase=MainActivity.CommonDatabase;
        int ID=1;

        try {
	        myDB = this.openOrCreateDatabase(CommonDatabase, MODE_PRIVATE, null);
	        Cursor count = myDB.rawQuery("SELECT count(*) FROM " + TableName , null);
	        boolean flag=true;
	        if(count.moveToFirst()){
	        	ID=count.getInt(0)+1;
	        	Cursor isPresent=myDB.rawQuery("SELECT ID FROM " + TableName + " WHERE Name = '"+groupName+"';", null);
	        	if(isPresent.getCount()>0){
	        		flag=false;
	        	}
	        }
	        if(flag){
	        	myDB.execSQL("INSERT INTO " + TableName + " ( ID, Name ) VALUES ( '" + ID+"', '"+groupName + "' );" );
		        
		        String DatabaseName="Database_"+ID;
		        createTables(DatabaseName,members);	        
		    }
	        else{
	        	//TODO error popup
	        }
        }
        catch(Exception e) {
        	Log.e("Error", "Error", e);
        }
        finally{ 
        	if(myDB!=null)
        		myDB.close();
        }
    }
    
    public void createTables(String databaseName,String[] members){
    	SQLiteDatabase groupDatabase=null;
    	try{
    		groupDatabase=this.openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
        	           + MainActivity.MemberTable
        	           + " ( ID int(11) NOT NULL, Name varchar(255) NOT NULL, Balance float NOT NULL );");
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
     	           + MainActivity.EventTable
     	           + " ( ID int(11) NOT NULL, Name varchar(255) NOT NULL );");
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
      	           + MainActivity.TransTable
      	           + " ( ID int(11) NOT NULL, MemberId int(11) NOT NULL, Amount float NOT NULL, EventId int(11) NOT NULL );");
        	groupDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
       	           + MainActivity.CashTable
       	           + " ( ID int(11) NOT NULL, FromMemberId int(11) NOT NULL, ToMemberId int(11) NOT NULL, Amount float NOT NULL);");
        	int length=members.length;
        	for(int j=0;j<length;j++){
        		groupDatabase.execSQL("INSERT INTO "+MainActivity.MemberTable + " ( ID, Name, Balance ) VALUES ( '" + (j+1)+"', '"+members[j] + "', '"+0+"' );" );
        	}
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(groupDatabase!=null)
        		groupDatabase.close();
        }
    }

}
