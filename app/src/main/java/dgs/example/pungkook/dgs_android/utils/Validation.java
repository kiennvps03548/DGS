package dgs.example.pungkook.dgs_android.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 8/25/2016.
 */
public class Validation {
    public static Context mContext;

    public static void onCreate(Context context){
        Validation.mContext=context;
    }

    public static boolean checkNullOrEmpty(String s){
        if(s==null)
            return true;
        else
            if(s.trim().equals("")||s.trim().equals("null"))
                return true;
            else return false;
    }

    public static boolean onCheckNoData(Integer size){
        if(size>0)
            return true;
        else{
           /* AppDialogManager.onCreate(mContext);
            AppDialogManager.setContent(mContext.getString(R.string.txt_nodata));
            AppDialogManager.setButton(mContext.getString(R.string.txt_login_error_try));
            AppDialogManager.onCreateDialogESuccess(mContext, new DialogAcceptClickListener() {
                @Override
                public void onAcceptClick(View v) {

                }
            });*/
            return false;
        }
    }

    public static boolean onCompareCurrentDate(String sDate){
        if (sDate==null){
            //Thong bao message
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateDenNgay = dateFormat.parse(sDate);
            Calendar c = Calendar.getInstance();

            String sTuNgay=dateFormat.format(c.getTime());
            Date dateTuNgay=dateFormat.parse(sTuNgay);
            //So sanh tinh hop le cua ngay thang, ngay bat dau phai < ngay ket thuc
            return Validation.onCompareSortDate(dateTuNgay,dateDenNgay);
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean onCompareSortDate(String sDateFrom, String sDateTo){
        //Neu mot trong hai ngay thang la rong
        if (sDateFrom==null || sDateTo==null){
            //Thong bao message
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateDenNgay = dateFormat.parse(sDateTo);
            Date dateTuNgay=dateFormat.parse(sDateFrom);
            //So sanh tinh hop le cua ngay thang, ngay bat dau phai < ngay ket thuc
            return Validation.onCompareSortDate(dateTuNgay,dateDenNgay);
        } catch (ParseException e) {
            return false;
        }
    }

    //Tham so: date bat dau, date ket thuc
    //return true: neu hop le, return false neu ngay thang khong hop le
    public static boolean onCompareSortDate(Date dateFrom, Date dateTo){
        //So sanh tinh hop le cua ngay thang, ngay bat dau phai < ngay ket thuc
        if (dateFrom.compareTo(dateTo)>0)
        {
           /* AppDialogManager.onCreate(mContext);
            AppDialogManager.setTitles(mContext.getString(R.string.txt_search_fail_title));
            AppDialogManager.setContent(mContext.getString(R.string.txt_search_error_compare));
            AppDialogManager.setButton(mContext.getString(R.string.txt_login_error_try));
            AppDialogManager.onCreateDialogESuccess(mContext, new DialogAcceptClickListener() {
                @Override
                public void onAcceptClick(View v) {

                }
            });*/
            return false;
        }
        return true;
    }
}
