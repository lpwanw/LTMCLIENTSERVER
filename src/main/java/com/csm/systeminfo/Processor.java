package com.csm.systeminfo;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Processor {
    private static long[] oldTicks;
    private static long[][] oldProcTicks;

    public static double getCpuPercent(){
        oldTicks = new long[CentralProcessor.TickType.values().length];
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SystemInfo si = new SystemInfo();
            CentralProcessor cpu = si.getHardware().getProcessor();
            oldProcTicks = new long[cpu.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];

            double d = cpu.getSystemCpuLoadBetweenTicks(oldTicks);
            oldTicks = cpu.getSystemCpuLoadTicks();
            list.add(d);
        }
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        list.stream().filter((e)->e<1&&e>0).forEach(sum::set);
        return sum.get()*100;
    }
}
