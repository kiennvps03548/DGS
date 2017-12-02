package dgs.example.pungkook.dgs_android.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;

import dgs.example.pungkook.dgs_android.R;

/**
 * Created by kien on 29-Aug-16.
 */

public class AppDialogManager {
    private DialogAcceptClickListener mClick;
    private static String titles;
    private static String content;
    private static String button;
    private static String buttonYes;
    private static String buttonNo;
    private static DialogAcceptClickListener mClickCancel;
    private CustomFontEditText txtValaueDialogAdd;


    public static String getButton() {
        return button;
    }

    public static void setButton(String button) {
        AppDialogManager.button = button;
    }

    public DialogAcceptClickListener getmClick() {
        return mClick;
    }

    public void setmClick(DialogAcceptClickListener mClick) {
        this.mClick = mClick;
    }

    public static String getTitles() {
        return titles;
    }

    public static void setTitles(String titles) {
        AppDialogManager.titles = titles;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        AppDialogManager.content = content;
    }

    public static Dialog onShowCustomDialog(final Context context, int layoutId){
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        AppCompatImageView img_close = (AppCompatImageView) view.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog onShowDialogAdd(Context context, String titles, String value, final DialogAcceptClickListener listener){
        final Dialog dialog = onShowCustomDialog(context, R.layout.dialog_add);
        CustomFontTextView txtTitle = (CustomFontTextView) dialog.findViewById(R.id.textView_titles);
        final CustomFontEditText txtValaue = (CustomFontEditText) dialog.findViewById(R.id.edt_content);
        CustomFontButton btnAccept = (CustomFontButton) dialog.findViewById(R.id.btn_accept);
        CustomFontButton btnDenice = (CustomFontButton) dialog.findViewById(R.id.btn_denice);
        txtTitle.setText(titles);
        txtValaue.setText(value);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(txtValaue.getText().toString());
                listener.onAcceptClick(v);
                dialog.dismiss();
            }
        });
        btnDenice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseClick(v);
                dialog.dismiss();
            }
        });

        dialog.show();

        AppCompatImageView img_close = (AppCompatImageView) dialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }



    public static Dialog onCreateDialogChoice(final Context context, int layoutId){
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        AppCompatImageView img_close = (AppCompatImageView) view.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }



    public static MaterialDialog onCreateDialogLoading(Context context) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.content("Loading").progress(true, 0).cancelable(false);
        MaterialDialog mDialog = mBuilder.build();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        return mDialog;
    }

}
