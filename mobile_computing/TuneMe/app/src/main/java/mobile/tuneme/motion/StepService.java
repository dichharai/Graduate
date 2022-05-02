package mobile.tuneme.motion;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StepService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor step_detector;
    private BlockingQueue<Sensor_Data> queue = new ArrayBlockingQueue<Sensor_Data>(200);
    private ExecutorService pool = Executors.newFixedThreadPool(200);
    private static long last_step_timestamp;
    private static long current_sensor_timestamp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Producer producer = null;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            producer = new Producer(queue,Sensor.TYPE_ACCELEROMETER,event.values,event.timestamp);
        }else{
            return;
        }

        if (producer !=null){
            new Thread(producer).start();
        }
        Consumer consumer = new Consumer(queue,getApplicationContext());
        //start Consumer
        new Thread(consumer).start();
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        sensorManager.unregisterListener(this, accelerometer);
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
