package net.dcgoodridge.cellrecorder;

import android.telephony.CellIdentityGsm;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;


public class CellInformationGsm implements CellInformation {

    private int cid;
    private int lac;
    private int mcc;
    private int mnc;
    private int rssi;

    public CellInformationGsm(CellInfoGsm cellInfoGsm) {

        CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();

        cid = cellIdentityGsm.getCid();
        lac = cellIdentityGsm.getLac();
        mcc = cellIdentityGsm.getMcc();
        mnc = cellIdentityGsm.getMnc();
        rssi = cellSignalStrengthGsm.getDbm();
    }

    public int getCid() {
        return cid;
    }

    public int getLac() {
        return lac;
    }

    public int getMcc() {
        return mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        String s = "";
        s = "$GSM" + SEP + String.valueOf(mcc) + SEP + String.valueOf(mnc) + SEP + String.valueOf(lac) + SEP +
            String.valueOf(cid) + SEP + String.valueOf(rssi);
        return s;
    }

    @Override
    public TYPE getType() {
        return TYPE.GSM;
    }
}
