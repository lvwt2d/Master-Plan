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


public class ProjDbUI {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "persons_name";
	public static final String KEY_HOTNESS = "persons_hotness";
	
	
	private static final String DATABASE_NAME = "HotOrNotdb";
	private static final String DATABASE_TABLE = "peopleTable";
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
					KEY_HOTNESS + " TEXT NOT NULL);"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public ProjDbUI(Context c){
		myContext = c;
	}
	
	public ProjDbUI(Context c, int select){
		myContext = c;
		spinSel = select;
	}
	
	public ProjDbUI open() throws SQLException{
		myHelper = new DbHelper(myContext);
		myDatabase = myHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		myHelper.close();
	}

	public long createEntry(String name, String hotness) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		//table must be pre-set up with these column names
		cv.put(KEY_NAME, name);
		cv.put(KEY_HOTNESS, hotness);
		return myDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_NAME);
		int iHotness = c.getColumnIndex(KEY_HOTNESS);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iHotness) + "\n";
		}
		return result;
	}
	
	public String[] getProjects() {
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
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
	
	public String getUpdates(){
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
		String result = "";
		Cursor c = myDatabase.query(DATABASE_TABLE, columns, KEY_NAME + "=" + spinSel, null, null, null, null);
		if(c != null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				result = result + c.getString(2) + "\n";
			}
			
		}
		return result;
	}
}
