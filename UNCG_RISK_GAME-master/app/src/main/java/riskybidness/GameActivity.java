package riskybidness;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import riskybidness.uncg_risk.R;


public class GameActivity extends ActionBarActivity {

    public TextView team1;
    public TextView team2, attText, turnText;
    public Button petty, euc, foust, curry, sullivan, jackson, mossman, bryan, aPetty, aEUC, aFoust, aCurry, aSullivan, aJackson, aMossman, aBryan, end;
    Random dice1 = new Random();
    Random dice2 = new Random();
    Random dice3 = new Random();
    public boolean team1Turn = false;
    public boolean team2Turn= false;
    public boolean gameSetup=true;
    public boolean gameWon=false;
    public int team1Troops=40;
    public int team2Troops=40;
    public String team1name = "Team1";
    public String team2name = "Team2";
   // String gameLine;
    public Button currentBuild;
    public String attacked;
    private String serverIpAddress="";

    public static String SERVERIP = "10.0.2.15";
    public static final int SERVERPORT = 8080;
    private Handler handler = new Handler();
    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        Intent callingIntent = this.getIntent();
        Bundle incomingBundle = callingIntent.getExtras();
        String str = incomingBundle.getString("username");
        String userInfo = incomingBundle.getString("key1"); //will be host or guest
        if(userInfo.equals("guest")) {
            serverIpAddress = incomingBundle.getString("Ip");
            team2name = str;
            Thread cThread = new Thread(new ClientThread());
            cThread.start();
        }
        else if(userInfo.equals("host")){
            team1name = str;
            SERVERIP = getLocalIpAddress();
            Thread fst = new Thread(new ServerThread());
            fst.start();
        }

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_activity);

       team1 = (TextView) findViewById(R.id.team1);
        team2 = (TextView) findViewById(R.id.team2);
        attText = (TextView) findViewById(R.id.dice);
        turnText  = (TextView) findViewById(R.id.turn);


        petty = (Button)findViewById(R.id.PettyButtonR1C0);
        euc = (Button)findViewById(R.id.EUCR1C2);
        foust = (Button)findViewById(R.id.FoustR3C0);
       curry = (Button)findViewById(R.id.curryR3C2);
        sullivan = (Button)findViewById(R.id.sullivanR3C4);
        jackson = (Button)findViewById(R.id.jacksonR3C6);
        mossman = (Button)findViewById(R.id.mossmanR1C4);
        bryan = (Button)findViewById(R.id.BryanR1C6);

        aPetty = (Button)findViewById(R.id.AttPetty);
        aCurry = (Button)findViewById(R.id.AttCurry);
        aSullivan = (Button)findViewById(R.id.AttSull);
        aEUC = (Button)findViewById(R.id.AttEUC);
       aBryan = (Button)findViewById(R.id.AttBryan);
        aFoust = (Button)findViewById(R.id.AttFoust);
        aMossman = (Button)findViewById(R.id.AttMoss);
        aJackson = (Button)findViewById(R.id.AttJack);
        end = (Button)findViewById(R.id.endturn);



        team1.setText(team1name + ":" + team1Troops);
        team1.setTextColor(Color.RED);
        team2.setText(team2name + ":" + team2Troops);
        team2.setTextColor(Color.BLUE);
        turnText.setText("Turn:"+team1name);

        petty.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        curry.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        foust.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        euc.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aEUC.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aCurry.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aPetty.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aFoust.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        end.setOnClickListener(new riskybidness.GameActivity.buttonListener());

        sullivan.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        jackson.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        mossman.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        bryan.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aBryan.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aSullivan.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aJackson.setOnClickListener(new riskybidness.GameActivity.buttonListener());
        aMossman.setOnClickListener(new riskybidness.GameActivity.buttonListener());

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();

        final ImageView myImageView = (ImageView) findViewById(R.id.roadR3C5);
        ViewTreeObserver vto = myImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // do something now when the object is loaded
                // e.g. find the real size of it etc
                myImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
               // runGame();
            }
        });

    }






    private class buttonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.curryR3C2:
                    //board setup case, adding troops to curry
                    if(gameSetup==true){
                    if(curry.getCurrentTextColor() != (Color.BLUE) && team1Turn==true && team1Troops > 0){
                                curry.setText((Integer.toString(Integer.parseInt(curry.getText().toString()) + 1)));
                                team1Troops--;
                                team1.setText(team1name + ":" + team1Troops);
                                curry.setTextColor(Color.RED);
                                Drawable d = getResources().getDrawable(R.drawable.curry_purple);
                                curry.setBackgroundDrawable(d);
                       // curry.setClickable(false);
                        petty.setClickable(false);
                        euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);
                        //switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }

                            }
                    else if(curry.getCurrentTextColor() != (Color.RED) && team2Turn==true &&team2Troops >0){
                        curry.setText((Integer.toString(Integer.parseInt(curry.getText().toString()) + 1)));
                        team2Troops--;
                        team2.setText(team2name + ":" + team2Troops);
                        curry.setTextColor(Color.BLUE);
                        Drawable d = getResources().getDrawable(R.drawable.curry_orange);
                        curry.setBackgroundDrawable(d);
                       // curry.setClickable(false);
                        petty.setClickable(false);
                        euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);
                        //switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }


                    }
                    }
                    ///once game setup is complete, buttons use these sub-cases, checks to see if buildings next to it are enemy and if they have enough troops to attack,
                    // if so, show attack option
                   else if(gameSetup==false&& team1Turn==true && Integer.parseInt(curry.getText().toString()) > 1){

                        if(curry.getCurrentTextColor() != euc.getCurrentTextColor() && euc.getCurrentTextColor()!= Color.RED ){
                             aEUC.setVisibility(View.VISIBLE);
                                euc.setClickable(false);

                        }
                        if(curry.getCurrentTextColor() != foust.getCurrentTextColor() && foust.getCurrentTextColor()!= Color.RED){
                            aFoust.setVisibility(View.VISIBLE);
                            foust.setClickable(false);
                        }
                        if(curry.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.RED){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                        currentBuild=curry;
                    }else if(gameSetup==false&& team2Turn==true && Integer.parseInt(curry.getText().toString()) > 1){

                        if(curry.getCurrentTextColor() != euc.getCurrentTextColor() && euc.getCurrentTextColor()!= Color.BLUE ){
                        aEUC.setVisibility(View.VISIBLE);
                        euc.setClickable(false);

                         }
                        if(curry.getCurrentTextColor() != foust.getCurrentTextColor() && foust.getCurrentTextColor()!= Color.BLUE){
                            aFoust.setVisibility(View.VISIBLE);
                            foust.setClickable(false);
                        }
                        if(curry.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.BLUE){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                    currentBuild=curry;
                }
                    //checks to see if the team owns this building, if so, they can add troops to the building
                    if(curry.getCurrentTextColor()!=Color.BLUE && team1Troops >=1&& team1Turn==true){
                        aCurry.setText("Add Troops");
                        aCurry.setVisibility(View.VISIBLE);
                        currentBuild=curry;
                    }else  if(curry.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aCurry.setText("Add Troops");
                        aCurry.setVisibility(View.VISIBLE);
                        currentBuild=curry;
                    }
                        break;
                case R.id.PettyButtonR1C0:
                    //game setup, place troops
                    if(gameSetup==true){
                    if(petty.getCurrentTextColor() !=(Color.BLUE) && team1Turn==true && team1Troops >0){
                                petty.setText((Integer.toString(Integer.parseInt(petty.getText().toString()) + 1)));
                                team1Troops--;
                                 team1.setText(team1name + ":" + team1Troops);
                                petty.setTextColor(Color.RED);
                                Drawable d = getResources().getDrawable(R.drawable.petty_purple);
                                petty.setBackgroundDrawable(d);
                        curry.setClickable(false);
                       // petty.setClickable(false);
                        euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);
                       // switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                           // setAttackInvisible();
                        }
                        }
                    else if(petty.getCurrentTextColor() !=(Color.RED) && team2Turn==true && team2Troops>0){
                        petty.setText((Integer.toString(Integer.parseInt(petty.getText().toString()) + 1)));
                        team2Troops--;
                        team2.setText(team2name + ":" + team2Troops);
                        petty.setTextColor(Color.BLUE);
                        Drawable d = getResources().getDrawable(R.drawable.petty_orange);
                        petty.setBackgroundDrawable(d);
                        curry.setClickable(false);
                       // petty.setClickable(false);
                        euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);
                       // switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                           // setAttackInvisible();
                        }

                    }}
                    else if(gameSetup==false&& team1Turn==true && Integer.parseInt(petty.getText().toString()) >1){

                        if(petty.getCurrentTextColor() != euc.getCurrentTextColor() && euc.getCurrentTextColor()!= Color.RED ){
                            aEUC.setVisibility(View.VISIBLE);
                            euc.setClickable(false);

                        }
                        if(petty.getCurrentTextColor() != foust.getCurrentTextColor() && foust.getCurrentTextColor()!= Color.RED){
                            aFoust.setVisibility(View.VISIBLE);
                            foust.setClickable(false);
                        }
                        currentBuild=petty;
                    }else  if(gameSetup==false&& team2Turn==true && Integer.parseInt(petty.getText().toString()) >1){

                        if(petty.getCurrentTextColor() != euc.getCurrentTextColor()&& euc.getCurrentTextColor()!= Color.BLUE ){
                            aEUC.setVisibility(View.VISIBLE);
                            euc.setClickable(false);
                        }
                        if(petty.getCurrentTextColor() != foust.getCurrentTextColor() && foust.getCurrentTextColor()!= Color.BLUE){
                            aFoust.setVisibility(View.VISIBLE);
                            foust.setClickable(false);
                        }
                        currentBuild=petty;
                    }
                   if(petty.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 &&team1Turn==true){
                        aPetty.setText("Add Troops");
                        aPetty.setVisibility(View.VISIBLE);
                        currentBuild=petty;
                    }else  if(petty.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aPetty.setText("Add Troops");
                        aPetty.setVisibility(View.VISIBLE);
                        currentBuild=petty;
                    }

                    break;


                case R.id.FoustR3C0:
                    if(gameSetup==true){
                    if(foust.getCurrentTextColor() != (Color.BLUE) && team1Turn==true && team1Troops > 0){
                        foust.setText((Integer.toString(Integer.parseInt(foust.getText().toString()) + 1)));
                        team1Troops--;
                        team1.setText(team1name + ":" + team1Troops);
                        foust.setTextColor(Color.RED);
                        Drawable d = getResources().getDrawable(R.drawable.foust_purple);
                        foust.setBackgroundDrawable(d);
                        curry.setClickable(false);
                        petty.setClickable(false);
                        euc.setClickable(false);
                       // foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);

                        //switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }

                    }
                    else if(foust.getCurrentTextColor() != (Color.RED) && team2Turn==true &&team2Troops >0){

                        foust.setText((Integer.toString(Integer.parseInt(foust.getText().toString()) + 1)));
                        team2Troops--;
                        team2.setText(team2name + ":" + team2Troops);
                        foust.setTextColor(Color.BLUE);
                        Drawable d = getResources().getDrawable(R.drawable.foust_orange);
                        foust.setBackgroundDrawable(d);
                        curry.setClickable(false);
                        petty.setClickable(false);
                        euc.setClickable(false);
                        //foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);

                       // switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }


                    }}
                   else if(gameSetup==false&& team1Turn==true && Integer.parseInt(foust.getText().toString()) >1){

                        if(foust.getCurrentTextColor() != petty.getCurrentTextColor() && petty.getCurrentTextColor()!= Color.RED ){
                            aPetty.setVisibility(View.VISIBLE);
                            petty.setClickable(false);
                        }
                        if(foust.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.RED){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        currentBuild=foust;
                    }else if(gameSetup==false&& team2Turn==true && Integer.parseInt(foust.getText().toString()) >1){

                        if(foust.getCurrentTextColor() != petty.getCurrentTextColor() && petty.getCurrentTextColor()!= Color.BLUE ){
                            aPetty.setVisibility(View.VISIBLE);
                            petty.setClickable(false);
                        }
                        if(foust.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.BLUE){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        currentBuild=foust;
                    }
                   if(foust.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 && team1Turn==true){
                        aFoust.setText("Add Troops");
                        aFoust.setVisibility(View.VISIBLE);
                        currentBuild=foust;
                    } else  if(foust.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aFoust.setText("Add Troops");
                        aFoust.setVisibility(View.VISIBLE);
                        currentBuild=foust;
                    }

                    break;

                case R.id.EUCR1C2:
                    if(gameSetup==true){
                    if(euc.getCurrentTextColor() != (Color.BLUE) && team1Turn==true && team1Troops > 0){
                        euc.setText((Integer.toString(Integer.parseInt(euc.getText().toString()) + 1)));
                        team1Troops--;
                        team1.setText(team1name + ":" + team1Troops);
                        euc.setTextColor(Color.RED);
                        Drawable d = getResources().getDrawable(R.drawable.euc_purple);
                        euc.setBackgroundDrawable(d);

                        //curry.setOnClickListener(null);
                        // gameRunning = false;
                        curry.setClickable(false);
                        petty.setClickable(false);
                       // euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);
                        //switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }



                    }
                    else if(euc.getCurrentTextColor() != (Color.RED) && team2Turn==true &&team2Troops >0) {


                        euc.setText((Integer.toString(Integer.parseInt(euc.getText().toString()) + 1)));
                        team2Troops--;
                        team2.setText(team2name + ":" + team2Troops);
                        euc.setTextColor(Color.BLUE);
                        Drawable d = getResources().getDrawable(R.drawable.euc_orange);
                        euc.setBackgroundDrawable(d);
                        curry.setClickable(false);
                        petty.setClickable(false);
                       // euc.setClickable(false);
                        foust.setClickable(false);
                        bryan.setClickable(false);
                        mossman.setClickable(false);
                        sullivan.setClickable(false);
                        jackson.setClickable(false);

                        //switchTurns();
                        if(team1Troops==0 && team2Troops ==0){
                            gameSetup=false;
                        }
                    }}
                       else if(gameSetup==false&& team1Turn==true && Integer.parseInt(euc.getText().toString()) >1){


                        if(euc.getCurrentTextColor() != petty.getCurrentTextColor() && petty.getCurrentTextColor()!= Color.RED ){
                                aPetty.setVisibility(View.VISIBLE);
                                petty.setClickable(false);

                            }
                        if(euc.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.RED){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        if(euc.getCurrentTextColor() != mossman.getCurrentTextColor() && mossman.getCurrentTextColor()!= Color.RED){
                            aMossman.setVisibility(View.VISIBLE);
                            mossman.setClickable(false);
                        }
                            currentBuild=euc;
                        }else if(gameSetup==false&& team2Turn==true && Integer.parseInt(euc.getText().toString()) >1){


                        if(euc.getCurrentTextColor() != petty.getCurrentTextColor() && petty.getCurrentTextColor()!= Color.BLUE){
                                aPetty.setVisibility(View.VISIBLE);
                                petty.setClickable(false);

                            }
                        if(euc.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.BLUE){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        if(euc.getCurrentTextColor() != mossman.getCurrentTextColor() && mossman.getCurrentTextColor()!= Color.BLUE){
                            aMossman.setVisibility(View.VISIBLE);
                           mossman.setClickable(false);
                        }
                            currentBuild=euc;
                        }
                    if(euc.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 && team1Turn==true){
                        aEUC.setText("Add Troops");
                        aEUC.setVisibility(View.VISIBLE);
                        currentBuild=euc;
                    }else  if(euc.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aEUC.setText("Add Troops");
                        aEUC.setVisibility(View.VISIBLE);
                        currentBuild=euc;
                    }

                    break;

                case R.id.mossmanR1C4:
                    if(gameSetup==true){
                        if(mossman.getCurrentTextColor() != (Color.BLUE) && team1Turn==true && team1Troops > 0){
                            mossman.setText((Integer.toString(Integer.parseInt(mossman.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                            mossman.setTextColor(Color.RED);
                            Drawable d = getResources().getDrawable(R.drawable.mossman_purple);
                            mossman.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                           // mossman.setClickable(false);
                            sullivan.setClickable(false);
                            jackson.setClickable(false);
                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }



                        }
                        else if(mossman.getCurrentTextColor() != (Color.RED) && team2Turn==true &&team2Troops >0){
                            mossman.setText((Integer.toString(Integer.parseInt(mossman.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                            mossman.setTextColor(Color.BLUE);
                            Drawable d = getResources().getDrawable(R.drawable.mossman_orange);
                            mossman.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                          //  mossman.setClickable(false);
                            sullivan.setClickable(false);
                            jackson.setClickable(false);

                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }


                        }
                    }
                    else if(gameSetup==false&& team1Turn==true && Integer.parseInt(mossman.getText().toString()) > 1){

                        if(mossman.getCurrentTextColor() != euc.getCurrentTextColor() && euc.getCurrentTextColor()!= Color.RED ){
                            aEUC.setVisibility(View.VISIBLE);
                            euc.setClickable(false);
                        }
                        if(mossman.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.RED){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                        if(mossman.getCurrentTextColor() != bryan.getCurrentTextColor() && bryan.getCurrentTextColor()!= Color.RED){
                            aBryan.setVisibility(View.VISIBLE);
                            bryan.setClickable(false);
                        }
                        currentBuild=mossman;
                    }else if(gameSetup==false&& team2Turn==true && Integer.parseInt(mossman.getText().toString()) > 1){

                        if(mossman.getCurrentTextColor() != euc.getCurrentTextColor() && euc.getCurrentTextColor()!= Color.BLUE ){
                            aEUC.setVisibility(View.VISIBLE);
                            euc.setClickable(false);

                        }
                        if(mossman.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.BLUE){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                        if(mossman.getCurrentTextColor() != bryan.getCurrentTextColor() && bryan.getCurrentTextColor()!= Color.BLUE){
                            aBryan.setVisibility(View.VISIBLE);
                            bryan.setClickable(false);
                        }
                        currentBuild=mossman;
                    }
                    if(mossman.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 && team1Turn==true){
                        aMossman.setText("Add Troops");
                        aMossman.setVisibility(View.VISIBLE);
                        currentBuild=mossman;
                    }else  if(mossman.getCurrentTextColor()!=Color.RED && team2Troops >=1 &&team2Turn==true){
                        aMossman.setText("Add Troops");
                        aMossman.setVisibility(View.VISIBLE);
                        currentBuild=mossman;
                    }
                    break;

                case R.id.sullivanR3C4:
                    if(gameSetup==true){
                        if(sullivan.getCurrentTextColor() != (Color.BLUE) && team1Turn==true && team1Troops > 0){
                            sullivan.setText((Integer.toString(Integer.parseInt(sullivan.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                            sullivan.setTextColor(Color.RED);
                            Drawable d = getResources().getDrawable(R.drawable.sullivan_purple);
                            sullivan.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                            mossman.setClickable(false);
                           // sullivan.setClickable(false);
                            jackson.setClickable(false);
                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }



                        }
                        else if(sullivan.getCurrentTextColor() != (Color.RED) && team2Turn==true &&team2Troops >0){
                            sullivan.setText((Integer.toString(Integer.parseInt(sullivan.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                            sullivan.setTextColor(Color.BLUE);
                            Drawable d = getResources().getDrawable(R.drawable.sullivan_orange);
                            sullivan.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                            mossman.setClickable(false);
                           // sullivan.setClickable(false);
                            jackson.setClickable(false);
                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }


                        }
                    }////selecting attck from point
                    else if(gameSetup==false&& team1Turn==true && Integer.parseInt(sullivan.getText().toString()) > 1){


                        if(sullivan.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.RED ){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        if(sullivan.getCurrentTextColor() != jackson.getCurrentTextColor() && jackson.getCurrentTextColor()!= Color.RED){
                            aJackson.setVisibility(View.VISIBLE);
                            jackson.setClickable(false);
                        }
                        if(sullivan.getCurrentTextColor() != mossman.getCurrentTextColor() && mossman.getCurrentTextColor()!= Color.RED){
                            aMossman.setVisibility(View.VISIBLE);
                            mossman.setClickable(false);
                        }
                        currentBuild=sullivan;
                    }else if(gameSetup==false&& team2Turn==true && Integer.parseInt(sullivan.getText().toString()) > 1){

                        if(sullivan.getCurrentTextColor() != curry.getCurrentTextColor() && curry.getCurrentTextColor()!= Color.BLUE ){
                            aCurry.setVisibility(View.VISIBLE);
                            curry.setClickable(false);
                        }
                        if(sullivan.getCurrentTextColor() != jackson.getCurrentTextColor() && jackson.getCurrentTextColor()!= Color.BLUE){
                            aJackson.setVisibility(View.VISIBLE);
                            jackson.setClickable(false);
                        }
                        if(sullivan.getCurrentTextColor() != mossman.getCurrentTextColor() && mossman.getCurrentTextColor()!= Color.BLUE){
                            aMossman.setVisibility(View.VISIBLE);
                            mossman.setClickable(false);
                        }
                        currentBuild=sullivan;
                    }
                    if(sullivan.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aSullivan.setText("Add Troops");
                        aSullivan.setVisibility(View.VISIBLE);
                        currentBuild=sullivan;
                    }else  if(sullivan.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 &&team1Turn==true){
                        aSullivan.setText("Add Troops");
                        aSullivan.setVisibility(View.VISIBLE);
                        currentBuild=sullivan;
                    }
                    break;

                case R.id.BryanR1C6:
                    //game setup, place troops
                    if(gameSetup==true){
                        if(bryan.getCurrentTextColor() !=(Color.BLUE) && team1Turn==true && team1Troops >0){
                            bryan.setText((Integer.toString(Integer.parseInt(bryan.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                            bryan.setTextColor(Color.RED);
                            Drawable d = getResources().getDrawable(R.drawable.bryan_purple);
                            bryan.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                           // bryan.setClickable(false);
                            mossman.setClickable(false);
                            sullivan.setClickable(false);
                            jackson.setClickable(false);
                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }
                        }
                        else if(bryan.getCurrentTextColor() !=(Color.RED) && team2Turn==true && team2Troops>0){

                            bryan.setText((Integer.toString(Integer.parseInt(bryan.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                            bryan.setTextColor(Color.BLUE);
                            Drawable d = getResources().getDrawable(R.drawable.bryan_orange);
                            bryan.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            //bryan.setClickable(false);
                            mossman.setClickable(false);
                            sullivan.setClickable(false);
                            jackson.setClickable(false);
                            //switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }

                        }}
                    ////selecting attck from point
                    else if(gameSetup==false&& team1Turn==true && Integer.parseInt(bryan.getText().toString()) >1){

                        if(bryan.getCurrentTextColor() != mossman.getCurrentTextColor() && mossman.getCurrentTextColor()!= Color.RED ){
                            aMossman.setVisibility(View.VISIBLE);
                            mossman.setClickable(false);

                        }
                        if(bryan.getCurrentTextColor() != jackson.getCurrentTextColor() && jackson.getCurrentTextColor()!= Color.RED){
                            aJackson.setVisibility(View.VISIBLE);
                            jackson.setClickable(false);
                        }
                        currentBuild=bryan;
                    }else  if(gameSetup==false&& team2Turn==true && Integer.parseInt(bryan.getText().toString()) >1){

                        if(bryan.getCurrentTextColor() != mossman.getCurrentTextColor()&& mossman.getCurrentTextColor()!= Color.BLUE ){
                            aMossman.setVisibility(View.VISIBLE);
                            mossman.setClickable(false);
                        }
                        if(bryan.getCurrentTextColor() != jackson.getCurrentTextColor() && jackson.getCurrentTextColor()!= Color.BLUE){
                            aJackson.setVisibility(View.VISIBLE);
                            jackson.setClickable(false);
                        }
                        currentBuild=bryan;
                    }
                     if(bryan.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 && team1Turn==true){
                        aBryan.setText("Add Troops");
                        aBryan.setVisibility(View.VISIBLE);
                        currentBuild=bryan;
                    }else if(bryan.getCurrentTextColor()!=Color.RED && team2Troops >=1 &&team2Turn==true){
                        aBryan.setText("Add Troops");
                        aBryan.setVisibility(View.VISIBLE);
                        currentBuild=bryan;
                    }

                    break;

                case R.id.jacksonR3C6:
                    //game setup, place troops
                    if(gameSetup==true){
                        if(jackson.getCurrentTextColor() !=(Color.BLUE) && team1Turn==true && team1Troops >0){
                            jackson.setText((Integer.toString(Integer.parseInt(jackson.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                            jackson.setTextColor(Color.RED);
                            Drawable d = getResources().getDrawable(R.drawable.jackson_purple);
                            jackson.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                            mossman.setClickable(false);
                            sullivan.setClickable(false);
                          //  jackson.setClickable(false);
                            //switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }

                        }
                        else if(jackson.getCurrentTextColor() !=(Color.RED) && team2Turn==true && team2Troops>0){

                            jackson.setText((Integer.toString(Integer.parseInt(jackson.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                            jackson.setTextColor(Color.BLUE);
                            Drawable d = getResources().getDrawable(R.drawable.jackson_orange);
                            jackson.setBackgroundDrawable(d);
                            curry.setClickable(false);
                            petty.setClickable(false);
                            euc.setClickable(false);
                            foust.setClickable(false);
                            bryan.setClickable(false);
                            mossman.setClickable(false);
                            sullivan.setClickable(false);
                          //  jackson.setClickable(false);
                           // switchTurns();
                            if(team1Troops==0 && team2Troops ==0){
                                gameSetup=false;
                            }

                        }}
                    ////selecting attck from point
                    else if(gameSetup==false&& team1Turn==true && Integer.parseInt(bryan.getText().toString()) >1){


                        if(jackson.getCurrentTextColor() != bryan.getCurrentTextColor() && bryan.getCurrentTextColor()!= Color.RED ){
                            aBryan.setVisibility(View.VISIBLE);
                            bryan.setClickable(false);

                        }
                        if(jackson.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.RED){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                        currentBuild=jackson;
                    }else  if(gameSetup==false&& team2Turn==true && Integer.parseInt(bryan.getText().toString()) >1){

                        if(jackson.getCurrentTextColor() != bryan.getCurrentTextColor()&& bryan.getCurrentTextColor()!= Color.BLUE ){
                            aBryan.setVisibility(View.VISIBLE);
                            bryan.setClickable(false);
                        }
                        if(jackson.getCurrentTextColor() != sullivan.getCurrentTextColor() && sullivan.getCurrentTextColor()!= Color.BLUE){
                            aSullivan.setVisibility(View.VISIBLE);
                            sullivan.setClickable(false);
                        }
                        currentBuild=jackson;
                    }
                    if(jackson.getCurrentTextColor()!=Color.BLUE && team1Troops >=1 && team1Turn==true){
                        aJackson.setText("Add Troops");
                        aJackson.setVisibility(View.VISIBLE);
                        currentBuild=jackson;
                    } else  if(jackson.getCurrentTextColor()!=Color.RED && team2Troops >=1 && team2Turn==true){
                        aJackson.setText("Add Troops");
                        aJackson.setVisibility(View.VISIBLE);
                        currentBuild=jackson;
                    }

                    break;


///**********************************attacking cases begin here***********************************************
                case R.id.AttCurry:
                    //if the buttons string is attack throw up a toast saying that building is being attacked
                    //then set the building attacked string to this building and send the attack method the string, button and current building
                    // which is the one that was click before to get the attack option.
                    if(aCurry.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Curry",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Curry",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "curry";
                        attack(curry, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                        ///if the button string is Add Troops, the player can then add reserve troops to that building
                    }else if(aCurry.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&curry.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            curry.setText((Integer.toString(Integer.parseInt(curry.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && curry.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            curry.setText((Integer.toString(Integer.parseInt(curry.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);

                        }

                    }
                    break;
                case R.id.AttPetty:
                    if(aPetty.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Petty",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Petty",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "petty";
                        attack(petty, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aPetty.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&petty.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            petty.setText((Integer.toString(Integer.parseInt(petty.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && petty.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            petty.setText((Integer.toString(Integer.parseInt(petty.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;
                case R.id.AttFoust:
                    if(aFoust.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Foust",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Foust",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "foust";
                        attack(foust, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aFoust.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&foust.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            foust.setText((Integer.toString(Integer.parseInt(foust.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && foust.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            foust.setText((Integer.toString(Integer.parseInt(foust.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;
                case R.id.AttEUC:
                    if(aEUC.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking EUC",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking EUC",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "euc";
                        attack(euc, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aEUC.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&euc.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            euc.setText((Integer.toString(Integer.parseInt(euc.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && euc.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            euc.setText((Integer.toString(Integer.parseInt(euc.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;

                case R.id.AttSull:
                    if(aSullivan.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Sullivan",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Sullivan",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "sullivan";
                        attack(sullivan, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aSullivan.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&sullivan.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            sullivan.setText((Integer.toString(Integer.parseInt(sullivan.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && sullivan.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            sullivan.setText((Integer.toString(Integer.parseInt(sullivan.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;

                case R.id.AttMoss:
                    if(aMossman.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Mossman",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Mossman",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "mossman";
                        attack(mossman, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aMossman.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true &&mossman.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                           mossman.setText((Integer.toString(Integer.parseInt(mossman.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && mossman.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            mossman.setText((Integer.toString(Integer.parseInt(mossman.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;

                case R.id.AttBryan:

                    ///to attack
                    if(aBryan.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Bryan",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Bryan",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "bryan";
                        attack(bryan, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                        //add troops from reserve
                    }else if(aBryan.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true && bryan.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            bryan.setText((Integer.toString(Integer.parseInt(bryan.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && bryan.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            bryan.setText((Integer.toString(Integer.parseInt(bryan.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;

                case R.id.AttJack:
                    if(aJackson.getText().toString().equalsIgnoreCase("Attack")) {
                        if (team1Turn == true) {
                            Toast.makeText(getApplicationContext(), team1name + " Attacking Jackson",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), team2name + " Attacking Jackson",
                                    Toast.LENGTH_SHORT).show();
                        }
                        attacked = "jackson";
                        attack(jackson, currentBuild, attacked);
                        curry.setClickable(true);
                        petty.setClickable(true);
                        euc.setClickable(true);
                        foust.setClickable(true);
                        bryan.setClickable(true);
                        mossman.setClickable(true);
                        sullivan.setClickable(true);
                        jackson.setClickable(true);
                    }else if(aJackson.getText().toString().equalsIgnoreCase("Add Troops")){
                        if(team1Turn==true && jackson.getCurrentTextColor()==Color.RED && team1Troops >=1) {
                            jackson.setText((Integer.toString(Integer.parseInt(jackson.getText().toString()) + 1)));
                            team1Troops--;
                            team1.setText(team1name + ":" + team1Troops);
                        }else if(team2Turn==true && jackson.getCurrentTextColor()==Color.BLUE && team2Troops >=1) {
                            jackson.setText((Integer.toString(Integer.parseInt(jackson.getText().toString()) + 1)));
                            team2Troops--;
                            team2.setText(team2name + ":" + team2Troops);
                        }
                    }
                    break;
                //calls the switch turn method and takes away all attack buttons.
                case R.id.endturn:
                  String board=  sendBoardInfo(); //this sends the opposite teams turn so it must be called before the switch turn ends
                    switchTurns();
                    setAttackInvisible();
                   // receiveBoardInfo(board);
            }

        }
    }

    public void switchTurns(){

        if(team1Turn==true){
            team1Turn = false;
            team2Turn = true;
               turnText.setText("Turn:" +team2name);
         /*   Toast.makeText(getApplicationContext(), "It is now Team 2's turn",
                    Toast.LENGTH_SHORT).show(); */
         // switchTurns();
            setAttackInvisible();
            ///give troop bonuses if game setup is over
            if(gameSetup==true){
            //do nothing
            }
           else if(gameSetup == false){
                ///if team has these territories get extra troops
                if(petty.getCurrentTextColor() == foust.getCurrentTextColor()&& petty.getCurrentTextColor() ==curry.getCurrentTextColor() && petty.getCurrentTextColor()== Color.RED){
                    team1Troops=team1Troops+8;
                }
                if(bryan.getCurrentTextColor() == jackson.getCurrentTextColor()&& mossman.getCurrentTextColor() ==bryan.getCurrentTextColor() && bryan.getCurrentTextColor()== Color.RED){
                    team1Troops=team1Troops+8;
                }

                ///then add 2 per territory owned
                if(petty.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(foust.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(curry.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(euc.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(sullivan.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(jackson.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                if(bryan.getCurrentTextColor()==Color.RED){
                    team1Troops=team1Troops+2;
                }
                team1.setText(team1name + ":" +team1Troops);
                //gameSetup=true;
            }

        }
       else if(team2Turn==true){
            team1Turn = true;
            team2Turn = false;
            turnText.setText("Turn:" +team1name);

          /*  Toast.makeText(getApplicationContext(), "It is now Team 1's turn",
                    Toast.LENGTH_SHORT).show(); */
          // switchTurns();
            setAttackInvisible();
            if(gameSetup==true){

            }
            else if(gameSetup == false){
                ///if team has these territories get extra troops
                if(petty.getCurrentTextColor() == foust.getCurrentTextColor()&& petty.getCurrentTextColor() ==curry.getCurrentTextColor() && petty.getCurrentTextColor()== Color.BLUE){
                    team2Troops=team2Troops+8;
                }
                if(bryan.getCurrentTextColor() == jackson.getCurrentTextColor()&& mossman.getCurrentTextColor() ==bryan.getCurrentTextColor() && bryan.getCurrentTextColor()== Color.BLUE){
                    team2Troops=team2Troops+8;
                }

                ///then add 2 per territory owned
                if(petty.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(foust.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(curry.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(euc.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(sullivan.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(jackson.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                if(bryan.getCurrentTextColor()==Color.BLUE){
                    team2Troops=team2Troops+2;
                }
                team2.setText(team2name + ":" +team2Troops);
                //gameSetup=true;
            }

        }
        aBryan.setText("Attack");
        aCurry.setText("Attack");
        aMossman.setText("Attack");
        aJackson.setText("Attack");
        aPetty.setText("Attack");
        aEUC.setText("Attack");
        aFoust.setText("Attack");
        aSullivan.setText("Attack");


    }
    //takes in button from where its attacking from
    public void attack(Button defending, Button attacking, String attackedName){
        setNotClickable(attacking);
        int defTroops = Integer.parseInt(defending.getText().toString());
        int attTroops = Integer.parseInt(attacking.getText().toString());
        int die1 = dice1.nextInt(6)+1;
        int die2 = dice2.nextInt(6)+1;
        int die3 = dice3.nextInt(6)+1;
        int die4 = dice3.nextInt(6)+1;
        int die5 = dice1.nextInt(6)+1;
        int attVal1=die3;
        int attVal2;
        int defVal1;
        int defVal2;
        String pic = attackedName + "_purple";
        Drawable change= getResources().getDrawable(getResources().getIdentifier(pic, "drawable", getPackageName()));

        if(defending.getCurrentTextColor() == Color.BLUE) {
             pic = attackedName + "_purple";
             change = getResources().getDrawable(getResources().getIdentifier(pic, "drawable", getPackageName()));
        }else{
             pic = attackedName + "_orange";
            change = getResources().getDrawable(getResources().getIdentifier(pic, "drawable", getPackageName()));
        }
        attText.setTextSize(10);
        attText.setText("Attacker: " + die1 + ","+ die2 + ","+ die3 + "\nDefender:" + die4 + "," + die5);
        ///find first attacker die (greatest)
        if(die1>= die2 && die1>= die3){
            attVal1=die1;

        }else if(die2>= die1 && die2>= die3){
            attVal1=die2;
        }else if(die3>= die1 && die3>= die2){
            attVal1=die3;
        }
        if(die1 < attVal1 && die1 >= die3){
            attVal2=die1;
        }else if(die1 < attVal1 && die1 >= die2){
            attVal2=die1;
        }else if(die2< attVal1 && die2>= die1){
            attVal2=die2;
        }else if(die2 < attVal1 && die2 >= die3){
            attVal2=die2;
        }else if(die3 < attVal1 && die3 >= die1){
            attVal2=die3;
        }else if(die3 < attVal1 && die3 >= die2){
            attVal2=die3;
        }
        else{
            attVal2=die3;
        }/// need to check the higher def die, the two biggest die are grouped
        if(die4>=die5){
            defVal1=die4;
            defVal2=die5;
        }else{
            defVal2=die4;
            defVal1=die5;
        }


        //first die
        boolean loop = true;
        while(loop==true) {
            //att dice > def dice
            if (attVal1 > defVal1 && attTroops > 1 && defTroops >= 1) {
                defending.setText(Integer.toString(--defTroops));
                loop = false;

            } else if (attVal1 < defVal1 && attTroops > 1 && defTroops >= 1) {
                attTroops--;
                if (attTroops > 1) {
                    attacking.setText(Integer.toString(attTroops));
                    loop=false;
                } else {
                    attacking.setText(Integer.toString(1));
                    setAttackInvisible();
                    switchTurns();
                    break;
                }
            } else if (attVal1 == defVal1) {
                attTroops--;

                if (attTroops > 1) {
                    attacking.setText(Integer.toString(attTroops));
                    loop=false;
                } else {
                    attacking.setText(Integer.toString(1));
                   // defending.setText(Integer.toString(--defTroops));
                    setAttackInvisible();
                    switchTurns();
                    break;
                }
               // defending.setText(Integer.toString(--defTroops));
                loop=false;

            }
            //second die
            if (attVal2 > defVal2 && attTroops > 1 && defTroops >= 1) {
                defending.setText(Integer.toString(--defTroops));
                loop=false;
            } else if (attVal2 < defVal2 && attTroops > 1 && defTroops >= 1) {
                attTroops--;
                if (attTroops > 1) {
                    attacking.setText(Integer.toString(attTroops));
                    loop=false;
                } else {
                    attacking.setText(Integer.toString(1));
                    setAttackInvisible();
                    switchTurns();
                    break;
                }
                loop=false;

            } else if (attVal2 == defVal2) {
                //defTroops--;
                attTroops--;
                if (attTroops > 1) {
                    attacking.setText(Integer.toString(attTroops));
                    loop=false;
                } else {
                    attacking.setText(Integer.toString(1));
                    //defending.setText(Integer.toString(--defTroops));
                    setAttackInvisible();
                    switchTurns();
                    break;
                }
                //defending.setText(Integer.toString(--defTroops));
                loop=false;

            }

            if (attTroops == 1) {  //attacker out of troops
                setAttackInvisible();
                //attText.setText(null);
                switchTurns();
                break;
            } else {

                // setAttackInvisible();
                //attText.setText(null);
                // switchTurns();
                loop=false;
            }
            //defending team is out of troops, attacker takes over
            if (defTroops <= 0) {
                defending.setBackgroundDrawable(change);
                defending.setTextColor(attacking.getTextColors());
                if (attTroops > 1) {
                    defending.setText(Integer.toString(attTroops / 2));
                    attacking.setText(Integer.toString(attTroops / 2));

                } else {
                    defending.setText(Integer.toString(1));
                }
                if (attTroops == 1){
                setAttackInvisible();
                 switchTurns();
                break;}
                else {

                    setAttackInvisible();
                    //attText.setText(null);
                    switchTurns();
                    break;
            }


        }
            setAttackInvisible();
            //switchTurns();

    }
        ////check to see if the game is won. (if team owns all buuldings call next activity passing the winning team name string
        if(curry.getCurrentTextColor()==petty.getCurrentTextColor() && curry.getCurrentTextColor()== sullivan.getCurrentTextColor()
                && curry.getCurrentTextColor()== jackson.getCurrentTextColor() && curry.getCurrentTextColor()== euc.getCurrentTextColor()
                && curry.getCurrentTextColor()== foust.getCurrentTextColor() && curry.getCurrentTextColor()== mossman.getCurrentTextColor() &&
                curry.getCurrentTextColor()== bryan.getCurrentTextColor() ){
            if(curry.getCurrentTextColor()==Color.RED){
                Intent winner = new Intent(this, WinnerScreen.class);
                Bundle outgoingBundle = new Bundle();
                winner.putExtras(outgoingBundle);
                winner.putExtra("key", team1name);
                gameWon=true;
                startActivityForResult(winner, 1);
            }else if(curry.getCurrentTextColor()==Color.BLUE){
                Intent winner = new Intent(this, WinnerScreen.class);
                winner.putExtra("key", team2name);
                gameWon=true;
                startActivityForResult(winner, 1);

            }

        }
    }
    public void setAttackInvisible(){
        aFoust.setVisibility(View.INVISIBLE);
        aEUC.setVisibility(View.INVISIBLE);
        aCurry.setVisibility(View.INVISIBLE);
        aPetty.setVisibility(View.INVISIBLE);
        aJackson.setVisibility(View.INVISIBLE);
        aMossman.setVisibility(View.INVISIBLE);
        aBryan.setVisibility(View.INVISIBLE);
        aSullivan.setVisibility(View.INVISIBLE);
        curry.setClickable(true);
        petty.setClickable(true);
        euc.setClickable(true);
        foust.setClickable(true);
        bryan.setClickable(true);
        mossman.setClickable(true);
        sullivan.setClickable(true);
        jackson.setClickable(true);
    }

    //sets all other buttons not clickable except the current.
    public void setNotClickable(Button b){

        if(!b.equals(curry)){
            curry.setClickable(false);
        }
        if(!b.equals(petty)){
            petty.setClickable(false);
        }
        if(!b.equals(euc)){
            euc.setClickable(false);
        }
        if(!b.equals(foust)){
            foust.setClickable(false);
        }
        if(!b.equals(sullivan)){
            sullivan.setClickable(false);
        }
        if(!b.equals(bryan)){
            bryan.setClickable(false);
        }
        if(!b.equals(jackson)){
            jackson.setClickable(false);
        }
        if(!b.equals(mossman)){
            mossman.setClickable(false);
        }

    }
    //puts the game boardupdated into a string line then sends it to the other device
    public String sendBoardInfo(){

     String turn="";
     if(team1Turn==true){
         turn =team2name;
     }else if(team2Turn==true){
         turn = team1name;
     }


     String gameLine = "Turn:" + turn + " Team1Troops:"+ team1Troops + " Team2Troops:" + team2Troops +" 1.Foust:" + foust.getText().toString() + "," + String.valueOf(foust.getCurrentTextColor()) +
            " 2.Curry:"+ curry.getText().toString() + "," + String.valueOf(curry.getCurrentTextColor()) +
            " 3.EUC:" + euc.getText().toString() + "," + String.valueOf(euc.getCurrentTextColor()) +
        " 4.Petty:" + petty.getText().toString() + "," + String.valueOf(petty.getCurrentTextColor()) +
            " 5.Sullivan:" + sullivan.getText().toString() + "," + String.valueOf(sullivan.getCurrentTextColor()) +
            " 6.Jackson:"+ jackson.getText().toString() + "," + String.valueOf(jackson.getCurrentTextColor())+
        " 7.Bryan:" + bryan.getText().toString() + "," + String.valueOf(bryan.getCurrentTextColor()) +
            " 8.Mossman:" + mossman.getText().toString() + "," + String.valueOf(mossman.getCurrentTextColor());
            return gameLine;
    }

    public void receiveBoardInfo(String board){
        if(board.contains(team1name)){
            team1Turn=true;
        }else if(board.contains(team2name)){
            team2Turn=true;
        }
        String team1 = board.substring(12+board.indexOf("Team1Troops:"), board.indexOf("Team2Troops:"));
        String team2 = board.substring(12+board.indexOf("Team2Troops:"), board.indexOf("1."));
        String foustLine = board.substring(board.indexOf("1."), board.indexOf("2."));
        String curryLine = board.substring(board.indexOf("2."), board.indexOf("3."));
        String eucLine = board.substring(board.indexOf("3."), board.indexOf("4."));
        String pettyLine = board.substring(board.indexOf("4."), board.indexOf("5."));
        String sullivanLine= board.substring(board.indexOf("5."), board.indexOf("6."));
        String jacksonLine = board.substring(board.indexOf("6."), board.indexOf("7."));
        String bryanLine = board.substring(board.indexOf("7."), board.indexOf("8."));;
        String mossmanLine = board.substring(board.indexOf("8."));

        team1Troops =Integer.parseInt(team1);
        team2Troops=Integer.parseInt(team2);

        if(foustLine.contains(String.valueOf(Color.RED))){
            foust.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.foust_purple);
            foust.setBackgroundDrawable(d);
            String troops = foustLine.substring(1+foustLine.indexOf(":"), foustLine.indexOf(","));
            foust.setText(Integer.parseInt(troops));
        }else if(foustLine.contains(String.valueOf(Color.BLUE))){
            foust.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.foust_orange);
            foust.setBackgroundDrawable(d);
            String troops = foustLine.substring(1+foustLine.indexOf(":"), foustLine.indexOf(","));
            foust.setText(Integer.parseInt(troops));
        }

        if(curryLine.contains(String.valueOf(Color.RED))){
            curry.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.curry_purple);
            curry.setBackgroundDrawable(d);
            String troops = curryLine.substring(1+curryLine.indexOf(":"), curryLine.indexOf(","));
            curry.setText(Integer.parseInt(troops));
        }else if(curryLine.contains(String.valueOf(Color.BLUE))){
            curry.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.curry_orange);
            curry.setBackgroundDrawable(d);
            String troops = curryLine.substring(1+curryLine.indexOf(":"), curryLine.indexOf(","));
            curry.setText(Integer.parseInt(troops));
        }
        if(eucLine.contains(String.valueOf(Color.RED))){
            euc.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.euc_purple);
            euc.setBackgroundDrawable(d);
            String troops = eucLine.substring(1+eucLine.indexOf(":"), eucLine.indexOf(","));
            euc.setText(Integer.parseInt(troops));
        }else if(eucLine.contains(String.valueOf(Color.BLUE))){
            euc.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.euc_orange);
            euc.setBackgroundDrawable(d);
            String troops = eucLine.substring(1+eucLine.indexOf(":"), eucLine.indexOf(","));
            euc.setText(Integer.parseInt(troops));
        }
        if(pettyLine.contains(String.valueOf(Color.RED))){
            petty.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.petty_purple);
            petty.setBackgroundDrawable(d);
            String troops = pettyLine.substring(1+pettyLine.indexOf(":"), pettyLine.indexOf(","));
            petty.setText(Integer.parseInt(troops));
        }else if(pettyLine.contains(String.valueOf(Color.BLUE))){
            petty.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.petty_orange);
            petty.setBackgroundDrawable(d);
            String troops = pettyLine.substring(1+pettyLine.indexOf(":"), pettyLine.indexOf(","));
            petty.setText(Integer.parseInt(troops));
        }
        if(sullivanLine.contains(String.valueOf(Color.RED))){
            sullivan.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.sullivan_purple);
            sullivan.setBackgroundDrawable(d);
            String troops = sullivanLine.substring(1+sullivanLine.indexOf(":"), sullivanLine.indexOf(","));
            sullivan.setText(Integer.parseInt(troops));
        }else if(sullivanLine.contains(String.valueOf(Color.BLUE))){
            sullivan.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.sullivan_orange);
            sullivan.setBackgroundDrawable(d);
            String troops = sullivanLine.substring(1+sullivanLine.indexOf(":"), sullivanLine.indexOf(","));
            sullivan.setText(Integer.parseInt(troops));
        }
        if(jacksonLine.contains(String.valueOf(Color.RED))){
            jackson.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.jackson_purple);
            jackson.setBackgroundDrawable(d);
            String troops = jacksonLine.substring(1+jacksonLine.indexOf(":"), jacksonLine.indexOf(","));
            jackson.setText(Integer.parseInt(troops));
        }else if(jacksonLine.contains(String.valueOf(Color.BLUE))){
            jackson.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.jackson_orange);
            jackson.setBackgroundDrawable(d);
            String troops = jacksonLine.substring(1+jacksonLine.indexOf(":"), jacksonLine.indexOf(","));
            jackson.setText(Integer.parseInt(troops));
        }
        if(bryanLine.contains(String.valueOf(Color.RED))){
            bryan.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.bryan_purple);
            bryan.setBackgroundDrawable(d);
            String troops = bryanLine.substring(1+bryanLine.indexOf(":"), bryanLine.indexOf(","));
            bryan.setText(Integer.parseInt(troops));
        }else if(bryanLine.contains(String.valueOf(Color.BLUE))){
            bryan.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.bryan_orange);
            bryan.setBackgroundDrawable(d);
            String troops = bryanLine.substring(1+bryanLine.indexOf(":"), bryanLine.indexOf(","));
            bryan.setText(Integer.parseInt(troops));
        }
        if(mossmanLine.contains(String.valueOf(Color.RED))){
            mossman.setTextColor(Color.RED);
            Drawable d = getResources().getDrawable(R.drawable.mossman_purple);
            mossman.setBackgroundDrawable(d);
            String troops = mossmanLine.substring(1+mossmanLine.indexOf(":"), mossmanLine.indexOf(","));
            mossman.setText(Integer.parseInt(troops));
        }else if(mossmanLine.contains(String.valueOf(Color.BLUE))){
            mossman.setTextColor(Color.BLUE);
            Drawable d = getResources().getDrawable(R.drawable.mossman_orange);
            mossman.setBackgroundDrawable(d);
            String troops = mossmanLine.substring(1+mossmanLine.indexOf(":"), mossmanLine.indexOf(","));
            mossman.setText(Integer.parseInt(troops));
        }


    }
    public class ClientThread implements Runnable {
        private boolean connected = false;
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, HostLobby.SERVERPORT);
                connected = true;
                boolean firstLine = true;
                boolean firstReply = true;
                String line;
                while (connected) {
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        // WHERE YOU ISSUE THE COMMANDS
                        if (firstLine) {
                            out.flush();
                            out.println(team2name);
                            firstLine = false;
                        }
                        out.flush();
                        out.println(team2name);
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }

                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        line = in.readLine();
                        Log.d("ServerActivity", line);
                        if (firstReply) {
                            team1name = line;
                            firstReply = false;
                            //team1.setText(team1name);
                            team1Turn=true;
                        }

                    } catch (Exception e) {
                    }




                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (SERVERIP != null) {
                    serverSocket = new ServerSocket(SERVERPORT);
                    boolean firstLine = true;
                    boolean firstReplyLine = true;
                    while (true) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = serverSocket.accept();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Connected.",Toast.LENGTH_SHORT).show();
                            }
                        });
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))/*, true*/);

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line = null;
                            int x =0;
                            while ((line = in.readLine()) != null) {
                                Log.d("ServerActivity", line);
                                if (firstLine) {
                                    team2name = line;
                                    firstLine = false;
                                    //team2.setText(team2name);
                                    team1Turn= true;
                                }
                                out.flush();
                                out.println(team1name);
                            }
                            /*
                            if(firstReplyLine){
                                out.flush();
                                out.println(username);
                                firstReplyLine = false;
                            }
                            */
                            //break;
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Oops. Connection interrupted. Please reconnect your phones.",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Couldn't detect internet connection.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error.",Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        }
    }
    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


