package com.csm;

import com.csm.model.*;

public class SIModel {
    private String namePC;
    private OsHWModel osHWModel;
    private DiskModel diskModel;
    private MemoryModel memoryModel;
    private ProcessModel processModel;
    private UsbModel usbModel;
    private InternetModel internetModel;

    public String getNamePC() {
        return namePC;
    }

    public void setNamePC(String namePC) {
        this.namePC = namePC;
    }

    public OsHWModel getOsHWModel() {
        return osHWModel;
    }

    public void setOsHWModel(OsHWModel osHWModel) {
        this.osHWModel = osHWModel;
    }

    public DiskModel getDiskModel() {
        return diskModel;
    }

    public void setDiskModel(DiskModel diskModel) {
        this.diskModel = diskModel;
    }

    public MemoryModel getMemoryModel() {
        return memoryModel;
    }

    public void setMemoryModel(MemoryModel memoryModel) {
        this.memoryModel = memoryModel;
    }

    public ProcessModel getProcessModel() {
        return processModel;
    }

    public void setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
    }

    public UsbModel getUsbModel() {
        return usbModel;
    }

    public void setUsbModel(UsbModel usbModel) {
        this.usbModel = usbModel;
    }

    public InternetModel getInternetModel() {
        return internetModel;
    }

    public void setInternetModel(InternetModel internetModel) {
        this.internetModel = internetModel;
    }
}
