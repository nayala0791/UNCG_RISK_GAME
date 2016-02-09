package riskybidness;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import riskybidness.uncg_risk.R;

public class JoinLobby extends Activity {

    private EditText serverIp;

    private Button connectPhones;

    private String theirServerIpAddress = "";

    private boolean connected = false;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lobby);
        Intent callingIntent = this.getIntent();
        Bundle incomingBundle = callingIntent.getExtras();
        username = incomingBundle.getString("username");

        serverIp = (EditText) findViewById(R.id.IpInput);
        connectPhones = (Button) findViewById(R.id.start4);
    }

    public void startGame(View v){
        Intent i = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putString("Ip", serverIp.getText().toString());
        b.putString("key1", "guest");
        i.putExtras(b);
        startActivity(i);
        finish();
    }


}