package xyz.mrdeveloper.ignite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static xyz.mrdeveloper.ignite.SignUp.appRunning;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.isRegistered;
import static xyz.mrdeveloper.ignite.UpdateFromFirebase.rank;

/**
 * Created by Vaibhav on 04-02-2017.
 */

public class Game extends AppCompatActivity implements View.OnClickListener {
    //    public View view;

    public boolean MUSIC_PLAYING = true;
    public boolean SOUND_PLAYING = true;
    public boolean FIRST_TIME;

    boolean leaderboardOpen = false;

    Random randomInt;

    public Button facebookButton;

    public Button pauseButton;
    public Button playButton;
    public Button exitButton;
    public Button aboutButton;
    public Button leaderboardButton;
    public TextView triviaText;
    public ImageView triviaImage;

    int triviaImageID;
    String triviaString;

    public Button toggleMusic;
    public Button toggleSound;

    public View gamePauseMenu;
    public TextView gamePauseMenuRank;
    public TextView gamePauseMenuHighScore;
    public View gameReallyExit;

    public View buttonRealExit;
    public View buttonDontExit;

    public View scoreBoard;
    public TextView scoreBoardCurrentScore;
    public TextView scoreBoardHighestScore;
    public TextView scoreBoardRank;
    public TextView scoreBoardPunchline;
    public View scoreBoardNext;
    public View scoreBoardReplay;

    public View licenseAgreement;
    public View buttonLicenseAgreement;

    ImageView backgroundImage;

    public View newHighScoreStamp;

    public ThingsWithWings currentGame;

    boolean running = false;

    TextView textScore;

    int playerScore;
    int playerHighestScore;
    int playerRank;

//    MyThread thread;

    public static Game newInstance() {
        return new Game();
    }

    public Game() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        SharedPreferences pref = this.getSharedPreferences("FirstTime", MODE_PRIVATE);
        String firstTime = pref.getString("FirstTime", null);

        FIRST_TIME = firstTime == null;

        this.getSharedPreferences("FirstTime", MODE_PRIVATE)
                .edit()
                .putString("FirstTime", "Game is loaded for first time!")
                .apply();

        pref = this.getSharedPreferences("SoundPlaying", MODE_PRIVATE);
        String SoundPlaying = pref.getString("SoundPlaying", null);

        pref = this.getSharedPreferences("MusicPlaying", MODE_PRIVATE);
        String MusicPlaying = pref.getString("MusicPlaying", null);

        if (FIRST_TIME) {
            SOUND_PLAYING = true;
            MUSIC_PLAYING = true;
        } else {
            SOUND_PLAYING = Boolean.parseBoolean(SoundPlaying);
            MUSIC_PLAYING = Boolean.parseBoolean(MusicPlaying);
        }

        Log.d("debug", "SOUND_PLAYING " + SOUND_PLAYING + " MUSIC_PLAYING " + MUSIC_PLAYING);

        randomInt = new Random();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Inflate the layout for this fragment
        setContentView(R.layout.game);

        facebookButton = (Button) findViewById(R.id.button_facebook);
        facebookButton.setOnClickListener(this);

        backgroundImage = (ImageView) findViewById(R.id.background_wallpaper);

        buttonLicenseAgreement = findViewById(R.id.license_next);
        buttonLicenseAgreement.setOnClickListener(this);

        textScore = (TextView) findViewById(R.id.textScore);
        textScore.setVisibility(GONE);

        pauseButton = (Button) findViewById(R.id.buttonPause);
        pauseButton.setOnClickListener(this);
        pauseButton.setVisibility(GONE);

        playButton = (Button) findViewById(R.id.buttonPlay);
        playButton.setOnClickListener(this);

        exitButton = (Button) findViewById(R.id.buttonDummyExit);
        exitButton.setOnClickListener(this);

        aboutButton = (Button) findViewById(R.id.button_about);
        aboutButton.setOnClickListener(this);

        triviaString = "DISCLAIMER: \nNo birds were harmed in the making of this game";
        triviaImage = (ImageView) findViewById(R.id.triviaImage);
        triviaText = (TextView) findViewById(R.id.triviaText);
        triviaText.setText(triviaString);

        leaderboardButton = (Button) findViewById(R.id.buttonLeaderboard);
        leaderboardButton.setOnClickListener(this);

