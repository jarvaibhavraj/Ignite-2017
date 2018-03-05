package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.isRegistered;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.leaderboard;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.rank;

/**
 * Created by Vaibhav on 24-01-2017.
 */

public class ThingsWithWings extends SurfaceView implements SurfaceHolder.Callback {

    private static final boolean GOD_MODE = false;
    public Resources resources;

    public final int BIRDIE_TIME_CHANCE = 50;

    public boolean playerVisible;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public boolean MUSIC_PLAYING;
    public boolean SOUND_PLAYING;

    public float UNIT_LENGTH;
    public final int DELTA_TIME = 25;
    public final int DELTA_TIME_DRAW = 0;

    public double PLAYER_VELOCITY_MODIFIER = 0.8;
    private int ENEMY_MIN_Y;

    double speedEnemy;
    double speedEnemyIncrement = 0.02;

    public boolean paused;
    public boolean gameEnd;

    Enemy fatalEnemy;
    Paint lastMessagePaint;
    String lastMessage;
    float scale;

    double timeStep = 1;

    public View gamePauseMenu;
    public TextView gamePauseMenuRank;
    public TextView gamePauseMenuHighScore;
    public Button pauseButton;

    public ImageView triviaImage;
    public TextView triviaText;
    public TextView textScore;

    public View scoreBoard;
    public View newHighScoreStamp;
    public TextView scoreBoardCurScore;
    public TextView scoreBoardHighestScore;
    public TextView scoreBoardRank;
    public View scoreBoardNext;
    public TextView scoreBoardPunchline;

    SoundPool soundPlayer;

    MediaPlayer backgroundMusic;

    int soundIds[] = new int[10];

    private SurfaceHolder holder;

    private MyThread myThread;
    private MyThread2 myThread2;

    Background currentBackground;

    Player currentPlayer;

    Coin currentCoin;

//    Enemy currentEnemy;

    ArrayList<Enemy> enemyArrayList = new ArrayList<>();

    Runnable updateScore;
    Runnable showPauseScreen;
    Runnable showScoreBoard;

    Random randomInt;

//    public BlurringView mBlurringView;

    long lastUpdated;
    long lastDrawUpdated;
    long lastPlayerUpdated;

    int playerScore;
    int playerHighestScore;
    int playerRank;

    int enemiesOnScreen;

    boolean itsTime;
    long waitTime;

    long coinTime;

    long endGameTime;

    boolean superPlaneGodMode;
    boolean playerInControl;
    boolean soundPlaying;
    boolean musicPlaying;

    boolean warningSoundPlaying;

    int totalEnemies;

    float distY;
    float distX;
//    public final float UNIT_LENGTH = resources.getDisplayMetrics().density;

    public ThingsWithWings(Context context) {
        super(context);
        ThingsWithWingsConstructor();
    }

    public ThingsWithWings(Context context, AttributeSet attrs) {
        super(context, attrs);
        ThingsWithWingsConstructor();
    }

    public ThingsWithWings(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThingsWithWingsConstructor();
    }

