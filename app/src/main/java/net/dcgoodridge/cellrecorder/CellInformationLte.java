package net.dcgoodridge.cellrecorder;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;

import static net.dcgoodridge.cellrecorder.UtilsBinary.extraerNumero;


public class CellInformationLte implements CellInformation {

    private int eci;
    private int mcc;
    private int mnc;
    private int pci;
    private int tac;

    private int cid;
    private int enb;

    private int rsrp;
    private int rsrq;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CellInformationLte(CellInfoLte cellInfoLte) {

        CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
        eci = cellIdentityLte.getCi();
        mcc = cellIdentityLte.getMcc();
        mnc = cellIdentityLte.getMnc();
        pci = cellIdentityLte.getPci();
        tac = cellIdentityLte.getTac();
        cid = extraerNumero(eci, 8, 0);
        enb = extraerNumero(eci, 20, 8);
        rsrp = cellSignalStrengthLte.getDbm();

        CellSignalStrengthLteComponents cellSignalStrengthLteComponents = new CellSignalStrengthLteComponents(cellSignalStrengthLte);
        rsrq = stringToIntegerSafe(cellSignalStrengthLteComponents.rsrq);
    }

    private int stringToIntegerSafe(String s) {
        if (s == null) return 0;
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            i = 0;
        }
        return i;
    }

    public int getEci() {
        return eci;
    }

    public int getMcc() {
        return mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public int getPci() {
        return pci;
    }

    public int getTac() {
        return tac;
    }

    public int getCid() {
        return cid;
    }

    public int getEnb() {
        return enb;
    }

    public int getRsrp() {
        return rsrp;
    }

    public int getRsrq() {
        return rsrq;
    }

    @Override
    public String toString() {
        String s = "";
        s = "$LTE" + SEP + String.valueOf(mcc) + SEP + String.valueOf(mnc) + SEP + String.valueOf(enb) +
            SEP + String.valueOf(cid) + SEP + String.valueOf(pci) +
            SEP + String.valueOf(tac) + SEP + String.valueOf(rsrp) + SEP + String.valueOf(rsrq);
        return s;

    }

    @Override
    public TYPE getType() {
        return TYPE.LTE;
    }
}