        if (!isRegistered) {
            leaderboardButton.setBackgroundResource(R.drawable.register);
        } else {
            leaderboardButton.setBackgroundResource(R.drawable.medal);
        }

        gamePauseMenu = findViewById(R.id.game_pause_menu);
        gamePauseMenu.setVisibility(View.VISIBLE);
        gamePauseMenu.setOnClickListener(this);

        gamePauseMenuRank = (TextView) findViewById(R.id.menu_rank);
        gamePauseMenuHighScore = (TextView) findViewById(R.id.menu_highscore);

        licenseAgreement = findViewById(R.id.license_agreement);
        if (FIRST_TIME) {
            licenseAgreement.setVisibility(VISIBLE);
            gamePauseMenu.setVisibility(GONE);
        } else
            licenseAgreement.setVisibility(GONE);

        scoreBoard = findViewById(R.id.score_board);
        scoreBoard.setVisibility(GONE);
        scoreBoardCurrentScore = (TextView) findViewById(R.id.score_current);
        scoreBoardHighestScore = (TextView) findViewById(R.id.score_highest);
        scoreBoardRank = (TextView) findViewById(R.id.score_rank);
        scoreBoardNext = findViewById(R.id.score_next);
        scoreBoardReplay = findViewById(R.id.score_replay);
        scoreBoardPunchline = (TextView) findViewById(R.id.score_punch_line);

        scoreBoardNext.setOnClickListener(this);
        scoreBoardReplay.setOnClickListener(this);

        newHighScoreStamp = findViewById(R.id.new_high_score_stamp);
        newHighScoreStamp.setVisibility(GONE);

        currentGame = (ThingsWithWings) findViewById(R.id.thingsWithWings);

        currentGame.soundPlaying = SOUND_PLAYING;
        currentGame.musicPlaying = MUSIC_PLAYING;


        currentGame.scoreBoard = scoreBoard;
        currentGame.scoreBoardCurScore = scoreBoardCurrentScore;
        currentGame.scoreBoardHighestScore = scoreBoardHighestScore;
        currentGame.scoreBoardRank = scoreBoardRank;
        currentGame.scoreBoardNext = scoreBoardNext;
        currentGame.scoreBoardPunchline = scoreBoardPunchline;


        currentGame.textScore = textScore;
        currentGame.gamePauseMenu = gamePauseMenu;
        currentGame.gamePauseMenuHighScore = gamePauseMenuHighScore;
        currentGame.gamePauseMenuRank = gamePauseMenuRank;

        currentGame.gamePauseMenuRank = gamePauseMenuRank;
        pref = this.getSharedPreferences("PlayerScore", MODE_PRIVATE);
        String storedPlayerScore = pref.getString("PlayerScore", null);
        if (storedPlayerScore == null) {
            storedPlayerScore = "0";
        }

        currentGame.gamePauseMenuHighScore.setText(storedPlayerScore);

        gamePauseMenuRank.setText(String.valueOf(rank));
        gamePauseMenuHighScore.setText(storedPlayerScore);

        currentGame.pauseButton = pauseButton;
        currentGame.triviaImage = triviaImage;
        currentGame.triviaText = triviaText;

        currentGame.newHighScoreStamp = newHighScoreStamp;

        currentGame.playerHighestScore = playerHighestScore;
        currentGame.playerRank = playerRank;

        currentGame.Pause();

        toggleMusic = (Button) gamePauseMenu.findViewById(R.id.buttonToggleMusic);
        toggleMusic.setOnClickListener(this);
        if (currentGame.musicPlaying) {
            toggleMusic.setBackgroundResource(R.drawable.music_on);
        } else {
            toggleMusic.setBackgroundResource(R.drawable.music_off);
        }

        toggleSound = (Button) gamePauseMenu.findViewById(R.id.buttonToggleSound);
        toggleSound.setOnClickListener(this);
        if (currentGame.soundPlaying) {
            toggleSound.setBackgroundResource(R.drawable.sound_on);
        } else {
            toggleSound.setBackgroundResource(R.drawable.sound_off);
        }

        gameReallyExit = findViewById(R.id.relativeLayoutReallyExit);
        gameReallyExit.setVisibility(GONE);

        buttonRealExit = gameReallyExit.findViewById(R.id.exitPopupYes);
        buttonRealExit.setOnClickListener(this);