    public void ThingsWithWingsConstructor() {
        randomInt = new Random();
        resources = getResources();

        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;

        UNIT_LENGTH = SCREEN_HEIGHT / 100;

        ENEMY_MIN_Y = (int) (-13.5 * UNIT_LENGTH);

        lastUpdated = System.currentTimeMillis();
        lastPlayerUpdated = lastUpdated;
        lastDrawUpdated = lastUpdated;

        holder = getHolder();
        holder.addCallback(this);

        superPlaneGodMode = GOD_MODE;
        playerInControl = true;
        playerVisible = true;

        timeStep = 1;

        speedEnemy = (int) (4 * UNIT_LENGTH);

        enemiesOnScreen = 0;

        itsTime = false;
        waitTime = 0;

        warningSoundPlaying = false;

        totalEnemies = 5;

        distX = 0;
        distY = 0;

        paused = false;
        gameEnd = false;

        playerScore = 0;

        lastMessagePaint = new Paint();
        scale = getResources().getDisplayMetrics().density;

        currentBackground = new Background(resources);

        currentPlayer = new Player(resources);

        currentCoin = new Coin(resources);

//        coinRemainTime = (randomInt.nextInt(4) * 2000) + 5000;
        coinTime = (randomInt.nextInt(4) * 2000) + 3000;

//        currentPlayer = null;
//        currentEnemy = new Enemy();
        for (int i = 0; i < totalEnemies; i++) {
            enemyArrayList.add(new Enemy(resources));
            enemyArrayList.get(i).xPos = SCREEN_WIDTH + (int) ((i + 1) * 60 * UNIT_LENGTH);
            enemyArrayList.get(i).yPos = (randomInt.nextInt(9) + 1) * enemyArrayList.get(i).size;
            enemiesOnScreen++;
        }

        soundPlayer = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundIds[0] = soundPlayer.load(getContext(), R.raw.warning, 1);
        soundIds[1] = soundPlayer.load(getContext(), R.raw.coin_appear, 1);
        soundIds[2] = soundPlayer.load(getContext(), R.raw.coin_collected, 1);
        soundIds[3] = soundPlayer.load(getContext(), R.raw.explosion, 1);

        backgroundMusic = MediaPlayer.create(getContext(), R.raw.back);
        backgroundMusic.setLooping(true);

        backgroundMusic.start();

        if (!musicPlaying)
            backgroundMusic.pause();

        updateScore = new Runnable() {
            @Override
            public void run() {
                textScore.setText(String.valueOf(playerScore));

            }
        };

        showScoreBoard = new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getContext().getSharedPreferences("PlayerScore", MODE_PRIVATE);
                String storedPlayerScore = pref.getString("PlayerScore", null);

                if (storedPlayerScore == null) {
                    storedPlayerScore = "0";
                }

                if (playerScore > Integer.parseInt(storedPlayerScore)) {
                    newHighScoreStamp.setVisibility(VISIBLE);
                    getContext().getSharedPreferences("PlayerScore", MODE_PRIVATE)
                            .edit()
                            .putString("PlayerScore", Integer.toString(playerScore))
                            .apply();

                    playerHighestScore = playerScore;

                    newHighScoreStamp.setVisibility(VISIBLE);

                    pref = getContext().getSharedPreferences("PlayerDetails", MODE_PRIVATE);
                    String playerDetailsInSP = pref.getString("PlayerDetails", null);

                    if (playerDetailsInSP != null) {
                        String[] playerDetails = playerDetailsInSP.split(",");
                        final DatabaseReference mFirebaseDatabase;
                        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

                        // get reference to 'users' node
                        mFirebaseDatabase = mFirebaseInstance.getReference("leaderboard");
                        mFirebaseDatabase.child(playerDetails[1]).child("score").setValue(playerScore);
                    }
                } else {
                    newHighScoreStamp.setVisibility(GONE);
                    playerHighestScore = Integer.parseInt(storedPlayerScore);
                }
                scoreBoardHighestScore.setText(String.valueOf(playerHighestScore));


                if (isRegistered) {
                    CalculateRank();
                    scoreBoardRank.setText(String.valueOf(rank));
                } else {
                    scoreBoardRank.setText("-");
                }

                Log.i("debug", "SHOWING SCORE BOARD");

                if (playerScore <= 2) {
                    switch (randomInt.nextInt(3)) {
                        case 0:
                            scoreBoardPunchline.setText("Duck Duck Go");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("You laid an egg");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("DODGE the birds, don't HIT them");
                            break;
                    }
                } else if (playerScore <= 15) {
                    switch (randomInt.nextInt(4)) {
                        case 0:
                            scoreBoardPunchline.setText("You have to look at the screen");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("Even a chicken lays more eggs");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("Were you even trying?");
                            break;
                        case 3:
                            scoreBoardPunchline.setText("My grandma scores better with her left hand");
                            break;
                    }
                } else if (playerScore <= 50) {
                    switch (randomInt.nextInt(4)) {
                        case 0:
                            scoreBoardPunchline.setText("Come on. I know you can do better");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("Keep Going");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("And that's how much I got in my board exams");
                            break;
                    }
                } else if (playerScore <= 100) {
                    switch (randomInt.nextInt(3)) {
                        case 0:
                            scoreBoardPunchline.setText("Way to go. Woo hoo");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("Good going");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("Come on dude, sky is not the limit");
                            break;
                    }
                } else if (playerScore <= 150) {
                    switch (randomInt.nextInt(3)) {
                        case 0:
                            scoreBoardPunchline.setText("The birds have started whispering");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("BOSS LEVEL UNLOCKED!");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("You've got way too much free time");
                            break;
                    }
                } else {
                    switch (randomInt.nextInt(3)) {
                        case 0:
                            scoreBoardPunchline.setText("You've got skills man!");
                            break;
                        case 1:
                            scoreBoardPunchline.setText("You broke my game dude!");
                            break;
                        case 2:
                            scoreBoardPunchline.setText("ULTRA BOSS LEVEL UNLOCKED!");
                            break;
                    }
                }

                scoreBoard.setVisibility(VISIBLE);
                scoreBoardCurScore.setText(String.valueOf(playerScore));
                scoreBoardHighestScore.setText(String.valueOf(playerHighestScore));
//                scoreBoardRank.setText(String.valueOf(playerRank));

                gamePauseMenuRank.setText(String.valueOf(rank));
                gamePauseMenuHighScore.setText(String.valueOf(playerHighestScore));

                pauseButton.setVisibility(GONE);
                textScore.setVisibility(GONE);
            }
        };

        showPauseScreen = new Runnable() {
            @Override
            public void run() {
                gamePauseMenu.setVisibility(VISIBLE);
                pauseButton.setVisibility(GONE);
            }
        };

