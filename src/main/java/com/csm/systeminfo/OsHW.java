package com.csm.systeminfo;

import com.csm.SIModel;
import com.csm.model.OsHWModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.software.os.OperatingSystem;
import oshi.util.EdidUtil;

import java.time.Instant;
import java.util.List;

public class OsHW {
    private static SystemInfo si = new SystemInfo();
    public static String getOsPrefix(SystemInfo si) {
        StringBuilder sb = new StringBuilder();

        OperatingSystem os = si.getOperatingSystem();
        sb.append(String.valueOf(os));
        return sb.toString();
    }

    public static  String getHw(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        ComputerSystem computerSystem = si.getHardware().getComputerSystem();
        try {
            sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(computerSystem));
        } catch (JsonProcessingException e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public static  String getProc(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        CentralProcessor proc = si.getHardware().getProcessor();
        sb.append(proc.toString().split("\n")[0]);

        return sb.toString();
    }

    public static  String getDisplay(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        List<Display> displays = si.getHardware().getDisplays();
        if (displays.isEmpty()) {
            sb.append("None detected.");
        } else {
            int i = 0;
            for (Display display : displays) {
                byte[] edid = display.getEdid();
                byte[][] desc = EdidUtil.getDescriptors(edid);
                String name = "Display " + i;
                for (byte[] b : desc) {
                    if (EdidUtil.getDescriptorType(b) == 0xfc) {
                        name = EdidUtil.getDescriptorText(b);
                    }
                }
                if (i++ > 0) {
                    sb.append('|');
                }
                sb.append(name).append(": ");
                int hSize = EdidUtil.getHcm(edid);
                int vSize = EdidUtil.getVcm(edid);
                sb.append(String.format("%d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54, vSize / 2.54));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        SIModel siModel = new SIModel();
        siModel.setNamePC("SDASD");
        OsHWModel os = new OsHWModel();
        os.setOSPreFix(getOsPrefix(si));
        os.setDisplay(getDisplay(si));
        os.setProc(getProc(si));
        siModel.setOsHWModel(os);
        String data = getInfo(siModel);
        System.out.println(data);
        System.out.println(getSIModelfromJson(data).getNamePC());
    }
    public static String getInfo(SIModel si){
        return new Gson().toJson(si);
    }
    public static SIModel getSIModelfromJson(String json){
        return new Gson().fromJson(json,SIModel.class);
    }
}
