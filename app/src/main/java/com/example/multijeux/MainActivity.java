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

		Button button = findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNumberSelectionDialog();
			}
		});

		Button button2 = findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LifeGame.class);
			}
		});

		Button button3 = findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showShapeSelectionDialog();
			}
		});
	}

	private void showShapeSelectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select a shape");

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

	private void showNumberSelectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Sélectionner un nombre");

		// Création du Spinner avec les nombres de 3 à 10
		final Spinner spinner = new Spinner(this);
		Integer[] numbers = {3, 4, 5, 6, 7, 8, 9, 10};
		ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
		spinner.setAdapter(adapter);
		builder.setView(spinner);

		// Bouton Annuler
		builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		// Bouton Confirmer
		builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int selectedNumber = (int) spinner.getSelectedItem();
				Toast.makeText(MainActivity.this, "Nombre sélectionné : " + selectedNumber, Toast.LENGTH_SHORT).show();

				// Lancer l'activité TicTacToe
				Intent intent = new Intent(MainActivity.this, TicTacToe.class);
				// Si vous voulez transmettre le nombre sélectionné à TicTacToe
				intent.putExtra("selectedNumber", selectedNumber);
				startActivity(intent);
			}
		});

		// Affichage de la boîte de dialogue
		builder.show();
	}
}
