package mobile.tuneme.motion;

import android.hardware.Sensor;

import java.util.concurrent.BlockingQueue;

/**
 * Created by jianxhe on 12/6/2016.
 */

public class Producer implements Runnable {
    private float axis[];
    private long timestamp;
    private int type;
    private BlockingQueue<Sensor_Data> queue;
    private static String PROD = "PRODUCER";
    private static final Producer instance = new Producer();

    private Producer(){
        //used for singleton
    }
    public static Producer get_instance(){
        return instance;
    }

    public Producer(BlockingQueue<Sensor_Data> q,
                    int type,
                    float axis[],
                    long timestamp) {
        this.type = type;
        this.queue = q;
        this.axis = axis;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        try{
            queue.put(produce(type,axis,timestamp));
            //Log.i(PROD,"Producing");
        }catch (InterruptedException ex){
            //handle the exception
        }
    }

    public Sensor_Data produce(int type, float axis[], long timestamp){
        Sensor_Data sensor_data = null;
        if (type == Sensor.TYPE_ACCELEROMETER){
            sensor_data = new Accelerometer_Data(axis, timestamp);
        }
        return sensor_data;
    }
}
