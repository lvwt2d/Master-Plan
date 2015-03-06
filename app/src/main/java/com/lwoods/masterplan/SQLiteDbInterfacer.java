package com.lwoods.masterplan;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;


public class SQLiteDbInterfacer {
//initializing variab
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "project_name";
	public static final String KEY_CHECKPOINT = "project_changes";
	public static final String KEY_DUEDATE = "project_due";
	public static final String KEY_GOALS = "project_goals";
	public static final String KEY_DEPENDS = "group_depends";
	public static final String KEY_DATEIN = "date_updated";
	
	
	private static final String DATABASE_NAME = "Projectsdb";
	private static final String DATABASE_TABLE = "projectTable";
	private static final String TIME_TABLE = "timeTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper myHelper;
	private final Context myContext;
	private SQLiteDatabase myDatabase;
	public int spinSel;
	
	//YOU DONT WANT TO BUILD A DATABASE ON AN USER INTERFACE THREAD
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT NOT NULL, " +
					KEY_CHECKPOINT + " TEXT NOT NULL, " +
					KEY_DUEDATE + " TEXT NOT NULL, " +
					KEY_GOALS + " TEXT NOT NULL, " +
					KEY_DEPENDS + " TEXT NOT NULL, " +
					KEY_DATEIN + " TEXT NOT NULL );"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public SQLiteDbInterfacer(Context c){
		myContext = c;
	}
	
	public SQLiteDbInterfacer(Context c, int select){
		myContext = c;
		spinSel = select;
	}
	
	public SQLiteDbInterfacer open() throws SQLException{
		myHelper = new DbHelper(myContext);
		myDatabase = myHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		myHelper.close();
	}

	public long createEntry(String name, String checkpoint, String due, 
			String goals, String depends, String date) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		//table must be pre-set up with these column names
		cv.put(KEY_NAME, name);
		cv.put(KEY_CHECKPOINT, checkpoint);
		cv.put(KEY_DUEDATE, due);
		cv.put(KEY_GOALS, goals);
		cv.put(KEY_DEPENDS, depends);
		cv.put(KEY_DATEIN, date);
		return myDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String getData(String name) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_NAME);
		int iCheck = c.getColumnIndex(KEY_CHECKPOINT);
		/*Dialog d = new Dialog(myContext);
		d.setTitle("Mike Check!");
		TextView tv = new TextView(myContext);
		tv.setText("The name passed is " + name);
		d.setContentView(tv);
		d.show();*/
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(name.equals((String)c.getString(1)) && !c.getString(2).isEmpty() && c.getString(2) != "" && c.getString(2) != "\n"){
			//result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iCheck) + "\n";
				result = result + c.getString(iCheck) + "\n";
			}else{
				
			}
		}
		c.close();
		return result;
	}
	
	public String getGoals(String name){
		
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(name.equals((String)c.getString(1)) && c.getString(4) != "" && !c.getString(4).isEmpty()){
			//result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iCheck) + "\n";
				result = c.getString(4);
			}
		}
		c.close();
		return result;
	}
	
	public String[] getProjectNames() {
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		ArrayList<String> projNames = new ArrayList<String>();
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		if(c != null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(!projNames.contains(c.getString(1)))	
				projNames.add(c.getString(1));
			}
		}
		String[] reString = new String[projNames.size()];
		try {
			for(int i = 0;i < projNames.size();i++){
				reString[i] = projNames.get(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Dialog d = new Dialog(myContext);
			d.setTitle("ProjUI issue!");
			TextView tv = new TextView(myContext);
			tv.setText(e.toString());
			d.setContentView(tv);
			d.show();
		}
		c.close();
		return reString;
	}
	
	public String getDue(String name){
		
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(name.equals((String)c.getString(1)) && !c.getString(3).isEmpty() && c.getString(3) != " "){
			//result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iCheck) + "\n";
				result = c.getString(3);
			}
		}
		c.close();
		return result;
	}
	
	public String getCheckDate(String name){
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(name.equals((String)c.getString(1)) && !c.getString(6).isEmpty() && !c.getString(2).isEmpty()){
			//result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iCheck) + "\n";
				String[] daydate = c.getString(6).split(" ");
				result += daydate[0] + " " + daydate[1] + " " + daydate[2] + "\n";
			}
		}
		c.close();
		return result;
	}

	public void updateGoals() {
		// SETTING GOALS TO TRUE OR FALSE IN DATABASE SO THEY CAN BE DISPLAYED
		
	}

	public String getGroup(String name) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_CHECKPOINT, KEY_DUEDATE, KEY_GOALS, KEY_DEPENDS, KEY_DATEIN};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if(name.equals((String)c.getString(1)) && !c.getString(5).isEmpty()){
			
				result = c.getString(5);
			}
		}
		c.close();
		return result;
	}
}
