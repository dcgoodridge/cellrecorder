package net.dcgoodridge.cellrecorder;

public interface CellInformation {

    String SEP = ",";

    TYPE getType();

    enum TYPE {GSM, CDMA, WCDMA, LTE, UNKNOWN}

}
