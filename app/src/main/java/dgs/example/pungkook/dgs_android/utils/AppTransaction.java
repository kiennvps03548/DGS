package dgs.example.pungkook.dgs_android.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import dgs.example.pungkook.dgs_android.R;


/**
 * Created by kien on 3/16/2017.
 */

public class AppTransaction {
    public static void replaceFragmentWithAnimation(FragmentManager fm, Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_to_top, 0);
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commitAllowingStateLoss();

    }




    public static void replaceActivityWithAnimation(Context c, Class cls)
    {
        Intent i = new Intent(c, cls);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i, bndlanimation);
    }

    public static void Toast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }
}
