package com.csm.model;

import java.io.Serializable;

public class OsHWModel implements Serializable {
    private String OSPreFix;
    private String proc;
    private String display;
    public String getOSPreFix() {
        return OSPreFix;
    }

    public void setOSPreFix(String OSPreFix) {
        this.OSPreFix = OSPreFix;
    }

    public String getProc() {
        return proc;
    }

    public void setProc(String proc) {
        this.proc = proc;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
