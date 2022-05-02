package mobile.tuneme.motion;

import android.hardware.Sensor;

/**
 * Created by jianxhe on 12/6/2016.
 */

public class Accelerometer_Data extends Sensor_Data{

    public Accelerometer_Data(float acc_axis[], long acc_timestamp) {
        super(acc_axis, acc_timestamp);
        type = Sensor.TYPE_ACCELEROMETER;
    }
}
