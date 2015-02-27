package com.lwoods.masterplan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.TabHost.TabSpec;

public class Projects extends Activity implements OnClickListener,
		OnItemSelectedListener {

	public static final String SPINNER_IND = "com.lwoods.masterplan._ID";
	TabHost th;
	Spinner spin, spinUp, groupChange;
	String[] projNames = { "LWoods Demo Projects" };
	Button sqlUpdate, sqlView, flip, vflip, gflip, dueflip, submit, addGoal,
			dependflip;
	EditText sqlChange, newName, newCheck, dueDate, goals, depends, dateChange,
			etAddGoal;
	ViewFlipper flips, checkFlip, goalFlip, dDateflip, dependFlip;

	FileOutputStream fos;
	String FILENAME = "InternalString";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.projects);
		initialize();
		displayGoals();

	}

	private void displayGoals() {
		// TODO Auto-generated method stub
		ProjDbUI goalie = new ProjDbUI(Projects.this);

		// String goalString = goalie.getGoals(name);
	}

	private void initialize() {
		setUpTabs();
		setUpSpin();

		sqlUpdate = (Button) findViewById(R.id.bUpdateDb);
		sqlView = (Button) findViewById(R.id.bSQLopenView);
		flip = (Button) findViewById(R.id.bGroup);
		submit = (Button) findViewById(R.id.bSubmitNew);
		vflip = (Button) findViewById(R.id.bUpCheck);
		addGoal = (Button) findViewById(R.id.bAddGoal);
		gflip = (Button) findViewById(R.id.bGoalflip);
		dueflip = (Button) findViewById(R.id.bDateflip);
		dependflip = (Button) findViewById(R.id.bDependflip);
		sqlChange = (EditText) findViewById(R.id.etSQLChange);
		newName = (EditText) findViewById(R.id.etNewProject);
		newCheck = (EditText) findViewById(R.id.etNewCheck);
		dueDate = (EditText) findViewById(R.id.etNewDate);
		goals = (EditText) findViewById(R.id.etGoals);
		depends = (EditText) findViewById(R.id.etDepends);
		dateChange = (EditText) findViewById(R.id.etDChange);
		etAddGoal = (EditText) findViewById(R.id.etAddGoal);
		flips = (ViewFlipper) findViewById(R.id.vfGroup);
		checkFlip = (ViewFlipper) findViewById(R.id.vfUpCheck);
		goalFlip = (ViewFlipper) findViewById(R.id.vfGoals);
		dDateflip = (ViewFlipper) findViewById(R.id.vfDateChange);
		dependFlip = (ViewFlipper) findViewById(R.id.vfDepend);
		sqlUpdate.setOnClickListener(this);
		sqlView.setOnClickListener(this);
		flip.setOnClickListener(this);
		vflip.setOnClickListener(this);
		submit.setOnClickListener(this);
		addGoal.setOnClickListener(this);
		dependflip.setOnClickListener(this);
		gflip.setOnClickListener(this);
		dueflip.setOnClickListener(this);
		etAddGoal.setHint("Add A Goal?('goal1-goal2-etc.-')");
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUpSpin() {
		// TODO Auto-generated method stub
		ProjDbUI namer = new ProjDbUI(Projects.this);
		String[] spinString = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"10" };
		try {
			namer.open();
			if (namer.getProjects().length != 0) {
				projNames = namer.getProjects();
			} else {
				Dialog d = new Dialog(this);
				d.setTitle("You Dont Have Any Projects?!");
				TextView tv = new TextView(this);
				tv.setText("See The New Project Section So We Can Get Started");
				d.setContentView(tv);
				d.show();
			}
			namer.close();
		} catch (Exception e) {
			// StackTraceElement[] errors =
			// Thread.currentThread().getStackTrace();
			Dialog d = new Dialog(this);
			d.setTitle("Problem!");
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			d.setContentView(tv);
			d.show();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Projects.this,
				android.R.layout.simple_spinner_item, projNames);
		ArrayAdapter<String> daTadapter = new ArrayAdapter<String>(
				Projects.this, android.R.layout.simple_spinner_item, spinString);
		spin = (Spinner) findViewById(R.id.spProjectChoise);
		spinUp = (Spinner) findViewById(R.id.spUpdateChoice);
		groupChange = (Spinner) findViewById(R.id.spGroupChange);
		spin.setAdapter(adapter);
		spinUp.setAdapter(adapter);
		groupChange.setAdapter(daTadapter);
		spin.setOnItemSelectedListener(this);
		spinUp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				LinearLayout ll = (LinearLayout) findViewById(R.id.layGoals);
				int count = ll.getChildCount();
				View etAddg = ll.getChildAt(0);
				View butAddg = ll.getChildAt(1);
				/*
				 * for (int i = 0; i < count; i++) { View child =
				 * ll.getChildAt(i); if (i <= 2) {
				 * 
				 * ll.removeView(child);
				 * 
				 * } }
				 */
				ll.removeAllViews();
				ll.addView(etAddg);
				ll.addView(butAddg);
				checkFlip.setDisplayedChild(0);
				goalFlip.setDisplayedChild(0);
				dDateflip.setDisplayedChild(0);
				dependFlip.setDisplayedChild(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		groupChange.setOnItemSelectedListener(this);
	}

	private void setUpTabs() {
		// TODO Auto-generated method stub
		th = (TabHost) findViewById(R.id.thProjects);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tCurrent);
		specs.setIndicator("Current Projects");
		th.addTab(specs);
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Update Projects");
		th.addTab(specs);
		specs = th.newTabSpec("tag3");
		specs.setContent(R.id.tab3);
		specs.setIndicator("Add A Project");
		th.addTab(specs);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bUpdateDb:
			boolean didItWork = true;
			try {
				int position = spinUp.getSelectedItemPosition();
				int gposition = groupChange.getSelectedItemPosition();
				Date riteNow = new Date();
				String name = spinUp.getItemAtPosition(position).toString();
				String change = sqlChange.getText().toString();
				String dueChange = dateChange.getText().toString();
				String depends = groupChange.getItemAtPosition(gposition)
						.toString();
				String compoundG = "";
				ProjDbUI entry = new ProjDbUI(Projects.this);

				LinearLayout ll = (LinearLayout) findViewById(R.id.layGoals);
				int count = ll.getChildCount();
				ArrayList<String> goalText = new ArrayList<String>();
				ArrayList<String> checkedOrNot = new ArrayList<String>();
				entry.open();

				for (int i = 0; i < count; i++) {
					View child = ll.getChildAt(i);
					if (child instanceof LinearLayout) {
						for (int j = 0; j < 2; j++) {
							View layoutChildren = ((LinearLayout) child)
									.getChildAt(j);
							if (layoutChildren instanceof CheckBox) {

								CheckBox box = (CheckBox) layoutChildren;
								if (box.isChecked()) {
									checkedOrNot.add("done");
								} else {
									checkedOrNot.add("incomplete");
								}
							}else{
								TextView text = (TextView) layoutChildren;
								goalText.add(text.getText().toString());
							}
						}
					}

				}


				if (goalText.size() != 0 && checkedOrNot.size() != 0) {
					for (int i = 0; i < goalText.size(); i++) {
							if (i != goalText.size() - 1) {
								compoundG += goalText.get(i) + ","
										+ checkedOrNot.get(i) + "-";
							}else{
								compoundG += goalText.get(i) + "," + checkedOrNot.get(i);
							}
						

					}
				}

				
				entry.createEntry(name, change, dueChange, compoundG, depends,
						riteNow.toString());
				setUpSpin();
				entry.close();
			} catch (Exception e) {
				didItWork = false;
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("Darn.. We Have An Error");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			} finally {
				if (didItWork) {
					Dialog d = new Dialog(this);
					d.setTitle("Project Updated!");
					TextView tv = new TextView(this);
					tv.setText("Your Checkpoint Was Saved");
					d.setContentView(tv);
					d.show();
				}
			}

			break;
		case R.id.bSQLopenView:
			Intent i = new Intent("com.lwoods.masterplan.SQLresultView");
			int positioned = spin.getSelectedItemPosition();
			String named = spin.getItemAtPosition(positioned).toString();
			i.putExtra("spinnerIndex", named);
			startActivity(i);
			break;
		case R.id.bGroup:
			flips.showNext();
			break;
		case R.id.bSubmitNew:
			boolean didItWorktwo = true;
			try {
				String name = newName.getText().toString();
				String change = newCheck.getText().toString();
				String due = dueDate.getText().toString();
				String goal = goals.getText().toString();
				String pend = depends.getText().toString();
				Date now = new Date();
				ProjDbUI entry = new ProjDbUI(Projects.this);
				entry.open();
				entry.createEntry(name, change, due, goal, pend, now.toString());
				setUpSpin();
				entry.close();
			} catch (Exception e) {
				didItWorktwo = false;
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("Darn.. We Have An Error");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			} finally {
				if (didItWorktwo) {
					Dialog d = new Dialog(this);
					d.setTitle("Your Project Was Saved!");
					TextView tv = new TextView(this);
					tv.setText("Good! Now You Can Get To Work.");
					d.setContentView(tv);
					d.show();
				}
			}
			break;
		case R.id.bUpCheck:
			checkFlip.showNext();
			break;
		case R.id.bGoalflip:
			goalFlip.showNext();
			dynaGoals();
			break;
		case R.id.bAddGoal:
			int position = spinUp.getSelectedItemPosition();
			Date riteNow = new Date();
			String name = spinUp.getItemAtPosition(position).toString();
			ProjDbUI groupEntry = new ProjDbUI(Projects.this);
			groupEntry.open();
			String addedG = etAddGoal.getText().toString();
			if (!groupEntry.getGoals(name).isEmpty()) {
				addedG = groupEntry.getGoals(name) + "-"
						+ etAddGoal.getText().toString();
			}
			groupEntry.open();
			groupEntry
					.createEntry(name, "", "", addedG, "", riteNow.toString());
			groupEntry.close();
			LinearLayout glay = (LinearLayout) findViewById(R.id.layGoals);
			glay.removeAllViews();
			dynaGoals();
			break;
		case R.id.bDateflip:
			dDateflip.showNext();
			break;
		case R.id.bDependflip:
			dependFlip.showNext();
			break;
		}
	}

	private void dynaGoals() {
		// THE PURPOSE OF THIS METHOD IS TO DYNAMICALLY PRINT GOAL TEXTVIEWS AND
		// CHECKBOXES
		CheckBox[] checks = { new CheckBox(this) };
		String goalStr = "";
		String[] goalA = { "" };
		LinearLayout goalLay = (LinearLayout) findViewById(R.id.layGoals);
		ProjDbUI goalie = new ProjDbUI(this);
		goalie.open();
		int position = spinUp.getSelectedItemPosition();
		final String name = spinUp.getItemAtPosition(position).toString();
		goalStr = goalie.getGoals(name);
		goalie.close();
		goalA = goalStr.split("-");
		checks = new CheckBox[goalA.length];
		if (goalStr != "") {
			for (int i = 0; i < goalA.length; i++) {
				String[] split = goalA[i].split(",");
				if (!split[0].isEmpty()) {
				TextView gView = new TextView(this);
				LinearLayout horLay = new LinearLayout(this);
				horLay.setOrientation(LinearLayout.HORIZONTAL);
				String comboGoals = "";
				goalLay.addView(horLay);
				checks[i] = new CheckBox(this);
				horLay.addView(checks[i]);
				horLay.addView(gView);
				gView.setText(split[0]);
				if (split.length == 2) {
					if (split[1].contentEquals("done")) {
						checks[i].setChecked(true);
					} else {
						checks[i].setChecked(false);
					}
				}
				}
			}
			goalA[0] = "";
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TRIGGERED ON THE SELECTION OF MY FIRST DROP DOWN LIST
		// USE DISPLAYCHILDAT(0) TO GO TO THE FIRST VIEW IN THE FLIPPER

		int position = spin.getSelectedItemPosition();
		String data = spin.getItemAtPosition(position).toString();
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
