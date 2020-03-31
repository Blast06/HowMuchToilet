package com.dtapps.howmuchtoilet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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


    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private MaterialButton shareBtn;
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


        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        //Initialize ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



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


        rollsTxt.setText(String.format(getString(R.string.rolls_Txt),rollsBar.getProgress()));
        toiletTxt.setText(String.format(getString(R.string.toilet_txt),toiletBar.getProgress()));
        wipesTxt.setText(String.format(getString(R.string.wipes_txt), wipesBar.getProgress()));
        peopleTxt.setText(String.format(getString(R.string.people_txt),peopleBar.getProgress()));
        sheetsTxt.setText(String.format(getString(R.string.sheets_txt),sheetsBar.getProgress()));
        sheetsOnRollsTxt.setText(String.format(getString(R.string.sheets_on_rolls_txt),daysOfQuarantineBar.getProgress()));
        daysOfQuarantineTxt.setText(String.format(getString(R.string.days_quarantine_txt),sheetsOnRollsBar.getProgress()));


    }

    private void calcular(float daysOfQuarantine, float rolls, float sheetsOnRolls, float toilet, float people, float sheets, float wipes  ) {

        sheetsPerDay = sheets * wipes * (toilet * people);
        rollsPerDay = sheetsOnRolls / sheetsPerDay;
        totalSheets = rolls * sheetsOnRolls;
        days = Math.round(totalSheets / sheetsPerDay);
        percent = Math.round((days / daysOfQuarantine) * 100);
        title2.setText(String.format(getString(R.string.title2), (int)days, (int)daysOfQuarantine));


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
                rollsTxt.setText(String.format(getString(R.string.rolls_Txt),i));
                calcular(daysOfQuarantine, i, sheetsOnRolls,toilet,people,sheets,wipes);
                break;
            case R.id.toiletBar:
                toiletTxt.setText(String.format(getString((R.string.toilet_txt)),i));
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,i,people,sheets,wipes);
                break;
            case R.id.wipesBar:
                wipesTxt.setText(String.format(getString(R.string.wipes_txt),i));
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,people,sheets,i);
                break;
            case R.id.peoplesBar:
                peopleTxt.setText(String.format(getString(R.string.people_txt),i));
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,i,sheets,wipes);
                break;
            case R.id.sheetsBar:
                sheetsTxt.setText(String.format(getString(R.string.sheets_txt),i));
                calcular(daysOfQuarantine, rolls, sheetsOnRolls,toilet,people,i,wipes);
                break;
            case R.id.daysOfQuarantineBar:
                daysOfQuarantineTxt.setText(String.format(getString(R.string.days_quarantine_txt),i));
                calcular(i, rolls, sheetsOnRolls,toilet,people,sheets,wipes);
                break;

            case R.id.sheetsOnRollsBar:
                sheetsOnRollsTxt.setText(String.format(getString(R.string.sheets_on_rolls_txt), i));
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

    @Override
    public void onClick(View view) {

        getPermissions();

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void saveBitmap(Bitmap bitmap) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


        File imagePath = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + timeStamp + "screnshot.png"); ////File imagePath

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

        shareIt(imagePath);

    }

    private void shareIt(File imagePath) {
        Uri imageUri = FileProvider.getUriForFile(
                MainActivity.this,
                "com.dtapps.howmuchtoilet.provider", //(use your app signature + ".provider" )
                imagePath);


        Uri uri = Uri.fromFile(imagePath);
        String shareText = getString(R.string.play_store_link) + getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_subject)));
    }



    private void getPermissions() {


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, getString(R.string.permission_txt), Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
