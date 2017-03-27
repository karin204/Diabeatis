package com.example.karin.diabeatis.UI;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.MyBootReceiver;

/**
 * Created by Avi on 27/03/2017.
 */

public class NotificationsSettings extends Fragment
{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.food_insertion, container, false);



        return v;
    }

    public void enableNoti()
    {
        ComponentName receiver = new ComponentName(this.getContext(), MyBootReceiver.class);
        PackageManager pm = this.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disableNoti()
    {
        ComponentName receiver = new ComponentName(this.getContext(), MyBootReceiver.class);
        PackageManager pm = this.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
