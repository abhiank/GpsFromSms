package abhiank.gpsfromsms;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mPinET;
    Button mSaveButton;

    public static final String SHARED_PREF_PIN = "pin";
    public static final String SHARED_PREF_SENT = "sms_sent";

    private final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private final int REQUEST_SEND_SMS_PERMISSION_CODE = 2;
    private final int REQUEST_RECIEVE_SMS_PERMISSION_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPinET = (EditText) findViewById(R.id.pin_edittext);
        mSaveButton = (Button) findViewById(R.id.save_button);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String pin = sharedPreferences.getString(SHARED_PREF_PIN, null);
        if (pin != null) {
            mPinET.setText(pin);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin = mPinET.getText().toString().trim();
                if (pin.equals("")) {
                    Toast.makeText(MainActivity.this, "PIN not entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pin.length() < 4) {
                    Toast.makeText(MainActivity.this, "PIN has to be min 4 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(SHARED_PREF_PIN, pin).apply();

                Toast.makeText(MainActivity.this, "PIN saved!", Toast.LENGTH_SHORT).show();
            }
        });

        checkAllPermissions();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "The app wont work without this permission", Toast.LENGTH_SHORT).show();
                }
                checkAllPermissions();
                break;

            case REQUEST_SEND_SMS_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "The app wont work without this permission", Toast.LENGTH_SHORT).show();
                }
                checkAllPermissions();
                break;

            case REQUEST_RECIEVE_SMS_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "The app wont work without this permission", Toast.LENGTH_SHORT).show();
                }
                checkAllPermissions();
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void checkAllPermissions() {
        //// TODO: 06/10/16 check if any of these permissions have been permanently disabled. In that case, open permissions page
        if (!checkLocationPermissionGranted() || !checkRecieveSmsPermissionGranted() || !checkSendSmsPermissionGranted()) {
            if (!checkLocationPermissionGranted()) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
                return;
            }
            if (!checkSendSmsPermissionGranted()) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS_PERMISSION_CODE);
                return;
            }
            if (!checkRecieveSmsPermissionGranted()) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_RECIEVE_SMS_PERMISSION_CODE);
                return;
            }
        }
    }

    public boolean checkLocationPermissionGranted() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkSendSmsPermissionGranted() {
        String permission = "android.permission.RECEIVE_SMS";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkRecieveSmsPermissionGranted() {
        String permission = "android.permission.SEND_SMS";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
