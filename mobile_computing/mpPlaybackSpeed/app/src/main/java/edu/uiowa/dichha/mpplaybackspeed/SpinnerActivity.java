package edu.uiowa.dichha.mpplaybackspeed;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by raidi01 on 11/11/2016.
 */
public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
    private float speed;
    private static final String TAG="Dropdown";
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Changing speed to "+selected, Toast.LENGTH_SHORT).show();
        Log.i(TAG, Integer.toString(position));
        if(position == 0){
            setSpeed( 0.75f);
        }else if(position == 1){
            setSpeed(1.00f);
        }else{
            setSpeed(1.25f);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public float getSpeed(){
        return speed;
    }
}
