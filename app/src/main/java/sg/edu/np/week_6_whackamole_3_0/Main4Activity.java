package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    Button backButton;
    private MyDBHandler dbhandler;
    private String username;
    private int level;
    private int currentscore;
    private int lvltimer;
    TextView scoreText;
    private int previous=0;
    private int previous2=0;





    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                Toast.makeText(getApplicationContext(),"Your game starts in "+millisUntilFinished/1000,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinish() {
                Log.v(TAG,"Countdown completed");
                readyTimer.cancel();
                placeMoleTimer();

            }
        };
       readyTimer.start();

    }



    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        newMolePlaceTimer = new CountDownTimer(1000*lvltimer,10000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "New Mole Location!");
                setNewMole();
            }

            @Override
            public void onFinish() {
                placeMoleTimer();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */

        dbhandler=new MyDBHandler(this,null,null,3);
        scoreText=findViewById(R.id.scoreText);
        backButton=findViewById(R.id.buttonback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intentback=new Intent(Main4Activity.this,Main3Activity.class);
                intentback.putExtra("username",username);
                startActivity(intentback);
            }
        });

        Intent get=getIntent();
        username=get.getExtras().getString("name");
        level=get.getExtras().getInt("level");






        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button button=findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheck(button);
                    setNewMole();
                }
            });
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        lvltimer=11-level;
        readyTimer();
    }



    private void doCheck(Button checkButton) {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        String select = checkButton.getText().toString();
        if (select == "*") {
            Log.v(TAG, "Score add");
            currentscore += 1;
        } else {
            if (currentscore > 0) {
                currentscore -= 1;
            }
            Log.v(TAG, "Minus score");

        }
        scoreText.setText("Score: "+currentscore);

    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        if(level<6){
            int randomLocation=ran.nextInt(9);
            Button click=findViewById(BUTTON_IDS[randomLocation]);
            click.setText("*");
            Button previousbutton=findViewById(BUTTON_IDS[previous]);
            previousbutton.setText("O");
            previous=randomLocation;
        }
        else{
        int randomLocation = ran.nextInt(9);
        int randomsecond = ran.nextInt(9);
        while (true) {
            if (randomLocation == randomsecond) {
                randomsecond = ran.nextInt(9);
            } else {
                break;
            }
        }
        Button click=findViewById(BUTTON_IDS[randomLocation]);
        Button click2=findViewById(BUTTON_IDS[randomsecond]);
        click.setText("*");
        click2.setText("*");
        Button previousbutton=findViewById(BUTTON_IDS[previous]);
        Button previousbutton2=findViewById(BUTTON_IDS[previous2]);
        previousbutton.setText("O");
        previousbutton2.setText("O");
        previous=randomLocation;
        previous2=randomsecond;
    }

    }



    private void updateUserScore()
    {
        UserData userdata=dbhandler.findUser(username);
        MyDBHandler dbhandler=new MyDBHandler(this,null,null,3);
     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();


        int level2=userdata.getLevels().indexOf(level);

        int highscore=userdata.getScores().get(level2);

        if (currentscore>highscore){
            Log.v(TAG,"TRying out sql update");
            highscore=currentscore;
            dbhandler.updateScore(username,highscore,level);
            Log.v(TAG,"Update ok");
        }
        else{
            Log.v(TAG,"Not high score");
        }
    }

}
