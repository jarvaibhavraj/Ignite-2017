package xyz.mrdeveloper.ignite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import java.util.Random;

/**
 * Created by Vaibhav on 11-02-2017.
 */

public class Player {
    Resources resources;
    Random randomInt;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public float UNIT_LENGTH;

    public int PLAYER_SIZE;
    public int PLAYER_COLLIDER_X;
    public int PLAYER_COLLIDER_Y;
    public int PLAYER_X_VELOCITY;
    public int PLAYER_Y_VELOCITY;
    public double PLAYER_VELOCITY_MODIFIER = 0.5;

    float angle = 0;

    public int size;
    public int colliderSizeX1, colliderSizeX2;
    public int colliderSizeY1, colliderSizeY2;
    public int xPos, yPos;

    Paint paint = new Paint();

    Bitmap[] playerImages = new Bitmap[3];

    public int direction = 0;

    float velocityX;
    float velocityY;

    Matrix matrix;

    int MAX_VELOCITYX;
    int MAX_VELOCITYY;

    public Player(Resources res) {

        matrix = new Matrix();

        randomInt = new Random();
        resources = res;

        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;

        UNIT_LENGTH = SCREEN_HEIGHT / 100;

        PLAYER_SIZE = (int) (10 * UNIT_LENGTH);

        PLAYER_COLLIDER_X = PLAYER_SIZE - 30;
        PLAYER_COLLIDER_Y = PLAYER_SIZE / 3 - 10;

        PLAYER_X_VELOCITY = (int) (5 * UNIT_LENGTH);
        PLAYER_Y_VELOCITY = (int) (5 * UNIT_LENGTH);

        MAX_VELOCITYX = PLAYER_Y_VELOCITY;
        MAX_VELOCITYY = PLAYER_X_VELOCITY;


        size = PLAYER_SIZE;

        xPos = 4 * size;
        yPos = SCREEN_WIDTH / 2;

        paint = new Paint();

        colliderSizeX1 = PLAYER_COLLIDER_X;
        colliderSizeX2 = PLAYER_COLLIDER_X;

        colliderSizeY1 = PLAYER_COLLIDER_Y;
        colliderSizeY2 = PLAYER_COLLIDER_Y;

        velocityX = 0;
        velocityY = 0;

        paint.setARGB(255, randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256));

//            playerImages[0] = BitmapFactory.decodeResource(resources, R.drawable.paper_plane41);
        playerImages[1] = BitmapFactory.decodeResource(resources, R.drawable.paper_plane45);
//            playerImages[2] = BitmapFactory.decodeResource(resources, R.drawable.paper_plane43);
//            playerImages[0] = Bitmap.createScaledBitmap(playerImages[0], 2 * size, 2 * size, true);
        playerImages[1] = Bitmap.createScaledBitmap(playerImages[1], 2 * size, 2 * size, true);
//        paint1.setColor(Color.DKGRAY);
        paint.setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
//            playerImages[2] = Bitmap.createScaledBitmap(playerImages[2], 2 * size, 2 * size, true);
    }

//        public boolean isColliding(int x, int y) {
//            if ((x > yPos - colliderSizeX) && (x < yPos + colliderSizeX)) {
//                if ((y > xPos - colliderSizeY) && (y < xPos + colliderSizeY)) {
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

    public void ResolveDirection() {
        if (velocityY > 0) {
            direction = -1;
        } else if (velocityY < 0) {
            direction = 1;
        } else {
            direction = 0;
        }
    }

    public void InitiateFreeFall() {
        velocityY = 0;
        velocityX /= 8;
    }

    public void Reset() {
        velocityY = 0;
        velocityX = 0;
    }

    public void MovePlayer(double timeStamp) {
        if (velocityX > MAX_VELOCITYX) {
            velocityX = MAX_VELOCITYX;
        } else if (velocityX < -MAX_VELOCITYX) {
            velocityX = -MAX_VELOCITYX;
        }

        if (velocityY > MAX_VELOCITYY) {
            velocityY = MAX_VELOCITYY;
        } else if (velocityY < -MAX_VELOCITYY) {
            velocityY = -MAX_VELOCITYY;
        }

        xPos += velocityX * timeStamp;
        yPos += velocityY * timeStamp;

        if (xPos < size || xPos > SCREEN_WIDTH - size) {
            xPos -= velocityX * timeStamp;
        }
        if (yPos < size / 2 || yPos > SCREEN_HEIGHT - size / 2) {
            yPos -= velocityY * timeStamp;
        }

        if (direction == -1) {
            colliderSizeY2 = PLAYER_COLLIDER_Y * 3 / 2;
            colliderSizeY1 = PLAYER_COLLIDER_Y / 2;
        } else if (direction == 1) {
            colliderSizeY1 = PLAYER_COLLIDER_Y * 3 / 2;
            colliderSizeY2 = PLAYER_COLLIDER_Y / 2;
        } else {
            colliderSizeY1 = PLAYER_COLLIDER_Y;
            colliderSizeY2 = PLAYER_COLLIDER_Y;
        }
    }

    public void DrawPlayer(Canvas canvas) {
//            canvas.drawBitmap(playerImages[direction + 1], yPos - size, xPos - size, null);

//        matrix.reset();
//        matrix.postTranslate(-size / 2, -size / 2);
//        if (direction == 1)
//            matrix.postRotate(-direction * 2);
//        else if (direction == -1)
//            matrix.postRotate(-direction * 4);
//        matrix.postTranslate(xPos - size / 2, yPos - size / 2);

//        canvas.drawBitmap(playerImages[1], matrix, null);
//        canvas.drawRect(xPos - colliderSizeX1, yPos - colliderSizeY1, xPos + colliderSizeX2, yPos + colliderSizeY2, paint1);

        canvas.drawBitmap(playerImages[1], xPos - size, yPos - size, null);
    }

    public void MovePlayerEndGame(double timeStamp) {
        xPos += velocityX;
        yPos += velocityY;

        angle += 1;
        velocityY += 0.7;

        if (yPos > SCREEN_HEIGHT - size / 2) {
//            yPos -= velocityY * timeStamp;
            velocityX = 0;
            velocityY = 0;
            angle -= 1;
        }
    }

    public void DrawPlayerEndGame(Canvas canvas) {

        matrix.reset();
        matrix.postTranslate(-size / 2, -size / 2);
        if (direction == 1)
            matrix.postRotate(angle);
        else
            matrix.postRotate(-angle);
        matrix.postTranslate(xPos - size / 2, yPos - size / 2);

        canvas.drawBitmap(playerImages[1], matrix, null);
    }
}
