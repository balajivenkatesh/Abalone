package com.abalone;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button resetButton = (Button) findViewById(R.id.buttonReset);

		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((GameView) findViewById(R.id.gamecanvas)).resetall();
			}
		});
		Button undoButton = (Button) findViewById(R.id.buttonUndo);

		undoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((GameView) findViewById(R.id.gamecanvas)).undo();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}