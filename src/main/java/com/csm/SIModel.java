package com.csm;

import com.csm.model.*;

public class SIModel {
    private String namePC;
    private OsHWModel osHWModel;

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
}