//        mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
//        mBlurringView.setBlurredView(this);
    }

    public void CalculateRank() {
        SharedPreferences pref = getContext().getSharedPreferences("PlayerDetails", MODE_PRIVATE);
        String playerDetailsInSP = pref.getString("PlayerDetails", null);

        if (playerDetailsInSP == null) {
            isRegistered = false;
        } else {
            isRegistered = true;
            String[] playerDetails;
            playerDetails = playerDetailsInSP.split(",");
            String playerSAPID = playerDetails[1];

            // get reference to 'users' node
            if (leaderboard != null)
                for (int i = 0; i < leaderboard.size(); ++i) {
                    Log.d("Check", "hehe Score: " + leaderboard.get(i).score);
                    Log.d("Check", "hehe Name: " + leaderboard.get(i).name);
                    Log.d("Check", "hehe LeaderBoard: " + leaderboard.size() + "  " + leaderboard.get(i).sapid + "  " + playerSAPID);
                    if (leaderboard.get(i).sapid.equals(playerSAPID)) {
                        rank = i + 1;
                        break;
                    }
                }
            else {
                Log.d("debug", "NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
            }
        }
    }

    boolean Colliding(Enemy a, Player b) {
        return (2 * (Math.abs(a.xPos - b.xPos)) <= (2 * a.colliderSizeX + b.colliderSizeX1 + b.colliderSizeX2) &&
                2 * (Math.abs(a.yPos - b.yPos)) <= (2 * a.colliderSizeY + b.colliderSizeY1 + b.colliderSizeY2));
    }

    boolean Colliding(Coin a, Player b) {
        return (2 * (Math.abs(a.xPos - b.xPos)) <= (2 * a.colliderSizeX + b.colliderSizeX1 + b.colliderSizeX2) &&
                2 * (Math.abs(a.yPos - b.yPos)) <= (2 * a.colliderSizeY + b.colliderSizeY1 + b.colliderSizeY2));
    }

    enum Formation {V_FORM, INVERSE_V_FORM, FENCE, BACK_SLASH, SLASH, X_FORM, ARROW, BLOCK}

    public void SetupEnemies(Formation formation) {
        int xFormation, yFormation, space;
        space = 10 * (int) UNIT_LENGTH;
        xFormation = SCREEN_WIDTH + space;
        yFormation = SCREEN_HEIGHT / 2;
        switch (formation) {

            case V_FORM:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(2) + 6);

                enemyArrayList.get(0).yPos = yFormation - 4 * space;
                enemyArrayList.get(1).yPos = yFormation - 2 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation + 2 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation + 4 * space;
                enemyArrayList.get(1).xPos = xFormation + 2 * space;
                enemyArrayList.get(2).xPos = xFormation + 0 * space;
                enemyArrayList.get(3).xPos = xFormation + 2 * space;
                enemyArrayList.get(4).xPos = xFormation + 4 * space;
                break;

            case INVERSE_V_FORM:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(2) + 6);

                enemyArrayList.get(0).yPos = yFormation - 4 * space;
                enemyArrayList.get(1).yPos = yFormation - 2 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation + 2 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation + 0 * space;
                enemyArrayList.get(1).xPos = xFormation + 2 * space;
                enemyArrayList.get(2).xPos = xFormation + 4 * space;
                enemyArrayList.get(3).xPos = xFormation + 2 * space;
                enemyArrayList.get(4).xPos = xFormation + 0 * space;
                break;

            case FENCE:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(3).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));

                enemyArrayList.get(0).yPos = yFormation - 5 * space;
                enemyArrayList.get(1).yPos = yFormation - 3 * space;
                enemyArrayList.get(2).yPos = yFormation - 1 * space;
                enemyArrayList.get(3).yPos = yFormation + 2 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation;
                enemyArrayList.get(1).xPos = xFormation;
                enemyArrayList.get(2).xPos = xFormation;
                enemyArrayList.get(3).xPos = xFormation + 8 * space;
                enemyArrayList.get(4).xPos = xFormation + 8 * space;
                break;

            case BLOCK:
                yFormation += (randomInt.nextInt(3) - 1) * 3 * space;
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));

                enemyArrayList.get(0).yPos = (int) (yFormation - 2.5 * space);
                enemyArrayList.get(1).yPos = (int) (yFormation - 1.2 * space);
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = (int) (yFormation + 1.2 * space);
                enemyArrayList.get(4).yPos = (int) (yFormation + 2.5 * space);

                enemyArrayList.get(0).xPos = xFormation - 4;
                enemyArrayList.get(1).xPos = xFormation - 2;
                enemyArrayList.get(2).xPos = xFormation;
                enemyArrayList.get(3).xPos = xFormation + 2;
                enemyArrayList.get(4).xPos = xFormation + 4;
                break;

            case SLASH:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));

                enemyArrayList.get(0).yPos = yFormation - 4 * space;
                enemyArrayList.get(1).yPos = yFormation - 2 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation + 2 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation + 8 * space;
                enemyArrayList.get(1).xPos = xFormation + 6 * space;
                enemyArrayList.get(2).xPos = xFormation + 4 * space;
                enemyArrayList.get(3).xPos = xFormation + 2 * space;
                enemyArrayList.get(4).xPos = xFormation + 0 * space;
                break;

            case BACK_SLASH:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(2).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));

                enemyArrayList.get(0).yPos = yFormation - 4 * space;
                enemyArrayList.get(1).yPos = yFormation - 2 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation + 2 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation + 0 * space;
                enemyArrayList.get(1).xPos = xFormation + 2 * space;
                enemyArrayList.get(2).xPos = xFormation + 4 * space;
                enemyArrayList.get(3).xPos = xFormation + 6 * space;
                enemyArrayList.get(4).xPos = xFormation + 8 * space;
                break;

            case X_FORM:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(0).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(1).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(2).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(2) + 6);

                enemyArrayList.get(0).yPos = yFormation - 4 * space;
                enemyArrayList.get(1).yPos = yFormation - 4 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation + 4 * space;
                enemyArrayList.get(4).yPos = yFormation + 4 * space;

                enemyArrayList.get(0).xPos = xFormation + 0 * space;
                enemyArrayList.get(1).xPos = xFormation + 4 * space;
                enemyArrayList.get(2).xPos = xFormation + 2 * space;
                enemyArrayList.get(3).xPos = xFormation + 4 * space;
                enemyArrayList.get(4).xPos = xFormation + 0 * space;
                break;

            case ARROW:
                enemyArrayList.get(0).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(1).SetImage(randomInt.nextInt(2) + 6);
                enemyArrayList.get(2).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(3).SetImage(randomInt.nextInt(6));
                enemyArrayList.get(4).SetImage(randomInt.nextInt(6));

                enemyArrayList.get(0).yPos = yFormation - 0 * space;
                enemyArrayList.get(1).yPos = yFormation - 0 * space;
                enemyArrayList.get(2).yPos = yFormation - 0 * space;
                enemyArrayList.get(3).yPos = yFormation - 3 * space;
                enemyArrayList.get(4).yPos = yFormation + 3 * space;

                enemyArrayList.get(0).xPos = xFormation + 0 * space;
                enemyArrayList.get(1).xPos = xFormation + 4 * space;
                enemyArrayList.get(2).xPos = xFormation + 8 * space;
                enemyArrayList.get(3).xPos = xFormation + 2 * space;
                enemyArrayList.get(4).xPos = xFormation + 2 * space;
                break;
        }
    }

    float prevX = 0;
    float prevY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (playerInControl && !paused) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                distX = event.getX() - currentPlayer.xPos;
                distY = event.getY() - currentPlayer.yPos;

                prevX = event.getX();
                prevY = event.getY();

