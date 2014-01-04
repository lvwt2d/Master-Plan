package com.lwoods.masterplan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SQLresultView extends Activity implements OnClickListener {

	LinearLayout layCheck;
	TextView tvInfo, tvDue, tvCheckDate, tvGroup;
	EditText etUp;
	Spinner spin;
	String FILENAME = "InternalString", checkResult = "";
	TextView proj, goal;
	Button up;
	loadSomeStuff spinMem;
	Thread memThr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlview);
		layCheck = (LinearLayout) findViewById(R.id.layCheckDates);
		tvInfo = (TextView) findViewById(R.id.tvInfoList);
		proj = (TextView) findViewById(R.id.tvProj);
		//goal = (TextView) findViewById(R.id.tvGoals);
		tvDue = (TextView) findViewById(R.id.tvDueDate);
		tvCheckDate = (TextView) findViewById(R.id.tvDateList);
		tvGroup = (TextView) findViewById(R.id.tvGroupDis);
		etUp = (EditText) findViewById(R.id.etUpTwo);
		up = (Button) findViewById(R.id.bUpdateTwo);
		up.setOnClickListener(this);
		etUp.setHint("Add A New CheckPoint?");
		new loadSomeStuff().execute(FILENAME);
		/*ProjDbUI info = new ProjDbUI(this);
		info.open();
		String checkpoints = info.getData(checkResult);
		String dueDate = info.getDue(checkResult);
		
		info.close();
		if (dueDate.isEmpty()) {
			dueDate = "No Specified Due Date";
		}
		goals(checkResult);
		tvInfo.setText(checkpoints);
		Dialog d = new Dialog(this);
		d.setTitle("Darn.. We Have An Error");
		TextView tv = new TextView(this);
		tv.setText("//" + checkpoints + "/" + "/ /");
		d.setContentView(tv);
		d.show();
		tvDue.setText(dueDate);
		
		chDates(checkResult);
		
		
		
		
		group(checkResult);*/
		
		//new DbProcThr().execute("");
		/*I NEED TO FIGURE OUT HOW TO ACCESS THE DATABASE OUTSIDE OF THE UI THREAD AFTER THE 
		NAME/SPINNER THREAD IS FINISHED */
	}

	private void goals(String name) {
		String goals = "";
		ProjDbUI goalie = new ProjDbUI(this);
		goalie.open();
		goals = goalie.getGoals(name);
//		goal.setText(goals);
		goalie.close();
		LinearLayout layGs = (LinearLayout) findViewById(R.id.layGs);
		String[] goalSplit = goals.split("-");
		for(int i = 0;i < goalSplit.length;i++){
			String[] compSplit = goalSplit[i].split(",");
			CheckBox check = new CheckBox(this);
			TextView textie = new TextView(this);
			LinearLayout hLay = new LinearLayout(this); 
			hLay.setOrientation(LinearLayout.HORIZONTAL);
			layGs.addView(hLay);
			hLay.addView(check);
			if (compSplit.length == 2) {
				if (compSplit[1].contentEquals("done")) {
					check.setChecked(true);
				} else {
					check.setChecked(false);
				}
			}
			check.setEnabled(false);
			hLay.addView(textie);
			textie.setText(compSplit[0]);
		}

	}

	private void chDates(String name) {
		ProjDbUI dater = new ProjDbUI(this);
		String checks = "";
		String dates = "";
		dater.open();
		dates = dater.getCheckDate(name);
		checks = dater.getData(name);
		dater.close();
		tvCheckDate.setText(dates);
		dates = dates.substring(0, dates.length());
		checks = checks.substring(0, checks.length());
		String[] dateArr = dates.split("\n");
		String[] checkArr = checks.split("\n");
		
		for(int i = 0;i < dateArr.length && i < checkArr.length;i++){
			LinearLayout zontalLay = new LinearLayout(this);
			TextView dateView = new TextView(this),checkView = new TextView(this);
			LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LinearLayout.LayoutParams dateLayPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layPar.weight = 1.0f;
			dateLayPar.weight = 2.0f;
			dateView.setLayoutParams(dateLayPar);
			checkView.setLayoutParams(layPar);
			layCheck.addView(zontalLay);
			zontalLay.addView(dateView);
			zontalLay.addView(checkView);
			dateView.setText(dateArr[i]);
			checkView.setText(checkArr[i]);
		}
	}

	private void group(String name) {
		ProjDbUI grouper = new ProjDbUI(this);
		String group = "";
		grouper.open();
		group = grouper.getGroup(name);
		grouper.close();
		if (!group.isEmpty()) {
			if(!group.contentEquals("1")){
			group = "Your Group Has " + group + " Members.";
			tvGroup.setText(group);
			}else{
				tvGroup.setText("");
			}
		}
	}

	public class loadSomeStuff extends AsyncTask<String, Integer, String> {

		ProgressDialog dialog;

		protected void onPreExecute() {
			// example of setting up something

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// update preferences
			String collected = null;
			FileInputStream fis = null;

			try {
				// set up a file input stream to read from my internal file
				fis = openFileInput(FILENAME);
				// I use a byte array to tell me the amount of bytes/space
				// available in my file
				byte[] dataArray = new byte[fis.available()];
				// once the entire file is read..fis.read(dataArr) will return
				// -1
				while (fis.read(dataArray) != -1) {
					collected = new String(dataArray);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fis.close();
					checkResult = collected;
					//return collected;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ProjDbUI infoBackG = new ProjDbUI(SQLresultView.this);
			String checks = "";
			String dates = "";
			String neededIn = "";
			infoBackG.open();
			dates = infoBackG.getCheckDate(checkResult);
			checks = infoBackG.getData(checkResult);
			String dueDate = infoBackG.getDue(checkResult);
			
			String group = "";
			group = infoBackG.getGroup(checkResult);
			
			
			String goals = "";
			goals = infoBackG.getGoals(checkResult);
			
			
			neededIn = checks + "<->" + dates;
			//dates = dates.substring(0, dates.length() - 4);
			//checks = checks.substring(0, checks.length() - 3);
			
			
			
			
			group = infoBackG.getGroup(checkResult);
			
			neededIn += "<->" + group;
			goals = infoBackG.getGoals(checkResult);
			
			neededIn += "<->" + goals;
			
			
			neededIn += "<->" + checkResult + "<->" + dueDate;
			
			
			return neededIn;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			
			String[] dbInfo = result.split("<->");
			proj.setText(dbInfo[4]);
			
			
			//new DbProcThr().execute("");
			
			//layCheck = (LinearLayout) findViewById(R.id.layCheckDates);
			
			tvCheckDate.setText(dbInfo[1]);
			
			tvDue.setText(dbInfo[5]);
			
			
			LinearLayout layGs = (LinearLayout) findViewById(R.id.layGs);
			String[] dateArr = dbInfo[1].split("\n");
			String[] checkArr = dbInfo[0].split("\n");
			for(int i = 0;i < dateArr.length && i < checkArr.length;i++){
				LinearLayout zontalLay = new LinearLayout(SQLresultView.this);
				TextView dateView = new TextView(SQLresultView.this),checkView = new TextView(SQLresultView.this);
				LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				LinearLayout.LayoutParams dateLayPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layPar.weight = 1.0f;
				dateLayPar.weight = 2.0f;
				dateView.setLayoutParams(dateLayPar);
				checkView.setLayoutParams(layPar);
				layCheck.addView(zontalLay);
				zontalLay.addView(dateView);
				zontalLay.addView(checkView);
				dateView.setText(dateArr[i]);
				checkView.setText(checkArr[i]);
			}
			
			String[] goalSplit = dbInfo[3].split("-");
			for(int i = 0;i < goalSplit.length;i++){
				String[] compSplit = goalSplit[i].split(",");
				CheckBox check = new CheckBox(SQLresultView.this);
				TextView textie = new TextView(SQLresultView.this);
				LinearLayout hLay = new LinearLayout(SQLresultView.this); 
				hLay.setOrientation(LinearLayout.HORIZONTAL);
				layGs.addView(hLay);
				hLay.addView(check);
				if (compSplit.length == 2) {
					if (compSplit[1].contentEquals("done")) {
						check.setChecked(true);
					} else {
						check.setChecked(false);
					}
				}
				check.setEnabled(false);
				hLay.addView(textie);
				textie.setText(compSplit[0]);
			}
			
			if (!dbInfo[2].isEmpty()) {
				if(!dbInfo[2].contentEquals("1")){
					dbInfo[2] = "Your Group Has " + dbInfo[2] + " Members.";
				tvGroup.setText(dbInfo[2]);
				}else{
					tvGroup.setText("");
				}
			}
		}
	}

	public class DbProcThr extends AsyncTask<String, Integer, String> {

		//ProgressDialog dialog;

		protected void onPreExecute() {
			// example of setting up something

		}

		@Override
		protected String doInBackground(String... params) {
			
			/*Dialog d = new Dialog(SQLresultView.this);
			d.setTitle("Executed");
			TextView tv = new TextView(SQLresultView.this);
			tv.setText("//" + "blah" + "/" + "/ /");
			d.setContentView(tv);
			d.show();*/
			
			/*Thread thread = Thread.currentThread();
			if(spinMem.getStatus() == AsyncTask.Status.PENDING){
				while(spinMem.getStatus() != AsyncTask.Status.FINISHED){
					try {
						synchronized(thread){
						thread.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(spinMem.getStatus() == AsyncTask.Status.RUNNING){
				while(spinMem.getStatus() != AsyncTask.Status.FINISHED){
					try {
						synchronized(thread){
						thread.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(spinMem.getStatus() == AsyncTask.Status.FINISHED){
				new DbProcThr().execute("");
			}*/
			
			ProjDbUI infoBackG = new ProjDbUI(SQLresultView.this);
			
			
			String neededIn = "";
			String checks = "";
			String dates = "";
			infoBackG.open();
			dates = infoBackG.getCheckDate(checkResult);
			checks = infoBackG.getData(checkResult);
			//dater.close();
			neededIn = checks + "<->" + dates;
			//dates = dates.substring(0, dates.length() - 4);
			//checks = checks.substring(0, checks.length() - 3);
			
			
			
			
			String group = "";
			group = infoBackG.getGroup(checkResult);
			
			neededIn += "<->" + group;
			String goals = "";
			goals = infoBackG.getGoals(checkResult);
			
			neededIn += "<->" + goals;
			
			
			
			
			
			return neededIn;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			
			
			
			layCheck = (LinearLayout) findViewById(R.id.layCheckDates);
			String[] dbInfo = result.split("<->");
			tvCheckDate.setText(dbInfo[1]);
			
			
			
			
			LinearLayout layGs = (LinearLayout) findViewById(R.id.layGs);
			String[] dateArr = dbInfo[1].split("\n");
			String[] checkArr = dbInfo[0].split("\n");
			for(int i = 0;i < dateArr.length && i < checkArr.length;i++){
				LinearLayout zontalLay = new LinearLayout(SQLresultView.this);
				TextView dateView = new TextView(SQLresultView.this),checkView = new TextView(SQLresultView.this);
				LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				LinearLayout.LayoutParams dateLayPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layPar.weight = 1.0f;
				dateLayPar.weight = 2.0f;
				dateView.setLayoutParams(dateLayPar);
				checkView.setLayoutParams(layPar);
				layCheck.addView(zontalLay);
				zontalLay.addView(dateView);
				zontalLay.addView(checkView);
				dateView.setText(dateArr[i]);
				checkView.setText(checkArr[i]);
			}
			
			String[] goalSplit = dbInfo[3].split("-");
			for(int i = 0;i < goalSplit.length;i++){
				String[] compSplit = goalSplit[i].split(",");
				CheckBox check = new CheckBox(SQLresultView.this);
				TextView textie = new TextView(SQLresultView.this);
				LinearLayout hLay = new LinearLayout(SQLresultView.this); 
				hLay.setOrientation(LinearLayout.HORIZONTAL);
				layGs.addView(hLay);
				hLay.addView(check);
				if (compSplit.length == 2) {
					if (compSplit[1].contentEquals("done")) {
						check.setChecked(true);
					} else {
						check.setChecked(false);
					}
				}
				check.setEnabled(false);
				hLay.addView(textie);
				textie.setText(compSplit[0]);
			}
			
			if (!dbInfo[2].isEmpty()) {
				if(!dbInfo[2].contentEquals("1")){
					dbInfo[2] = "Your Group Has " + dbInfo[2] + " Members.";
				tvGroup.setText(dbInfo[2]);
				}else{
					tvGroup.setText("");
				}
			}
			
			Dialog d = new Dialog(SQLresultView.this);
			d.setTitle("Darn.. We Have An Error");
			TextView tv = new TextView(SQLresultView.this);
			tv.setText("//" + checkResult + "/" + dbInfo[1] + "/" + dbInfo[2] + " /");
			d.setContentView(tv);
			d.show();
			
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.bUpdateTwo:
			ProjDbUI entry = new ProjDbUI(this);
			Date now = new Date();
			entry.open();
			entry.createEntry(checkResult, etUp.getText().toString(), "", "", "", now.toString());
			chDates(checkResult);
			entry.close();
			layCheck.removeAllViews();
			break;
		}
	}
}
