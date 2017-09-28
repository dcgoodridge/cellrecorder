
![CELL Recorder](logo.png)
[CELL Recorder in Google Play](https://play.google.com/store/apps/details?id=net.dcgoodridge.cellrecorder)

Con esta aplicación podrás grabar en un fichero los parámetros de telefonóa móvil, segundo a segundo. El fichero grabado se puede compartir para hacer un análisis posterior.

CELL Recorder genera tramas con la información de célula actual, como el MCC, MNC, CID o el tipo de red. Además, la trama contiene la marca temporal "timestamp" en la que se genera. El "timestamp" es el valor Unix Epoch en milisegundos.

El formato de tramas, según el tipo de red, es el siguiente:

timestamp;$GSM,mcc,mnc,lac,cid,rssi
timestamp;$CDMA,bsid,nid,sid,rssi
timestamp;$WCDMA,mcc,mnc,lac,rncid,cid,psc,rssi
timestamp;$LTE,mcc,mnc,enb,cid,pci,tac,rsrp,rsrq
timestamp;$UNKNOWN