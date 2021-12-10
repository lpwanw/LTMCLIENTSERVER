package com.csm.systeminfo;

import com.csm.model.UsbModel;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.UsbDevice;

public class Usb {

    private UsbModel getUsbInfo(SystemInfo si) {
        UsbModel usbModel = new UsbModel();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (UsbDevice usbDevice : si.getHardware().getUsbDevices(true)) {
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append(String.valueOf(usbDevice));
        }
        usbModel.setData( sb.toString());
        return usbModel;
    }
    public static void main(String[] args){
        SystemInfo si = new SystemInfo();
        Usb usb= new Usb();
        UsbModel usbModel = new UsbModel();
        usbModel = usb.getUsbInfo(si);
        System.out.println(usbModel.getData());
    }
}
