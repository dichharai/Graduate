package edu.uiowa.dichha.mpplaybackspeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

import edu.uiowa.dichha.mpplaybackspeed.motion.StepService;


public class MainActivity extends AppCompatActivity {
    private Button playbackBtn;
    private Button pauseBtn;
    private Button nextBtn;
    private TextView displayName;
    private static MediaPlayer mediaPlayer;
    private static final String TAG="Dropdown";
    ArrayList<Integer> playlist;
    SpinnerActivity spinnerActivity = new SpinnerActivity();
    private int num=0;
    private int musicId;
    private MyReceiver receiver=null;
    Random random = new Random();
    static private float speed;
    static private float formertempo;
    static private float currenttempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playlist = new ArrayList<>();
        playbackBtn = (Button) findViewById(R.id.playback);
        pauseBtn = (Button) findViewById(R.id.pause);
        nextBtn = (Button) findViewById(R.id.next);
        displayName = (TextView) findViewById(R.id.music_name);
        playlist.add(R.raw.aurora);
        playlist.add(R.raw.milky_way_stars);
        playlist.add(R.raw.voyage);
        playlist.add(R.raw.horizon);
        if (mediaPlayer == null){
            mediaPlayer = mediaPlayer.create(this, playlist.get(0));
        }

        //String name = Resources.get
        displayName.setText(getResources().getResourceEntryName(playlist.get(0)));


        /* dropdown */
        Spinner spinner = (Spinner) findViewById(R.id.speed_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition("Slow");
        spinner.setSelection((spinnerPosition));

        spinner.setOnItemSelectedListener(spinnerActivity);

        //begin of insertion jianxin
        //Start step detection service
        Intent intentStart = new Intent(MainActivity.this,StepService.class);
        MainActivity.this.startService(intentStart);
        //end of insertion jianxin

        playbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing music", Toast.LENGTH_SHORT).show();

                //speed = 1.25f;
                //mediaPlayer.start();
                playMusic();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing music", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                musicId = getNextMusicId();
                mediaPlayer = MediaPlayer.create(MainActivity.this,playlist.get(musicId));
                //mediaPlayer.start();
                playNextMusic(musicId);
                Toast.makeText(getApplicationContext(),"Next music", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this, Help.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public int getNextMusicId(){
        num = random.nextInt(playlist.size());
        return num;
    }
    public void playNextMusic(int mId){
        setMusicName(mId);
        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(spinnerActivity.getSpeed()));
        //BEGIN OF INSERTION JIANXIN
        //Get Speed
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("motion.Consumer");
        MainActivity.this.registerReceiver(receiver,filter);
        //END OF INSERTION JIANXIN
    }
    public void playMusic(){
        //BEGIN OF INSERTION JIANXIN
        //Get Speed
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("motion.Consumer");
        MainActivity.this.registerReceiver(receiver,filter);
        //END OF INSERTION JIANXIN
    
        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(spinnerActivity.getSpeed()));
    
    }

    public void setMusicName(int mId){
        displayName.setText(getResources().getResourceEntryName(playlist.get(mId)));

    }

    //receive broadcasted speed
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            speed = bundle.getFloat("Timestamp");
            //map speed to tempo
            if (speed == 0.0){
                speed = 2000;
            }

            Log.i("reciever:", Float.toString(speed));
            if (speed >= 1200 && speed <= 2000){//slow mode

                currenttempo =0.75f;
            }else if (speed >= 700 && speed <1200){ // Normal

                currenttempo = 1.00f;
            } else { // fast < 700

                currenttempo = 1.25f;
            }

            if (currenttempo != formertempo){
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));
                spinnerActivity.setSpeed(currenttempo);
                formertempo = currenttempo;
            }
        }
    }
    public  void onStop(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
       super.onStop();

    }
    public void onDestroy(){
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        super.onDestroy();
    }
}
