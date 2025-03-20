package com.example.multijeux;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class TicTacToe extends AppCompatActivity {

    private String[][] board;
    private boolean playerXTurn = true;
    private Button[][] buttons;
    private TextView statusTextView;
    private TextView playerScoreTextView;
    private TextView aiScoreTextView;

    private TextView scoreTextView;
    private int gridSize;
    private int playerScore = 0;
    private int aiScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        gridSize = getIntent().getIntExtra("selectedNumber", 3);

        statusTextView = findViewById(R.id.statusTextView);
        playerScoreTextView = findViewById(R.id.playerScore);
        aiScoreTextView = findViewById(R.id.aiScore);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        initializeBoard(gridSize, gridLayout);
        resetBoard();
    }

    private void initializeBoard(int size, GridLayout gridLayout) {
        board = new String[size][size];
        buttons = new Button[size][size];

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int gridWidth = (int) (screenWidth * 0.9);
        int padding = (int) (4 * getResources().getDisplayMetrics().density);
        int availableWidth = gridWidth - (2 * padding);
        int buttonSize = availableWidth / size;

        gridLayout.setRowCount(size);
        gridLayout.setColumnCount(size);
        gridLayout.setPadding(padding, padding, padding, padding);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button(this);
                buttons[i][j].setTextSize(buttonSize / 5);
                buttons[i][j].setPadding(0, 0, 0, 0);
                buttons[i][j].setGravity(Gravity.CENTER);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                buttons[i][j].setLayoutParams(params);

                final int row = i, col = j;
                buttons[i][j].setOnClickListener(v -> onCellClicked(row, col));
                gridLayout.addView(buttons[i][j]);
            }
        }
    }

    private void onCellClicked(int row, int col) {
        if (board[row][col] != null) return;

        board[row][col] = playerXTurn ? "X" : "O";
        buttons[row][col].setText(board[row][col]);

        if (checkWinner()) {
            if (playerXTurn) playerScore++;
            else aiScore++;
            Toast.makeText(this, (playerXTurn ? "Joueur (X)" : "Ordinateur (O)") + " a gagné !", Toast.LENGTH_LONG).show();
            updateScore();
            resetBoard();
        } else if (isBoardFull()) {
            Toast.makeText(this, "Match nul", Toast.LENGTH_LONG).show();
            resetBoard();
        } else {
            playerXTurn = !playerXTurn;
            if (!playerXTurn) aiMove();
            updateStatus();
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < gridSize; i++) {
            if (checkLine(board[i][0], board[i])) return true;
            if (checkLine(board[0][i], getColumn(i))) return true;
        }
        return checkLine(board[0][0], getDiagonal1()) || checkLine(board[0][gridSize - 1], getDiagonal2());
    }

    private boolean checkLine(String first, String[] line) {
        if (first == null) return false;
        for (String cell : line) if (!first.equals(cell)) return false;
        return true;
    }

    private String[] getColumn(int col) {
        String[] column = new String[gridSize];
        for (int i = 0; i < gridSize; i++) column[i] = board[i][col];
        return column;
    }

    private String[] getDiagonal1() {
        String[] diag = new String[gridSize];
        for (int i = 0; i < gridSize; i++) diag[i] = board[i][i];
        return diag;
    }

    private String[] getDiagonal2() {
        String[] diag = new String[gridSize];
        for (int i = 0; i < gridSize; i++) diag[i] = board[i][gridSize - 1 - i];
        return diag;
    }

    private boolean isBoardFull() {
        for (String[] row : board) for (String cell : row) if (cell == null) return false;
        return true;
    }

    private void aiMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(gridSize);
            col = rand.nextInt(gridSize);
        } while (board[row][col] != null);

        board[row][col] = "O";
        buttons[row][col].setText("O");

        if (checkWinner()) {
            aiScore++;
            Toast.makeText(this, "L'ordinateur a gagné !", Toast.LENGTH_LONG).show();
            updateScore();
            resetBoard();
        } else {
            playerXTurn = !playerXTurn;
            updateStatus();
        }
    }

    private void updateStatus() {
        statusTextView.setText("Tour du : " + (playerXTurn ? "Joueur (X)" : "Ordinateur (O)"));
    }

    private void updateScore() {
        playerScoreTextView.setText("Joueur (X): " + playerScore);
        aiScoreTextView.setText("Ordinateur (O): " + aiScore);
    }


    private void resetBoard() {
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++) {
                board[i][j] = null;
                buttons[i][j].setText("");
            }
        playerXTurn = true;
        updateStatus();
    }
}
