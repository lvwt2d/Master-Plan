package com.lwoods.masterplan;

import java.util.Date;

import android.app.Activity;
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

public class DetailedProjectViewController extends Activity implements OnClickListener {

	LinearLayout layCheck;
	TextView tvInfo, tvDue, tvGroup;
	EditText etUp;
	Spinner spin;
	String checkResult = "";
	TextView proj, goal;
	Button up;
	Thread memThr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlview);
		layCheck = (LinearLayout) findViewById(R.id.layCheckDates);
		proj = (TextView) findViewById(R.id.tvProj);
		tvDue = (TextView) findViewById(R.id.tvDueDate);
		tvGroup = (TextView) findViewById(R.id.tvGroupDis);
		etUp = (EditText) findViewById(R.id.etUpTwo);
		up = (Button) findViewById(R.id.bUpdateTwo);
		up.setOnClickListener(this);
		etUp.setHint(getString(R.string.addACheckPointHint));
		String pow = getIntent().getExtras().getString(getString(R.string.projChoiceSpinnerIndexFromPreviousActivity));
		checkResult = pow;
		SQLiteDbInterfacer info = new SQLiteDbInterfacer(this);
		info.open();
		String checkpoints = info.getCheckDate(checkResult);
		String dueDate = info.getDue(checkResult);
		info.close();
		if (dueDate.isEmpty()) {
			dueDate = getString(R.string.noSetDueDate);
		}
		goals(checkResult);
		tvDue.setText(dueDate);
		chDates(checkResult);
		group(checkResult);
		proj.setText(checkResult);
	}

	private void goals(String name) {
		String goals = "";
		SQLiteDbInterfacer goalie = new SQLiteDbInterfacer(this);
		goalie.open();
		goals = goalie.getGoals(name);
		goalie.close();
		LinearLayout layGs = (LinearLayout) findViewById(R.id.layGs);
		String[] goalSplit = goals.split("-");
		for (int i = 0; i < goalSplit.length; i++) {
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
		SQLiteDbInterfacer dater = new SQLiteDbInterfacer(this);
		String checks = "";
		String dates = "";
		dater.open();
		dates = dater.getCheckDate(name);
		checks = dater.getData(name);
		dater.close();
		dates = dates.substring(0, dates.length());
		checks = checks.substring(0, checks.length());
		if (!dates.startsWith("\n")) {
			dates = "\n" + dates;
		}
		if (!checks.startsWith("\n")) {
			dates = "\n" + dates;
		}
		String[] dateArr = dates.split("\n");
		String[] checkArr = checks.split("\n");

		for (int i = 0; i < dateArr.length && i < checkArr.length; i++) {
			if (checkArr[i].isEmpty()) {
				continue;
			}else if(dateArr[i].isEmpty()){
				dateArr[i] = getString(R.string.checkPointDateErrorText);
			}
			if (checkArr[i].contains("\n")) {
				continue;
			}
			LinearLayout zontalLay = new LinearLayout(this);
			TextView dateView = new TextView(this), checkView = new TextView(
					this);
			LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LinearLayout.LayoutParams dateLayPar = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
		SQLiteDbInterfacer grouper = new SQLiteDbInterfacer(this);
		String group = "";
		grouper.open();
		group = grouper.getGroup(name);
		grouper.close();
		if (!group.isEmpty()) {
			if (!group.contentEquals("1")) {
				group = "Your Group Has " + group + " Members.";
				tvGroup.setText(group);
			} else {
				tvGroup.setText("");
			}
		}
	}

	public class DbProcThr extends AsyncTask<String, Integer, String> {

		// ProgressDialog dialog;

		protected void onPreExecute() {
			// example of setting up something

		}

		@Override
		protected String doInBackground(String... params) {

			/*
			 * Thread thread = Thread.currentThread(); if(spinMem.getStatus() ==
			 * AsyncTask.Status.PENDING){ while(spinMem.getStatus() !=
			 * AsyncTask.Status.FINISHED){ try { synchronized(thread){
			 * thread.wait(); } } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } } }
			 * if(spinMem.getStatus() == AsyncTask.Status.RUNNING){
			 * while(spinMem.getStatus() != AsyncTask.Status.FINISHED){ try {
			 * synchronized(thread){ thread.wait(); } } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } } } if(spinMem.getStatus() ==
			 * AsyncTask.Status.FINISHED){ new DbProcThr().execute(""); }
			 */

			SQLiteDbInterfacer infoBackG = new SQLiteDbInterfacer(DetailedProjectViewController.this);

			String neededIn = "";
			String checks = "";
			String dates = "";
			infoBackG.open();
			dates = infoBackG.getCheckDate(checkResult);
			checks = infoBackG.getData(checkResult);
			neededIn = checks + "<->" + dates;
			final String dueDate = infoBackG.getDue(checkResult);
			final String check = checks;
			final String date = dates;
			runOnUiThread(new Runnable() {
				public void run() {
					tvDue.setText(dueDate);
					// date = date.substring(0, date.length());
					// check = check.substring(0, check.length());
					String[] dateArr = date.split("\n");
					String[] checkArr = check.split("\n");

					for (int i = 0; i < dateArr.length && i < checkArr.length; i++) {
						LinearLayout zontalLay = new LinearLayout(
								DetailedProjectViewController.this);
						TextView dateView = new TextView(DetailedProjectViewController.this), checkView = new TextView(
								DetailedProjectViewController.this);
						LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						LinearLayout.LayoutParams dateLayPar = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
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
			});
			// dates = dates.substring(0, dates.length() - 4);
			// checks = checks.substring(0, checks.length() - 3);

			String group = "";
			group = infoBackG.getGroup(checkResult);

			neededIn += "<->" + group;
			String goals = "";
			goals = infoBackG.getGoals(checkResult);
			infoBackG.close();
			neededIn += "<->" + goals;

			return neededIn;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {

			/*
			 * Dialog d = new Dialog(SQLresultView.this);
			 * d.setTitle("Executed"); TextView tv = new
			 * TextView(SQLresultView.this); tv.setText("//" + "blah" + "/" +
			 * "/ /"); d.setContentView(tv); d.show();
			 */
			// layCheck = (LinearLayout) findViewById(R.id.layCheckDates);
			/*
			 * String[] dbInfo = result.split("<->"); LinearLayout layGs =
			 * (LinearLayout) findViewById(R.id.layGs); String[] dateArr =
			 * dbInfo[1].split("\n"); String[] checkArr = dbInfo[0].split("\n");
			 * for(int i = 0;i < dateArr.length && i < checkArr.length;i++){
			 * LinearLayout zontalLay = new LinearLayout(SQLresultView.this);
			 * TextView dateView = new TextView(SQLresultView.this),checkView =
			 * new TextView(SQLresultView.this); LinearLayout.LayoutParams
			 * layPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
			 * LayoutParams.MATCH_PARENT); LinearLayout.LayoutParams dateLayPar
			 * = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
			 * LayoutParams.MATCH_PARENT); layPar.weight = 1.0f;
			 * dateLayPar.weight = 2.0f; dateView.setLayoutParams(dateLayPar);
			 * checkView.setLayoutParams(layPar); layCheck.addView(zontalLay);
			 * zontalLay.addView(dateView); zontalLay.addView(checkView);
			 * dateView.setText(dateArr[i]); checkView.setText(checkArr[i]); }
			 * String[] goalSplit = dbInfo[3].split("-"); for(int i = 0;i <
			 * goalSplit.length;i++){ String[] compSplit =
			 * goalSplit[i].split(","); CheckBox check = new
			 * CheckBox(SQLresultView.this); TextView textie = new
			 * TextView(SQLresultView.this); LinearLayout hLay = new
			 * LinearLayout(SQLresultView.this);
			 * hLay.setOrientation(LinearLayout.HORIZONTAL);
			 * layGs.addView(hLay); hLay.addView(check); if (compSplit.length ==
			 * 2) { if (compSplit[1].contentEquals("done")) {
			 * check.setChecked(true); } else { check.setChecked(false); } }
			 * check.setEnabled(false); hLay.addView(textie);
			 * textie.setText(compSplit[0]); } if (!dbInfo[2].isEmpty()) {
			 * if(!dbInfo[2].contentEquals("1")){ dbInfo[2] = "Your Group Has "
			 * + dbInfo[2] + " Members."; tvGroup.setText(dbInfo[2]); }else{
			 * tvGroup.setText(""); } }
			 * 
			 * Dialog d = new Dialog(SQLresultView.this);
			 * d.setTitle("Darn.. We Have An Error"); TextView tv = new
			 * TextView(SQLresultView.this); tv.setText("//" + checkResult + "/"
			 * + dbInfo[1] + "/" + dbInfo[2] + " /"); d.setContentView(tv);
			 * d.show();
			 */

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bUpdateTwo:
			SQLiteDbInterfacer entry = new SQLiteDbInterfacer(this);
			Date now = new Date();
			entry.open();
			entry.createEntry(checkResult, etUp.getText().toString(), "", "",
					"", now.toString());
			chDates(checkResult);
			entry.close();
			layCheck.removeAllViews();
			break;
		}
	}
}
