package com.kevalpatel2106.sample;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
//import com.facebook.appevents.AppEventsLogger;
import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.kevalpatel2106.sample.MessagesRecyclerAdapter;
//import com.favex.Applications.ChatApplication;
//import com.favex.Helpers.databaseHelper;
import com.kevalpatel2106.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.database.DatabaseUtils;
import android.widget.Toast;

//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;

/**
 * Created by tavish on 2/24/18.
 */

public class MessagesActivity extends HiddenCameraActivity{

        private databaseHelper dbh;
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager llm;
        //private Cursor messages;
        private ArrayList<String> messages;
        private EditText mEditText;
        private FloatingActionButton mSendButton;
        private FloatingActionButton mEmojiButton;
//    private Socket mSocket;

        private SharedPreferences prefs;

        private String senderFacebookId;
        private String senderName;
        private String myFacebookId;
//    private Receiver mReceiver;
//    private AppEventsLogger logger;

    private CameraConfig mCameraConfig;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messages);
            mCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();
        //Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //Start camera preview
            startCamera(mCameraConfig);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }
            Intent in = getIntent();
            senderFacebookId = "id2";//in.getStringExtra("facebookId");
            senderName = "friend";//in.getStringExtra("sender");

//        getSupportActionBar().setTitle(senderName);

//        prefs = this.getSharedPreferences(
//                "com.favex", Context.MODE_PRIVATE);
            myFacebookId = "id";//prefs.getString("facebookId", "default");

//        mReceiver = new Receiver();

//        IntentFilter filter = new IntentFilter("com.favex.NEW_MESSAGE");
//        registerReceiver(mReceiver, filter);

            mEditText = (EditText) findViewById(R.id.enter_message);
            mSendButton = (FloatingActionButton) findViewById(R.id.send_button);
            mEmojiButton = (FloatingActionButton) findViewById(R.id.emoji_button);
//        dbh = new databaseHelper(this);
//
//        dbh.updateRead(senderFacebookId);

            mRecyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
            mRecyclerView.setHasFixedSize(true);

            llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);

            //messages = dbh.getMessagesByFacebookId(senderFacebookId, myFacebookId);
            messages = new ArrayList<>();
            if(messages!=null) {
                mAdapter = new MessagesRecyclerAdapter(messages, myFacebookId);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

        //Take a picture
        mEmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take picture using the camera without preview.
                takePicture();
            }
        });

            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = mEditText.getText().toString();
                    if(message.compareTo("") == 0){
                        return;
                    }
                    mEditText.setText("");

//                ChatApplication app = (ChatApplication) getApplication();
//                mSocket = app.getSocket();
//                mSocket.connect();

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String time = sdf.format(cal.getTime());

                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int year = cal.get(Calendar.YEAR);
                    String date = String.valueOf(day)+ "/" + String.valueOf(month) + "/" + String.valueOf(year);

                    String sender = "sender";/*prefs.getString("name", "default");*/

                    JSONObject mJSON = new JSONObject();

                    try {
                        mJSON.put("message",message);
                        mJSON.put("sender", sender); //my name
                        mJSON.put("recipient", senderFacebookId); //fb id of other person
                        mJSON.put("facebookId", "id"/*myFacebookId*/); //my fb id
                        mJSON.put("time", time);
                        mJSON.put("date", date);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //in db stores other persons fb id but sends own fb id in json object
//                mSocket.emit("new message", mJSON);
//
//                logger = AppEventsLogger.newLogger(MessagesActivity.this);
//                logger.logEvent("newMessageSent");

                    //sender in local db should be my fb id to display messages correctly
//                dbh.insertMessage(message, myFacebookId, senderFacebookId, time, date, myFacebookId);
                    //messages = dbh.getMessagesByFacebookId(senderFacebookId, myFacebookId);
                    messages.add(message);

                    mAdapter = new MessagesRecyclerAdapter(messages, myFacebookId);

                    mRecyclerView.swapAdapter(mAdapter, true);
                    mRecyclerView.getLayoutManager().scrollToPosition(mAdapter.getItemCount()-1);

                    //Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(messages));
                }
            });

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //noinspection MissingPermission
                startCamera(mCameraConfig);
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onImageCapture(@NonNull final File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        final String charset = "UTF-8";
        final String requestURL = "http://flask-env.rryqyssj5m.ca-central-1.elasticbeanstalk.com/emoji";

        class uploadTask extends AsyncTask<Void, Void, String> {

            private Exception exception;

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                    multipart.addFilePart("image", imageFile);

                    List<String> response = multipart.finish();

                    for (String line : response) {
                        System.out.println(line);
                    }
                } catch (IOException ex) {
                    System.err.println(ex);
                }
                return null;
            }
        }

        new uploadTask().execute();
    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, "Cannot open camera.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, "Cannot write image captured by camera.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camra permission before initializing it.
                Toast.makeText(this, "Camera permission not available.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //This error will never happen while hidden camera is used from activity or fragment
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, "Your device does not have front camera.", Toast.LENGTH_LONG).show();
                break;
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            unregisterReceiver(mReceiver);
//        }
//        catch (Exception e){
//            Log.e("Receiver Exception", "Exception.");
//        }
//    }

        private class Receiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                //messages = dbh.getMessagesByFacebookId(senderFacebookId, myFacebookId);

                mAdapter = new MessagesRecyclerAdapter(messages, "id"/*myFacebookId*/);

                mRecyclerView.swapAdapter(mAdapter, true);
                mRecyclerView.getLayoutManager().scrollToPosition(mAdapter.getItemCount() - 1);
                dbh.updateRead(senderFacebookId);
            }
        }
}
