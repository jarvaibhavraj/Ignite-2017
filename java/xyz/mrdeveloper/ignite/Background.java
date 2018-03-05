package xyz.mrdeveloper.ignite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Vaibhav on 11-02-2017.
 */

public class Background {
    Resources resources;
    Random randomInt;

    public int SCREEN_HEIGHT;
    public int SCREEN_WIDTH;
    public int TEACHER;
    public float UNIT_LENGTH;

    int xPos, yPos;

    Paint paint1, paint2;

    public int size;

    class Explosion {
        public Bitmap image;
        public int xPos, yPos;
        public int speed;
        public int size;
        Rect destination;

        public Explosion() {
            image = BitmapFactory.decodeResource(resources, R.drawable.explosion);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH, SCREEN_WIDTH, true);
            speed = 0;
            xPos = SCREEN_WIDTH / 2;
            yPos = SCREEN_HEIGHT / 2;

            destination = new Rect(xPos - size, yPos - size, xPos + size, yPos + size);
        }

        public void Reset() {
            speed = 0;
            destination = new Rect(xPos - size, yPos - size, xPos + size, yPos + size);
        }
    }

    class Sun {
        public Bitmap image;
        int position;
        int speed;

        public Sun() {
            image = BitmapFactory.decodeResource(resources, R.drawable.sun);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH / 9, SCREEN_WIDTH / 9, true);

            position = 0;
            speed = 0;

        }
    }

    class Grass {
        public Bitmap image;
        public int position;
        public int speed;

        /*
        public final int speed1 = (int) (UNIT_LENGTH * 28 / 16);
        public final int speed2 = (int) (UNIT_LENGTH * 22 / 16);
        */
        public Grass(int id) {
            image = BitmapFactory.decodeResource(resources, id);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH * 50 / 49, SCREEN_WIDTH, true);
            position = 0;
            speed = 0;
        }
    }

    class Fence {
        public Bitmap image;
        public int position;
        public boolean presence;
        public final int speed = (int) (UNIT_LENGTH * 25 / 16);

        public Fence() {
            image = BitmapFactory.decodeResource(resources, R.drawable.fence);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, true);
            position = 0;
            presence = true;
        }
    }

    class Cloud {
        public Bitmap image;
        public int position;
        public int altitude;
        public int speed = (int) (UNIT_LENGTH * 3 / 9);

        public Cloud(int id) {
            image = BitmapFactory.decodeResource(resources, id);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH / 6, SCREEN_WIDTH / 6, true);
            position = SCREEN_WIDTH - 3 * size;
            altitude = 0;
        }
    }

    class Mountain {
        public Bitmap image;
        public int position;
        public int speed = 3 * (int) UNIT_LENGTH;

        public Mountain(int id) {
            image = BitmapFactory.decodeResource(resources, id);
            image = Bitmap.createScaledBitmap(image, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, true);
            position = 0;
        }
    }

    Explosion explosion;
    Sun sun;
    Grass[][] grass = new Grass[2][2];
    Fence[] fence = new Fence[3];
    Mountain[] mountain = new Mountain[3];
    Cloud[] cloud = new Cloud[3];
    Random random = new Random();

    int structFence = 1;                    // Marks Fence Structure, weather it's 1,2 or 3 continuous
    int passedFence = 0;                    // Checks if the constructed fence has passed
    int MountPos = TEACHER + 41 * size / 10;
    //    int sunPos = TEACHER + 51 * size / 10;
    int fencePos = TEACHER + 7 * SCREEN_HEIGHT / 10;


    public Background(Resources res) {
        randomInt = new Random();
        resources = res;

        paint1 = new Paint();
        paint2 = new Paint();

        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;
        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;

        TEACHER = SCREEN_HEIGHT - SCREEN_WIDTH;

        UNIT_LENGTH = SCREEN_WIDTH / 100;

        xPos = 0;
        yPos = 0;

        size = (int) UNIT_LENGTH * 10;

        /** Declaring and creating Objects of Background Elements */

        /** Explosion */
        explosion = new Explosion();

        /** Sun */
        sun = new Sun();
        sun.speed = (int) UNIT_LENGTH * 2 / 24;
        sun.position = SCREEN_WIDTH * 5 / 6;

        /** Grass */
        grass[0][0] = new Grass(R.drawable.grass_1);
        grass[0][1] = new Grass(R.drawable.grass_1);
        grass[1][0] = new Grass(R.drawable.grass_2);
        grass[1][1] = new Grass(R.drawable.grass_2);

        grass[0][0].speed = grass[0][1].speed = (int) (UNIT_LENGTH * 22 / 16);
        grass[1][0].speed = grass[1][1].speed = (int) (UNIT_LENGTH * 28 / 16);

        grass[0][1].position = grass[0][0].position + SCREEN_WIDTH;
        grass[1][1].position = grass[1][0].position + SCREEN_WIDTH;

        /** Fence */
        fence[0] = new Fence();
        fence[1] = new Fence();
        fence[2] = new Fence();

        fence[0].position = 3 * SCREEN_WIDTH;
        fence[1].presence = false;
        fence[2].presence = false;

        /** Mountain */
        mountain[0] = new Mountain(R.drawable.mountain_1);
        mountain[1] = new Mountain(R.drawable.mountain_2);
        mountain[2] = new Mountain(R.drawable.mountain_3);

        mountain[0].position = 3 * SCREEN_WIDTH;
        mountain[1].position = 2 * SCREEN_WIDTH;
        mountain[2].position = 1 * SCREEN_WIDTH;

        mountain[0].speed = (int) (UNIT_LENGTH * 4 / 16);
        mountain[1].speed = (int) (UNIT_LENGTH * 6 / 16);
        mountain[2].speed = (int) (UNIT_LENGTH * 8 / 16);

        /** Cloud */
        cloud[0] = new Cloud(R.drawable.cloud_1);
        cloud[1] = new Cloud(R.drawable.cloud_2);
        cloud[2] = new Cloud(R.drawable.cloud_3);

        cloud[0].speed = (int) (UNIT_LENGTH * 10 / 24);
        cloud[1].speed = (int) (UNIT_LENGTH * 8 / 24);
        cloud[2].speed = (int) (UNIT_LENGTH * 6 / 24);

        cloud[0].position = 1 * SCREEN_WIDTH / 4;
        cloud[1].position = 2 * SCREEN_WIDTH / 4;
        cloud[2].position = 3 * SCREEN_WIDTH / 4;

        cloud[0].altitude = TEACHER + 40 * size / 10;
        cloud[1].altitude = TEACHER + 43 * size / 10;
        cloud[2].altitude = TEACHER + 46 * size / 10;
    }

    public void DrawBackground(Canvas canvas, int dx, int dy) {

        /** Shitty Calculations */
        int sunPos = TEACHER + 46 * size / 10;
        int MountPos = TEACHER + 41 * size / 10;
        int fencePos = TEACHER + 8 * SCREEN_HEIGHT / 10;


        /** Sun */
        canvas.drawBitmap(sun.image, sun.position + dy + yPos, sunPos + dx + xPos, null);

        /** Cloud Draw*/
        canvas.drawBitmap(cloud[0].image, cloud[0].position + dy + yPos, cloud[0].altitude + dx + xPos, null);
        canvas.drawBitmap(cloud[1].image, cloud[1].position + dy + yPos, cloud[1].altitude + dx + xPos, null);
        canvas.drawBitmap(cloud[2].image, cloud[2].position + dy + yPos, cloud[2].altitude + dx + xPos, null);

        /** Mountains Draw */
        canvas.drawBitmap(mountain[0].image, mountain[0].position + dy + yPos, MountPos + dx + xPos, null);
        canvas.drawBitmap(mountain[1].image, mountain[1].position + dy + yPos, MountPos + dx + xPos, null);
        canvas.drawBitmap(mountain[2].image, mountain[2].position + dy + yPos, MountPos + dx + xPos, null);

        /** Grass Draw with Fence*/
        canvas.drawBitmap(grass[1][0].image, grass[0][0].position + dy + yPos, TEACHER + dx + xPos, paint1);
        canvas.drawBitmap(grass[1][1].image, grass[0][1].position + dy + yPos, TEACHER + dx + xPos, paint1);

        canvas.drawBitmap(fence[0].image, fence[0].position + dy + yPos, fencePos + dx + xPos, paint1);
        if (fence[1].presence) {
            canvas.drawBitmap(fence[1].image, fence[1].position + dy + yPos, fencePos + dx + xPos, paint1);
            if (fence[2].presence) {
                canvas.drawBitmap(fence[2].image, fence[2].position + dy + yPos, fencePos + dx + xPos, paint1);
            }
        }

        canvas.drawBitmap(grass[0][0].image, grass[1][0].position + dy + yPos, TEACHER + dx + xPos, paint2);
        canvas.drawBitmap(grass[0][1].image, grass[1][1].position + dy + yPos, TEACHER + dx + xPos, paint2);
        //canvas.drawColor(0x50000000);
    }

    public void DoStuff(double timeStep) {

        /** Grass Control */
        if (grass[0][0].position + SCREEN_WIDTH <= 0)
            grass[0][0].position = grass[0][1].position + SCREEN_WIDTH;
        if (grass[0][1].position + SCREEN_WIDTH <= 0)
            grass[0][1].position = grass[0][0].position + SCREEN_WIDTH;

        if (grass[1][0].position + SCREEN_WIDTH <= 0)
            grass[1][0].position = grass[1][1].position + SCREEN_WIDTH;
        if (grass[1][1].position + SCREEN_WIDTH <= 0)
            grass[1][1].position = grass[1][0].position + SCREEN_WIDTH;


        grass[0][0].position -= grass[0][0].speed * timeStep;
        grass[0][1].position -= grass[0][1].speed * timeStep;
        grass[1][0].position -= grass[1][0].speed * timeStep;
        grass[1][1].position -= grass[1][1].speed * timeStep;

        /** Fence Control */
        int rand = random.nextInt(10);

        if (fence[structFence - 1].position + SCREEN_WIDTH * 2 <= 0)
            fence[0].position = 2 * SCREEN_WIDTH;

        if (passedFence + SCREEN_WIDTH <= 0) {
            structFence = 1;

            if (rand < 6 && rand >= 0) {
                /** 2 continuous fence */

                fence[1].presence = true;
                fence[1].position = fence[0].position + SCREEN_WIDTH / 2;
                passedFence = fence[1].position + SCREEN_WIDTH / 2;
                structFence = 2;

                if (rand < 2 && rand >= 0) {
                        /* 3 continuous Fence */

                    fence[2].presence = true;
                    fence[2].position = fence[1].position + SCREEN_WIDTH / 2;
                    passedFence = fence[2].position + SCREEN_WIDTH / 2;
                    structFence = 3;
                } else
                    fence[2].presence = false;

            } else
                fence[1].presence = false;
        }

        fence[0].position -= fence[0].speed * timeStep;
        fence[1].position -= fence[1].speed * timeStep;
        fence[2].position -= fence[2].speed * timeStep;

        passedFence = fence[0].position + SCREEN_WIDTH / 2 * structFence;

        /** Mountain Control */
        if (mountain[0].position + SCREEN_WIDTH / 2 <= 0) {
            if (random.nextInt(10000) == 0)
                mountain[0].position = SCREEN_WIDTH;
        }
        if (mountain[1].position + SCREEN_WIDTH / 2 <= 0) {
            if (random.nextInt(10000) == 2)
                mountain[1].position = 2 * SCREEN_WIDTH;
        }
        if (mountain[2].position + SCREEN_WIDTH / 2 <= 0) {
            if (random.nextInt(10000) == 3)
                mountain[2].position = 3 * SCREEN_WIDTH;
        }
        mountain[0].position -= mountain[0].speed * timeStep;
        mountain[1].position -= mountain[1].speed * timeStep;
        mountain[2].position -= mountain[2].speed * timeStep;

        /** Cloud Control */
            /*  -- Generation  + Altitude Control */
        if (cloud[0].position + SCREEN_WIDTH / 6 <= 0) {                    //Slowest Cloud
            cloud[0].position = 7 * SCREEN_WIDTH / 6;
            cloud[0].altitude = TEACHER + (40 + random.nextInt(6)) * size / 10;
        }
        if (cloud[1].position + SCREEN_WIDTH / 6 <= 0)
            if (random.nextInt(10) < 4) {
                cloud[1].position = 7 * SCREEN_WIDTH / 6;
                cloud[1].altitude = TEACHER + (40 + random.nextInt(6)) * size / 10;
            }
        if (cloud[2].position + SCREEN_WIDTH / 6 <= 0)                    // Fastest Cloud
            if (random.nextInt(10) < 3) {
                cloud[2].position = 7 * SCREEN_WIDTH / 6;
                cloud[2].altitude = TEACHER + (40 + random.nextInt(6)) * size / 10;
            }
            /*  --position Control */
        cloud[0].position -= cloud[0].speed * timeStep;
        cloud[1].position -= cloud[1].speed * timeStep;
        cloud[2].position -= cloud[2].speed * timeStep;

        /** SUn Control */
        sun.position -= sun.speed * timeStep;
        if (sun.position + SCREEN_WIDTH / 9 < 0)
            sun.position = 10 * SCREEN_WIDTH / 9;
    }

    public void Reset() {
        explosion.Reset();
        paint1.setColorFilter(null);
        paint2.setColorFilter(null);
    }

    public void DoBackgroundEndGame(double timeStep) {
        explosion.speed += 50;
        int explosionSpeed = explosion.speed;
        explosion.destination.right += explosionSpeed;
        explosion.destination.left -= explosionSpeed;
        explosion.destination.bottom += explosionSpeed;
        explosion.destination.top -= explosionSpeed;
    }

    public void DrawBackgroundEndGame(Canvas canvas) {
        canvas.drawBitmap(explosion.image, null, explosion.destination, null);
    }
}