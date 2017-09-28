package net.dcgoodridge.cellrecorder;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Recopilador de datos CELL para android API 17...en adelante
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class CellrecorderCollectorApi17 implements CellrecorderCollector {

    private static final int UNKNOWN_SIGNAL_STRENGTH = -200;

    private static final Logger LOG = LoggerFactory.getLogger(CellrecorderCollectorApi17.class);

    private static final int PHONE_STATE_LISTENER_MASK = PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
    private TelephonyManager telephonyManager;

    private int signalStrengthDbm = UNKNOWN_SIGNAL_STRENGTH;

    private String collectedCellString = "";
    private CellInformation cellInformation = new CellInformationUnknown();
    private List<CellInformation> cellNeighbourInformation = new ArrayList<>();

    private MyPhoneStateListener myPhoneStateListener;

    public CellrecorderCollectorApi17(TelephonyManager telephonyManager) {
        this.telephonyManager = telephonyManager;
        myPhoneStateListener = new MyPhoneStateListener();
    }

    @Override
    public void collect() {
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        cellInformation = getCellInformation(cellInfoList);
        cellNeighbourInformation = getCellNeighbourInformation(cellInfoList);
        collectedCellString = null;

        switch (cellInformation.getType()) {
            case GSM:
                collectedCellString = cellInformation.toString();
                signalStrengthDbm = ((CellInformationGsm) cellInformation).getRssi();
                break;
            case CDMA:
                collectedCellString = cellInformation.toString();
                signalStrengthDbm = ((CellInformationCdma) cellInformation).getRssi();
                break;
            case WCDMA:
                collectedCellString = cellInformation.toString();
                signalStrengthDbm = ((CellInformationWcdma) cellInformation).getRssi();
                break;
            case LTE:
                collectedCellString = cellInformation.toString();
                signalStrengthDbm = ((CellInformationLte) cellInformation).getRsrp();
                break;
            default:
                collectedCellString = null;
                signalStrengthDbm = UNKNOWN_SIGNAL_STRENGTH;
                break;

        }

    }

    @Override
    public void start() {
        telephonyManager.listen(myPhoneStateListener, PHONE_STATE_LISTENER_MASK);
    }

    @Override
    public String getCellrecorderString() {
        if (collectedCellString == null) return UNKNOWN_CELLINFO;
        else return collectedCellString;
    }

    @Override
    public CellInformation getCellInformation() {
        return cellInformation;
    }

    @Override
    public List<CellInformation> getNeighboursCellInformation() {
        return cellNeighbourInformation;
    }

    @Override
    public int getSignalStrength() {
        return signalStrengthDbm;
    }

    @Override
    public void stop() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private CellInformation getCellInformation(List<CellInfo> cellInfoList) {
        if (cellInfoList == null || cellInfoList.size() == 0) {
            return new CellInformationUnknown();
        }
        CellInfo currentCellInfo = null;
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo.isRegistered()) {
                currentCellInfo = cellInfo;
                break;
            }
        }

        if (currentCellInfo == null) {
            return new CellInformationUnknown();
        }
        CellInformation cellInformation = UtilsCell.getCellInformation(currentCellInfo);
        return cellInformation;
    }

    private List<CellInformation> getCellNeighbourInformation(List<CellInfo> cellInfoList) {
        List<CellInformation> cellInformationList = new ArrayList<>();
        if (cellInfoList == null || cellInfoList.size() <= 1) {
            return cellInformationList;
        }
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo != null && !cellInfo.isRegistered()) {
                CellInformation neighbour = UtilsCell.getCellInformation(cellInfo);
                cellInformationList.add(neighbour);
            }
        }
        return cellInformationList;
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
        }

    }

}
