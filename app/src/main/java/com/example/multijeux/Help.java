package com.example.multijeux;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Help extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// Vous pouvez éventuellement personnaliser le texte ici si besoin
		// TextView helpText = findViewById(R.id.helpText);
		// helpText.setText("Votre nouveau texte d'aide ici.");
	}
}