package dgs.example.pungkook.dgs_android.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import java.util.Calendar;
import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;

public class HistoryFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private CustomFontEditText edtDate;
    private DatePickerDialog datePickerDialog;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_history, container, false);
        edtDate = (CustomFontEditText) root.findViewById(R.id.edt_date);
        MainActivity.imgBack.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();

        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        CustomFontTextView txtTitle = ((CustomFontTextView) mToolbar.findViewById(R.id.title_toolbar));
        txtTitle.setText(getString(R.string.txt_history));

        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        edtDate.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
    }

    /*private void getHistory(){
        JSONObject object = new JSONObject();
        try {
            object.put("MCHN_ID", MainActivity.machine_id);
            object.put("PLN_DATE", edtDate.getText().toString());
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equals("NO ERROR")){
                                HistoryItem inputItem = new HistoryItem();

                            adapter = new TasklistAdapter(getActivity(), inputList, new TasklistAdapter.NavigationClickListener() {
                                @Override
                                public void onItemClick(View v, final int position) {
                                    AppDialogManager.onShowDialogAdd(getActivity(), inputList.get(position).getConfig_name(), inputList.get(position).getConfig_value(), new DialogAcceptClickListener() {
                                        @Override
                                        public void onAcceptClick(View v) {
                                            setupInput(position, v.getTag().toString());
                                        }

                                        @Override
                                        public void onCloseClick(View v) {

                                        }
                                    });
                                }
                            });
                            recyclerInput.setAdapter(adapter);
                            Toasty.success(getActivity(), "Get list input successfully!", Toast.LENGTH_SHORT, true).show();

                        }else Toasty.error(getActivity(), "Machine is not found!", Toast.LENGTH_SHORT, true).show();
                    } catch (JSONException e) {
                        Toasty.error(getActivity(), "Server error, Please try again!", Toast.LENGTH_SHORT, true).show();
                        Log.d(AppConstants.TAG, "Error while parsing json: "+e.toString());
                    }
                }
            }, getActivity()).execute(AppConstants.URL_GEt_LIST_INPUT, 6object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
