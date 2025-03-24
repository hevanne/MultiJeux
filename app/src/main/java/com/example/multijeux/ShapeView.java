package com.example.multijeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShapeView extends View {

    private Paint paint;
    private Paint cursor;
    private String shape = ""; //forme par défaut
    private String difficulty = ""; //difficulté par défault

    private float lastX, lastY; // Pour stocker la position précédente
    private float posX, posY; // Position du dessin
    private float posXStart, posYStart;
    private float squareLeft, squareTop, squareRight, squareBottom; //Positions du carré
    private float circleCenterX, circleCenterY, circleRadius; //Positions du cercle
    private float triangleCenterX, triangleCenterY, triangleTaille; //Positions du triangle
    private boolean bSortie = false; //pour savoir quand le cursor va sortir

    private float scoreFull, scoreIn = 0f;

    private Path path = new Path();

    private LineTracker lineTracker;

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

    public void attachLineTracker(LineTracker tracker) {
        this.lineTracker = tracker;
    }

    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); //pour les contours
        paint.setAntiAlias(true); //Lisser les bords
    }

    public void startGame(){
        cursor = new Paint();
        cursor.setColor(Color.RED); // Couleur du point (blanc)
        cursor.setStyle(Paint.Style.STROKE);
        cursor.setStrokeWidth(10f); // Taille du point
        cursor.setAntiAlias(true);

        this.bSortie = false;
        this.scoreIn=0f;
        this.scoreFull=0f;
        setCursorPosition();
        invalidate();

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
        if (cursor != null) {
            canvas.drawPath(path, cursor);
        }
    }

    public void updatePosition(float deltaX, float deltaY) {
        float oldX = posX;
        float oldY = posY;

        // Mettre à jour la position
        posX -= deltaX*2;
        posY -= deltaY*2;

        // Limiter la position aux bords de l'écran
        posX = Math.max(0, Math.min(posX, getWidth()));
        posY = Math.max(0, Math.min(posY, getHeight()));

        // Mettre à jour le path
        if (path.isEmpty()) {
            // Premier point
            path.moveTo(posX, posY);
        } else {
            // Ajouter une ligne depuis la dernière position
            path.lineTo(posX, posY);
        }
        if (posX >= posXStart-20f && posX <= posXStart+20f) {
                if (posY >= posYStart-20f && posY <= posYStart+20f) {
                    if (this.bSortie){clear();}
                }
                else {this.bSortie = true;}
        }
        else{this.bSortie = true;}

        // Changer la couleur du curseur selon la position
        if (cursor != null) {
            if (this.shape.equals("Square")) {
                if (areaSquare(posX, posY)) {
                    cursor.setColor(Color.RED);
                    scoreIn++;
                } else {
                    cursor.setColor(Color.BLUE);
                }
            }
            if (this.shape.equals("Circle")) {
                if (areaCircle(posX, posY)) {
                    cursor.setColor(Color.RED);
                    scoreIn++;
                } else {
                    cursor.setColor(Color.BLUE);
                }
            }

            if (this.shape.equals("Triangle")) {
                if (areaTriangle(posX, posY)) {
                    cursor.setColor(Color.RED);
                    scoreIn++;
                } else {
                    cursor.setColor(Color.BLUE);
                }
            }
        }

        scoreFull++;

        invalidate(); // Redessiner la vue
    }

    private void drawSquare(Canvas canvas) {
        float taille = 500f;
        squareLeft = (getWidth() - taille) / 2f;
        squareTop = (getHeight() - taille) / 2f;
        squareRight = squareLeft + taille;
        squareBottom = squareTop + taille;
        canvas.drawRect(squareLeft, squareTop, squareRight, squareBottom, paint);
    }

    private void drawCircle(Canvas canvas) {
        circleCenterX = getWidth()/2f;
        circleCenterY = getHeight()/2f;
        circleRadius = 300f;
        canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, paint);
    }

    public void drawTriangle(Canvas canvas) {
        triangleCenterX = getWidth() / 2f;
        triangleCenterY = getHeight() / 2f;
        triangleTaille = 500f;

        Path pathTriangle = new Path();
        pathTriangle.moveTo(triangleCenterX, triangleCenterY - triangleTaille / 2f);
        pathTriangle.lineTo(triangleCenterX - triangleTaille / 2f, triangleCenterY + triangleTaille / 2f);
        pathTriangle.lineTo(triangleCenterX + triangleTaille / 2f, triangleCenterY + triangleTaille / 2f);
        pathTriangle.close();
        canvas.drawPath(pathTriangle, paint);
    }

    public boolean areaSquare(float px, float py) {
        float size;
        switch(this.difficulty) {
            case "easy":
                size = 100f;
                break;
            case "medium":
                size = 75f;
                break;
            case "hard":
                size = 50f;
                break;
            default :
                size = 100f;
        }//Epaisseur

        float left = (getWidth() - 500f) / 2f;
        float top = (getHeight() - 500f) / 2f;
        float right = left + 500f;
        float bottom = top + 500f;
        // Si le point est à l'intérieur du carré
        if (px >= left-(size/2) && px <= right+(size/2) && py >= top-(size/2) && py-(size/2) <= bottom) {

            if (px >= left+(size/2) && px <= right-(size/2) && py >= top+(size/2) && py+(size/2) <= bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false; // Si le point ne se trouve sur aucun bord, retourne false
    }

    public boolean areaCircle(float px, float py) {
        float size;
        switch(this.difficulty) {
            case "easy":
                size = 100f;
                break;
            case "medium":
                size = 75f;
                break;
            case "hard":
                size = 50f;
                break;
            default :
                size = 100f;
        }//Epaisseur

        circleCenterX = getWidth()/2f;
        circleCenterY = getHeight()/2f;
        circleRadius = 300f;
        float distance = (float) Math.sqrt(Math.pow(px - circleCenterX, 2) + Math.pow(py - circleCenterY, 2));
        if (distance <= circleRadius+(size/2)) {
            if (distance <= circleRadius-(size/2)) {
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean areaTriangle(float px, float py) {

        float size;
        switch(this.difficulty) {
            case "easy":
                size = 100f;
                break;
            case "medium":
                size = 75f;
                break;
            case "hard":
                size = 50f;
                break;
            default :
                size = 100f;
        }//Epaisseur

        // Coordonnées des sommets du triangle
        float x1 = triangleCenterX;
        float y1 = triangleCenterY-(size) - triangleTaille / 2f;  // Sommet supérieur
        float x2 = triangleCenterX-(size) - triangleTaille / 2f;
        float y2 = triangleCenterY+(size/2f) + triangleTaille / 2f;  // Sommet gauche
        float x3 = triangleCenterX+(size) + triangleTaille / 2f;
        float y3 = triangleCenterY+(size/2f) + triangleTaille / 2f;  // Sommet droit

        // Calculer les produits en croix pour chaque côté
        float d1 = (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
        float d2 = (px - x3) * (y2 - y3) - (x2 - x3) * (py - y3);
        float d3 = (px - x1) * (y3 - y1) - (x3 - x1) * (py - y1);

        // Vérifier si tous les produits en croix ont le même signe
        boolean hasNegative = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPositive = (d1 > 0) || (d2 > 0) || (d3 > 0);

        // Le point est dans le triangle si tous les produits en croix ont le même signe ou sont égaux à 0
        if(!(hasNegative && hasPositive)) {
            float x1_2 = triangleCenterX;
            float y1_2 = triangleCenterY+(size) - triangleTaille / 2f;  // Sommet supérieur
            float x2_2 = triangleCenterX+(size) - triangleTaille / 2f;
            float y2_2 = triangleCenterY-(size/2f) + triangleTaille / 2f;  // Sommet gauche
            float x3_2 = triangleCenterX-(size) + triangleTaille / 2f;
            float y3_2 = triangleCenterY-(size/2f) + triangleTaille / 2f;  // Sommet droit

            // Calculer les produits en croix pour chaque côté
            float d1_2 = (px - x2_2) * (y1_2 - y2_2) - (x1_2 - x2_2) * (py - y2_2);
            float d2_2 = (px - x3_2) * (y2_2 - y3_2) - (x2_2 - x3_2) * (py - y3_2);
            float d3_2 = (px - x1_2) * (y3_2 - y1_2) - (x3_2 - x1_2) * (py - y1_2);

            // Vérifier si tous les produits en croix ont le même signe
            boolean hasNegative_2 = (d1_2 < 0) || (d2_2 < 0) || (d3_2 < 0);
            boolean hasPositive_2 = (d1_2 > 0) || (d2_2 > 0) || (d3_2 > 0);

            if(!(hasNegative_2 && hasPositive_2)){return false;}
            else{return true;}
        }

        return false;
    }

    public void setCanvas(String shape, String difficulty){
        this.shape = shape;
        this.difficulty = difficulty;
        //setCursorPosition();
        invalidate(); // Redessiner la vue
    }

    public void clear() {
        path.reset();
        cursor = null;
        invalidate();
        lineTracker.endGame();
    }

    private void setCursorPosition(){
        path.reset();
        switch (this.shape){
            case "Square":
                // Placer le point sur le côté gauche du carré
                posX = squareLeft;
                posY = squareTop + (squareBottom - squareTop) / 2f;
                break;
            case "Circle":
                // Placer le point sur le haut du cercle
                posX = circleCenterX;
                posY = circleCenterY - circleRadius;
                break;
            case "Triangle":
                // Placer le point sur le sommet du triangle
                posX = triangleCenterX;
                posY = triangleCenterY - triangleTaille/2f;
                break;
            default:
                posX = getWidth()/2f;
                posY = getHeight()/2f;
        }
        this.posXStart = posX;
        this.posYStart = posY;
        invalidate();
    }

    public float getScore()
    {
        return (this.scoreIn/this.scoreFull)*100;
    }
}