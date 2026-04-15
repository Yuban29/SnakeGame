package com.example.snakegame;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SnakeGame snakeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mantener pantalla encendida durante el juego
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Obtener dimensiones reales de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // Inicializar el juego con las dimensiones de la pantalla
        snakeGame = new SnakeGame(this, screenWidth, screenHeight);
        setContentView(snakeGame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeGame.pause();
    }
}
