package net.dcgoodridge.cellrecorder;


import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;

public class UtilsCell {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static CellInformation getCellInformation(CellInfo cellInfo) {
        CellInformation cellInformation = null;
        if (cellInfo instanceof CellInfoGsm) {
            cellInformation = new CellInformationGsm((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoLte) {
            cellInformation = new CellInformationLte((CellInfoLte) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma) {
            cellInformation = new CellInformationWcdma((CellInfoWcdma) cellInfo);
        } else if (cellInfo instanceof CellInfoCdma) {
            cellInformation = new CellInformationCdma((CellInfoCdma) cellInfo);
        } else {
            cellInformation = new CellInformationUnknown();
        }
        return cellInformation;
    }

}
