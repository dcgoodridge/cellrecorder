package net.dcgoodridge.cellrecorder;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthLte;

/**
 * Descompone el toString del objeto SignalStrength, formando variables separadas accesibles.
 */
public class CellSignalStrengthLteComponents {

    public String signalStrength = "";
    public String rsrp = "";
    public String rsrq = "";
    public String rssnr = "";
    public String cqi = "";
    public String timingAdvance = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CellSignalStrengthLteComponents(CellSignalStrengthLte cellSignalStrengthLte) {
        String ssignal = cellSignalStrengthLte.toString();
        String[] parts = ssignal.split(" ");

        signalStrength = new KeyValueString(parts[1]).getValue();
        rsrp = new KeyValueString(parts[2]).getValue();
        rsrq = new KeyValueString(parts[3]).getValue();
        rssnr = new KeyValueString(parts[4]).getValue();
        cqi = new KeyValueString(parts[5]).getValue();
        timingAdvance = new KeyValueString(parts[6]).getValue();

    }

    @Override
    public String toString() {
        return "CellSignalStrengthLteComponents{" +
               "signalStrength='" + signalStrength + '\'' +
               ", rsrp='" + rsrp + '\'' +
               ", rsrq='" + rsrq + '\'' +
               ", rssnr='" + rssnr + '\'' +
               ", cqi='" + cqi + '\'' +
               ", timingAdvance='" + timingAdvance + '\'' +
               '}';
    }

    private static class KeyValueString {
        private String key;
        private String value;

        public KeyValueString(String keyValueString) {
            String[] parts = keyValueString.split("=");
            this.key = parts[0].trim();
            this.value = parts[1].trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