        buttonDontExit = gameReallyExit.findViewById(R.id.exitPopupNo);
        buttonDontExit.setOnClickListener(this);

        final Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Quicksand-Regular.ttf");

        textScore.setTypeface(typeface);

//        textScore.post(new Runnable() {
//            @Override
//            public void run() {
//                while (running)
//                textScore.setText(String.valueOf(currentGame.playerScore));
//            }
//        });

        running = true;

//        thread = new MyThread();
//        thread.setRunning(true);
//        thread.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_about:
//                gamePauseMenu.setVisibility(GONE);
                licenseAgreement.setVisibility(VISIBLE);
                break;

            case R.id.button_facebook:
                Log.d("debug", "hehe Facebook open!!");
                Intent intent1 = new Intent(this, FacebookPost.class);
                startActivity(intent1);
                break;

            case R.id.score_next:
                playerScore = 0;
                currentGame.playerScore = 0;

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                currentGame.Restart();
                currentGame.superPlaneGodMode = true;
                currentGame.playerVisible = false;
//                currentGame.Restart();
                scoreBoard.setVisibility(GONE);
                newHighScoreStamp.setVisibility(GONE);
                GenerateNewTrivia();
                triviaImage.setImageResource(triviaImageID);
                triviaText.setText(triviaString);

            case R.id.buttonPause:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                Log.i("debug", "Game Paused");
                gamePauseMenu.setVisibility(VISIBLE);
                currentGame.Pause();
                pauseButton.setVisibility(GONE);
                textScore.setVisibility(GONE);
                pauseButton.setVisibility(GONE);
                break;

            case R.id.score_replay:
                playerScore = 0;
                currentGame.playerScore = 0;

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
//                Log.i("debug", "Game Restarted Replayed");
//                scoreBoard.setVisibility(GONE);
//                newHighScoreStamp.setVisibility(GONE);
//                pauseButton.setVisibility(VISIBLE);
//                textScore.setVisibility(VISIBLE);
//                currentGame.superPlaneGodMode = false;
//                currentGame.playerVisible = true;
//                currentGame.Restart();
//                break;

                Log.i("debug", "Game Started");
                gamePauseMenu.setVisibility(GONE);
                currentGame.Play();
                pauseButton.setVisibility(VISIBLE);
                textScore.setVisibility(VISIBLE);
                pauseButton.setVisibility(VISIBLE);
                backgroundImage.setVisibility(GONE);
                scoreBoard.setVisibility(GONE);
                newHighScoreStamp.setVisibility(GONE);
                currentGame.paused = false;
                currentGame.superPlaneGodMode = false;
                currentGame.playerInControl = true;
                currentGame.playerVisible = true;
                currentGame.speedEnemy = (int) (4 * currentGame.UNIT_LENGTH);
                backgroundImage.setVisibility(GONE);

                for (int i = 0; i < currentGame.totalEnemies; i++) {
                    currentGame.enemyArrayList.get(i).velocityX = -(int) currentGame.speedEnemy;
//            enemyArrayList.add(new Enemy(resources));
//                    currentGame.enemyArrayList.get(i).Reset();
//                    currentGame.enemyArrayList.get(i).SetImage(randomInt.nextInt(8));
                    currentGame.enemyArrayList.get(i).xPos = currentGame.SCREEN_WIDTH + (int) ((i + 1) * 60 * currentGame.UNIT_LENGTH);
                    currentGame.enemyArrayList.get(i).yPos = (randomInt.nextInt(9) + 1) * currentGame.enemyArrayList.get(i).size;
//                    enemiesOnScreen++;
                }
                break;

//                scoreBoard.setVisibility(GONE);
//                newHighScoreStamp.setVisibility(GONE);
//                currentGame.Restart();
//                GenerateNewTrivia();
//                currentGame.superPlaneGodMode = false;
//                currentGame.playerVisible = true;
//                currentGame.playerInControl = true;
//                triviaImage.setImageResource(triviaImageID);
//                triviaText.setText(triviaString);

//                Log.i("debug", "Game Paused");
//                gamePauseMenu.setVisibility(VISIBLE);
//                currentGame.Pause();
//                pauseButton.setVisibility(GONE);
//                textScore.setVisibility(GONE);
//                pauseButton.setVisibility(GONE);
//                break;

