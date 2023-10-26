package com.anantop.emergencySupportSystem;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int MY_CALL_PHONE_MOM_PERMISSION_CODE = 51;
    private static final int MY_CALL_PHONE_DAD_PERMISSION_CODE = 52;
    private static final int MY_CALL_PHONE_POLICE_PERMISSION_CODE = 53;
    private static final int MY_READ_CONTACTS_PERMISSION_CODE = 100;
    private static final int MY_ACTION_OPEN_DOCUMENT_PERMISSION_CODE = 110;
    private static final int MY_SMS_LOCATION_PERMISSION_CODE = 60;
    private static final int MY_CAMERA_PERMISSION_CODE = 80;
    private static final int MY_READ_EXTERNAL_STORAGE_PERMISSION_CODE = 90;

    Button button3 = null;
    private LocationManager locationManager;
    String ph_no;
    Uri imageUriMom;
    Uri imageUriDad;
    Uri imageUriPolice;
    String imageButtonClicked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info", "Inside On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button3 = (Button) findViewById(R.id.back);
        setButtonText(button3,"name1","numberMom");
        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberMom");
                return true;
            }
        });
        button3 = (Button) findViewById(R.id.buttonDad);
        setButtonText(button3,"name2","numberDad");
        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberDad");
                return true;
            }
        });
        button3 = (Button) findViewById(R.id.buttonPolice);
        setButtonText(button3,"name3","numberPolice");
        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberPolice");
                return true;
            }
        });
    }

    public void settingsPage(View view) {
        setContentView(R.layout.settings_activity);

        /*if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("info", "No permissions for READ_EXTERNAL_STORAGE hence return from here");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_ACTION_OPEN_DOCUMENT_PERMISSION_CODE);
        }
        else
        {
            Log.i("tag", "Permissions for READ_EXTERNAL_STORAGE is already there");
        }*/

        SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
        Log.i("number",sharedPreferences.getString("numberMom", "Not Set"));
        EditText number1 = (EditText) findViewById(R.id.editTextTextPersonName2);
        number1.setText(sharedPreferences.getString("numberMom", "Not Set"));
        EditText number2 = (EditText) findViewById(R.id.editTextTextPersonName);
        number2.setText(sharedPreferences.getString("numberDad", "Not Set"));
        EditText number3 = (EditText) findViewById(R.id.editTextTextPersonName3);
        number3.setText(sharedPreferences.getString("numberPolice", "Not Set"));
        EditText name1 = (EditText) findViewById(R.id.editTextTextPersonName4);
        name1.setText(sharedPreferences.getString("name1", "Not Set"));
        EditText name2 = (EditText) findViewById(R.id.editTextTextPersonName5);
        name2.setText(sharedPreferences.getString("name2", "Not Set"));
        EditText name3 = (EditText) findViewById(R.id.editTextTextPersonName6);
        name3.setText(sharedPreferences.getString("name3", "Not Set"));

        String imageMom = sharedPreferences.getString("imageMom1", "Not Set");
        String imageDad = sharedPreferences.getString("imageDad", "Not Set");
        String imagePolice = sharedPreferences.getString("imagePolice", "Not Set");

        try {
            imageUriMom = Uri.parse(imageMom);
            imageUriDad = Uri.parse(imageDad);
            imageUriPolice = Uri.parse(imagePolice);
        }
        catch (Exception exception){
            Log.i("infoooo",exception.toString());
        }

        Bitmap bitmap;
        if(imageMom != "") {
            try {
                ImageView imageview = findViewById(R.id.imageView4);
                Log.i("image str frm pref", imageMom);
                Log.i("image uri-parsed-String", String.valueOf(imageUriMom));
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriMom);
                imageview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(imageDad != "") {
            try {
                ImageView imageview1 = findViewById(R.id.imageView3);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriDad);
                imageview1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(imagePolice != "") {
            try {
                ImageView imageview2 = findViewById(R.id.imageView5);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriPolice);
                imageview2.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveSettings(View view) {

        EditText number1 = (EditText) findViewById(R.id.editTextTextPersonName2);
        SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numberMom", number1.getText().toString());
        editor.putString("imageMom1",imageUriMom.toString());
        editor.commit();

        EditText number2 = (EditText) findViewById(R.id.editTextTextPersonName);
        editor.putString("numberDad", number2.getText().toString());
        editor.putString("imageDad",imageUriDad.toString());
        editor.commit();

        EditText number3 = (EditText) findViewById(R.id.editTextTextPersonName3);
        editor.putString("numberPolice", number3.getText().toString());
        editor.putString("imagePolice",imageUriPolice.toString());
        editor.commit();

        EditText name1 = (EditText) findViewById(R.id.editTextTextPersonName4);
        editor.putString("name1", name1.getText().toString());
        editor.commit();

        EditText name2 = (EditText) findViewById(R.id.editTextTextPersonName5);
        editor.putString("name2", name2.getText().toString());
        editor.commit();

        EditText name3 = (EditText) findViewById(R.id.editTextTextPersonName6);
        editor.putString("name3", name3.getText().toString());
        editor.commit();

        Log.i("name1",sharedPreferences.getString("name1", "Not Set"));
        Log.i("name2",sharedPreferences.getString("name2", "Not Set"));
        Log.i("name3",sharedPreferences.getString("name3", "Not Set"));
    }

    public void mainPage(View view) {
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.back);
        setButtonText(button1,"name1","numberMom");
        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberMom");
                return true;
            }
        });

        Button button2 = (Button) findViewById(R.id.buttonDad);
        setButtonText(button2,"name2","numberDad");
        button2.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberDad");
                return true;
            }
        });

        Button button3 = (Button) findViewById(R.id.buttonPolice);
        setButtonText(button3,"name3","numberPolice");
        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View v) {
                longClickAction("numberPolice");
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callPerson(View view) { //module to call a person
        Log.i("info", getId(view));
        String buttonClicked = null;
        int MY_CALL_PHONE_PERMISSION_CODE = 0;

        if(getId(view).equals("com.anantop.emergencycall:id/back")){
            Log.i("click", "yyyyyyy"+getId(view));
            buttonClicked="numberMom";
            MY_CALL_PHONE_PERMISSION_CODE = MY_CALL_PHONE_MOM_PERMISSION_CODE;
        } else if(getId(view).equals("com.anantop.emergencycall:id/buttonDad")){
            buttonClicked="numberDad";
            MY_CALL_PHONE_PERMISSION_CODE = MY_CALL_PHONE_DAD_PERMISSION_CODE;
        }
        else if (getId(view).equals("com.anantop.emergencycall:id/buttonPolice")) {
            buttonClicked="numberPolice";
            MY_CALL_PHONE_PERMISSION_CODE = MY_CALL_PHONE_POLICE_PERMISSION_CODE;
        }
        else{
            Log.i("click", "unknown button clicked");
        }

        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("info", "No permissions hence return from here");
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_CALL_PHONE_PERMISSION_CODE);
        }
        else
        {
            SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
            String number = sharedPreferences.getString(buttonClicked, "Not Set");
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void longClickAction(String person) {
        SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
        ph_no = sharedPreferences.getString(person, "Not Set");
        Log.i("info", "inside longClickAction");
        Log.i("info", "phone number"+ph_no);


        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("info", "No permissions for SMS or Location hence requesting access");
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, MY_SMS_LOCATION_PERMISSION_CODE);
        }
        else
        {
            getLocationUpdates();
        }

        Toast.makeText(getApplicationContext(), "Message Will Be Sent Shortly", Toast.LENGTH_LONG).show();
    }

    void getLocationUpdates() {
        Log.i("info", "inside getLocation");
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Log.i("info", "locationManager is not null");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            } else {
                Log.i("info", "locationManager is null");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override//module for sending sms
    public void onLocationChanged(Location location) {

        Log.i("info", "Inside onLocationChanged");
        Log.i("Latitude", location.getLatitude() + "");
        Log.i("longitude", location.getLongitude() + "");

        //below code is for sending sms
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        SmsManager sms = SmsManager.getDefault();
        String address = null;
        try {
            address = getLocationAddress(location);
            Log.i("info", address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sms.sendTextMessage(ph_no, null,
                "Need Help. Current Location: " +
                        location.getLatitude() + ", " + location.getLongitude() + "Address=" + address, pi, null);
        Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    public String getLocationAddress(Location location) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Log.i("info","inside getLocation");

        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        Log.i("address", address);
        return address;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == MY_CALL_PHONE_MOM_PERMISSION_CODE) ||
                (requestCode == MY_CALL_PHONE_DAD_PERMISSION_CODE) ||
                (requestCode == MY_CALL_PHONE_POLICE_PERMISSION_CODE)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String buttonClicked = null;
                if (requestCode == MY_CALL_PHONE_MOM_PERMISSION_CODE) {
                    Log.i("click", "Call Mom Clicked");
                    buttonClicked = "numberMom";
                } else if (requestCode == MY_CALL_PHONE_DAD_PERMISSION_CODE) {
                    Log.i("click", "Call Dad Clicked");
                    buttonClicked = "numberDad";
                } else if (requestCode == MY_CALL_PHONE_POLICE_PERMISSION_CODE) {
                    Log.i("click", "Call Police Clicked");
                    buttonClicked = "numberPolice";
                } else {
                    Log.i("click", "unknown button clicked");
                }

                Toast.makeText(this, "Phone Permission Granted", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
                String number = sharedPreferences.getString(buttonClicked, "Not Set");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Phone Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_SMS_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS And Location Permission Granted", Toast.LENGTH_LONG).show();
                getLocationUpdates();
            } else {
                Toast.makeText(this, "SMS Or Location Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am taking photo");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (imageButtonClicked == "mom") {
                    startActivityForResult(cameraIntent, 70);
                } else if (imageButtonClicked == "dad") {
                    startActivityForResult(cameraIntent, 72);
                } else if (imageButtonClicked == "police") {
                    startActivityForResult(cameraIntent, 74);
                }
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == MY_READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am choosing photo");
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                // pickPhoto.setType("*/*.jpg");

                if (imageButtonClicked.equals("mom")) {
                    Log.i("inside if mom", "i am inside");
                    startActivityForResult(pickPhoto, 71);
                } else if (imageButtonClicked.equals("dad")) {
                    Log.i("inside if dad", "i am inside");
                    startActivityForResult(pickPhoto, 73);
                } else if (imageButtonClicked.equals("police")) {
                    Log.i("inside if police", "i am inside");
                    startActivityForResult(pickPhoto, 75);
                }
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contacts Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am choosing contact");
                //Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                //startActivityForResult(intent, 7);

                if (imageButtonClicked.equals("con1")) {
                    Log.i("inside if con1", "i am inside");
                    startActivityForResult(intent, 101);
                } else if (imageButtonClicked.equals("con2")) {
                    Log.i("inside if con2", "i am inside");
                    startActivityForResult(intent, 102);
                } else if (imageButtonClicked.equals("con3")) {
                    Log.i("inside if con3", "i am inside");
                    startActivityForResult(intent, 103);
                }
            } else {
                Toast.makeText(this, "Contact Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

/*        if (requestCode == MY_ACTION_OPEN_DOCUMENT_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "Open Doc Permission Granted");
            }
            else
            {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    public static String getId(View view) {
        if (view.getId() == View.NO_ID) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }

    public void imagePerson(View view) {
        String options_array[]=new String[2];
        options_array[0]="Take Photo";
        options_array[1]="Choose Photo From Folder";

        Log.i("click", "xxxxx"+getId(view));
        if(getId(view).equals("com.anantop.emergencycall:id/imageView4")){
            Log.i("click", "yyyyyyy"+getId(view));
            imageButtonClicked="mom";
        } else if(getId(view).equals("com.anantop.emergencycall:id/imageView3")){
            imageButtonClicked="dad";
        }
        else if (getId(view).equals("com.anantop.emergencycall:id/imageView5")) {
            imageButtonClicked="police";
        }
        else{
            Log.i("click", "unknown button clicked");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image").setItems(options_array, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int which) {
                Log.i("info","item clicked number="+which);
                switch (which) {
                    case 0:
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Log.i("tag", "I am taking photo");
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            if (imageButtonClicked == "mom") {
                                startActivityForResult(cameraIntent, 70);
                            } else if (imageButtonClicked == "dad") {
                                startActivityForResult(cameraIntent, 72);
                            } else if (imageButtonClicked == "police") {
                                startActivityForResult(cameraIntent, 74);
                            }
                        }
                        break;
                    case 1:
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_EXTERNAL_STORAGE_PERMISSION_CODE);
                        } else {
                            Log.i("tag", "I am choosing photo");
                            Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            // pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                            // pickPhoto.setType("*/*.jpg");
                            if (imageButtonClicked.equals("mom")) {
                                Log.i("inside if mom", "i am inside");
                                startActivityForResult(pickPhoto, 71);
                            } else if (imageButtonClicked.equals("dad")) {
                                Log.i("inside if dad", "i am inside");
                                startActivityForResult(pickPhoto, 73);
                            } else if (imageButtonClicked.equals("police")) {
                                Log.i("inside if police", "i am inside");
                                startActivityForResult(pickPhoto, 75);
                            }
                            break;
                        }
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        Log.i("info","inside on activity result");
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        Bitmap bitmap=null;
        Bundle extras;

        switch(requestCode) {
            case 70:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ImageView imageviewMom = findViewById(R.id.imageView4);
                    imageviewMom.setImageBitmap(photo);
                    imageUriMom = getImageUri(getApplicationContext(), photo);
                }
                break;
            case 71:
                if(resultCode == RESULT_OK){
                    imageUriMom = imageReturnedIntent.getData();
                    Log.i("image_uri", String.valueOf(imageUriMom));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriMom);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView imageviewMom = findViewById(R.id.imageView4);
                    imageviewMom.setImageBitmap(bitmap);
                }
                break;
            case 72:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ImageView imageviewDad = findViewById(R.id.imageView3);
                    imageviewDad.setImageBitmap(photo);
                    imageUriDad = getImageUri(getApplicationContext(), photo);
                }
                break;
            case 73:
                if(resultCode == RESULT_OK){
                    imageUriDad = imageReturnedIntent.getData();
                    Log.i("info", String.valueOf(imageUriDad));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriDad);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView imageviewDad = findViewById(R.id.imageView3);
                    imageviewDad.setImageBitmap(bitmap);
                }
                break;
            case 74:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ImageView imageviewPolice = findViewById(R.id.imageView5);
                    imageviewPolice.setImageBitmap(photo);
                    imageUriPolice = getImageUri(getApplicationContext(), photo);
                }
                break;
            case 75:
                if(resultCode == RESULT_OK){
                    imageUriPolice = imageReturnedIntent.getData();
                    Log.i("info", String.valueOf(imageUriPolice));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriPolice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView imageviewPolice = findViewById(R.id.imageView5);
                    imageviewPolice.setImageBitmap(bitmap);
                }
                break;
            case 101:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;
                    uri = imageReturnedIntent.getData();
                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {
                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex( Contract.Contacts.DISPLAY_NAME));
                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {
                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                            " = " + TempContactID, null, null);
                            while (cursor2.moveToNext()) {
                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                EditText name = (EditText) findViewById(R.id.editTextTextPersonName4);
                                EditText number = (EditText) findViewById(R.id.editTextTextPersonName2);
                                name.setText(TempNameHolder);
                                number.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;
                    uri = imageReturnedIntent.getData();
                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {
                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {
                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                            " = " + TempContactID, null, null);
                            while (cursor2.moveToNext()) {
                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                EditText name = (EditText) findViewById(R.id.editTextTextPersonName5);
                                EditText number = (EditText) findViewById(R.id.editTextTextPersonName);
                                name.setText(TempNameHolder);
                                number.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;
                    uri = imageReturnedIntent.getData();
                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {
                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {
                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                            " = " + TempContactID, null, null);
                            while (cursor2.moveToNext()) {
                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                EditText name = (EditText) findViewById(R.id.editTextTextPersonName6);
                                EditText number = (EditText) findViewById(R.id.editTextTextPersonName3);
                                name.setText(TempNameHolder);
                                number.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void setButtonText(Button button, String name, String number){
        SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
        String nameValue = null;
        nameValue = sharedPreferences.getString(name, "Not Set");
        String numberValue = null;
        numberValue = sharedPreferences.getString(number, "Not Set");
        if((nameValue != null && nameValue.length() != 0) && (numberValue != null && numberValue.length() != 0)) {
            Log.i("inside","inside if:"+nameValue);
            button.setText(nameValue);
        }else{
            Log.i("inside","inside else:"+nameValue);
            button.setText("Not Set");
            button.setEnabled(false);
        }
    }

    public void pickFromContacts(View view){
        Log.i("inside", "inside pickFromContacts");

        if(getId(view).equals("com.anantop.emergencycall:id/imageView6")){
            Log.i("click", "yyyyyyy"+getId(view));
            imageButtonClicked="con1";
        } else if(getId(view).equals("com.anantop.emergencycall:id/imageView7")){
            imageButtonClicked="con2";
        }
        else if (getId(view).equals("com.anantop.emergencycall:id/imageView8")) {
            imageButtonClicked="con3";
        }
        else{
            Log.i("click", "unknown button clicked");
        }

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("info", "No permissions hence return from here");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_READ_CONTACTS_PERMISSION_CODE);
        }
        else
        {
            Log.i("tag", "I am choosing contact");
            //Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            //startActivityForResult(intent, 7);

            if (imageButtonClicked.equals("con1")) {
                Log.i("inside if con1", "i am inside");
                startActivityForResult(intent, 101);
            } else if (imageButtonClicked.equals("con2")) {
                Log.i("inside if con2", "i am inside");
                startActivityForResult(intent, 102);
            } else if (imageButtonClicked.equals("con3")) {
                Log.i("inside if con3", "i am inside");
                startActivityForResult(intent, 103);
            }
        }

    }
}