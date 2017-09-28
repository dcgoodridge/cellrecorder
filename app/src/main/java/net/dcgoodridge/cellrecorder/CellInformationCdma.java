package net.dcgoodridge.cellrecorder;

import android.telephony.CellIdentityCdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellSignalStrengthCdma;


public class CellInformationCdma implements CellInformation {

    private int basestationId;
    private int networkId;
    private int systemId;
    private int latitude;
    private int longitude;
    private int rssi;

    public CellInformationCdma(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
        CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();

        basestationId = cellIdentityCdma.getBasestationId();
        networkId = cellIdentityCdma.getNetworkId();
        systemId = cellIdentityCdma.getSystemId();
        latitude = cellIdentityCdma.getLatitude();
        longitude = cellIdentityCdma.getLongitude();
        rssi = cellSignalStrengthCdma.getDbm();
    }

    public int getBasestationId() {
        return basestationId;
    }

    public int getNetworkId() {
        return networkId;
    }

    public int getSystemId() {
        return systemId;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        String s = "";
        s = "$CDMA" + SEP + String.valueOf(basestationId) + SEP + String.valueOf(networkId) + SEP +
            String.valueOf(systemId) + SEP + String.valueOf(rssi);
        return s;
    }

    @Override
    public TYPE getType() {
        return TYPE.CDMA;
    }
}
