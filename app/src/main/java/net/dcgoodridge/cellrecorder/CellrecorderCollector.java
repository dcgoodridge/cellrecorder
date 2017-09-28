package net.dcgoodridge.cellrecorder;


import java.util.List;

public interface CellrecorderCollector {

    String UNKNOWN_CELLINFO = "UNKNOWN";

    void start();

    String getCellrecorderString();

    CellInformation getCellInformation();

    List<CellInformation> getNeighboursCellInformation();

    int getSignalStrength();

    void stop();

    void collect();


}
