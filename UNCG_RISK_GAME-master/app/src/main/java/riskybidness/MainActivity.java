package riskybidness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import riskybidness.uncg_risk.R;

public class MainActivity extends Activity {


    TextView screenSize;
    static boolean Host = false, Guest = false;
    public static ObjectOutputStream objOutputStream;
    private Button CreateButton, FindButton;
    private EditText UserName;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        CreateButton =(Button) findViewById(R.id.CreateButton);
        FindButton = (Button) findViewById(R.id.FindButton);
        UserName = (EditText) findViewById(R.id.UserInput);

    }

    public void createGame(View v){
        Host = true;
        Guest = false;
        Bundle b = new Bundle();
        String str = UserName.getText().toString();
        b.putString("username", str);
        Intent intent= new Intent(riskybidness.MainActivity.this, HostLobby.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void findGame(View v){
        Host = false;
        Guest = true;
        Bundle b = new Bundle();
        b.putString("username", UserName.getText().toString());
        Intent intent= new Intent(riskybidness.MainActivity.this, JoinLobby.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onDestroy();
        super.onBackPressed();
    }
}
