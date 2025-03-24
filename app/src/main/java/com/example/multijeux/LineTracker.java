package  com.example.multijeux;

import android.graphics.drawable.shapes.Shape;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LineTracker extends AppCompatActivity implements SensorEventListener {

    private ShapeView shapeView;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_tracker);



        // Initialiser la vue personnalisée
        shapeView = findViewById(R.id.shapeView);
        shapeView.attachLineTracker(this);

        // Dessiner la forme sélectionnée
        String shape = getIntent().getStringExtra("selectedShape");
        String difficulty = getIntent().getStringExtra("selectedDifficulty");

        if (shape == null) {
            shape = "Square"; // Forme par défaut
        }

        if (difficulty == null) {
            difficulty = "easy"; // Difficulté par défaut
        }

        // Définir la forme dans ShapeView
        shapeView.setCanvas(shape, difficulty);

        // Initialiser le SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        Button start = findViewById(R.id.button4);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeView.startGame();
                TextView textView = findViewById(R.id.textView3);
                textView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void endGame() {
        TextView textView = findViewById(R.id.textView3);
        textView.setText("Score : " + Math.round(getScore()) + "%");
        textView.setVisibility(View.VISIBLE);
    }

    public float getScore() {
        return shapeView.getScore();
    }

    protected void onResume() {
        super.onResume();
        // Enregistrer l'écouteur du capteur
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Désenregistrer l'écouteur du capteur
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Récupérer les valeurs de l'accéléromètre
            float deltaX = event.values[0]; // Inclinaison sur l'axe X
            float deltaY = event.values[1]; // Inclinaison sur l'axe Y

            // Mettre à jour la position du dessin
            shapeView.updatePosition(deltaX, -deltaY); // Inverser deltaY pour un mouvement naturel
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé ici
    }
}