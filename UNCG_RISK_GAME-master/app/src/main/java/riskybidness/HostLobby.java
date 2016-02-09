package riskybidness;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import riskybidness.uncg_risk.R;

public class HostLobby extends Activity {

    private TextView serverStatus;

    // DEFAULT IP
    public static String SERVERIP = "10.0.2.15";

    // DESIGNATE A PORT
    public static final int SERVERPORT = 8080;

    private Handler handler = new Handler();

    private ServerSocket serverSocket;

    private String username;
    private String guest;
    private String myServerIpAddress;
    private String theirServerIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_lobby);
        Intent callingIntent = this.getIntent();
        Bundle incomingBundle = callingIntent.getExtras();
        String str = incomingBundle.getString("username");
        username = str;
        serverStatus = (TextView) findViewById(R.id.server_status);
        serverStatus.setText("Server Ip: "+getLocalIpAddress());
    }

    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public void startTheGame(View v){
        Intent i = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putString("key1", "host");
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}
