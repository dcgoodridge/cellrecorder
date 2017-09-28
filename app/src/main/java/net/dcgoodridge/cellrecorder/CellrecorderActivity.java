package net.dcgoodridge.cellrecorder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CellrecorderActivity extends AppCompatActivity {

    public static final String[] ACTIVITY_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
                                                         Manifest.permission.READ_PHONE_STATE};
    private static final int TIMER_PERIOD_SECONDS = 1;
    private static final Logger LOG = LoggerFactory.getLogger(CellrecorderActivity.class);
    private static final int PERMISSIONS_REQUEST_CODE = 125;

    private boolean permissionsGranted = false;
    private ListView listview;
    private CellrecorderListAdapter adapter;
    private Button buttonRecordStart;
    private Button buttonRecordStop;
    private TelephonyManager telephonyManager;
    private Timer timer;
    private TimerTask timerTask;
    private CellrecorderCollector cellrecorderCollector;
    private Intent starterIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellrecorder);
        Toolbar supportToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(supportToolbar);
        getSupportActionBar().setTitle(null);
        permissionsGranted = checkPermissionsAndRequest();
        init();
        if (permissionsGranted) initPermissionsGranted();
    }

    private boolean checkPermissionsAndRequest() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(this, ACTIVITY_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    private boolean permissionsGranted() {
        for (int i = 0; i < ACTIVITY_PERMISSIONS.length; i++) {
            String permission = ACTIVITY_PERMISSIONS[i];
            boolean granted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                LOG.warn("Missing permission: " + permission);
                return false;
            }
        }
        return true;
    }

    private void init() {
        starterIntent = getIntent();
        initGui();
    }

    private void initPermissionsGranted() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        cellrecorderCollector = new CellrecorderCollectorApi17(telephonyManager);
        collectorTasksStart();
    }

    private void setRecordGui(boolean isRecording) {
        if (isRecording) {
            buttonRecordStart.setEnabled(false);
            buttonRecordStop.setEnabled(true);
        } else {
            buttonRecordStart.setEnabled(true);
            buttonRecordStop.setEnabled(false);
        }
    }

    private void initGui() {
        listview = (ListView) findViewById(R.id.cellrecorder_listview);
        adapter = new CellrecorderListAdapter(this, new ArrayList<String>());
        adapter.textColor = Color.WHITE;
        listview.setAdapter(adapter);

        buttonRecordStart = (Button) findViewById(R.id.cellrecorder_button_start);
        buttonRecordStop = (Button) findViewById(R.id.cellrecorder_button_stop);

        buttonRecordStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    eventRecordStart();
                } catch (Exception e) {
                    Toast.makeText(CellrecorderActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonRecordStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRecordStop();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && hasAllPermissionsGranted(grantResults)) {
                    //recreate();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void eventRecordStart() throws Exception {
        Intent startIntent = new Intent(CellrecorderActivity.this, CellrecorderService.class);
        startIntent.setAction(CellrecorderService.RECORD_START_ACTION);
        startService(startIntent);

        setRecordGui(true);
    }

    private void eventRecordStop() {
        Intent stopIntent = new Intent(CellrecorderActivity.this, CellrecorderService.class);
        stopIntent.setAction(CellrecorderService.RECORD_STOP_ACTION);
        startService(stopIntent);

        setRecordGui(false);
    }

    private boolean isRecording() {
        return CellrecorderService.isRecording();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cellrecorder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nmearecorder_filelist:
                if (isRecording()) {
                    Toast.makeText(this, "Detener grabación primero", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, CellrecorderListActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.menu_config:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        if (permissionsGranted) onResumePermissionsGranted();
        else onResumeNoPermissions();
        super.onResume();
    }

    private void onResumePermissionsGranted() {
        setRecordGui(CellrecorderService.isRecording());
    }

    private void collectorTasksStart() {
        cellrecorderCollector.start();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //LOG.debug("Timer tick");
                        cellrecorderCollector.collect();
                        final String cellrecorderString = cellrecorderCollector.getCellrecorderString();
                        adapter.add(cellrecorderString);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        };
        timer.schedule(timerTask, 0, TIMER_PERIOD_SECONDS * 1000);
    }

    private void collectorTasksStop() {
        timer.cancel();
        timer = null;
        timerTask = null;
        cellrecorderCollector.stop();
    }

    private void onResumeNoPermissions() {
        buttonRecordStart.setEnabled(false);
        buttonRecordStop.setEnabled(false);
    }

    @Override
    protected void onPause() {
        if (permissionsGranted) onPausePermissionsGranted();
        else onPauseNoPermissions();
        super.onPause();
    }

    private void onPauseNoPermissions() {
    }

    private void onPausePermissionsGranted() {
    }

    private void onDestroyPermissionsGranted() {
        collectorTasksStop();
    }


    @Override
    protected void onDestroy() {
        if (permissionsGranted) onDestroyPermissionsGranted();
        else onDestroyNoPermissions();
        super.onDestroy();
    }

    private void onDestroyNoPermissions() {

    }


}
