package net.dcgoodridge.cellrecorder;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;

import static net.dcgoodridge.cellrecorder.UtilsBinary.extraerNumero;


public class CellInformationWcdma implements CellInformation {


    private int LCID;
    private int cid;
    private int rncId;
    private int mcc;
    private int psc;
    private int lac;
    private int mnc;
    private int rssi;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public CellInformationWcdma(CellInfoWcdma cellInfoWcdma) {

        CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
        LCID = cellIdentityWcdma.getCid();
        cid = extraerNumero(LCID, 16, 0);
        rncId = extraerNumero(LCID, 12, 16);
        mcc = cellIdentityWcdma.getMcc();
        mnc = cellIdentityWcdma.getMnc();
        psc = cellIdentityWcdma.getPsc();
        lac = cellIdentityWcdma.getLac();
        rssi = cellSignalStrengthWcdma.getDbm();
    }

    public int getLCID() {
        return LCID;
    }

    public int getCid() {
        return cid;
    }

    public int getRncId() {
        return rncId;
    }

    public int getMcc() {
        return mcc;
    }

    public int getPsc() {
        return psc;
    }

    public int getLac() {
        return lac;
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
        s = "$WCDMA" + SEP + String.valueOf(mcc) + SEP + String.valueOf(mnc) + SEP + String.valueOf(lac) +
            SEP + String.valueOf(rncId) + SEP + String.valueOf(cid) + SEP + String.valueOf(psc) + SEP +
            String.valueOf(rssi);
        return s;
    }

    @Override
    public TYPE getType() {
        return TYPE.WCDMA;
    }
}
