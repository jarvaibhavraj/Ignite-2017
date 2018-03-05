package xyz.mrdeveloper.ignite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static xyz.mrdeveloper.ignite.UpdateFromFirebase.isRegistered;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public String name;
    public Long sapid;
    public Long number;
    public static int appRunning = 10;
    public Long playerScore;
    public String email;

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_sapid)
    EditText _sapidText;
    @InjectView(R.id.input_number)
    EditText _numberText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        if (_nameText == null || _emailText == null || _sapidText == null || _numberText == null
                || _nameText.equals("") || _emailText.equals("") || _sapidText.equals("") || _numberText.equals(""))
            Toast.makeText(this, "Please complete the form to enter the Arena", Toast.LENGTH_LONG).show();
        else {
            name = _nameText.getText().toString();
            email = _emailText.getText().toString();
            sapid = Long.parseLong(_sapidText.getText().toString());
            number = Long.parseLong(_numberText.getText().toString());


            StringBuilder playerDetails = new StringBuilder();
            playerDetails.append(name).append(",").append(sapid).append(",").append(number).append(",").append(email);

            // TODO: Implement your own signup logic here.
            this.getSharedPreferences("PlayerDetails", MODE_PRIVATE)
                    .edit()
                    .putString("PlayerDetails", playerDetails.toString())
                    .apply();

            SharedPreferences pref = this.getSharedPreferences("PlayerScore", MODE_PRIVATE);
            String storedPlayerScore = pref.getString("PlayerScore", null);

            if (storedPlayerScore == null) {
                playerScore = Long.parseLong("0");
            } else {
                playerScore = Long.parseLong(storedPlayerScore);
            }

            this.getSharedPreferences("PlayerScore", MODE_PRIVATE)
                    .edit()
                    .putString("PlayerScore", playerScore.toString())
                    .apply();

            pref = this.getSharedPreferences("PlayerDetails", MODE_PRIVATE);
            String playerDetailsInSP = pref.getString("PlayerDetails", null);

            Log.d("Check", "hehe Player Details: " + playerDetailsInSP);

            appRunning = 1;

            final DatabaseReference mFirebaseDatabase;
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

            // get reference to 'users' node
            mFirebaseDatabase = mFirebaseInstance.getReference("leaderboard");
            String stringSAPID = sapid.toString();
            mFirebaseDatabase.child(stringSAPID).child("name").setValue(name);
            mFirebaseDatabase.child(stringSAPID).child("sapid").setValue(sapid);
            mFirebaseDatabase.child(stringSAPID).child("number").setValue(number);
            mFirebaseDatabase.child(stringSAPID).child("email").setValue(email);
            mFirebaseDatabase.child(stringSAPID).child("score").setValue(playerScore);

            isRegistered = true;
            Toast.makeText(this, "You're in the system", Toast.LENGTH_LONG).show();

            onSignupSuccess();
        }
    }

    public void onSignupSuccess() {

//        if (!isRegistered) {
//            leaderboardButton.setBackgroundResource(R.drawable.register);
//        } else {
//            leaderboardButton.setBackgroundResource(R.drawable.medal);
//        }

        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String sapid = _sapidText.getText().toString();
        String number = _numberText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (sapid.isEmpty() || sapid.length() != 9) {
            _sapidText.setError("Invalid length");
            valid = false;
        } else {
            _sapidText.setError(null);
        }

        if (number.isEmpty() || number.length() != 10) {
            _numberText.setError("Invalid length");
            valid = false;
        } else {
            _numberText.setError(null);
        }

        if (email.isEmpty() || email.length() < 5 || !email.contains("@") || !email.contains(".")) {
            _emailText.setError("Invalid format of email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        return valid;
    }
}