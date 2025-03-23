package com.example.multijeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ShapeView extends View {

    private Paint paint;
    private Paint cursor;
    private String shape = "Square"; //forme par défaut
    private String difficulty = "easy"; //difficulté par défault

    private Path path;
    private float posX, posY; // Position du dessin

    public ShapeView(Context context) {
        super(context);
        init();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); //pour les contours
        paint.setAntiAlias(true); //Lisser les bords

        cursor = new Paint();
        cursor.setColor(Color.RED); // Couleur du point (blanc)
        cursor.setStyle(Paint.Style.STROKE);
        cursor.setStrokeWidth(10f); // Taille du point
        cursor.setAntiAlias(true);
        path = new Path();
        posX = 100; // Position initiale au centre
        posY = 100;
    }

    private void setPaintWidth() {

        switch(this.difficulty) {
            case "easy":
                paint.setStrokeWidth(100f);
                break;
            case "medium":
                paint.setStrokeWidth(75f);
                break;
            case "hard":
                paint.setStrokeWidth(50f);
                break;
            default :
                paint.setStrokeWidth(100f);
        }//Epaisseur
    }

    private void setPaintColor() {
        switch(this.difficulty) {
            case "easy" :
                paint.setColor(Color.GREEN);
                break;
            case "medium" :
                paint.setColor(Color.YELLOW);
                break;
            case "hard" :
                paint.setColor(Color.RED);
                break;
            default:
                paint.setColor(Color.WHITE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        setPaintWidth();
        setPaintColor();

        canvas.drawPath(path, cursor);

        switch (this.shape){
            case "Square":
                drawSquare(canvas);
                break;
            case "Circle":
                drawCircle(canvas);
                break;
            case "Triangle":
                drawTriangle(canvas);
                break;

            default:
        }

        canvas.drawPath(path, cursor);
    }

     public void updatePosition(float deltaX, float deltaY) {
            posX -= deltaX;
            posY -= deltaY;

            // Limiter la position aux bords de l'écran
            posX = Math.max(0, Math.min(posX, getWidth()));
            posY = Math.max(0, Math.min(posY, getHeight()));

            // Ajouter un point au chemin
            if (path.isEmpty()) {
                path.moveTo(posX, posY);
            } else {
                path.lineTo(posX, posY);
            }

            invalidate(); // Redessiner la vue
        }

    private void drawSquare(Canvas canvas) {
        float taille = 500f;
        float left = (getWidth() - taille) / 2f;
        float top = (getHeight() - taille) / 2f;
        float right = left + taille;
        float bottom = top + taille;
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawCircle(Canvas canvas) {
        float centerX = getWidth()/2f;
        float centerY = getHeight()/2f;
        float radius = 300f;
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public void drawTriangle(Canvas canvas) {

    }

    public void setCanvas(String shape, String difficulty){
        this.shape = shape;
        this.difficulty = difficulty;
        invalidate(); // Redessiner la vue
    }

    public void clear() {
        path.reset();
        invalidate();
    }
}