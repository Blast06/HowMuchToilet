package com.dtapps.howmuchtoilet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, NavigationView.OnNavigationItemSelectedListener {

    private SeekBar rollsBar;
    private SeekBar toiletBar;
    private SeekBar wipesBar;
    private SeekBar peopleBar;
    private SeekBar sheetsBar;
    private SeekBar sheetsOnRollsBar;
    private SeekBar daysOfQuarantineBar;

    // nav vars
    private  DrawerLayout drawerLayout;
    private NavigationView navigationView;
//    private Toolbar toolbar;







    private TextView title1;
    private TextView title2;
    private TextView rollsTxt;
    private TextView toiletTxt;
    private TextView wipesTxt;
    private TextView peopleTxt;
    private TextView sheetsTxt;
    private TextView sheetsOnRollsTxt;
    private TextView daysOfQuarantineTxt;
    public float days = 14, percent = 0, rolls = 10, toilet = 3, people = 1, sheets = 2, wipes = 5,  quar =1,
    rollRange = 1, sheetsOnRolls = 160, daysOfQuarantine = 14, sheetsPerDay, rollsPerDay, totalSheets;
    private boolean show = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);
        title2.setText(String.format(getString(R.string.title2), (int)days, (int)daysOfQuarantine));


        calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,people,sheets,wipes);

        rollsTxt = findViewById(R.id.rollsTxt);
        toiletTxt = findViewById(R.id.toiletTxt);
        wipesTxt = findViewById(R.id.wipesTxt);
        peopleTxt = findViewById(R.id.peopleTxt);
        sheetsTxt = findViewById(R.id.sheetsTxt);
        sheetsOnRollsTxt = findViewById(R.id.sheetsOnRollsTxt);
        daysOfQuarantineTxt = findViewById(R.id.daysOfQuarantineTxt);

        rollsBar = findViewById(R.id.rollsBar);
        toiletBar = findViewById(R.id.toiletBar);
        wipesBar = findViewById(R.id.wipesBar);
        peopleBar = findViewById(R.id.peoplesBar);
        sheetsBar = findViewById(R.id.sheetsBar);
        daysOfQuarantineBar = findViewById(R.id.daysOfQuarantineBar);
        sheetsOnRollsBar = findViewById(R.id.sheetsOnRollsBar);


        rollsBar.setOnSeekBarChangeListener(this);
        toiletBar.setOnSeekBarChangeListener(this);
        wipesBar.setOnSeekBarChangeListener(this);
        peopleBar.setOnSeekBarChangeListener(this);
        sheetsBar.setOnSeekBarChangeListener(this);
        daysOfQuarantineBar.setOnSeekBarChangeListener(this);
        sheetsOnRollsBar.setOnSeekBarChangeListener(this);

        rollsBar.setProgress((int)rolls);
        toiletBar.setProgress((int)toilet);
        wipesBar.setProgress((int)wipes);
        peopleBar.setProgress((int)people);
        sheetsBar.setProgress((int)sheets);
        daysOfQuarantineBar.setProgress((int)daysOfQuarantine);
        sheetsOnRollsBar.setProgress((int)sheetsOnRolls);


        rollsTxt.setText("Rolls you have: " + rollsBar.getProgress());
        toiletTxt.setText("Toilet visits: " + toiletBar.getProgress());
        wipesTxt.setText("Average wipes per trip: " + wipesBar.getProgress());
        peopleTxt.setText("People in household: " + peopleBar.getProgress());
        sheetsTxt.setText("Sheets per wipe: " + sheetsBar.getProgress());
        sheetsOnRollsTxt.setText("Sheets on roll: " + daysOfQuarantineBar.getProgress());
        daysOfQuarantineTxt.setText("Days of quarantine: " + sheetsOnRollsBar.getProgress());


    }

    private void calcular(float daysOfQuarantine, float rolls, float sheetsOnRolls, float toilet, float people, float sheets, float wipes  ) {

        sheetsPerDay = sheets * wipes * (toilet * people);
        rollsPerDay = sheetsOnRolls / sheetsPerDay;
        totalSheets = rolls * sheetsOnRolls;
        days = Math.round(totalSheets / sheetsPerDay);
        percent = Math.round((days / daysOfQuarantine) * 100);
        title2.setText(String.format(getString(R.string.title2), (int)days, (int)daysOfQuarantine));
//        title3.setText(getString(R.string.title3) + percent +"% "  + getString(R.string.title4));


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()){
            case R.id.rollsBar:
                rollsTxt.setText("Rolls you have: " + i);
                calcular(daysOfQuarantine, i, sheetsOnRolls,toilet,people,sheets,wipes);
                break;
            case R.id.toiletBar:
                toiletTxt.setText("Toilet visits: " + i);
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,i,people,sheets,wipes);
                break;
            case R.id.wipesBar:
                wipesTxt.setText("Average wipes per trip: " + i);
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,people,sheets,i);
                break;
            case R.id.peoplesBar:
                peopleTxt.setText("People in household: " + i);
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,i,sheets,wipes);
                break;
            case R.id.sheetsBar:
                sheetsTxt.setText("Sheets per wipe: " + i);
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,people,i,wipes);
                break;
            case R.id.daysOfQuarantineBar:
                daysOfQuarantineTxt.setText("Days of quarantine: " + i);
                calcular(i, rolls, sheetsOnRolls,toilet,people,sheets,wipes);
                break;

            case R.id.sheetsOnRollsBar:
                sheetsOnRollsTxt.setText("Sheets on roll: " + i);
                calcular(daysOfQuarantine, rolls, i,toilet,people,sheets,wipes);
                break;


        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_privacy:
                Intent intent = new Intent(MainActivity.this,PrivacyPolicy.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                Intent intent2 = new Intent(MainActivity.this,About.class);
                startActivity(intent2);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
