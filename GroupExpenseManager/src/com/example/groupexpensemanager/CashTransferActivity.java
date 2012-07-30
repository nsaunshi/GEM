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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

@TargetApi(11)
public class CashTransferActivity extends Activity {
	public String grpName = "";
	Spinner spinner1 = null;
	Spinner spinner2 = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_transfer);
    	
    	Intent  intent = getIntent();
    	grpName = intent.getStringExtra(GroupsActivity.GROUP_NAME);

    	MemberList(grpName);
    	
    	/*String grpDatabase = groupNameToDatabase(grpName);
    	SQLiteDatabase db=null;
    	db = this.openOrCreateDatabase(grpDatabase, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT count(*) FROM " + TableName , null);*/
    	/*String[] from = new String[1];
    	from[0] = "Name";
    	int[] to = new int[1];
    	to[0] = R.id.spinner1;
    	Cursor c = MemberList(grpName);
    	SimpleCursorAdapter ca = new SimpleCursorAdapter(this, R.layout.activity_cash_transfer, c, from, null);
    	
    	spinner1 = (Spinner) findViewById(R.id.spinner1);
    	spinner2 = (Spinner) findViewById(R.id.spinner2);
    	
    	spinner1.setAdapter(ca);
    	spinner2.setAdapter(ca);*/
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cash_transfer, menu);
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
    
    public void MemberList(String GroupName){
    	int databaseId=GroupNameToDatabaseId(GroupName);
    	String gdName="Database_"+databaseId;
    	SQLiteDatabase groupDb=null;
    	int count=0;
        
        String[] name = new String[5];
        
    	try{
	        groupDb = this.openOrCreateDatabase(gdName, MODE_PRIVATE, null);
	        Cursor mquery = groupDb.rawQuery("SELECT Name FROM " + MainActivity.MemberTable+";",null);
	        //int count=0;
	        //count = mquery.getCount();
	        
	        mquery.moveToFirst();
		    do{
		    	name[count] = mquery.getString(0);
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
			TextView n1;
			if (j==0) { n = (TextView) findViewById(R.id.radio00); n1 = (TextView) findViewById(R.id.radio0); }
			else if (j==1) { n = (TextView) findViewById(R.id.radio01); n1 = (TextView) findViewById(R.id.radio1); }
			else if (j==2) { n = (TextView) findViewById(R.id.radio02); n1 = (TextView) findViewById(R.id.radio2); }
			else if (j==3) { n = (TextView) findViewById(R.id.radio03); n1 = (TextView) findViewById(R.id.radio3); }
			else { n = (TextView) findViewById(R.id.radio04);  n1 = (TextView) findViewById(R.id.radio4); }
			n.setText(name[j]);
			n1.setText(name[j]);
		}
    	for (int j=count; j<5; j++) {
    		View nameV;
    		View balanceV;
			if (j==0) { nameV = findViewById(R.id.radio00); balanceV = findViewById(R.id.radio0); }
			else if (j==1) { nameV = findViewById(R.id.radio01); balanceV = findViewById(R.id.radio1); }
			else if (j==2) { nameV = findViewById(R.id.radio02); balanceV = findViewById(R.id.radio2); }
			else if (j==3) { nameV = findViewById(R.id.radio03); balanceV = findViewById(R.id.radio3); }
			else { nameV = findViewById(R.id.radio04); balanceV = findViewById(R.id.radio4); }
			nameV.setVisibility(View.GONE);
			balanceV.setVisibility(View.GONE);
    	}
    }
    
    public int MemberNameToId(int GroupID,String member){
    	int memberId=0;
    	String database="Database_"+GroupID;
    	SQLiteDatabase commonDb=null;
    	try{
    		commonDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
	        Cursor idquery = commonDb.rawQuery("SELECT ID FROM " + MainActivity.MemberTable +" WHERE Name = '"+member+"';", null);
	        idquery.moveToFirst();
        	memberId=idquery.getInt(0);
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(commonDb!=null)
        		commonDb.close();
        }
    	return memberId;
    }
    
    public void CashTransfer(String groupName, String fromM, String toM, float amount){
    	int GroupId=GroupNameToDatabaseId(groupName);
    	int fromMember=MemberNameToId(GroupId,fromM);
    	int toMember=MemberNameToId(GroupId,toM);
    	SQLiteDatabase groupDb=null;
    	String database="Database_"+GroupId;
    	int ID=1;
    	try{
	        groupDb = this.openOrCreateDatabase(database, MODE_PRIVATE, null);
	        Cursor count = groupDb.rawQuery("SELECT count(*) FROM " + MainActivity.CashTable , null);
	        if(count.getCount()>0){
	        	count.moveToFirst();
	        	ID=count.getInt(0)+1;
	        }
	        groupDb.execSQL("INSERT INTO " + MainActivity.CashTable + " ( ID, FromMemberId, ToMemberId, Amount ) VALUES ( '" + ID+"', '"+fromMember+ "', '"+toMember+"', '"+amount+"' );" );
	        groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance+'"+amount+"' WHERE ID = '"+fromMember+"';");
	        groupDb.execSQL("UPDATE "+MainActivity.MemberTable+" SET Balance = Balance-'"+amount+"' WHERE ID = '"+toMember+"';");
    	}catch(Exception e) {
    		Log.e("Error", "Error", e);
        }
        finally{ 
        	if(groupDb!=null)
        		groupDb.close();
        }
    }
    
    public void trasferDone() {
    	RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1);
		int selected = g.getCheckedRadioButtonId();
		EditText b = (EditText) findViewById(selected);
		String fM = b.getText().toString();
		
		RadioGroup g0 = (RadioGroup) findViewById(R.id.radioGroup01);
		int selected0 = g0.getCheckedRadioButtonId();
		EditText b0 = (EditText) findViewById(selected0);
		String tM = b0.getText().toString();
		
		TextView tv =new TextView(this);
		tv.setText(fM);
		setContentView(tv);
		
		EditText editText;
		editText = (EditText) findViewById(R.id.amountText);
		float a = Float.valueOf(editText.getText().toString());
		
    	//CashTransfer(grpName, fM, tM, a);
    	
    	//Intent intent = new Intent(this, GroupSummaryActivity.class);
    	//startActivity(intent);
    }
    
}
