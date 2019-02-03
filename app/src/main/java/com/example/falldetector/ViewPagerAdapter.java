package com.example.falldetector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        FragmentAlarm alarm_frag = new FragmentAlarm();
        FragmentSettings sett_frag = new FragmentSettings();
        FragmentCall contact_frag = new FragmentCall();
        Fragment f = new Fragment();
        i=i+1;
        Bundle bundle = new Bundle();
        if(i==1) {
            //bundle.putString("message","Alarm");
            f = alarm_frag;
        }
        else if(i==2) {
            bundle.putString("message","Emergency Contacts");
            f = contact_frag;
        }
        else if(i==3) {
            bundle.putString("message","Settings");
            f = sett_frag;
        }
        f.setArguments(bundle);
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String s="";
        if(position==0)
            s="alarm";
        if(position==1)
            s="Emergency Contacts";
        if(position==2)
            s="Settings";
        return  s;
    }
}
