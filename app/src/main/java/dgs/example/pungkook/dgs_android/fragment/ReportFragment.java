package dgs.example.pungkook.dgs_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import dgs.example.pungkook.dgs_android.MainActivity;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.model.ReportItem;
import dgs.example.pungkook.dgs_android.utils.AppConstants;
import dgs.example.pungkook.dgs_android.utils.HTTPRequest;
import es.dmoral.toasty.Toasty;

public class ReportFragment extends Fragment {

    //pie chart
    private static String TAG = "ReportFragment";
    private int[]yData = new int[2];
    private String[]xData = {"Target", "Counter"};
    PieChart chart;
    Context context;

    //app
    CustomFontTextView txtValueTarget, txtValueCounter;
    ReportItem item;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_report, container, false);

        chart = (PieChart)root.findViewById(R.id.chart);

        txtValueTarget = (CustomFontTextView) root.findViewById(R.id.txt_value_target);
        txtValueCounter = (CustomFontTextView) root.findViewById(R.id.txt_value_counter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.item!=null && chart!=null) getReport();
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

    private void setupPieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < yData.length; i ++){
            pieEntries.add(new PieEntry(yData[i], xData[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        PieData data = new PieData(dataSet);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.animateY(1000);
        chart.setData(data);
        chart.invalidate();
    }

    private void getReport(){
        final JSONObject object = new JSONObject();
        try {
            MainActivity.mDialogLoading.show();
            object.put("PLN_DATE", MainActivity.item.getSet_plan_date());
            object.put("MCHN_ID", MainActivity.machine_id);
            object.put("DLY_SEQ", MainActivity.item.getDaily_seq());

            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG,  "report "+ output);
                    MainActivity.mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                            item = new ReportItem();
                            item.setValue_target(oj.getInt("TARGET"));
                            item.setValue_counter(oj.getInt("COUNTER"));
                            yData[0] = oj.getInt("TARGET");
                            yData[1] = oj.getInt("COUNTER");
                            setupPieChart();
                            setReport();
                            Toasty.success(getActivity(), "Get report successfully!", Toast.LENGTH_LONG).show();
                        }else {
                            Toasty.info(getActivity(), "No data!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json report "+e.toString());
                        Toasty.error(getActivity(), "Get report failed. Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }, getActivity()).execute(AppConstants.URL_GEt_REPORT, object.toString());
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json report "+e.toString());
            Toasty.error(getActivity(), "Get report failed. Please try again!", Toast.LENGTH_LONG).show();
        }

    }

    private void setReport(){
        if(item != null){
            txtValueTarget.setText(String.valueOf(item.getValue_target()));
            txtValueCounter.setText(String.valueOf(item.getValue_counter()));
        }else Toasty.info(getActivity(), "No data!", Toast.LENGTH_LONG).show();
    }
}
