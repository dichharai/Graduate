package edu.uiowa.dichha.mpplaybackspeed.motion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * Created by jianxhe on 12/6/2016.
 */

public class Consumer implements  Runnable{
    private BlockingQueue<Sensor_Data> queue;
    private Context context;
    private static final String CONS ="CONSUMER";
    private static int elementIndex;
    private static float former_step;
    private static float latter_step;
    private static float current_timestamp;
    private static float avg_diff;
    final static int valueNum = 10;
    private static float stepPool[] = new float[valueNum];


    public Consumer(BlockingQueue<Sensor_Data> queue,Context context) {
        this.queue = queue;
        this.context = context;
    }

    @Override
    public void run() {
        Sensor_Data sensor_Data = queue.poll();
        if (sensor_Data == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            //Step detector return 1 & timestamp of the step
            //Log.i("xyz", sensor_Data.toString());
            StepDetector1.detect(sensor_Data.toString());
            if (StepDetector1.dectectedStep[0] == 0){ //not a step
                current_timestamp = sensor_Data.timestamp;
                if ((current_timestamp - former_step) / 1000000 >= 2000){

                }else{
                    return;
                }
            }else{ // a step
                current_timestamp = StepDetector1.dectectedStep[1];
            }

            if (former_step == 0 && latter_step == 0){
                former_step = Float.parseFloat(String.valueOf(current_timestamp));
            }
            if (former_step != 0 && latter_step == 0){
                latter_step = Float.parseFloat(String.valueOf(current_timestamp));
            }
            //Log.i("CONSUMER latter step",String.valueOf(latter_step));
            //Log.i("CONSUMER former step",String.valueOf(former_step));
            //Log.i("CONSUMER", String.valueOf(latter_step - former_step));
            //Log.i("Differences:", Arrays.toString(stepPool));
            if(former_step != 0 && latter_step != 0) {
                float step_diff = (latter_step - former_step) / 1000000; //time difference in millisecond
                if (step_diff < 0) { //make sure step difference greater than 0
                    step_diff = 500;
                } else if (step_diff == 0) {
                    step_diff = averageValue(stepPool, valueNum);
                } else if (step_diff > 2000) { // we allow maximum 2000 milliseconds' interval
                    step_diff = 2000;
                }

                if (elementIndex < valueNum) {
                    stepPool[elementIndex] = step_diff;
                    elementIndex++;
                } else {
                    avg_diff = averageValue(stepPool, valueNum);
                    for (int i = 1; i < valueNum; i++) {
                        stepPool[i - 1] = stepPool[i];
                    }
                    stepPool[valueNum - 1] = step_diff;
                    //Log.i("Broadcast AVG:",String.valueOf(avg_diff));
                    //broadcast the average difference
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //broadcast
                            Intent intent = new Intent();
                            intent.putExtra("Timestamp", avg_diff);
                            intent.setAction("motion.Consumer");
                            context.sendBroadcast(intent);
                            Log.i("Sender:", Float.toString(avg_diff));

                        }
                    }).start();
                }

                // prepare for next step diff
                former_step = latter_step;
                latter_step = 0;
            }
        }
    }

    public  float averageValue(float value[], int n){
        float ave = 0;
        for(int i = 0; i<n; i++) {
            ave += value[i];
        }

        ave = ave / n;
        return ave;
    }

}

