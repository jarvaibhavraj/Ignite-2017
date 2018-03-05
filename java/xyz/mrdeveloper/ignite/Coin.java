package xyz.mrdeveloper.ignite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by Vaibhav on 11-02-2017.
 */

public class Coin {
    Resources resources;
    Random randomInt;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public float UNIT_LENGTH = SCREEN_HEIGHT / 100;

    public int COIN_SIZE = (int) (10 * UNIT_LENGTH);
    public int COIN_COLLIDER_X = COIN_SIZE - 15;
    public int COIN_COLLIDER_Y = COIN_SIZE / 3 - 5;

    public int size;
    public int colliderSizeX, colliderSizeY;
    public int xPos, yPos;

    public int velocityX, velocityY;

    Paint paint = new Paint();

    Bitmap coinImage;

    Rect source;
    Rect destination;

    boolean isActive;

    int state;

    public Coin(Resources res) {
        randomInt = new Random();
        resources = res;

        isActive = true;

        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;

        UNIT_LENGTH = SCREEN_HEIGHT / 100;

        COIN_SIZE = (int) (5 * UNIT_LENGTH);
        COIN_COLLIDER_X = COIN_SIZE / 2;
        COIN_COLLIDER_Y = COIN_SIZE;

        xPos = randomInt.nextInt(SCREEN_WIDTH - 5 * size) + 4 * size;
        yPos = randomInt.nextInt(SCREEN_HEIGHT - 2 * size) + size;

        size = COIN_SIZE;

        velocityX = -size / 10;
        velocityY = 0;

        state = 0;
        destination = new Rect(xPos - size, yPos - size, xPos + size, yPos + size);
        source = new Rect(state * size * 2, 0, (state + 1) * size * 2, size * 2);

        colliderSizeX = COIN_COLLIDER_X;
        colliderSizeY = COIN_COLLIDER_Y;

        coinImage = BitmapFactory.decodeResource(resources, R.drawable.coin_spin);
        coinImage = Bitmap.createScaledBitmap(coinImage, 14 * size, 2 * size, true);
    }

    public void RandomizePosition() {
        xPos = randomInt.nextInt(SCREEN_WIDTH - 5 * size) + 4 * size;
        yPos = randomInt.nextInt(SCREEN_HEIGHT - 2 * size) + size;

        destination = new Rect(xPos - size, yPos - size, xPos + size, yPos + size);
    }

    public void MoveCoin(double timeStamp) {
        xPos += velocityX;
        yPos += velocityY;

        destination = new Rect(xPos - size, yPos - size, xPos + size, yPos + size);
    }

    public void DrawCoin(Canvas canvas) {
        state %= 7;
        source.left = state * size * 2;
        source.right = (state + 1) * size * 2;
//        canvas.drawBitmap(coinImage, xPos - size, yPos - size, null);
        canvas.drawBitmap(coinImage, source, destination, null);

//        Log.i("debug", "COIN COIN COIN");
    }
}
