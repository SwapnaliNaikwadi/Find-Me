package com.swapnali.a.sendlocation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    TextView locationText;

    double lat,lng;
    LocationManager locationManager;

    SessionManager session;
    private static final int REQUEST_SMS=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();


        getLocationBtn = (Button) findViewById(R.id.sendLocation);
        locationText = (TextView) findViewById(R.id.locationText);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        getLocation();

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.M){
                    int hasSMSPermission= checkSelfPermission(Manifest.permission.SEND_SMS);
                    if (hasSMSPermission != PackageManager.PERMISSION_GRANTED){
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)){

                            showMessageOKCancel("You need to allow access to send SMS",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog,int which){
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                requestPermissions(new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
                                            }
                                        }

                                    });
                            return;
                        }
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS},REQUEST_SMS);

                        return;
                    }
                    sendMySMS();


                }
            }
        });


    }

    private void sendMySMS() {

        HashMap<String, String> user = session.getUserDetails();


        final String contact1 = user.get(SessionManager.KEY_CONTACT1);

        String contact2 = user.get(SessionManager.KEY_CONTACT2);

        String contact3=user.get(SessionManager.KEY_CONTACT3);

        String contact[]={contact1,contact2,contact3};

        String msg=locationText.getText().toString();
        SmsManager sms = SmsManager.getDefault();
       /* List<String> messages = sms.divideMessage(locationText.getText().toString());
        for (String msg : messages) {
            PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(contact1, null, msg, null, null);

            Toast.makeText(getApplicationContext(),"Address send successfully",Toast.LENGTH_SHORT).show();

        }*/

        for(String number : contact) {
            sms.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(getApplicationContext(),"Address send successfully",Toast.LENGTH_SHORT).show();

        }

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lat=location.getLatitude();
        lng=location.getLongitude();
       // locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
          //  locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
            //        addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));


            locationText.setText(addresses.get(0).getAddressLine(0));



        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.no) {
            session.logoutUser();
        }
        else if(id==R.id.share)
        {
            String shareBody="https://play.google.com/store/apps/details?id=com.swapnali.a.sendlocation";
            Intent sharing=new Intent(Intent.ACTION_SEND);
            sharing.setType("text/plain");
            sharing.putExtra(Intent.EXTRA_SUBJECT,"APP NAME(open it in google play store)");
            sharing.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(sharing,"Share via"));
        }
        else if(id==R.id.showOnMap)
        {
            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener){
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
        .setMessage(message)
        .setPositiveButton("OK",okListener)
        .setNegativeButton("cancel", null)
        .create()
        .show();
        }

}