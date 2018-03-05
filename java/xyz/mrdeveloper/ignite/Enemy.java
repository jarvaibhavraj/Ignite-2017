package xyz.mrdeveloper.ignite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * Created by Vaibhav on 11-02-2017.
 */

public class Enemy {
    Resources resources;
    Random randomInt;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public float UNIT_LENGTH;

    public int ENEMY_SIZE;
    public int ENEMY_COLLIDER_X;
    public int ENEMY_COLLIDER_Y;
    public int ENEMY_X_VELOCITY;

    public int size;
    public int colliderSizeY, colliderSizeX;

    public int xPos, yPos;

    float angle = 0;

    Paint paint = new Paint();

    int id;

    Bitmap[] enemyImages = new Bitmap[2];

    public int direction = 0;

    public int wingState = 0;

    float velocityX;
    float velocityY;

    Matrix matrix;

    public Enemy(Resources res) {
        randomInt = new Random();
        resources = res;
        matrix = new Matrix();

        direction = randomInt.nextInt(2);

        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;

        UNIT_LENGTH = SCREEN_HEIGHT / 100;

        ENEMY_SIZE = (int) (10 * UNIT_LENGTH);

        ENEMY_COLLIDER_X = ENEMY_SIZE - 20;
        ENEMY_COLLIDER_Y = ENEMY_SIZE / 2 - 10;

        ENEMY_X_VELOCITY = (int) (4 * UNIT_LENGTH);

        xPos = SCREEN_WIDTH + size;
        yPos = SCREEN_HEIGHT / 2;

        id = -1;
        paint.setARGB(255, randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256));

        size = ENEMY_SIZE;

        colliderSizeX = ENEMY_COLLIDER_X;
        colliderSizeY = ENEMY_COLLIDER_Y;

        velocityX = -ENEMY_X_VELOCITY;
        velocityY = 0;

        yPos = randomInt.nextInt(SCREEN_HEIGHT - 4 * size) + 2 * size;

        SetImage(randomInt.nextInt(6));
    }

    public void SetImage(int i) {

        id = i;

        switch (i) {
            case 0:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_11);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_12);
                break;
            case 1:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_21);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_22);
                break;
            case 2:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_31);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_32);
                break;
            case 3:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_41);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_42);
                break;
            case 4:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_51);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_52);
                break;
            case 5:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_61);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_62);
                break;
            case 6:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_71);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_72);
                break;
            case 7:
                enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.bird_81);
                enemyImages[1] = BitmapFactory.decodeResource(resources, R.drawable.bird_82);
                break;
        }
        enemyImages[0] = Bitmap.createScaledBitmap(enemyImages[0], 2 * size, 2 * size, true);
        enemyImages[1] = Bitmap.createScaledBitmap(enemyImages[1], 2 * size, 2 * size, true);
    }

//        public boolean isColliding(int x, int y) {
//            if ((x > yPos - size) && (x < yPos + size)) {
//                if ((y > xPos - size) && (y < xPos + size)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        public boolean isColliding(int x1, int y1, int x2, int y2) {
//            if (isColliding(x1, y1) || isColliding(x2, y2))
//                return true;
//            return false;
//        }
//
//        public boolean isColliding(Enemy enemy) {
//            if (isColliding(enemy.yPos - enemy.size, enemy.xPos - enemy.size, enemy.yPos + enemy.size, enemy.xPos + enemy.size))
//                return true;
//            return false;
//        }

    public void InitiateFreeFall() {
        velocityY = 0;
//        velocityX = randomInt.nextInt()%velocityX/4;
        velocityX = 0;
    }

    public void Reset() {
        velocityY = 0;
        velocityX = -ENEMY_X_VELOCITY;
    }

    public void MoveEnemy(double timeStamp) {
        xPos += velocityX * timeStamp;
        yPos += velocityY * timeStamp;

        if (id == 7 || id == 6)
            yPos = (int) ((randomInt.nextInt(4) + 1) * 5 * Math.sin(0.005 * xPos)) + yPos;

        if (id == 4)
            velocityX = - (float) (ENEMY_X_VELOCITY * 1.5);
    }

    public void DrawEnemy(Canvas canvas, int dx, int dy) {
        canvas.drawBitmap(enemyImages[wingState], xPos - size + dx, yPos - size + dy, paint);
//        canvas.drawRect(xPos - colliderSizeX + dx, yPos - colliderSizeY + dy,
//                xPos + colliderSizeX + 2 * dx, yPos + colliderSizeY + 2 * dy, paint1);
    }

    public void MoveEnemyEndGame(double timeStamp) {
        xPos += velocityX * timeStamp;
        yPos += velocityY * timeStamp;

        velocityY += 0.7;
        angle += 1;

//        Log.i("debug", " YYYYYYYYYYYYYY" + yPos);

        if (yPos > SCREEN_HEIGHT - size / 2) {
//            yPos -= velocityY * timeStamp;
            velocityX = 0;
            velocityY = 0;
            angle -= 1;
        }
    }

    public void DrawEnemyEndGame(Canvas canvas) {

        matrix.reset();
        matrix.postTranslate(-size / 2, -size / 2);
        if (direction == 1)
            matrix.postRotate(angle);
        else
            matrix.postRotate(-angle);
        matrix.postTranslate(xPos - size / 2, yPos - size / 2);

        canvas.drawBitmap(enemyImages[0], matrix, paint);
    }
}