package mobile.tuneme;

import mobile.tuneme.motion.StepService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnChronometerTickListener {
    private SeekBar seek_bar;
    private Button playBtn;
    private Button nextBtn;
    private static MediaPlayer mediaPlayer;
    private TextView displayName;
    private TextView du_time;
    private TextView cu_time;
    private TextView tempo;
    private Spinner spinner;
    private Spinner spinner2;
    private ArrayAdapter<String> adapter;
    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private static final String TAG = "Dropdown";
    private int num = 0;
    private int musicId;
    private String genre = "";
    private String currentgenre = "";
    private String formergenre = "";
    private String mode = "speed";
    private MyReceiver receiver = null;
    Locale locale = Locale.getDefault();
    ArrayList<Integer> playlist;
    ArrayList<Integer> playlist_jazz;
    ArrayList<Integer> playlist_electronic;
    ArrayList<Integer> playlist_classical;
    SpinnerActivity spinnerActivity = new SpinnerActivity();
    ModeActivity modeActivity = new ModeActivity();
    Handler seekHandler = new Handler();
    Random random = new Random();

    static private float speed;
    static private float formertempo;
    static private float currenttempo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInit();

    }


    public void getInit() {

        displayName = (TextView) findViewById(R.id.music_name);
        playlist_jazz = new ArrayList<>();
        playlist_jazz.add(R.raw.morning_sunrise);
        playlist_jazz.add(R.raw.boss_of_nova);
        playlist_jazz.add(R.raw.so_nice);

        playlist_electronic = new ArrayList<>();
        playlist_electronic.add(R.raw.aurora);
        playlist_electronic.add(R.raw.milky_way_stars);
        playlist_electronic.add(R.raw.voyage);
        playlist_electronic.add(R.raw.horizon);

        playlist_classical = new ArrayList<>();
        playlist_classical.add(R.raw.minuet_in_g);
        playlist_classical.add(R.raw.meditation);
        playlist_classical.add(R.raw.scherzo_presto);


        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        playBtn = (Button) findViewById(R.id.play);
        nextBtn = (Button) findViewById(R.id.next);
        cu_time = (TextView) findViewById(R.id.current);
        du_time = (TextView) findViewById(R.id.duration);
        tempo = (TextView) findViewById(R.id.tempo);
        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

         /* dropdown */
        spinner = (Spinner) findViewById(R.id.spinner_genre);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.genre, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        int genrePosition = adapter.getPosition("electronic");

        spinner.setSelection((genrePosition));

        spinner.setOnItemSelectedListener(spinnerActivity);
        playlist = playlist_electronic;
        if (mediaPlayer == null){
            mediaPlayer = mediaPlayer.create(this, playlist.get(0));
        }
        mediaPlayer = mediaPlayer.create(this, playlist.get(0));
        //String name = Resources.get
        displayName.setText(getResources().getResourceEntryName(playlist.get(0)));

        //wenxue
        spinner2 = (Spinner) findViewById(R.id.spinner_mode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.mode, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        int genrePosition2 = adapter.getPosition("speed");
        spinner2.setSelection((genrePosition2));
        spinner2.setOnItemSelectedListener(modeActivity);


        //begin of insertion jianxin
        //Start step detection service
        Intent intentStart = new Intent(MainActivity.this, StepService.class);
        MainActivity.this.startService(intentStart);
        //end of insertion jianxin

    }

    //wenxue
    Runnable run = new Runnable() {
        @Override
        public void run() {
            //seekUpdation();
            startTime = mediaPlayer.getCurrentPosition();
            currentTime();
            seek_bar.setProgress(mediaPlayer.getCurrentPosition());
            seekHandler.postDelayed(run, 100);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (mediaPlayer.isPlaying()) {
                    Toast.makeText(getApplicationContext(), "Pausing music", Toast.LENGTH_SHORT).show();
                    pause();
                } else {
                    Toast.makeText(getApplicationContext(), "Playing music", Toast.LENGTH_SHORT).show();
                    play();
                }
                break;
            case R.id.next:
                mediaPlayer.reset();
                musicId = getNextMusicId();
                mediaPlayer = MediaPlayer.create(MainActivity.this, playlist.get(musicId));
                //mediaPlayer.start();
                playNextMusic(musicId);
                Toast.makeText(getApplicationContext(), "Next music", Toast.LENGTH_SHORT).show();

        }


    }

    //Wenxue
    public void list() { //if genre is changed, change the playlist
        genre = spinnerActivity.getGenre();
        if (genre == "classical") {
            playlist = playlist_classical;
        } else if (genre == "jazz") {
            playlist = playlist_jazz;
        } else {
            playlist = playlist_electronic;
        }
        mediaPlayer.reset();
        mediaPlayer = mediaPlayer.create(this, playlist.get(0));

        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));

        displayName.setText(getResources().getResourceEntryName(playlist.get(0)));
    }

    public void play() {
        playBtn.setBackgroundResource(R.drawable.pause);
        //BEGIN OF INSERTION JIANXIN
        //Get Speed
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("motion.Consumer");
        MainActivity.this.registerReceiver(receiver, filter);
        //END OF INSERTION JIANXIN



        mode = "speed";
        mode = modeActivity.getMode();
        if (mode == "speed") {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));
        } else if (mode == "genre") {
            //spinner.setVisibility(View.INVISIBLE);
            mediaPlayer.start();
        }

        seek_bar.setMax(mediaPlayer.getDuration());
        startTime = mediaPlayer.getCurrentPosition();
        finalTime = mediaPlayer.getDuration();
        currentTime();
        durationTime();
        seek_bar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 100);
        //cu_time.setBase(SystemClock.elapsedRealtime() - mediaPlayer.getCurrentPosition());
        //cu_time.start();
    }

    public void pause() {
        playBtn.setBackgroundResource(R.drawable.play);
        mediaPlayer.pause();
        //genre = spinnerActivity.getGenre();
        //formergenre = genre;

        //cu_time.stop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this, Help.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getNextMusicId() {
        num = random.nextInt(playlist.size());
        return num;
    }

    public void playNextMusic(int mId) {
        setMusicName(mId);
        mode = modeActivity.getMode();
        if (mode == "speed") {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));
        } else if (mode == "genre") {
            mediaPlayer.start();
        }
        //BEGIN OF INSERTION JIANXIN
        //Get Speed
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("motion.Consumer");
        MainActivity.this.registerReceiver(receiver, filter);
        //END OF INSERTION JIANXIN
    }

    public void setMusicName(int mId) {
        displayName.setText(getResources().getResourceEntryName(playlist.get(mId)));

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            speed = bundle.getFloat("Timestamp");
            //map speed to tempo
            if (speed == 0.0) {
                speed = 2000;
            }

            Log.i("reciever:", Float.toString(speed));
            if (speed >= 1200 && speed <= 2000) {//slow mode
                //tempo = 1;
                currenttempo = 0.75f;
                tempo.setText("speed: slow");
            } else if (speed >= 700 && speed < 1200) { // Normal
                //tempo = 2;
                currenttempo = 1.00f;
                tempo.setText("speed: normal");
            } else { // fast < 700
                //tempo = 3;
                currenttempo = 1.25f;
                tempo.setText("speed: fast");
            }


            if (currenttempo != formertempo) {
                mode = modeActivity.getMode();
                if (mode == "speed") {

                    mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));

                    //spinnerActivity.setSpeed(currenttempo);
                } else if (mode == "genre") {
                    if (currenttempo == 0.75f) {
                        playlist = playlist_classical;
                    } else if (currenttempo == 1.00f) {
                        playlist = playlist_jazz;
                    } else if (currenttempo == 1.25f) {
                        playlist = playlist_electronic;
                    }

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.reset();
                        mediaPlayer = mediaPlayer.create(MainActivity.this, playlist.get(0));
                        mediaPlayer.start();
                        displayName.setText(getResources().getResourceEntryName(playlist.get(0)));
                    }

                }
                formertempo = currenttempo;

            }

        }
    }

    //Wenxue
    public void currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        cu_time.setText(sdf.format(startTime));
    }

    public void durationTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        du_time.setText(sdf.format(finalTime));

    }


    public void onChronometerTick(Chronometer chronometer) {

    }

    //wenxue
    //change genre
    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
        private String genre;
        private static final String TAG = "Dropdown";

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Changing genre to " + selected, Toast.LENGTH_SHORT).show();
            Log.i(TAG, Integer.toString(position));
            if (position == 0) {
                setGenre("electronic");
                genre = "electronic";
            } else if (position == 1) {
                setGenre("jazz");
                genre = "jazz";
            } else {
                setGenre("classical");
                genre = "classical";
            }
            list();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getGenre() {
            return genre;
        }
    }

    //wenxue
    //change mode
    public class ModeActivity implements AdapterView.OnItemSelectedListener {
        private String mode;
        private static final String TAG = "Dropdown";

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Changing mode to " + selected, Toast.LENGTH_SHORT).show();
            Log.i(TAG, Integer.toString(position));
            if (position == 0) {
                setMode("speed");
                spinner.setVisibility(View.VISIBLE);
                //mediaPlayer.reset();
                //mediaPlayer = mediaPlayer.create(MainActivity.this, playlist.get(0));
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(currenttempo));
                displayName.setText(getResources().getResourceEntryName(playlist.get(0)));

            } else {
                setMode("genre");
                spinner.setVisibility(View.INVISIBLE);

                mediaPlayer.reset();
                mediaPlayer = mediaPlayer.create(MainActivity.this, playlist.get(0));
                mediaPlayer.start();
                displayName.setText(getResources().getResourceEntryName(playlist.get(0)));

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }
    }
    public void onStop(){
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}


