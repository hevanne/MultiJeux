package com.example.multijeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LifeGame extends AppCompatActivity {
    private LifeGameView gameView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isRunning = false;
    private int iterationCount = 0;
    private TextView iterationTextView;
    private Spinner sizeSpinner, densitySpinner;

    // Override : vérif pour le compilateur et la lisibilité , à retirer une fois finie 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_game);

        // Initialisation des vues
        gameView = findViewById(R.id.game_view);
        iterationTextView = findViewById(R.id.iteration_text);
        Button startButton = findViewById(R.id.start_button);
        Button stopButton = findViewById(R.id.stop_button);
        Button resetButton = findViewById(R.id.reset_button);
        sizeSpinner = findViewById(R.id.size_spinner);
        densitySpinner = findViewById(R.id.density_spinner);

        // Configuration des Spinners
        setupSpinners();

        // Initialisation par défaut
        resetGameWithCurrentParams();

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                startSimulation();
            }
        });

        stopButton.setOnClickListener(v -> stopGame());

        resetButton.setOnClickListener(v -> resetGameWithCurrentParams());
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.grid_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(1);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isRunning) return;
                resetGameWithCurrentParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> densityAdapter = ArrayAdapter.createFromResource(this,
                R.array.density_levels, android.R.layout.simple_spinner_item);
        densityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        densitySpinner.setAdapter(densityAdapter);
        densitySpinner.setSelection(2);

        densitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isRunning) return;
                resetGameWithCurrentParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void resetGameWithCurrentParams() {
        stopGame();
        int newSize = Integer.parseInt(sizeSpinner.getSelectedItem().toString());
        double newDensity = Double.parseDouble(densitySpinner.getSelectedItem().toString());
        gameView.initialize(newSize, newDensity);
        iterationCount = 0;
        iterationTextView.setText("Itérations: 0");
    }

    private void startSimulation() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning && iterationCount < 1000) {
                    gameView.nextGeneration();
                    iterationCount++;
                    iterationTextView.setText("Itérations: " + iterationCount);

                    if (gameView.isStable() || gameView.isPeriodic()) {
                        stopGame();
                        iterationTextView.setText("Terminé! Itérations: " + iterationCount);
                        return;
                    }

                    handler.postDelayed(this, 200);
                } else if (iterationCount >= 1000) {
                    iterationTextView.setText("Terminé (max 1000 itérations)");
                    stopGame();
                }
            }
        };
        handler.post(runnable);
    }

    private void stopGame() {
        isRunning = false;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public static class LifeGameView extends View {
        private boolean[][] grid;
        private int size;
        private double density;
        private List<boolean[][]> previousStates = new ArrayList<>();
        private Paint cellPaint = new Paint();
        private Paint gridPaint = new Paint();
        private Random random = new Random();

        public LifeGameView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setupPaints();
        }

        private void setupPaints() {
            cellPaint.setColor(Color.BLUE);
            cellPaint.setStyle(Paint.Style.FILL);
            gridPaint.setColor(Color.GRAY);
            gridPaint.setStyle(Paint.Style.STROKE);
            gridPaint.setStrokeWidth(1);
        }

        public void initialize(int newSize, double newDensity) {
            this.size = newSize;
            this.density = newDensity;
            reset();
        }

        public void reset() {
            grid = new boolean[size][size];
            previousStates.clear();
            randomizeGrid();
            saveState();
            invalidate();
        }

        private void randomizeGrid() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    grid[i][j] = random.nextDouble() < density;
                }
            }
        }

        public void nextGeneration() {
            boolean[][] newGrid = new boolean[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int liveNeighbors = countLiveNeighbors(i, j);

                    if (grid[i][j]) {
                        newGrid[i][j] = liveNeighbors == 2 || liveNeighbors == 3;
                    } else {
                        newGrid[i][j] = liveNeighbors == 3;
                    }
                }
            }

            grid = newGrid;
            saveState();
            invalidate();
        }

        private int countLiveNeighbors(int x, int y) {
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;

                    int nx = x + i, ny = y + j;
                    if (nx >= 0 && nx < size && ny >= 0 && ny < size && grid[nx][ny]) {
                        count++;
                    }
                }
            }
            return count;
        }

        private void saveState() {
            boolean[][] state = new boolean[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(grid[i], 0, state[i], 0, size);
            }
            previousStates.add(state);
        }

        public boolean isStable() {
            if (previousStates.size() < 2) return false;
            return arraysEqual(previousStates.get(previousStates.size()-1),
                    previousStates.get(previousStates.size()-2));
        }
        // Vérif pour pas que ça se repete à l'infi
        public boolean isPeriodic() {
            if (previousStates.size() < 4) return false;
            boolean[][] current = previousStates.get(previousStates.size()-1);
            for (int i = previousStates.size()-2; i >= Math.max(0, previousStates.size()-10); i--) {
                if (arraysEqual(current, previousStates.get(i))) {
                    return true;
                }
            }
            return false;
        }

        private boolean arraysEqual(boolean[][] a1, boolean[][] a2) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (a1[i][j] != a2[i][j]) return false;
                }
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (size == 0) return;

            float cellSize = Math.min(getWidth(), getHeight()) / (float)size;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    float left = j * cellSize;
                    float top = i * cellSize;
                    float right = left + cellSize;
                    float bottom = top + cellSize;

                    if (grid[i][j]) {
                        canvas.drawRect(left, top, right, bottom, cellPaint);
                    }
                    canvas.drawRect(left, top, right, bottom, gridPaint);
                }
            }
        }
    }
}
