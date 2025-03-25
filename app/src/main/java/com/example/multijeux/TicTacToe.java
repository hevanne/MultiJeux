package com.example.multijeux;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;


public class TicTacToe extends AppCompatActivity {
    private int gridSize;
    private char[][] board;
    private boolean playerTurn = true;
    private GridLayout gridLayout;
    private TextView turnIndicator;
    private TextView scoreTextView;
    private int playerScore = 0;
    private int computerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        // Afficher la boîte de dialogue pour choisir la taille de la grille
        showGridSizeDialog();
    }

    private void showGridSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir la taille de la grille");

        final Spinner spinner = new Spinner(this);
        Integer[] sizes = {3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sizes);
        spinner.setAdapter(adapter);
        builder.setView(spinner);

        builder.setPositiveButton("Jouer", (dialog, which) -> {
            gridSize = (Integer) spinner.getSelectedItem();
            initializeGame();
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> finish());

        builder.setCancelable(false);
        builder.show();
    }

    private void initializeGame() {
        setContentView(R.layout.activity_tic_tac_toe);

        gridLayout = findViewById(R.id.gameGrid);
        turnIndicator = findViewById(R.id.turnIndicator);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Calcul de la taille des cellules
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Réduire la hauteur disponible pour tenir compte des autres éléments UI
        int availableHeight = screenHeight - 300; // Ajustez cette valeur selon vos besoins
        int cellSize = Math.min(screenWidth, availableHeight) / gridSize;

        // Configuration de la grille
        gridLayout.setColumnCount(gridSize);
        gridLayout.setRowCount(gridSize);
        gridLayout.removeAllViews();

        board = new char[gridSize][gridSize];

        // Création des boutons
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = cellSize;
                params.height = cellSize;
                params.setMargins(2, 2, 2, 2);
                button.setLayoutParams(params);

                // Style du bouton
                button.setBackgroundResource(R.drawable.cell_background);
                button.setTextColor(Color.BLACK); // Texte en noir
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, cellSize * 0.4f); // 40% de la hauteur de la cellule
                button.setTypeface(null, Typeface.BOLD); // Texte en gras
                button.setPadding(0, 0, 0, 0); // Pas de padding interne

                button.setOnClickListener(new CellClickListener(row, col));
                gridLayout.addView(button);
            }
        }

        updateScore();
        updateTurnIndicator();
        adjustGridSize(); // Ajustement final
    }

    private void adjustGridSize() {
        gridLayout.post(() -> {
            int availableWidth = gridLayout.getWidth();
            int availableHeight = gridLayout.getHeight();
            int cellSize = Math.min(availableWidth, availableHeight) / gridSize;

            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View child = gridLayout.getChildAt(i);
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) child.getLayoutParams();
                params.width = cellSize;
                params.height = cellSize;
                child.setLayoutParams(params);

                if (child instanceof Button) {
                    Button button = (Button) child;
                    adjustTextSize(button);
                    button.setTextColor(Color.BLACK);
                }
            }
        });
    }

    private class CellClickListener implements View.OnClickListener {
        private final int row;
        private final int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (!playerTurn || board[row][col] != '\0') return;

            makeMove(row, col, 'X');

            if (checkWin('X')) {
                playerScore++;
                endGame("Vous avez gagné !");
                return;
            }

            if (isBoardFull()) {
                endGame("Match nul !");
                return;
            }

            playerTurn = false;
            updateTurnIndicator();

            // L'ordinateur joue après un court délai
            v.postDelayed(() -> computerTurn(), 500);
        }
    }

    private void computerTurn() {
        Random rand = new Random();
        int row, col;

        do {
            row = rand.nextInt(gridSize);
            col = rand.nextInt(gridSize);
        } while (board[row][col] != '\0');

        makeMove(row, col, 'O');

        if (checkWin('O')) {
            computerScore++;
            endGame("L'ordinateur a gagné !");
            return;
        }

        if (isBoardFull()) {
            endGame("Match nul !");
            return;
        }

        playerTurn = true;
        updateTurnIndicator();
    }

    private void makeMove(int row, int col, char symbol) {
        board[row][col] = symbol;
        Button button = (Button) gridLayout.getChildAt(row * gridSize + col);
        button.setText(String.valueOf(symbol));
        button.setTextColor(Color.BLACK); // S'assurer que le texte est noir
        button.setEnabled(false);
    }

    private void adjustTextSize(Button button) {
        float textSize = button.getHeight() * 0.4f; // 40% de la hauteur du bouton
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    private boolean checkWin(char player) {
        int neededToWin = gridSize == 3 ? 3 : 4;

        // Vérifier les lignes
        for (int row = 0; row < gridSize; row++) {
            int consecutive = 0;
            for (int col = 0; col < gridSize; col++) {
                consecutive = (board[row][col] == player) ? consecutive + 1 : 0;
                if (consecutive >= neededToWin) return true;
            }
        }

        // Vérifier les colonnes
        for (int col = 0; col < gridSize; col++) {
            int consecutive = 0;
            for (int row = 0; row < gridSize; row++) {
                consecutive = (board[row][col] == player) ? consecutive + 1 : 0;
                if (consecutive >= neededToWin) return true;
            }
        }

        // Vérifier les diagonales (haut-gauche vers bas-droit)
        for (int d = 0; d < 2 * gridSize - 1; d++) {
            int consecutive = 0;
            int startRow = Math.max(0, d - gridSize + 1);
            int endRow = Math.min(gridSize - 1, d);
            for (int row = startRow; row <= endRow; row++) {
                int col = d - row;
                consecutive = (board[row][col] == player) ? consecutive + 1 : 0;
                if (consecutive >= neededToWin) return true;
            }
        }

        // Vérifier les diagonales (haut-droit vers bas-gauche)
        for (int d = 0; d < 2 * gridSize - 1; d++) {
            int consecutive = 0;
            int startRow = Math.max(0, d - gridSize + 1);
            int endRow = Math.min(gridSize - 1, d);
            for (int row = startRow; row <= endRow; row++) {
                int col = gridSize - 1 - (d - row);
                consecutive = (board[row][col] == player) ? consecutive + 1 : 0;
                if (consecutive >= neededToWin) return true;
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] == '\0') return false;
            }
        }
        return true;
    }

    private void updateTurnIndicator() {
        turnIndicator.setText(playerTurn ? "Votre tour (X)" : "Tour de l'ordinateur (O)");
    }

    private void updateScore() {
        scoreTextView.setText("Score: Vous " + playerScore + " - Ordinateur " + computerScore);
    }

    private void endGame(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        updateScore();

        // Réinitialiser après un délai
        gridLayout.postDelayed(() -> {
            initializeGame();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}