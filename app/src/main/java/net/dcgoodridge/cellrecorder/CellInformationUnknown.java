package net.dcgoodridge.cellrecorder;

public class CellInformationUnknown implements CellInformation {

    private static final String UNKNOWN_SENTENCE = "$UNKNOWN";

    public CellInformationUnknown() {
    }

    @Override
    public String toString() {
        String s = UNKNOWN_SENTENCE;
        return s;
    }

    @Override
    public TYPE getType() {
        return TYPE.UNKNOWN;
    }
}
