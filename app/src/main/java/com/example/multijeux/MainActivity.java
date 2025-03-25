package com.example.multijeux;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_main);

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		Button ticTacToeButton = findViewById(R.id.button);
		ticTacToeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Lancer directement l'activité TicTacToe avec une taille par défaut
				Intent intent = new Intent(MainActivity.this, TicTacToe.class);
				intent.putExtra("GRID_SIZE", 3); // Taille par défaut 3x3
				startActivity(intent);
			}
		});

		Button button2 = findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, LifeGame.class));
			}
		});

		Button button3 = findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showShapeSelectionDialog();
			}
		});

		Button button5 = findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Help.class);
				startActivity(intent);
			}
		});

		Button button6 = findViewById(R.id.button6);
		button6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showShapeSelectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sélectionner une forme et une difficulté");

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final Spinner spinnerShape = new Spinner(this);
		String[] shapes = {"Square", "Circle", "Triangle"};
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, shapes);
		spinnerShape.setAdapter(adapter);
		layout.addView(spinnerShape);

		final Spinner spinnerDifficulty = new Spinner(this);
		String[] difficulties = {"easy", "medium", "hard"};
		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficulties);
		spinnerDifficulty.setAdapter(adapter2);
		layout.addView(spinnerDifficulty);

		builder.setView(layout);

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String selectedShape = (String) spinnerShape.getSelectedItem();
				Toast.makeText(MainActivity.this, "Selected shape : " + selectedShape, Toast.LENGTH_SHORT).show();
				String selectedDifficulty = (String) spinnerDifficulty.getSelectedItem();
				Toast.makeText(MainActivity.this, "Selected difficulty : " + selectedDifficulty, Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(MainActivity.this, LineTracker.class);

				intent.putExtra("selectedShape", selectedShape);
				intent.putExtra("selectedDifficulty", selectedDifficulty);
				startActivity(intent);
			}
		});

		builder.show();
	}
}
