package dgs.example.pungkook.dgs_android.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by vinh on 08-Sep-16.
 */
public class AppUltils {

    public static int onGetPositionTouch(Activity activity, View v) {
        int[] mLocation = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int actionBarHeight = 0;
        //= dm.heightPixels - mViewFullFrag.getMeasuredHeight();
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        v.getLocationOnScreen(mLocation);
        return mLocation[1] - actionBarHeight - 100;
    }

    public static int onGetHeightScreen(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return height;
    }

    public static int onGetWidthtScreen(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static double tabletSize(Context context) {
        double size = 0;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            size = Math.sqrt(x + y);
        } catch (Throwable t) {
            Log.d("tabletSize",t.getMessage());
        }
        return size;
    }
    public static double getScreenDimension(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        String[] screenInformation = new String[3];
        screenInformation[0] = String.valueOf(width) + " px";
        screenInformation[1] = String.valueOf(height) + " px" ;
        screenInformation[2] = String.format("%.2f", screenInches) + " inches" ;

        return screenInches;
    }

    public static String fomatDate(String date){
        String longV = date.replace("/Date(","").replace(")/","");
        long millisecond = Long.parseLong(longV);
        String dateString="";
        // or you already have long value of date, use this instead of milliseconds variable.
        try {
            dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        }catch (Exception e){
            Log.d("Kien", "Error while format dat "+e.toString());
        }
        return dateString;
    }

    public static String fomatDateFromString(String date){
        String dateString="";
        // or you already have long value of date, use this instead of milliseconds variable. 10/03/2017
        if(!Validation.checkNullOrEmpty(date)) {
            try {
                dateString = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, date.length());
            } catch (Exception e) {
                Log.d("Kien", "Error while format dat " + e.toString());
            }
        }
        return dateString;
    }

    public static void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[2048];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            Log.d("kien", e.toString());
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                Log.d("kien", e.toString());
            }
        }
    }

    public static boolean isOnline(Context context) {
        NetworkInfo netInfo = null;
        if(context!=null) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
