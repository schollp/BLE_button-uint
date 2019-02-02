
BLE-button-uint
===================================

This program serves for the data exchange between Arduino and an Android smartphone.

ARDUINO
------------
The Arduino receives sensor signals in UINT16, converts them into two UINT8 Bytes each and transmits them to HM-10.

ANDROID
--------------
On the smartphone, the data is received and re-converted to UINT16 and (yet TO DO) saved in a .txt-file.
