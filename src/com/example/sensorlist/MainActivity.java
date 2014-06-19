package com.example.sensorlist;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.ListActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private final static String TAG = "MainActivity";

    private ArrayList<Sensor> sensorList = null;
    private ArrayAdapter<Sensor> sensorListAdapter;

    private SensorManager sensorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        sensorList = new ArrayList<Sensor>();

        sensorListAdapter = new ArrayAdapter<Sensor>(this, 0, sensorList) {
            @Override
            public View getView(int pos, View view, ViewGroup parent) {
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                Sensor sensor = getItem(pos);
                ((TextView) view.findViewById(android.R.id.text2)).setText(sensorTypeName(sensor
                        .getType()));
                ((TextView) view.findViewById(android.R.id.text1)).setText(sensor.getName());
                return view;
            }
        };
        setListAdapter(sensorListAdapter);

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ALL))
            sensorListAdapter.add(sensor);
    }

    protected void onListItemClick(ListView listView, View view, int pos, long id) {
        Log.i(TAG, "onListItemClick: pos=" + pos);
        Toast.makeText(this, sensorList.get(pos).getName(), Toast.LENGTH_SHORT).show();
    }

    private String sensorTypeName(int type) {
        try {
            for (Field field : Sensor.class.getFields())
                if (field.getName().startsWith("TYPE_") && field.getInt(Sensor.class) == type)
                    return field.getName();
        }
        catch (Exception e) {}
        return null;
    }

}
