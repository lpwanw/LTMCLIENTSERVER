package com.csm.systeminfo;

import com.csm.model.DiskModel;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.List;

public class FileStores {

    public List<DiskModel> getInfo (SystemInfo si){
        List<DiskModel> diskModelList = new ArrayList<>();
        for(OSFileStore osFileStore : si.getOperatingSystem().getFileSystem().getFileStores()){
            DiskModel diskModel = new DiskModel();
            diskModel.setName(osFileStore.getName());
            diskModel.setAvailable((double)osFileStore.getUsableSpace());
            diskModel.setUsed((double)(osFileStore.getTotalSpace()-osFileStore.getUsableSpace()));
            diskModelList.add(diskModel);
        }
        return diskModelList;
    }

}
