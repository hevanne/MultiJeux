package com.example.multijeux;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
			}
		});

		// Affichage de la boîte de dialogue
		builder.show();
	}
}