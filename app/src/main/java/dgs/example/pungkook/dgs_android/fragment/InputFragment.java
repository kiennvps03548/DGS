package dgs.example.pungkook.dgs_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.adapter.TasklistAdapter;
import dgs.example.pungkook.dgs_android.model.InputItem;
import dgs.example.pungkook.dgs_android.model.OutputItem;
import dgs.example.pungkook.dgs_android.utils.AppConstants;
import dgs.example.pungkook.dgs_android.utils.AppDialogManager;
import dgs.example.pungkook.dgs_android.utils.AppTransaction;
import dgs.example.pungkook.dgs_android.utils.DialogAcceptClickListener;
import dgs.example.pungkook.dgs_android.utils.HTTPRequest;
import es.dmoral.toasty.Toasty;

public class InputFragment extends Fragment{
    private Button btnSaveInput;
    private RecyclerView recyclerInput;
    ArrayList<InputItem> inputList;
    public static TasklistAdapter adapter;

    public InputFragment() {
        // Required empty public constructor
    }

    public static InputFragment newInstance() {
        InputFragment fragment = new InputFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_input, container, false);
        btnSaveInput = (Button) root.findViewById(R.id.btn_save_input);
        btnSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput();
            }
        });

        recyclerInput = (RecyclerView) root.findViewById(R.id.recycler_input);
        inputList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerInput.setLayoutManager(mLayoutManager);
        recyclerInput.setItemAnimator(new DefaultItemAnimator());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.machine_id!=null) getInputList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getInputList(){
        JSONObject object = new JSONObject();
        try {
            object.put("MCHN_ID", MainActivity.machine_id);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equals("NO ERROR")){
                            inputList.clear();
                            JSONArray input_names = oj.getJSONArray("INPUTS");
                            for(int i=0; i<input_names.length(); i++){
                                InputItem inputItem = new InputItem();
                                inputItem.setConfig_seq(input_names.getJSONObject(i).getString("CNFG_SEQ"));
                                inputItem.setMachine_mater_code(input_names.getJSONObject(i).getString("MCHN_MST_CD"));
                                inputItem.setConfig_code(input_names.getJSONObject(i).getString("CNFG_NM_CD"));
                                inputItem.setConfig_name(input_names.getJSONObject(i).getString("CNFG_NM_NM"));
                                inputItem.setConfig_value(input_names.getJSONObject(i).getString("CNFG_STNDRD_VAL"));
                                inputList.add(inputItem);
                            }
                            Log.d(AppConstants.TAG, "Size inputs "+ String.valueOf(inputList.size()));
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
        }, getActivity()).execute(AppConstants.URL_GEt_LIST_INPUT, object.toString());
        } catch (JSONException e) {
        e.printStackTrace();
        }
    }

    private void setupInput(final int position, final String value){
        JSONObject object = new JSONObject();
        try {
            MainActivity.mDialogLoading.show();
            object.put("CNFG_NM_CD", inputList.get(position).getConfig_code());
            object.put("CNFG_STNDRD_VAL", value);
            object.put("CNFG_SEQ",Integer.parseInt(inputList.get(position).getConfig_seq()));
            object.put("MCHN_MST_CD", inputList.get(position).getMachine_mater_code());
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject res = new JSONObject(output);
                        if(res.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                            Toasty.success(getActivity(), "Setup input successfully!", Toast.LENGTH_LONG).show();
                            InputItem item = new InputItem();
                            item.setConfig_value(value);
                            item.setConfig_name(inputList.get(position).getConfig_name());
                            item.setConfig_code(inputList.get(position).getConfig_code());
                            item.setMachine_mater_code(inputList.get(position).getMachine_mater_code());
                            item.setConfig_seq(inputList.get(position).getConfig_seq());
                            inputList.set(position, item);
                            adapter.notifyItemChanged(position);
                        }else {
                            Toasty.error(getActivity(), "Setup input failed, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json "+e.toString());
                        Toasty.error(getActivity(), "Setup input failed, Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }, getActivity()).execute(AppConstants.URL_SETUP_INPUT, object.toString());
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
            Toasty.error(getActivity(), "Server error, Please try again!!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveInput(){
        JSONObject object = new JSONObject();
        try {
            MainActivity.mDialogLoading.show();
            object.put("MCHN_MST_CD", inputList.get(0).getMachine_mater_code());
            object.put("MCHN_ID", MainActivity.machine_id);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject res = new JSONObject(output);
                        if(res.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                            Toasty.success(getActivity(), "Save input successfully!", Toast.LENGTH_LONG).show();
                        }else {
                            Toasty.error(getActivity(), "Save input failed, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json "+e.toString());
                        Toasty.error(getActivity(), "Save input failed, Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }, getActivity()).execute(AppConstants.URL_SAVE_INPUT, object.toString());
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
            Toasty.error(getActivity(), "Server error, Please try again!!", Toast.LENGTH_LONG).show();
        }
    }
}
