package dgs.example.pungkook.dgs_android;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import dgs.example.pungkook.dgs_android.fragment.HistoryFragment;
import dgs.example.pungkook.dgs_android.fragment.MainFragment;
import dgs.example.pungkook.dgs_android.model.SetPlanItem;
import dgs.example.pungkook.dgs_android.utils.AppConstants;
import dgs.example.pungkook.dgs_android.utils.AppDialogManager;
import dgs.example.pungkook.dgs_android.utils.AppTransaction;
import dgs.example.pungkook.dgs_android.utils.HTTPRequest;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static MaterialDialog mDialogLoading;
    public IntentIntegrator qrScan;
    public static Dialog mDialog;
    public static ImageView imgBack;
    private NfcAdapter mNfcAdapter;

    FloatingActionButton btnFap;
    PendingIntent mNfcPendingIntent;
    IntentFilter ndefDetected;
    IntentFilter[] mNdefExchangeFilters;
    CustomFontTextView txtTitle;
    MainFragment mainFragment;

    public static SetPlanItem item;
    public static String machine_id;

    public static Activity mContext;

    private void getPlan(final String id){
        final JSONObject object = new JSONObject();
        try {
            mDialogLoading.show();
            object.put("MCHN_ID", id);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    mDialogLoading.dismiss();
                    try {
                        JSONObject oj = new JSONObject(output);
                        if(oj.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                            item = new SetPlanItem();
                            item.setSet_plan_date(oj.getString("PLN_DATE"));
                            item.setDaily_seq(oj.getInt("DLY_SEQ"));
                            item.setWorking_time(oj.getLong("WORK_TIME"));
                            item.setWorking_target_hours(Short.parseShort(String.valueOf(oj.getInt("WORK_TRGT_HOURS"))));
                            Toasty.success(MainActivity.this, "Get list set plan successfully!", Toast.LENGTH_LONG).show();
                                if(txtTitle.getText().equals(getString(R.string.app_name))) {
                                    mainFragment.mViewPager.setCurrentItem(2);
                                }else{
                                    AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), mainFragment);
                                    mainFragment.mViewPager.setCurrentItem(2);
                            }
                        }else {
                            mDialogLoading.dismiss();
                            Toasty.info(MainActivity.this, "Machine has no plan!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json "+e.toString());
                        Toasty.error(MainActivity.this, "Get set plan failed, please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }, this).execute(AppConstants.URL_GET_SET_PLAN, object.toString());
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
            Toasty.error(MainActivity.this, "Get set plan failed, please try again!", Toast.LENGTH_LONG).show();
        }
    }

    private void setupRealtime(String machine_id){
        JSONObject object = new JSONObject();
        try {
            object.put(AppConstants.MACHINE_ID, machine_id);
            object.put(AppConstants.TOKEN, PreferenceManager.getDefaultSharedPreferences(this).getString(AppConstants.TOKEN_GCM, ""));
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
        }
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.d(AppConstants.TAG, output);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(output);
                    if(obj.getString("RESULT_MESSAGE").equalsIgnoreCase("NO ERROR")){
                        Toasty.success(MainActivity.this, "Realtime is ready", Toast.LENGTH_LONG).show();
                    }else Toasty.error(MainActivity.this, "An error has occured, please try again!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.e(AppConstants.TAG, "Error json "+e.toString());
                }
            }
        }, this).execute(AppConstants.URL_SENT_TOKEN, object.toString());
    }

    private void enableNdefExchangeMode() {
        if(mNfcAdapter == null) { return; }

        if(Build.VERSION.SDK_INT < 14) {
            mNfcAdapter.enableForegroundNdefPush(this, getNoteAsNdef());
        } else {
            mNfcAdapter.setNdefPushMessage(getNoteAsNdef(), this);
        }
    /* Register foreground dispatch to handler notes from inside our application
    * This will give give priority to the foreground activity when dispatching a discovered Tag to an application. */
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    // receive
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            //Replace current text with new pay-load
            mDialogLoading.dismiss();
            promptForContent(msgs[0]);
        }
    }

    // send
    private NdefMessage getNoteAsNdef() {
        byte[] textBytes = "hello".getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{
                textRecord
        });
    }

    private void promptForContent(final NdefMessage msg) {
        String msgPayload = new String(msg.getRecords()[0].getPayload());
        String body = new String(msgPayload);
        //setNoteBody(body);
        Log.d(AppConstants.TAG, body);
        machine_id = body.split("\n")[0].substring(3);
        getPlan(machine_id);
        setupRealtime(machine_id);
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();

        //when a tag with NDEF pay-load is discovered.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {msg};
            }
        } else {
            Log.d(AppConstants.TAG, "Unknown intent.");
           // finish();
        }
        return msgs;
    }

    void showDialogChoice() {
        mDialog = AppDialogManager.onCreateDialogChoice(this, R.layout.dialog_choice);
        ImageView imgNFC = (ImageView) mDialog.findViewById(R.id.img_nfc);
        ImageView imgQrCode = (ImageView) mDialog.findViewById(R.id.img_qr_code);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        imgNFC.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mDialogLoading.show();
                enableNdefExchangeMode();
            }
        });
        imgQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mDialogLoading.show();
                qrScan.initiateScan();
            }
        });

        mDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = ((CustomFontTextView) toolbar.findViewById(R.id.title_toolbar));
        mainFragment = new MainFragment();
        imgBack = (ImageView) toolbar.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        });
        showDialogChoice();

        mDialogLoading = AppDialogManager.onCreateDialogLoading(this);
        qrScan = new IntentIntegrator(this);
        AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), mainFragment);

        btnFap = (FloatingActionButton) findViewById(R.id.btn_fap);
        btnFap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNfcAdapter == null) {
            AppTransaction.Toast(this, "NFC not available on this device!");
            return;
        }

        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p (android to android).
        ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toasty.error(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                machine_id = result.getContents();
                getPlan(machine_id);
                setupRealtime(machine_id);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_history:
                Fragment frag = HistoryFragment.newInstance();
                AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), frag);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
