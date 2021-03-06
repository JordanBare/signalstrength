package org.jordanbare.signalstrength;

/**
 * Created by jordanbare on 1/1/18.
 * Note that this is a work in progress.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private Intent mLocationServiceIntent;
    private BroadcastReceiver mBroadcastReceiver;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private Canvas mCanvas;
    private ImageView mDrawView;
    private GraphView mGraphView;
    private List<CellphoneInfoSnapshot> mCellphoneInfoSnapshotList;

    //Program keeps requesting permission until it's received.
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkHazardousPermissions();
        initializeTextViews();
        initializeViewAndBitmap();
        initializeCellphoneInfoSnapshotList();
        initializeBroadcastReceiver();

        ToggleButton monitorSignalToggleBtn = (ToggleButton) findViewById(R.id.monitorSignalToggleBtn);
                monitorSignalToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            mLocationServiceIntent = new Intent(getApplicationContext(), LocationService.class);
                            startService(mLocationServiceIntent);
                        }
                        else {
                            stopService(mLocationServiceIntent);
                        }
                    }
                });
    }

    private void initializeBroadcastReceiver() {
        if(mBroadcastReceiver == null){
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Location location = (Location) intent.getExtras().get("location");
                    CellInfoDataImpl cellInfoData = new CellInfoDataImpl(getApplicationContext());

                    CellphoneInfoSnapshot cellphoneInfoSnapshot = new CellphoneInfoSnapshot(location, cellInfoData.retrieveCellTowerData(),
                            cellInfoData.retrieveNetworkType(), cellInfoData.retrievePhoneRadioType());

                    mLatitudeTextView.setText("Latitude: " + cellphoneInfoSnapshot.getLocation().getLatitude());
                    mLongitudeTextView.setText("Longitude: " + cellphoneInfoSnapshot.getLocation().getLongitude());
                    mGraphView.initializeDataToDraw(cellphoneInfoSnapshot.getCellInfoMap());
                    mGraphView.draw(mCanvas);
                    mCellphoneInfoSnapshotList.add(cellphoneInfoSnapshot);
                }
            };
            registerReceiver(mBroadcastReceiver, new IntentFilter("org.jordanbare.signalstrength.location_update"));
        }
    }

    private void initializeCellphoneInfoSnapshotList() {
        mCellphoneInfoSnapshotList = new ArrayList<>();
    }

    private void checkHazardousPermissions(){
        int courseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(courseLocationPermission != PackageManager.PERMISSION_GRANTED || fineLocationPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case 1:
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    //Ideally, an AlertDialog asking the user to reconsider would go here
                    MainActivity.this.finish();
                }
                break;
        }
    }

    private void initializeViewAndBitmap(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (displayMetrics.heightPixels) / 2 + 100;
        int width = displayMetrics.widthPixels;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        mCanvas.drawColor(Color.WHITE);
        mDrawView = (ImageView) this.findViewById(R.id.drawImageView);
        mDrawView.setImageBitmap(bitmap);
        mGraphView = new GraphView(getApplicationContext(), width, height);
    }

    private void initializeTextViews(){
        mLatitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        mLatitudeTextView.setText("Waiting for latitude...");
        mLongitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        mLongitudeTextView.setText("Waiting for longitude...");
    }

    //Currently just stores the info locally..longterm plan is to store in AWS S3 buckets
    private void storeCellInfo(){
        String filename = "cellInfo.data";
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        try {
            fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(mCellphoneInfoSnapshotList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        storeCellInfo();

        if(mBroadcastReceiver != null){
            unregisterReceiver(mBroadcastReceiver);
        }
        try {
            stopService(mLocationServiceIntent);
        } catch(Exception e){

        }
    }
}
