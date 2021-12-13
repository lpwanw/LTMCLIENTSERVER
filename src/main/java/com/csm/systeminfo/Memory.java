package com.csm.systeminfo;

import com.csm.model.MemoryModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;

import java.lang.reflect.Type;
import java.util.List;

public class Memory {
    public static String GetMemoryText(SystemInfo si){
        StringBuilder sb = new StringBuilder();
        GlobalMemory globalMemory = si.getHardware().getMemory();
        List<PhysicalMemory> pmList = globalMemory.getPhysicalMemory();
        for (PhysicalMemory pm : pmList) {
            sb.append('\n').append(pm.toString());
        }
        return sb.toString();
    }
    public static Double GetPhysicMemAvailable(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getAvailable();
    }
    public static Double GetPhysicMemUsed(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getTotal()-globalMemory.getAvailable();
    }
    public static Double GetVirtualMemUsed(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double)virtualMemory.getSwapUsed();
    }
    public static Double GetVirtualMemAvailable(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double) virtualMemory.getSwapTotal() - virtualMemory.getSwapUsed();
    }

    public static String getMemory(){
        SystemInfo si = new SystemInfo();
        MemoryModel memoryModel = new MemoryModel();
        memoryModel.setPhysicalMemoryAvailable(String.format("%.1f",100d *GetPhysicMemAvailable(si) ));
        memoryModel.setPhysicalMemoryUsed(String.format("%.1f",100d * GetPhysicMemUsed(si) ));
        memoryModel.setVirtualMemoryUsed(String.format("%.1f",100d * GetVirtualMemUsed(si)));
        memoryModel.setVirtualMemoryAvailable(String.format("%.1f",100d * GetVirtualMemAvailable(si)));
        memoryModel.setPhysicalMemoryInfo(GetMemoryText(si));
        return toJson(memoryModel);
    }

    public static String toJson(MemoryModel memoryModel) {
        return new Gson().toJson(memoryModel, MemoryModel.class);
    }

    public static void main(String[] args) {
        System.out.println(Memory.getMemory());
    }
}