//            currentPlayer.velocityY = 20;
//            currentPlayer.velocityY = (int) (event.getY() - currentPlayer.yPos) / 10;
//            currentPlayer.velocityX = (int) (event.getX() - currentPlayer.xPos) / 10;
//            currentPlayer.ResolveDirection();
                return true;

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

//                if (Math.abs(event.getX() - prevX) > 10 && Math.abs(event.getY() - prevY) > 10) {

                if (currentPlayer.xPos > event.getX() - 5 && currentPlayer.xPos < event.getX() + 5) {
                    currentPlayer.velocityX = 0;
                } else {
                    currentPlayer.velocityX = (int) ((event.getX() - currentPlayer.xPos - distX) * PLAYER_VELOCITY_MODIFIER);
                }

                if (currentPlayer.yPos > event.getY() - 5 && currentPlayer.yPos < event.getY() + 5) {
                    currentPlayer.velocityY = 0;
                } else {
                    currentPlayer.velocityY = (int) ((event.getY() - currentPlayer.yPos - distY) * PLAYER_VELOCITY_MODIFIER);
                }

                currentPlayer.ResolveDirection();

//            currentPlayer.xPos = (int) (event.getX() - distY);
//            currentPlayer.yPos = (int) (event.getY() - distX);
//            currentPlayer.ResolveDirection();
//                }
                return true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                currentPlayer.velocityX = 0;
                currentPlayer.velocityY = 0;
                currentPlayer.ResolveDirection();

                return true;
            }
        }
        return false;
    }

