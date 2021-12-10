package com.csm.systeminfo;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;

import java.util.List;

public class Memory {
    public String GetMemoryText(SystemInfo si){
        StringBuilder sb = new StringBuilder();
        GlobalMemory globalMemory = si.getHardware().getMemory();
        List<PhysicalMemory> pmList = globalMemory.getPhysicalMemory();
        for (PhysicalMemory pm : pmList) {
            sb.append('\n').append(pm.toString());
        }
        return sb.toString();
    }
    public Double GetPhysicMemAvailable(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getAvailable();
    }
    public Double GetPhysicMemUsed(SystemInfo si){
        GlobalMemory globalMemory= si.getHardware().getMemory();
        return (double)globalMemory.getTotal()-globalMemory.getAvailable();
    }
    public Double GetVirtualMemUsed(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double)virtualMemory.getSwapUsed();
    }
    public Double GetVirtualMemAvailable(SystemInfo si){
        VirtualMemory virtualMemory = si.getHardware().getMemory().getVirtualMemory();
        return (double) virtualMemory.getSwapTotal() - virtualMemory.getSwapUsed();
    }


}
