package com.dtapps.howmuchtoilet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashScreen extends AppCompatActivity {

    InterstitialAd Inter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //ocultar la barra para ponerlo fullscreen
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        InternetSi();

        Inter = new InterstitialAd(this);
        AdRequest adRespuesta = new AdRequest.Builder().build();
        Inter.setAdUnitId(getApplicationContext().getString(R.string.ad_interstitial_splash));
        Inter.loadAd(adRespuesta);
    }

    public final boolean InternetSi() {


        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Inter.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            MostrarInterstitial();
                        }

                        @Override
                        public void onAdClosed() {
//                            checkAccount();
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
//                            checkAccount();
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }, 200);

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this,"CONECTATE A INTERNET",Toast.LENGTH_LONG).show();

            return false;
        }
        return false;

    }

    private void MostrarInterstitial() {
        if (Inter.isLoaded()) {
            Inter.show();
        }
    }
}