            case R.id.buttonPlay:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                Log.i("debug", "Game Started");
                gamePauseMenu.setVisibility(GONE);
                currentGame.Play();
                pauseButton.setVisibility(VISIBLE);
                textScore.setVisibility(VISIBLE);
                pauseButton.setVisibility(VISIBLE);
                backgroundImage.setVisibility(GONE);
                currentGame.paused = false;
                currentGame.superPlaneGodMode = false;
                currentGame.playerVisible = true;
                currentGame.playerInControl = true;
                backgroundImage.setVisibility(GONE);
                break;

            case R.id.buttonLeaderboard:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                Log.i("debug", "Leaderboard " + isRegistered);

                ConnectivityManager cm =
                        (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (!isConnected)
                    Toast.makeText(this,
                            "This information might be outdated\nPlease connect to the internet", Toast.LENGTH_LONG).show();

                if (isRegistered) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.gameFrame, LeaderboardFragment.newInstance(), "Leaderboard")
                            .addToBackStack("Leaderboard")
                            .commitAllowingStateLoss();
                    //appRunning = 10;
                    leaderboardOpen = true;
                } else {
                    Intent intent = new Intent(this, SignUp.class);
                    startActivity(intent);
                }
                break;

//            case R.id.buttonReplay:
//                currentGame.Restart();
//                break;

            case R.id.buttonDummyExit:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                gameReallyExit.setVisibility(VISIBLE);
                break;

            case R.id.exitPopupNo:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                gameReallyExit.setVisibility(GONE);
                break;

            case R.id.exitPopupYes:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                finish();
                break;

            case R.id.buttonToggleMusic:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                    MUSIC_PLAYING = false;
                    currentGame.MUSIC_PLAYING = false;
                    currentGame.musicPlaying = false;
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                    currentGame.musicPlaying = true;
                    MUSIC_PLAYING = true;
                    currentGame.MUSIC_PLAYING = true;
                }
                break;

            case R.id.buttonToggleSound:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }

                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                    currentGame.soundPlaying = false;
                    SOUND_PLAYING = false;
                    currentGame.SOUND_PLAYING = false;
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                    currentGame.soundPlaying = true;
                    SOUND_PLAYING = false;
                    currentGame.SOUND_PLAYING = false;
                }
                break;

            case R.id.license_next:

                if (currentGame.musicPlaying) {
                    toggleMusic.setBackgroundResource(R.drawable.music_on);
                } else {
                    toggleMusic.setBackgroundResource(R.drawable.music_off);
                }
                if (currentGame.soundPlaying) {
                    toggleSound.setBackgroundResource(R.drawable.sound_on);
                } else {
                    toggleSound.setBackgroundResource(R.drawable.sound_off);
                }
                licenseAgreement.setVisibility(GONE);
                gamePauseMenu.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (leaderboardOpen) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();

            if (!isRegistered) {
                leaderboardButton.setBackgroundResource(R.drawable.register);
            } else {
                leaderboardButton.setBackgroundResource(R.drawable.medal);
            }

            leaderboardOpen = false;
        }
//        } else if (scoreBoard.getVisibility() == VISIBLE) {
//            scoreBoard.setVisibility(GONE);
//            gamePauseMenu.setVisibility(VISIBLE);
//
//        } else {
//            if (!currentGame.paused) {
//                currentGame.Pause();
//            } else {
//                if (gameReallyExit.getVisibility() == GONE)
//                    gameReallyExit.setVisibility(VISIBLE);
//            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        currentGame.Pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isRegistered) {
            leaderboardButton.setBackgroundResource(R.drawable.register);
        } else {
            leaderboardButton.setBackgroundResource(R.drawable.medal);
        }

        currentGame.Pause();
        gamePauseMenu.setVisibility(VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();

        MUSIC_PLAYING = currentGame.musicPlaying;
        SOUND_PLAYING = currentGame.soundPlaying;

        this.getSharedPreferences("SoundPlaying", MODE_PRIVATE)
                .edit()
                .putString("SoundPlaying", Boolean.toString(SOUND_PLAYING))
                .apply();

        this.getSharedPreferences("MusicPlaying", MODE_PRIVATE)
                .edit()
                .putString("MusicPlaying", Boolean.toString(MUSIC_PLAYING))
                .apply();

        Log.d("debug", "SOUND_PLAYING " + Boolean.toString(SOUND_PLAYING) + " MUSIC_PLAYING " + Boolean.toString(MUSIC_PLAYING));

//        currentGame.Stop();
        currentGame.Pause();
//        currentGame.backgroundMusic.release();

        running = false;

//        boolean retry = true;
//        thread.setRunning(false);
//
//        while (retry) {
//            try {
//                thread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                Log.d(getClass().getSimpleName(), "Interrupted Exception", e);
//            }
//        }
    }

