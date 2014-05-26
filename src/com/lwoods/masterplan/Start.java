package com.lwoods.masterplan;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Start extends ListActivity {

	String[] functions = {"Projects"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //FULL SCREEN
      		requestWindowFeature(Window.FEATURE_NO_TITLE);
      		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      		WindowManager.LayoutParams.FLAG_FULLSCREEN);
      		setListAdapter(new ArrayAdapter<String>(Start.this, android.R.layout.simple_list_item_1, functions));
    
      		try{
      			Class funClass = Class.forName("com.lwoods.masterplan.Projects");
      			Intent myIntent = new Intent(Start.this, funClass);
      			startActivity(myIntent);
      			}catch(ClassNotFoundException e){
      				e.printStackTrace();
      			}
      		
    }


    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String funChoise = functions[position];
		try{
		Class funClass = Class.forName("com.lwoods.masterplan." + funChoise);
		Intent myIntent = new Intent(Start.this, funClass);
		startActivity(myIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }
    
}
