package dgs.example.pungkook.dgs_android.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.adapter.OutputAdapter;
import dgs.example.pungkook.dgs_android.model.OutputItem;
import dgs.example.pungkook.dgs_android.utils.AppConstants;
import dgs.example.pungkook.dgs_android.utils.HTTPRequest;
import es.dmoral.toasty.Toasty;

public class OutputFragment extends Fragment {
    private RecyclerView recyclerOnput;
    static ArrayList<OutputItem> outputList;
    static OutputAdapter adapter;

    
    private Context context;
    public static Activity activity;

    public OutputFragment() {
        // Required empty public constructor
    }

    public static OutputFragment newInstance() {
        OutputFragment fragment = new OutputFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_output, container, false);

        recyclerOnput = (RecyclerView) root.findViewById(R.id.recycler_output);
        outputList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerOnput.setLayoutManager(mLayoutManager);
        recyclerOnput.setItemAnimator(new DefaultItemAnimator());

        return root;
    }

    public static void receiveMessage(final String mess){
        activity.runOnUiThread(new Runnable() {
            public void run() {
               // Toast.makeText(activity, mess, Toast.LENGTH_LONG).show();
                try {
                    JSONObject obj = new JSONObject(mess);
                    OutputItem outputItem = new OutputItem();
                    outputItem.setOutput_name(obj.getString("name"));
                    outputItem.setOutput_value(obj.getString("value"));
                    boolean isExist = false;
                    for(int i=0; i<outputList.size(); i++){
                        if(outputList.get(i).getOutput_name().equals(outputItem.getOutput_name())){
                            isExist = true;
                            outputList.set(i, outputItem);
                        }
                    }
                    if(!isExist) outputList.add(outputItem);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                   Log.e(AppConstants.TAG, e.toString());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.item!=null && outputList.isEmpty()) getOutputList();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getOutputList(){
        JSONObject object = new JSONObject();
        try {
            object.put("PLN_DATE", MainActivity.item.getSet_plan_date());
            object.put("MCHN_ID", MainActivity.machine_id);
            object.put("DLY_SEQ", MainActivity.item.getDaily_seq());
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equals("NO ERROR")){
                            outputList.clear();
                            JSONArray output_names = oj.getJSONArray("OUTPUTS");
                            for(int i=0; i<output_names.length(); i++){
                                OutputItem outputItem = new OutputItem();
                                outputItem.setConfig_seq(output_names.getJSONObject(i).getString("CNFG_SEQ"));
                                outputItem.setOutput_name(output_names.getJSONObject(i).getString("OTPT_NM"));
                                outputItem.setOutput_value(output_names.getJSONObject(i).getString("OTPT_VAL"));

                                outputList.add(outputItem);
                            }
                            Log.d(AppConstants.TAG, "Size inputs "+ String.valueOf(outputList.size()));
                           adapter = new OutputAdapter(context, outputList);
                            recyclerOnput.setAdapter(adapter);
                            Toasty.success(context, "Get list output successfully!", Toast.LENGTH_SHORT, true).show();

                        }else Toasty.error(context, "Get list output failed!", Toast.LENGTH_SHORT, true).show();
                    } catch (JSONException e) {
                        Toasty.error(context, "Server error, Please try again!", Toast.LENGTH_SHORT, true).show();
                        Log.d(AppConstants.TAG, "Error while parsing json: "+e.toString());
                    }
                }

            }, context).execute(AppConstants.URL_GEt_LIST_OUTPUT, object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
