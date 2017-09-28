package net.dcgoodridge.cellrecorder;

import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Recopilador de datos CELL para android API 1..16
 */
public class CellrecorderCollectorApi1 implements CellrecorderCollector {

    private static final int TIMER_PERIOD_SECONDS = 1;

    private static final Logger LOG = LoggerFactory.getLogger(CellrecorderCollectorApi1.class);

    private static final int PHONE_STATE_LISTENER_MASK =
            PhoneStateListener.LISTEN_CELL_INFO + PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
    private TelephonyManager telephonyManager;


    private String cellInfoString = "";
    private String collectedCellString = "";
    private String signalStrengthString = "";

    private MyPhoneStateListener myPhoneStateListener;

    public CellrecorderCollectorApi1(TelephonyManager telephonyManager) {
        this.telephonyManager = telephonyManager;
        myPhoneStateListener = new MyPhoneStateListener();
    }


    @Override
    public void collect() {
        collectedCellString = cellInfoString + "," + signalStrengthString;
    }

    @Override
    public void start() {
        telephonyManager.listen(myPhoneStateListener, PHONE_STATE_LISTENER_MASK);
    }

    @Override
    public String getCellrecorderString() {
        return collectedCellString;
    }

    @Override
    public CellInformation getCellInformation() {
        return new CellInformationUnknown();
    }

    @Override
    public List<CellInformation> getNeighboursCellInformation() {
        return new ArrayList<>();
    }

    @Override
    public int getSignalStrength() {
        return -99;
    }


    @Override
    public void stop() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private String getCellInfoString(List<CellInfo> cellInfoList) {
        if (cellInfoList == null || cellInfoList.size() == 0) {
            return "NULL";
        }
        return "not implemented";
    }


    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
        }

        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfoList) {
            super.onCellInfoChanged(cellInfoList);
        }

    }

}
