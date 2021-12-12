package com.csm.systeminfo;

import com.csm.model.ProcessModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Processes {

    public static List<ProcessModel> getInfo(SystemInfo si) {
        long totalMem = si.getHardware().getMemory().getTotal();
        int cpuCount = si.getHardware().getProcessor().getLogicalProcessorCount();
        List<ProcessModel> processModelList = new ArrayList<>();
        for (OSProcess osProcess : si.getOperatingSystem().getProcesses()) {
            ProcessModel processModel = new ProcessModel();
            processModel.setPID(osProcess.getProcessID());
            processModel.setPPID(osProcess.getParentProcessID());
            processModel.setThreads(osProcess.getThreadCount());
            processModel.setCpu(String.format("%.1f",
                    100d * osProcess.getProcessCpuLoadBetweenTicks(osProcess)));
            processModel.setRSS(osProcess.getResidentSetSize());
            processModel.setVZS(osProcess.getVirtualSize());
            processModel.setCumulative(String.format("%.1f", 100d * osProcess.getProcessCpuLoadCumulative()));
            processModel.setMemory(String.format("%.3f", 100d * osProcess.getResidentSetSize() / totalMem));
            processModel.setProcessName(osProcess.getName());
            processModelList.add(processModel);
        }

        return processModelList;
    }
    public static String getProcessInfo(){
        SystemInfo si = new SystemInfo();
        List<ProcessModel> list = getInfo(si);
        return toJson(list);
    }
    public static String toJson(List<ProcessModel> list) {
        Type listType = new TypeToken<List<ProcessModel>>() {
        }.getType();
        return new Gson().toJson(list, listType);
    }


    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        for (ProcessModel processModel : getInfo(si)) {
            System.out.println(processModel.getProcessName() + "___" + processModel.getPID() + "___" + processModel.getPPID() + "___"
                    + processModel.getThreads() + "___" + processModel.getMemory());
        }

    }
}
