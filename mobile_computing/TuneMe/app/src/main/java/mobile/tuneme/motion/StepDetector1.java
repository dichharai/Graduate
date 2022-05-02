package mobile.tuneme.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

/**
 * Created by jianxhe on 12/6/2016.
 */
// gravity = 9.81 m/s2 ~ 10
// valley <= 10
// peak >= 10
// peak - valley >= 2
public class StepDetector1{
    static float[] oriValues = new float[3];
    static final int valueNum = 5;        //No. of differences of peak and valley
    static float[] tempValue = new float[valueNum];
    static int tempCount = 0;             //up trend
    static boolean isDirectionUp = false; //up trend now?
    static int continueUpCount = 0;       //last up trend point
    static int continueUpFormerCount = 0; //
    static boolean lastStatus = false;
    static float peakOfWave = 0;
    static float valleyOfWave = 0;
    static float timeOfThisPeak = 0;
    static float timeOfLastPeak = 0;
    static float timeOfNow = 0;
    static float gravityNew = 0;
    static float gravityOld = 0;
    static final float initialValue = (float) 2;
    static float ThreadValue = (float) 3;
    static float[] dectectedStep = new float[2];
    //private StepListener mStepListeners;
    static private Context context;
    static public int CURRENT_STEP = 0;
    static private float timestamp;

    public StepDetector1(){}
    public StepDetector1(Context context){this.context = context;}

    public static void detect(String input) {
        dectectedStep[0] = 0; //initial value
        String values[] = input.split(",");
        int type;
        type = Integer.parseInt(values[1]);
        if (type != Sensor.TYPE_ACCELEROMETER){return;}
        timestamp = Float.parseFloat(values[0]);
        oriValues[0] = Float.parseFloat(values[2]);
        oriValues[1] = Float.parseFloat(values[3]);
        oriValues[2] = Float.parseFloat(values[4]);

        gravityNew = (float) Math.sqrt(oriValues[0] * oriValues[0]
                + oriValues[1] * oriValues[1] + oriValues[2] * oriValues[2]);
        DetectorNewStep(gravityNew);
    }

    public static void DetectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (DetectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = timestamp;
                if ((timeOfNow - timeOfLastPeak)/1000000 >= 500
                        && (peakOfWave - valleyOfWave >= ThreadValue)) {
                    timeOfThisPeak = timeOfNow;

                    CURRENT_STEP += 1;
                    //Log.i("CURRENT_STEP", String.valueOf(CURRENT_STEP));
                    dectectedStep[0] = 1;
                    dectectedStep[1] = timeOfNow;
                }
                if ((timeOfNow - timeOfLastPeak)/1000000 >= 500
                        && (peakOfWave - valleyOfWave >= initialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                    //Log.i("CURRENT_STEP","ThreadValue"+String.valueOf(ThreadValue));
                    //Log.i("CURRNT_STEP","peakofwave"+String.valueOf(peakOfWave)+"valleyofwave"+String.valueOf(valleyOfWave));
                }
            }
        }
        gravityOld = values;
    }

    public static boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }
        if (!isDirectionUp && lastStatus && (continueUpFormerCount >= 1 || oldValue >= 20)) {   //oldValue >= 20 useless
            if (oldValue <= 10){return false;} //peak has to be greater than 10
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else { return false;
        }
    }

    public static float Peak_Valley_Thread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < valueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;
    }

    public static float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;

        if (ave >= 8)
            ave = (float) 5.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 4.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 3.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 3.0;
        else {
            ave = (float) 2.5;
        }

        return ave;
    }
}
