package net.dcgoodridge.cellrecorder;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class CellrecorderService extends Service {

    public static final String FILE_EXT = "cell";
    public static final String INTERNAL_FOLDER_CELL = "cell";
    public static final String RECORD_START_ACTION = CellrecorderService.class.getCanonicalName() + ".recordStart";
    public static final String RECORD_STOP_ACTION = CellrecorderService.class.getCanonicalName() + ".recordStop";
    private static final Logger LOG = LoggerFactory.getLogger(CellrecorderService.class);
    private static final int TIMER_PERIOD_SECONDS = 1;

    private static final int FOREGROUND_SERVICE_ID = 102;
    private static boolean isRecording = false;

    private FileWriter recordFw;
    private BufferedWriter recordBw;
    private PrintWriter recordPw;
    private File recordFile;

    private TelephonyManager telephonyManager;
    private Timer timer;
    private TimerTask timerTask;

    private SimpleDateFormat dateFormatter;

    private CellrecorderCollector cellrecorderCollector;


    public static boolean isRecording() {
        return isRecording;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        cellrecorderCollector = new CellrecorderCollectorApi17(telephonyManager);
        dateFormatter = new SimpleDateFormat("HH:mm:ss");
        cellUpdatesStart();
    }

    private void cellUpdatesStart() {
        cellrecorderCollector.start();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                LOG.debug("Timer tick");
                cellrecorderCollector.collect();
                String cellrecorderString = cellrecorderCollector.getCellrecorderString();
                String dataToAdd = getTimestamp() + ";" + cellrecorderString;
                if (isRecording) {
                    recordPw.println(dataToAdd);
                }
            }
        };
        timer.schedule(timerTask, 0, TIMER_PERIOD_SECONDS * 1000);
    }

    private String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private void cellUpdatesStop() {
        cellrecorderCollector.stop();
        timer.cancel();
        timer = null;
        timerTask = null;
    }


    private void recordStart() {
        if (isRecording) {
            Toast.makeText(this, "Grabación ya en curso", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            fileRecordOpen();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            recordStop();
        }

        isRecording = true;
        startForegroundNotification();
        Toast.makeText(this, "Iniciada grabación", Toast.LENGTH_SHORT).show();
    }


    private void startForegroundNotification() {
        Intent buttonPrevIntent = new Intent(this, NotificationPrevButtonHandler.class);
        buttonPrevIntent.putExtra("action", "prev");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        Intent notificationIntent = new Intent(this, CellrecorderActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notificationBuilder.setContentIntent(intent).setContentTitle("CELL Recorder").setSmallIcon(R.mipmap.notif_icon_white).setContentText("Grabando informacion a fichero").setOngoing(true);
        Notification notification = notificationBuilder.build();
        startForeground(FOREGROUND_SERVICE_ID, notification);
    }

    private void recordStop() {
        if (isRecording) {
            fileRecordClose();
            isRecording = false;
            stopForeground(true);
            Toast.makeText(this, "Fichero generado: " + recordFile.getName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        if (isRecording) {
            recordStop();
        }
        cellUpdatesStop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(RECORD_START_ACTION)) {
            recordStart();
        } else if (intent.getAction().equals(RECORD_STOP_ACTION)) {
            recordStop();
            stopSelf();
        }
        return START_NOT_STICKY;
    }


    private String computeFileName(File directory) {
        String timestamp = Long.toString(System.currentTimeMillis());
        String fileName = timestamp + "." + FILE_EXT;

        File file = new File(directory, fileName);
        if (file.exists()) {
            fileName = timestamp + "-2." + FILE_EXT;
            file = new File(directory, fileName);
        }
        if (file.exists()) {
            throw new RuntimeException(fileName + " ya existe.");
        }
        return fileName;
    }


    private void fileRecordOpen() throws Exception {
        File recordFolder = new File(getFilesDir().getAbsolutePath() + File.separator + INTERNAL_FOLDER_CELL);
        if (!recordFolder.exists()) {
            recordFolder.mkdirs();
        }

        String fileName = computeFileName(recordFolder);
        recordFile = new File(recordFolder, fileName);
        recordFw = new FileWriter(recordFile, true);
        recordBw = new BufferedWriter(recordFw);
        recordPw = new PrintWriter(recordBw);
    }


    private void fileRecordClose() {
        if (recordPw != null) {
            recordPw.flush();
            recordPw.close();
        }

        try {
            if (recordFw != null) recordFw.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        try {
            if (recordBw != null) recordBw.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static class NotificationPrevButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, ".", Toast.LENGTH_SHORT).show();
        }
    }

}
