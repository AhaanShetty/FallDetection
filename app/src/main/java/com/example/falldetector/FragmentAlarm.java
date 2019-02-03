package com.example.falldetector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.SENSOR_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */


public class FragmentAlarm extends Fragment implements SensorEventListener {
    public int counter = 30;
    private TextView textView;
    private Button stop;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private Sensor lin_acc;
    private SensorManager sm;
    int flag;
    public FragmentAlarm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        textView = view.findViewById(R.id.display_timer);
        stop = view.findViewById(R.id.button_stop);
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (checkPermission(Manifest.permission.SEND_SMS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }



        //Request SMS permission


        //Accelerometer sensor
        lin_acc = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //Rotation angle sensor
        //rot_vec=sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //Register sensor listener
        sm.registerListener(this, lin_acc, SensorManager.SENSOR_DELAY_NORMAL);
        //sm.registerListener(this,rot_vec,SensorManager.SENSOR_DELAY_NORMAL);
        flag = 0;
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x_acc = event.values[0];
        float y_acc = event.values[1];
        float z_acc = event.values[2];
        float min = 0.0f;
        float res_acc = (float) Math.sqrt(Math.pow(x_acc, 2) + Math.pow(y_acc, 2) + Math.pow(z_acc, 2));
        //r_text.setText("Resultant= " + res_acc);
        if (res_acc > 8 && flag == 0) {
            //thresh_text.setText("Threshold reached");
            flag = 1;
            final CountDownTimer[] timer = new CountDownTimer[1];
            //textView.setText(getArguments().getString("message"));

            timer[0] = new CountDownTimer(30000, 1000) {
                public void onTick(long millisUntilFinished) {
                    stop.setVisibility(View.VISIBLE);
                    textView.setText(String.valueOf(counter));
                    counter--;
                }

                public void onFinish() {
                    stop.setVisibility(View.GONE);
                    //onSend();
                    String phoneNumber="9619745680";
                    String smsMessage="Help Me";
                    if(checkPermission(Manifest.permission.SEND_SMS)){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber,null,smsMessage,null,null);
                        //Toast.makeText(this,"message sent!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show();
                    }
                    textView.setText("Message Sent to emergency contacts!!");
                }

            }.start();

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(timer[0] !=null) {

                        timer[0].cancel();
                        counter=30;
                        flag=0;
                        textView.setText("You have successfully stopped the alarm");
                        stop.setVisibility(View.GONE);
                    }
                    timer[0]=null;
                }
            });

        } else {
            //thresh_text.setText("");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //No need
    }
//Send Message
//    public void onSend(View v) {
//    String phoneNumber="8779132038";
//    String smsMessage="Help Me";
//        if(checkPermission(Manifest.permission.SEND_SMS)){
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber,null,smsMessage,null,null);
//            //Toast.makeText(this,"message sent!",Toast.LENGTH_SHORT).show();
//        }
//        else {
//            //Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show();
//        }
//    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getActivity(),permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    //Request Location Permission
//    private void requestPermission(){
//        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
//    }

    }

