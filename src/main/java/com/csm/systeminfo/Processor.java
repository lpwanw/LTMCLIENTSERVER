package com.csm.systeminfo;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class Processor {
    private static long[] oldTicks;
    private static long[][] oldProcTicks;

    public static void getCpuPercent(){
        SystemInfo si = new SystemInfo();
        CentralProcessor cpu = si.getHardware().getProcessor();
        oldTicks = new long[CentralProcessor.TickType.values().length];
        oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];

        double d = cpu.getSystemCpuLoadBetweenTicks(oldTicks);
        oldTicks = cpu.getSystemCpuLoadTicks();

        System.out.println(String.format("%.1f",
                100d * d ));
    }
    public static void main(String[] args){
        while(true)

            getCpuPercent();
    }

}
