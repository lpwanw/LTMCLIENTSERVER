package com.csm.systeminfo;

import com.csm.model.DiskModel;
import com.csm.model.ProcessModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileStores {

    public static  List<DiskModel> getInfo (SystemInfo si){
        List<DiskModel> diskModelList = new ArrayList<>();
        for(OSFileStore osFileStore : si.getOperatingSystem().getFileSystem().getFileStores()){
            DiskModel diskModel = new DiskModel();
            diskModel.setName(osFileStore.getName());
            diskModel.setAvailable((double)osFileStore.getUsableSpace());
            diskModel.setUsed((double)(osFileStore.getTotalSpace()-osFileStore.getUsableSpace()));
            diskModel.setTotal((double)osFileStore.getTotalSpace());
            diskModel.setPercent(diskModel.getPercent());
            diskModelList.add(diskModel);
        }
        return diskModelList;
    }
    public static String toJson(){
        List<DiskModel> list = getInfo(new SystemInfo());
            Type listType = new TypeToken<List<DiskModel>>() {}.getType();
            return new Gson().toJson(list, listType);
    }
    public static List<DiskModel> fromJson(String json){
        Type listType = new TypeToken<List<DiskModel>>() {}.getType();
        return new Gson().fromJson(json,listType);
    }
}
