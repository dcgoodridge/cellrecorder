package net.dcgoodridge.cellrecorder;

import android.telephony.SignalStrength;

/**
 * Descompone el toString del objeto SignalStrength, formando variables separadas accesibles.
 */
public class SignalStrengthComponents {

    public String GsmSignalStrength;
    public String GsmBitErrorRate;
    public String CdmaDbm;
    public String CdmaEcio;
    public String EvdoDbm;
    public String EvdoEcio;
    public String EvdoSnr;
    public String LteSignalStrength;
    public String LteRsrp;
    public String LteRsrq;
    public String LteRssnr;
    public String LteCqi;

    public SignalStrengthComponents(SignalStrength signalStrength) {
        String ssignal = signalStrength.toString();
        String[] parts = ssignal.split(" ");

        GsmSignalStrength = parts[1];
        GsmBitErrorRate = parts[2];

        CdmaDbm = parts[3];
        CdmaEcio = parts[4];

        EvdoDbm = parts[5];
        EvdoEcio = parts[6];
        EvdoSnr = parts[7];

        LteSignalStrength = parts[8];
        LteRsrp = parts[9];
        LteRsrq = parts[10];
        LteRssnr = parts[11];
        LteCqi = parts[12];
    }

    @Override
    public String toString() {
        String s = "";
        s += "GsmSignalStrength=" + GsmSignalStrength + "\n";
        s += "GsmBitErrorRate=" + GsmBitErrorRate + "\n";
        s += "CdmaDbm=" + CdmaDbm + "\n";
        s += "CdmaEcio=" + CdmaEcio + "\n";
        s += "EvdoDbm=" + EvdoDbm + "\n";
        s += "EvdoEcio=" + EvdoEcio + "\n";
        s += "EvdoSnr=" + EvdoSnr + "\n";
        s += "signalStrength=" + LteSignalStrength + "\n";
        s += "rsrp=" + LteRsrp + "\n";
        s += "rssnr=" + LteRssnr + "\n";
        s += "cqi=" + LteCqi;

        return s;
    }
}
