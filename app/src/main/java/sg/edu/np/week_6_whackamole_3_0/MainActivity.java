package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    //public String GLOBAL_PREFS="MyPrefs";
    //public String MY_USERNAME="MyUserName";
    //public String MY_PASSWORD="MyPassword";

    private TextView newUser;
    private Button loginButton;
    //SharedPreferences sharedPreferences;

    MyDBHandler dbHandler=new MyDBHandler(this,null,null,3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newUser=findViewById(R.id.textView_createNewuser);
        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            }
        });


        loginButton=findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername=findViewById(R.id.loginUsername);
                EditText etPassword=findViewById(R.id.loginPassword);
                Log.v(TAG,FILENAME+" : Login with info: "+etUsername.getText().toString()+":"+etPassword.getText().toString());
                Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());

                if(isValidUser(etUsername.getText().toString(),etPassword.getText().toString())){
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent intent=new Intent(MainActivity.this,Main3Activity.class);
                    intent.putExtra("username",etUsername.getText().toString());
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"Valid",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        /*sharedPreferences=getSharedPreferences(GLOBAL_PREFS,MODE_PRIVATE);
        String shareduserName=sharedPreferences.getString(MY_USERNAME,"");
        String sharedpassword=sharedPreferences.getString(MY_PASSWORD,"");*/

        UserData dbData=dbHandler.findUser(userName);
        if (dbData == null){
            Log.v(TAG,"Cannot find user");
            Toast.makeText(this,"No such user",Toast.LENGTH_SHORT).show();
            return false;
        }


        if (dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password)){
            Log.v(TAG,"Logging in");
            Toast.makeText(this,"Logging in",Toast.LENGTH_SHORT).show();
            return true;
        }

        else{
            Log.v(TAG,"Wrong password entered");
        Toast.makeText(this,"Wrong password, try again",Toast.LENGTH_SHORT).show();
        return false;}


    }

}