//    Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.glass);

    public void DoImportantStuff2() {

        if (System.currentTimeMillis() - lastPlayerUpdated > DELTA_TIME) {

//            if(System.currentTimeMillis() - lastPlayerTime > DELTA_TIME + 5)
//            Log.i("debug", "TIME 2 : " + (System.currentTimeMillis() - lastPlayerUpdated));

            currentCoin.state = (int) (lastUpdated / 100) % 7;

            if (coinTime < lastUpdated) {
                coinTime = lastUpdated + (randomInt.nextInt(4) * 2000) + 2000;
                currentCoin.isActive = !currentCoin.isActive;
                if (currentCoin.isActive) {
                    if (soundPlaying && playerVisible)
                        soundPlayer.play(soundIds[1], 1, 1, 1, 0, (float) 1.0);
                    currentCoin.RandomizePosition();
                }
            }

            if (currentCoin.isActive && Colliding(currentCoin, currentPlayer)) {
                if (soundPlaying && playerVisible)
                    soundPlayer.play(soundIds[2], 1, 1, 1, 0, (float) 1.0);
                playerScore += 3;
                coinTime = 0;
            }

//            if (currentCoin != null)
            currentCoin.MoveCoin(timeStep);

            currentBackground.DoStuff(timeStep);

            currentPlayer.MovePlayer(timeStep);

            lastPlayerUpdated = System.currentTimeMillis();
        }
    }

    Enemy tempEnemy;

    public void DoImportantStuff() {

//        timeStep = (System.currentTimeMillis() - lastUpdated)/10;
//        double var = (System.currentTimeMillis() - lastUpdated)/10;
//        Log.i("debug", "DIFF: " + (System.currentTimeMillis() - lastUpdated));
//        lastUpdated = System.currentTimeMillis();
//        Log.i("debug", "TIMESTEP: " + var);


//        if (System.currentTimeMillis() - lastPlayerUpdated > DELTA_TIME) {
//            currentPlayer.MovePlayer(timeStep);
//            lastPlayerUpdated = System.currentTimeMillis();
//        }

        if (System.currentTimeMillis() - lastUpdated > DELTA_TIME) {

//            if(System.currentTimeMillis() - lastUpdated > DELTA_TIME + 5)
//            Log.i("debug", "TIME 1 : " + (System.currentTimeMillis() - lastUpdated));

//            currentCoin.state = (int) (lastUpdated / 100) % 7;
//
//            if (coinTime < lastUpdated) {
//                coinTime = lastUpdated + (randomInt.nextInt(4) * 2000) + 2000;
//                currentCoin.isActive = !currentCoin.isActive;
//                if (currentCoin.isActive) {
//                    if (soundPlaying)
//                        soundPlayer.play(soundIds[1], 1, 1, 1, 0, (float) 1.0);
//                    currentCoin.RandomizePosition();
//                }
//            }
//
//            if (currentCoin.isActive && Colliding(currentCoin, currentPlayer)) {
//                if (soundPlaying)
//                    soundPlayer.play(soundIds[2], 1, 1, 1, 0, (float) 1.0);
//                playerScore += 3;
//                coinTime = 0;
//            }
////
////            if (currentCoin != null)
//            currentCoin.MoveCoin(timeStep);

//            if (currentPlayer != null)
//            currentPlayer.MovePlayer(timeStep);

//            if (currentBackground != null)
//            currentBackground.DoStuff(timeStep);

//            for (Enemy enemy : enemyArrayList) {
//                enemy.velocityX = -(int) speedEnemy;
//            }
            int count = 0;
//
            for (int i = 0; i < totalEnemies; i++) {

                tempEnemy = enemyArrayList.get(i);

                Log.i("debug", "ENEMY EARLY pos: " + tempEnemy.xPos);

                tempEnemy.velocityX = -(int) speedEnemy;

                Log.i("debug", "ENEMY LATER pos: " + tempEnemy.xPos);

                Log.i("debug", "ENEMY LATER velocity: " + tempEnemy.velocityX);

                Log.i("debug", "Enemy Speed X " + speedEnemy);

//                Log.i("debug", "VELOCITY : " + tempEnemy.velocityX);

                if (tempEnemy.xPos >= ENEMY_MIN_Y) {

//                    Log.i("debug", "BROWSING ENEMIES");
                    tempEnemy.MoveEnemy(timeStep);

                    if (lastUpdated % 250 > 125)
                        tempEnemy.wingState = 1;
                    else
                        tempEnemy.wingState = 0;

                    if (Colliding(tempEnemy, currentPlayer)) {

//                        superPlaneGodMode = true;

                        if (!superPlaneGodMode) {
                            InitiateEndGameScenario();
                        }
                    }

                    if (tempEnemy.xPos < ENEMY_MIN_Y) {
//                    enemyArrayList.remove(i);
                        if (randomInt.nextInt(BIRDIE_TIME_CHANCE) < 5)
                            itsTime = true;
                        enemiesOnScreen--;
                        playerScore++;
//                    soundPlayer.play(soundIds[0], 1, 1, 1, 0, (float) 1.0);
                    }

                } else {

                    if (!itsTime) {
                        tempEnemy.SetImage(randomInt.nextInt(8));
                        tempEnemy.xPos = SCREEN_WIDTH + (int) ((i + 1) * 60 * UNIT_LENGTH);
//                    tempEnemy.yPos = randomInt.nextInt((SCREEN_WIDTH - 4 * tempEnemy.size) / 50) * 50 + 2 * tempEnemy.size;
                        if (randomInt.nextInt(3) < 1 && !superPlaneGodMode)
                            tempEnemy.yPos = currentPlayer.yPos + (randomInt.nextInt(3) - 1) * 2 * tempEnemy.size;
                        else
                            tempEnemy.yPos = (randomInt.nextInt(9) + 1) * tempEnemy.size;
                        enemiesOnScreen++;
                    }
                }
            }

            if (itsTime && enemiesOnScreen <= 0) {

                if (!warningSoundPlaying) {
                    if (soundPlaying && playerVisible) {
                        soundPlayer.play(soundIds[0], (float) 0.6, (float) 0.6, 1, 0, (float) 1.0);
                    }
                    warningSoundPlaying = true;
                    waitTime = System.currentTimeMillis();
                }

                if (lastUpdated - waitTime > (randomInt.nextInt(2) + 1) * 1000) {
//                    Log.i("debug", "TASK FORCE");

                    if (speedEnemy < 10 * UNIT_LENGTH) {
                        speedEnemy += speedEnemy / 7;
//                        PLAYER_VELOCITY_MODIFIER += 0.1;

                        for (Enemy enemy : enemyArrayList) {
                            enemy.velocityX = -(int) speedEnemy;
                        }
                    }

                    SetupEnemies(Formation.values()[randomInt.nextInt(Formation.values().length)]);

                    enemiesOnScreen = totalEnemies;
                    itsTime = false;
                    warningSoundPlaying = false;
//                    soundPlayer.play(soundIds[1], 1, 1, 1, 0, (float) 1.0);
                }
            }

            int position = enemyArrayList.get(0).xPos;
            if (position < ENEMY_MIN_Y) {
                int i;
                for (i = 1; i < totalEnemies; i++) {
                    if (enemyArrayList.get(i).xPos != position)
                        break;
                }

                if (i >= totalEnemies) {
                    for (int j = 0; j < totalEnemies; j++) {
                        tempEnemy = enemyArrayList.get(j);
                        tempEnemy.SetImage(randomInt.nextInt(8));
                        tempEnemy.xPos = SCREEN_WIDTH + (int) ((j + 1) * 60 * UNIT_LENGTH);
                        Log.i("once","temp Xpos" + tempEnemy.xPos);
                        tempEnemy.yPos = (randomInt.nextInt(9) + 1) * tempEnemy.size;
                    }
//                    SetupEnemies(Formation.values()[randomInt.nextInt(Formation.values().length)]);
                    Log.i("once", "FORMATION FORCED");
                    enemiesOnScreen = totalEnemies;
                }
            }

            textScore.post(updateScore);

            lastUpdated = System.currentTimeMillis();
        }
    }

    public void DoDrawing(Canvas canvas) {

//        if (System.currentTimeMillis() - lastDrawUpdated > DELTA_TIME_DRAW) {
        canvas.drawColor(0xFF64B5F6);

        if (currentBackground != null)
            currentBackground.DrawBackground(canvas, 0, 0);

        if (currentPlayer != null && playerVisible)
            currentPlayer.DrawPlayer(canvas);

        for (int i = 0; i < totalEnemies; i++) {
            enemyArrayList.get(i).DrawEnemy(canvas, 0, 0);
        }

        if (currentCoin.isActive) {
            currentCoin.DrawCoin(canvas);
        }

        lastDrawUpdated = System.currentTimeMillis();
//        }
    }

    public void InitiateEndGameScenario() {

        playerVisible = false;

        pauseButton.post(new Runnable() {
            @Override
            public void run() {
                pauseButton.setVisibility(GONE);
            }
        });

        fatalEnemy = tempEnemy;
        endGameTime = System.currentTimeMillis();
        gameEnd = true;

        lastMessagePaint.setColor(0xffdddddd);
        lastMessagePaint.setTextSize((int) (18 * scale));
        lastMessagePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        lastMessagePaint.setTextAlign(Paint.Align.LEFT);
        lastMessagePaint.setStrokeWidth(2 * scale);

        switch (randomInt.nextInt(14)) {
            case 0:
                lastMessage = "Aw nuts!!";
                break;
            case 1:
                lastMessage = "Why were we even doing this?";
                break;
            case 2:
                lastMessage = "Who's idea was this? Huh!";
                break;
            case 3:
                lastMessage = "I'm Finger Lickin' Good";
                break;
            case 4:
                lastMessage = "What the hell just happened!";
                break;
            case 5:
                lastMessage = "I hate you guys. I am going home.";
                break;
            case 6:
                lastMessage = "Well... this is awkward.";
                break;
            case 7:
                lastMessage = "Man! This grass is soft.";
                break;
            case 8:
                lastMessage = "Ironically, I was a vegetarian once.";
                break;
            case 9:
                lastMessage = "And we gonna let it BURN BURN BURN BURN";
                break;
            case 10:
                lastMessage = "Somebody call The Doctor.";
                break;
            case 11:
                lastMessage = "And I thought it was a paper plane.";
                break;
            case 12:
                lastMessage = "That's it. I quit";
                break;
            case 13:
                lastMessage = "I am not doing this any more";
                break;
        }

//        if (currentBackground != null)
        currentBackground.paint1.setColorFilter(new PorterDuffColorFilter(0xFF555555, PorterDuff.Mode.SRC_IN));
        currentBackground.paint2.setColorFilter(new PorterDuffColorFilter(0xFF333333, PorterDuff.Mode.SRC_IN));

        playerInControl = false;

        backgroundMusic.pause();

        if (soundPlaying)
            soundPlayer.play(soundIds[3], 1, 1, 1, 0, (float) 1.0);

        lastUpdated = System.currentTimeMillis();

        currentPlayer.InitiateFreeFall();

        currentBackground.explosion.xPos = currentPlayer.xPos + currentPlayer.size;
        currentBackground.explosion.yPos = currentPlayer.yPos;
        currentBackground.explosion.Reset();

        for (Enemy enemy : enemyArrayList) {

            if (randomInt.nextInt(3) < 2 && enemy.id != 5) {
                enemy.enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.fried_full);

            } else {
                enemy.enemyImages[0] = BitmapFactory.decodeResource(resources, R.drawable.fried_half);
            }
            enemy.enemyImages[0] = Bitmap.createScaledBitmap(enemy.enemyImages[0], 2 * enemy.size, 2 * enemy.size, true);
            enemy.InitiateFreeFall();
        }

