package riskybidness;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.*;
import android.os.Bundle;

import riskybidness.uncg_risk.R;

public class testActivity extends Activity {
    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);


    }
}
