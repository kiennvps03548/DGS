package dgs.example.pungkook.dgs_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontTextView;

import org.json.JSONException;
import org.json.JSONObject;

import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.utils.AppConstants;
import dgs.example.pungkook.dgs_android.utils.AppDialogManager;
import dgs.example.pungkook.dgs_android.utils.DialogAcceptClickListener;
import dgs.example.pungkook.dgs_android.utils.HTTPRequest;
import es.dmoral.toasty.Toasty;


public class SetPlanFragment extends Fragment {
    private Button btnSaveSetPlan;
    private CustomFontTextView txtValueWorkingTime, txtValueWorkingTarget;
    private LinearLayout layoutWorkingTime, layoutWorkingTarget;
    private MaterialDialog mDialogLoading;
    View root;
    private boolean isPlanSet = false;

    public SetPlanFragment() {
        // Required empty public constructor
    }

    public static SetPlanFragment newInstance() {
        SetPlanFragment fragment = new SetPlanFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       root =  inflater.inflate(R.layout.fragment_set_plan, container, false);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());

        btnSaveSetPlan = (Button) root.findViewById(R.id.btn_save_set_plan);
        btnSaveSetPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              saveSetPlan();
            }
        });

       txtValueWorkingTime = (CustomFontTextView) root.findViewById(R.id.txt_value_working_time);
       txtValueWorkingTarget = (CustomFontTextView) root.findViewById(R.id.txt_value_working_target);

        if( MainActivity.item!=null && !isPlanSet) setSetPlanList();
        layoutWorkingTime = (LinearLayout) root.findViewById(R.id.layout_working_time);
        layoutWorkingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogManager.onShowDialogAdd(getActivity(), "Working Time", txtValueWorkingTime.getText().toString(), new DialogAcceptClickListener() {
                    @Override
                    public void onAcceptClick(View v) {
                        txtValueWorkingTime.setText(v.getTag().toString());
                    }

                    @Override
                    public void onCloseClick(View v) {

                    }
                });
            }
        });

        layoutWorkingTarget= (LinearLayout) root.findViewById(R.id.layout_working_target);
        layoutWorkingTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogManager.onShowDialogAdd(getActivity(), "Working Target Hours", txtValueWorkingTarget.getText().toString(), new DialogAcceptClickListener() {
                    @Override
                    public void onAcceptClick(View v) {
                        txtValueWorkingTarget.setText(v.getTag().toString());
                    }

                    @Override
                    public void onCloseClick(View v) {

                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if( MainActivity.item!=null && !isPlanSet) setSetPlanList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Kiên Nguyễn
    public void setSetPlanList(){
        try {
            isPlanSet = true;

            txtValueWorkingTime.setText(String.valueOf(MainActivity.item.getWorking_time()));
            txtValueWorkingTarget.setText(String.valueOf(MainActivity.item.getWorking_target_hours()));
        }catch (Exception e){
            Log.e(AppConstants.TAG, e.toString());
        }

    }

    private void saveSetPlan(){
        String working_time = txtValueWorkingTime.getText().toString();
        String working_target = txtValueWorkingTarget.getText().toString();
        final JSONObject object = new JSONObject();
        try {
            MainActivity.mDialogLoading.show();
            object.put("PLN_DATE", MainActivity.item.getSet_plan_date());
            object.put("MCHN_ID", MainActivity.machine_id);
            object.put("DLY_SEQ", MainActivity.item.getDaily_seq());
            object.put("WORK_TIME", Integer.parseInt(working_time));
            object.put("WORK_TRGT_HOURS", Integer.parseInt(working_target));

            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                            Toasty.success(getActivity(), "Save set plan successfully!", Toast.LENGTH_LONG).show();
                        }else{
                            Toasty.error(getActivity(), "Save set plan failed, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json "+e.toString());
                        Toasty.error(getActivity(), "Save set plan failed, Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }, getActivity()).execute(AppConstants.URL_SAVE_SET_PLAN, object.toString());
        } catch (Exception e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
            Toasty.error(getActivity(), "Server error, Please try again!", Toast.LENGTH_LONG).show();
        }
    }
}
