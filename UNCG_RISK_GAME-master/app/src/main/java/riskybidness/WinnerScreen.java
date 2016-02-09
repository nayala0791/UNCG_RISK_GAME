package riskybidness;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import riskybidness.uncg_risk.R;


public class WinnerScreen extends ActionBarActivity {

    public TextView winText;
    public Button playAgain, quit;
    public String winTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            winTeam = extras.getString("key");
        }

        winText = (TextView) findViewById(R.id.textView);
        playAgain = (Button) findViewById(R.id.playagain);
        quit = (Button) findViewById(R.id.quit);

        winText.setText(winTeam + " Wins");

        quit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        playAgain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(WinnerScreen.this, MainActivity.class);
                startActivityForResult(i,1);

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_winner_screen, menu);
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
}
