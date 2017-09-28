package net.dcgoodridge.cellrecorder;


public class UtilsBinary {

    /**
     * Extrae un grupo de bits de un entero para formar otro. Por ejemplo: Para "00001110", bitLength=3 y bitOffset=1,
     * el resultado será "111".
     */
    public static int extraerNumero(int number, int bitLength, int bitOffset) {
        int result = 0;
        result = (number >> bitOffset);
        int mask = (1 << bitLength) - 1;
        result = result & mask;
        return result;
    }


}
