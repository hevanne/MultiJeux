package com.example.multijeux;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class TicTacToe extends AppCompatActivity {

    private String[][] board; // La grille du jeu
    private boolean playerXTurn = true; // Indicateur pour alterner entre les joueurs X et O
    private Button[][] buttons; // Les boutons de la grille
    private TextView statusTextView; // Affichage du statut (tour du joueur)
    private int gridSize; // Taille de la grille (par exemple, 3x3, 4x4, etc.)
    private int playerScore = 0; // Score du joueur
    private int aiScore = 0; // Score de l'ordinateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        // Récupérer la taille de la grille depuis les extras de l'Intent
        gridSize = getIntent().getIntExtra("selectedNumber", 3); // Par défaut, 3 si aucun nombre n'est passé

        statusTextView = findViewById(R.id.statusTextView);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Initialiser la grille et les boutons en fonction de la taille choisie
        initializeBoard(gridSize, gridLayout);

        // Réinitialiser la grille du jeu
        resetBoard();
    }

    // Initialiser la grille et les boutons dynamiquement
    private void initializeBoard(int size, GridLayout gridLayout) {
        board = new String[size][size]; // Créer la grille de taille variable
        buttons = new Button[size][size]; // Créer le tableau de boutons de taille variable

        gridLayout.setRowCount(size); // Définir le nombre de lignes
        gridLayout.setColumnCount(size); // Définir le nombre de colonnes

        // Calculer la largeur des cases pour qu'elles remplissent l'écran
        int buttonWidth = getResources().getDisplayMetrics().widthPixels / size;

        // Créer les boutons pour la grille
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button(this);
                buttons[i][j].setTextSize(30);

                // Définir la largeur et la hauteur du bouton
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonWidth; // Définir la largeur
                params.height = buttonWidth; // Garder la hauteur égale à la largeur pour avoir des cases carrées
                buttons[i][j].setLayoutParams(params);

                final int row = i, col = j;

                // Ajouter un listener pour chaque bouton
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCellClicked(row, col);
                    }
                });

                // Ajouter chaque bouton à la grille
                gridLayout.addView(buttons[i][j]);
            }
        }
    }

    // Gérer le clic sur une case
    private void onCellClicked(int row, int col) {
        if (board[row][col] != null) {
            return; // Si la case est déjà occupée, ne rien faire
        }

        // Affecter X ou O à la case
        board[row][col] = playerXTurn ? "X" : "O";
        buttons[row][col].setText(board[row][col]);

        // Vérifier si le joueur a gagné
        if (checkWinner()) {
            String winner = playerXTurn ? "Joueur (X)" : "Ordinateur (O)";
            if (playerXTurn) {
                playerScore++;
            } else {
                aiScore++;
            }
            Toast.makeText(this, winner + " a gagné !", Toast.LENGTH_LONG).show();
            updateScore();
            resetBoard(); // Réinitialiser la grille après une victoire
        } else {
            // Si la grille est pleine et qu'il n'y a pas de gagnant
            if (isBoardFull()) {
                Toast.makeText(this, "Match nul", Toast.LENGTH_LONG).show();
                resetBoard();
            } else {
                // L'ordinateur joue après le joueur
                playerXTurn = !playerXTurn;
                if (!playerXTurn) {
                    aiMove(); // L'ordinateur joue
                }
                updateStatus();
            }
        }
    }

    // Vérifier si un joueur a gagné
    private boolean checkWinner() {
        // Vérification des lignes et des colonnes pour 3 symboles alignés
        for (int i = 0; i < gridSize; i++) {
            boolean rowWin = true, colWin = true;
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] == null || !board[i][j].equals(board[i][0])) {
                    rowWin = false;
                }
                if (board[j][i] == null || !board[j][i].equals(board[0][i])) {
                    colWin = false;
                }
            }
            if (rowWin || colWin) {
                return true;
            }
        }

        // Vérification des diagonales pour 3 symboles alignés
        boolean diagonal1Win = true, diagonal2Win = true;
        for (int i = 0; i < gridSize; i++) {
            if (board[i][i] == null || !board[i][i].equals(board[0][0])) {
                diagonal1Win = false;
            }
            if (board[i][gridSize - 1 - i] == null || !board[i][gridSize - 1 - i].equals(board[0][gridSize - 1])) {
                diagonal2Win = false;
            }
        }
        return diagonal1Win || diagonal2Win;
    }

    // Vérifier si la grille est pleine
    private boolean isBoardFull() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // L'ordinateur fait un mouvement aléatoire
    private void aiMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(gridSize);
            col = rand.nextInt(gridSize);
        } while (board[row][col] != null); // Trouver une case vide

        board[row][col] = "O";
        buttons[row][col].setText("O");

        // Vérifier si l'ordinateur a gagné
        if (checkWinner()) {
            aiScore++;
            Toast.makeText(this, "L'ordinateur a gagné !", Toast.LENGTH_LONG).show();
            updateScore();
            resetBoard(); // Réinitialiser la grille après une victoire
        } else {
            playerXTurn = !playerXTurn; // Alterner le tour
            updateStatus();
        }
    }

    // Mettre à jour le statut affiché (tour du joueur)
    private void updateStatus() {
        String currentPlayer = playerXTurn ? "Joueur (X)" : "Ordinateur (O)";
        statusTextView.setText("Tour du : " + currentPlayer);
    }

    // Réinitialiser la grille
    private void resetBoard() {
        // Réinitialiser la grille de jeu
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                board[i][j] = null;
                buttons[i][j].setText("");
            }
        }
        playerXTurn = true; // Commencer par le joueur X
        updateStatus();
    }

    // Mettre à jour le score affiché
    private void updateScore() {
        String score = "Joueur (X): " + playerScore + " - Ordinateur (O): " + aiScore;
        statusTextView.setText(score);
    }
}
