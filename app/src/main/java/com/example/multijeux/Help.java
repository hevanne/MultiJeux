package com.example.multijeux;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class Help extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		TextView helpText = findViewById(R.id.helpText);

		String helpContent = "Comment jouer à chaque jeu :\n\n" +
				"1. Line Tracker :\n" +
				"- Choisissez une forme et une difficulté\n" +
				"- Inclinez l'appareil pour suivre le contour\n" +
				"- Votre score s'affiche à la fin\n\n" +
				"2. Jeu de la Vie :\n" +
				"- Choisissez taille et densité de la grille\n" +
				"- Lancez la simulation et observez\n" +
				"- Les cellules évoluent automatiquement\n\n" +
				"3. Morpion :\n" +
				"- Choisissez la taille de grille (3x3 à 10x10)\n" +
				"- Alignez 3-4 symboles pour gagner\n" +
				"- Jouez contre l'ordinateur";

		helpText.setText(helpContent);
	}
}