//        playerScore = 0;
//        postDelayed(showScoreBoard, 500);
    }

    public boolean DoEndGameImportantStuff() {

        long timeDiff = System.currentTimeMillis() - endGameTime;

        if (System.currentTimeMillis() - lastUpdated > DELTA_TIME) {
            if (timeDiff < 2500) {

                if (timeDiff < 1000) {
                    currentBackground.DoBackgroundEndGame(timeStep);
                    if (timeDiff > 600) {

                        currentPlayer.MovePlayerEndGame(timeStep);

                        for (Enemy enemy : enemyArrayList) {
                            enemy.MoveEnemyEndGame(timeStep);
                        }
                    }
                    lastUpdated = System.currentTimeMillis();
                    return true;

                } else if (timeDiff < 2500) {
                    currentPlayer.MovePlayerEndGame(timeStep);

                    for (Enemy enemy : enemyArrayList) {
                        enemy.MoveEnemyEndGame(timeStep);
                    }

                    lastUpdated = System.currentTimeMillis();
                    return true;
                }

                lastUpdated = System.currentTimeMillis();
                return true;

            } else {
                lastUpdated = System.currentTimeMillis();
                return false;
            }
        }
        return true;
    }

    public void DoEndGameDrawing(Canvas canvas) {

//        if (System.currentTimeMillis() - lastDrawUpdated > DELTA_TIME_DRAW) {
        canvas.drawColor(0xFF222222);
//
//        if (currentCoin.isActive) {
//            currentCoin.DrawCoin(canvas);
//        }

        if (currentBackground != null)
            currentBackground.DrawBackground(canvas, 0, 0);

        if (currentPlayer != null && playerVisible)
            currentPlayer.DrawPlayerEndGame(canvas);

        for (Enemy enemy : enemyArrayList) {
            enemy.DrawEnemyEndGame(canvas);
        }

        if (System.currentTimeMillis() - endGameTime < 500) {
            currentBackground.DrawBackgroundEndGame(canvas);
        }

        lastDrawUpdated = System.currentTimeMillis();

        canvas.drawLine(fatalEnemy.xPos - 20, fatalEnemy.yPos - fatalEnemy.size / 2 - 20,
                fatalEnemy.xPos, fatalEnemy.yPos - (int) (1.2 * fatalEnemy.size), lastMessagePaint);

        canvas.drawText(lastMessage, fatalEnemy.xPos, fatalEnemy.yPos - (int) (1.5 * fatalEnemy.size), lastMessagePaint);
//        }
    }

    public void ShowEndGameScreen() {
        Log.i("debug", "You are dead. Did you hear me. DEAD!!");
        post(showScoreBoard);
//        playerScore = 0;
        Stop();
        superPlaneGodMode = true;
    }

    public void Stop() {
        currentPlayer.xPos = 20 * (int) UNIT_LENGTH;
        currentPlayer.yPos = SCREEN_HEIGHT / 2;

        lastUpdated = System.currentTimeMillis();
        speedEnemy = (int) (4 * UNIT_LENGTH);

        enemiesOnScreen = 0;

        playerVisible = false;

        itsTime = false;
        waitTime = 0;

        totalEnemies = 5;

        distY = 0;
        distX = 0;

//        playerScore = 0;

        gameEnd = false;
//        paused = true;

        randomInt = new Random();

        currentBackground.Reset();
        currentPlayer.Reset();

        for (int i = 0; i < totalEnemies; i++) {
//            enemyArrayList.add(new Enemy(resources));
            enemyArrayList.get(i).Reset();
            enemyArrayList.get(i).SetImage(randomInt.nextInt(8));
            enemyArrayList.get(i).xPos = SCREEN_WIDTH + (int) ((i + 1) * 60 * UNIT_LENGTH);
            enemyArrayList.get(i).yPos = (randomInt.nextInt(9) + 1) * enemyArrayList.get(i).size;
            enemiesOnScreen++;
        }

        if (musicPlaying) {
            backgroundMusic.release();
            backgroundMusic = MediaPlayer.create(getContext(), R.raw.back);
            backgroundMusic.start();
        }

        if (musicPlaying)
            backgroundMusic.pause();
    }

    public void Play() {
//        currentPlayer = new Player();
//        playerScore = 0;
        superPlaneGodMode = false;
        if (!musicPlaying)
            backgroundMusic.pause();
        if (musicPlaying)
            backgroundMusic.start();
        paused = false;
    }

    public void Pause() {
        gamePauseMenu.post(showPauseScreen);
        if (musicPlaying)
            backgroundMusic.pause();
        paused = true;
    }

    public void Restart() {
        currentPlayer.xPos = 20 * (int) UNIT_LENGTH;
        currentPlayer.yPos = SCREEN_HEIGHT / 2;

        lastUpdated = System.currentTimeMillis();
        speedEnemy = (int) (4 * UNIT_LENGTH);

        enemiesOnScreen = 0;

        itsTime = false;
        waitTime = 0;

        totalEnemies = 5;

        distY = 0;
        distX = 0;

        playerInControl = true;

        paused = false;
        gameEnd = false;

        randomInt = new Random();
        playerScore = 0;

        currentBackground.Reset();
        currentPlayer.Reset();
        for (int i = 0; i < totalEnemies; i++) {
//            enemyArrayList.add(new Enemy(resources));
            enemyArrayList.get(i).Reset();
            enemyArrayList.get(i).SetImage(randomInt.nextInt(8));
            enemyArrayList.get(i).xPos = SCREEN_WIDTH + (int) ((i + 1) * 60 * UNIT_LENGTH);
            enemyArrayList.get(i).yPos = (randomInt.nextInt(9) + 1) * enemyArrayList.get(i).size;
            enemiesOnScreen++;
        }

        if (musicPlaying) {
            backgroundMusic.release();
            backgroundMusic = MediaPlayer.create(getContext(), R.raw.back);
            backgroundMusic.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setWillNotDraw(false);
        myThread = new MyThread(holder);
        myThread.setRunning(true);
        myThread.start();

        myThread2 = new MyThread2();
        myThread2.setRunning(true);
        myThread2.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        myThread.setRunning(false);
        myThread2.setRunning(false);
        while (retry) {
            try {
                myThread.join();
//                backgroundMusic.stop();
//                backgroundMusic.release();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(getClass().getSimpleName(), "Interrupted Exception", e);
            }
        }
        retry = true;
        while (retry) {
            try {
                myThread2.join();
//                backgroundMusic.stop();
//                backgroundMusic.release();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(getClass().getSimpleName(), "Interrupted Exception", e);
            }
        }
    }

    public class MyThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean running = false;

        public MyThread(SurfaceHolder holder) {
            surfaceHolder = holder;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running) {
                if (!paused) {
                    try {
                        canvas = surfaceHolder.lockCanvas(null);

                        if (canvas != null) {
                            if (!gameEnd)
                                DoDrawing(canvas);
                            else
                                DoEndGameDrawing(canvas);
                        }
                        if (!gameEnd) {
                            DoImportantStuff2();
                        }

//                        if (!gameEnd)
//                            DoImportantStuff();
//                        else if (!DoEndGameImportantStuff()) {
//                            ShowEndGameScreen();
//                        }

                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            running = b;
        }
    }

    public class MyThread2 extends Thread {
        private boolean running = false;

        public MyThread2() {

        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running) {
                if (!paused) {

                    if (!gameEnd)
                        DoImportantStuff();
                    else if (!DoEndGameImportantStuff()) {
                        ShowEndGameScreen();
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            running = b;
        }
    }
}