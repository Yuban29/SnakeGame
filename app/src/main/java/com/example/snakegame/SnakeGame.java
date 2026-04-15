package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private boolean isGameOver = false;

    // Puntuación
    private int score = 0;
    private int highScore = 0;

    // Dimensiones del tablero
    private int numBlocksWide = 20;
    private int numBlocksHigh;
    private int blockSize;
    private int screenWidth;
    private int screenHeight;

    // Serpiente
    private ArrayList<Point> snakeBody;
    private int directionX = 1; // 1 = derecha, -1 = izquierda
    private int directionY = 0; // 1 = abajo, -1 = arriba

    // Comida
    private Point food;
    private Random random;

    // Control táctil
    private float touchStartX;
    private float touchStartY;

    // Paint
    private Paint snakePaint;
    private Paint foodPaint;
    private Paint bgPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private Paint headPaint;

    // Velocidad (ms por frame)
    private long FPS = 10;
    private long nextFrameTime;

    // Constructor
    public SnakeGame(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        blockSize = screenWidth / numBlocksWide;
        numBlocksHigh = screenHeight / blockSize;

        random = new Random();
        initPaints();
        initGame();
    }

    private void initPaints() {
        snakePaint = new Paint();
        snakePaint.setColor(Color.parseColor("#00CC00"));
        snakePaint.setStyle(Paint.Style.FILL);

        headPaint = new Paint();
        headPaint.setColor(Color.parseColor("#00FF00"));
        headPaint.setStyle(Paint.Style.FILL);

        foodPaint = new Paint();
        foodPaint.setColor(Color.parseColor("#FF3333"));
        foodPaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#0A0A0A"));

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#111111"));
        gridPaint.setStrokeWidth(1);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
    }

    private void initGame() {
        snakeBody = new ArrayList<>();
        // Inicializar serpiente en el centro
        int startX = numBlocksWide / 2;
        int startY = numBlocksHigh / 2;
        snakeBody.add(new Point(startX, startY));
        snakeBody.add(new Point(startX - 1, startY));
        snakeBody.add(new Point(startX - 2, startY));

        directionX = 1;
        directionY = 0;

        score = 0;
        isGameOver = false;

        spawnFood();
        nextFrameTime = System.currentTimeMillis();
    }

    private void spawnFood() {
        boolean validPosition = false;
        int fx = 0, fy = 0;

        while (!validPosition) {
            fx = random.nextInt(numBlocksWide);
            fy = random.nextInt(numBlocksHigh - 3) + 2; // evitar zona de puntuación
            validPosition = true;

            for (Point p : snakeBody) {
                if (p.x == fx && p.y == fy) {
                    validPosition = false;
                    break;
                }
            }
        }
        food = new Point(fx, fy);
    }

    @Override
    public void run() {
        while (isPlaying) {
            long currentTime = System.currentTimeMillis();
            if (currentTime > nextFrameTime && !isGameOver) {
                update();
                nextFrameTime = currentTime + (1000 / FPS);
            }
            draw();

            // Pequeña pausa para no saturar el CPU
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (snakeBody.isEmpty()) return;

        // Mover el cuerpo (cada segmento sigue al anterior)
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).set(snakeBody.get(i - 1).x, snakeBody.get(i - 1).y);
        }

        // Mover la cabeza
        Point head = snakeBody.get(0);
        head.x += directionX;
        head.y += directionY;

        // Verificar colisión con paredes
        if (head.x < 0 || head.x >= numBlocksWide || head.y < 0 || head.y >= numBlocksHigh) {
            isGameOver = true;
            if (score > highScore) highScore = score;
            return;
        }

        // Verificar colisión consigo misma
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.x == snakeBody.get(i).x && head.y == snakeBody.get(i).y) {
                isGameOver = true;
                if (score > highScore) highScore = score;
                return;
            }
        }

        // Verificar si comió la comida
        if (head.x == food.x && head.y == food.y) {
            score += 10;
            // Aumentar velocidad progresivamente
            if (score % 50 == 0 && FPS < 25) {
                FPS++;
            }

            // Agregar nuevo segmento al final
            Point last = snakeBody.get(snakeBody.size() - 1);
            snakeBody.add(new Point(last.x, last.y));

            spawnFood();
        }
    }

    private void draw() {
        if (!getHolder().getSurface().isValid()) return;

        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) return;

        try {
            // Fondo
            canvas.drawRect(0, 0, screenWidth, screenHeight, bgPaint);

            // Dibujar cuadrícula
            for (int i = 0; i < numBlocksWide; i++) {
                canvas.drawLine(i * blockSize, 0, i * blockSize, screenHeight, gridPaint);
            }
            for (int j = 0; j < numBlocksHigh; j++) {
                canvas.drawLine(0, j * blockSize, screenWidth, j * blockSize, gridPaint);
            }

            // Dibujar comida (círculo)
            if (food != null) {
                float margin = blockSize * 0.1f;
                canvas.drawOval(
                        food.x * blockSize + margin,
                        food.y * blockSize + margin,
                        (food.x + 1) * blockSize - margin,
                        (food.y + 1) * blockSize - margin,
                        foodPaint
                );
            }

            // Dibujar serpiente
            for (int i = 0; i < snakeBody.size(); i++) {
                Point p = snakeBody.get(i);
                float margin = blockSize * 0.05f;
                Paint paint = (i == 0) ? headPaint : snakePaint;
                canvas.drawRoundRect(
                        p.x * blockSize + margin,
                        p.y * blockSize + margin,
                        (p.x + 1) * blockSize - margin,
                        (p.y + 1) * blockSize - margin,
                        8, 8, paint
                );
            }

            // Dibujar puntuación
            textPaint.setTextSize(50);
            canvas.drawText("Score: " + score, 20, 60, textPaint);
            canvas.drawText("Best: " + highScore, screenWidth / 2f, 60, textPaint);

            // Pantalla de Game Over
            if (isGameOver) {
                Paint overlay = new Paint();
                overlay.setColor(Color.argb(180, 0, 0, 0));
                canvas.drawRect(0, 0, screenWidth, screenHeight, overlay);

                textPaint.setTextSize(100);
                textPaint.setColor(Color.RED);
                float textWidth = textPaint.measureText("GAME OVER");
                canvas.drawText("GAME OVER", (screenWidth - textWidth) / 2f, screenHeight / 2f - 60, textPaint);

                textPaint.setTextSize(55);
                textPaint.setColor(Color.WHITE);
                String scoreText = "Puntuación: " + score;
                float sw = textPaint.measureText(scoreText);
                canvas.drawText(scoreText, (screenWidth - sw) / 2f, screenHeight / 2f + 20, textPaint);

                textPaint.setTextSize(45);
                textPaint.setColor(Color.YELLOW);
                String restartText = "Toca para reiniciar";
                float rw = textPaint.measureText(restartText);
                canvas.drawText(restartText, (screenWidth - rw) / 2f, screenHeight / 2f + 100, textPaint);

                textPaint.setColor(Color.WHITE);
            }

        } finally {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getX();
                touchStartY = event.getY();

                if (isGameOver) {
                    initGame();
                }
                break;

            case MotionEvent.ACTION_UP:
                float dx = event.getX() - touchStartX;
                float dy = event.getY() - touchStartY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    // Movimiento horizontal
                    if (dx > 0 && directionX != -1) {
                        directionX = 1;
                        directionY = 0;
                    } else if (dx < 0 && directionX != 1) {
                        directionX = -1;
                        directionY = 0;
                    }
                } else {
                    // Movimiento vertical
                    if (dy > 0 && directionY != -1) {
                        directionX = 0;
                        directionY = 1;
                    } else if (dy < 0 && directionY != 1) {
                        directionX = 0;
                        directionY = -1;
                    }
                }
                break;
        }
        return true;
    }

    public void pause() {
        isPlaying = false;
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
}