//    @Override
//    public void onSignInSucceeded() {
//        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onSignInFailed() {
//        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
//    }
//
//    public class MyThread extends Thread {
//
//        private boolean running = false;
//
//        @Override
//        public void run() {
////        Log.i("debug", "SHOWING SCORE.................");
//            while (running) {
//                textScore.setText(String.valueOf(currentGame.playerScore));
////            Log.i("debug", "SHOWING SCORE");
//            }
//        }
//
//        public void setRunning(boolean b) {
//            running = b;
//        }
//    }

    public void GenerateNewTrivia() {
        switch (randomInt.nextInt(23)) {
            case 0:
                triviaImageID = R.drawable.bird_42;
                triviaString = "This is Ross. Ross likes Rachel";
                break;
            case 1:
                triviaImageID = R.drawable.bird_42;
                triviaString = "This is Ross. He believes that the moon landing was faked";
                break;
            case 2:
                triviaImageID = R.drawable.bird_71;
                triviaString = "This is Mr. Devil. He says hell is nice in winters";
                break;
            case 3:
                triviaImageID = R.drawable.bird_72;
                triviaString = "This is Mr. Devil. He now lives here";
                break;
            case 4:
                triviaImageID = R.drawable.mrdeveloper_white;
                triviaString = "This is Mr. Developer. He made the game";
                break;
            case 5:
                triviaImageID = R.drawable.bird_41;
                triviaString = "This is Ross. He just keeps asking for a raise";
                break;
            case 6:
                triviaImageID = R.drawable.bird_61;
                triviaString = "This is Rachel. She doesn't know how to ride a bicycle";
                break;
            case 7:
                triviaImageID = R.drawable.bird_11;
                triviaString = "This is Monica. She works for Bird Rights Association";
                break;
            case 8:
                triviaImageID = R.drawable.bird_11;
                triviaString = "This is Monica. Monica is a cat person";
                break;
            case 9:
                triviaImageID = R.drawable.jon;
                triviaString = "This is Jon Snow. He knows nothing";
                break;
            case 10:
                triviaImageID = R.drawable.bird_81;
                triviaString = "This is Joey. Joey always says, \"how you doin\""; //Serahccspklc;sl
                break;
            case 11:
                triviaImageID = R.drawable.bird_32;
                triviaString = "This is Phoebe. Phoebe hates yellow color";
                break;
            case 12:
                triviaImageID = R.drawable.bird_21;
                triviaString = "This is Mr. Eagle. He is pure vegetarian";
                break;
            case 13:
                triviaImageID = R.drawable.bird_22;
                triviaString = "This is Mr. Eagle. He aspires to be a software engineer";
                break;
            case 14:
                triviaImageID = R.drawable.bird_11;
                triviaString = "This is Monica. Monica is afraid of heights";
                break;
            case 15:
                triviaImageID = R.drawable.bird_51;
                triviaString = "This is Chandler. He doesn't have a flying license";
                break;
            case 16:
                triviaImageID = R.drawable.bird_52;
                triviaString = "This is Chandler on fire";
                break;
            case 17:
                triviaImageID = R.drawable.bird_32;
                triviaString = "This is Pheobe. Pheobe is a national level swimmer";
                break;
            case 18:
                triviaImageID = R.drawable.rock;
                triviaString = "This is a rock. It does nothing";
                break;
            case 19:
                triviaImageID = R.drawable.bird_61;
                triviaString = "This is Rachael. She just won a beauty pageant";
                break;
            case 20:
                triviaImageID = R.drawable.mug;
                triviaString = "This is Mug. He is a superhero";
                break;
            case 21:
                triviaImageID = R.drawable.bird_82;
                triviaString = "This is Joey. Joey doesn't get 3D movies";
                break;
            case 22:
                triviaImageID = R.drawable.bird_21;
                triviaString = "This is Mr. Eagle. We don't know his real name";
                break;
        }
    }
}
