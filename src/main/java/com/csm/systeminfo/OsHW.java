package com.csm.systeminfo;

import com.csm.SIModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.EdidUtil;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class OsHW {
    private static SystemInfo si = new SystemInfo();

    public static String getOsPrefix(SystemInfo si) {
        StringBuilder sb = new StringBuilder();

        OperatingSystem os = si.getOperatingSystem();
        sb.append(String.valueOf(os));
        return sb.toString();
    }

    public static String getHw(SystemInfo si) {
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

    public static String getProc(SystemInfo si) {
        StringBuilder sb = new StringBuilder();
        CentralProcessor proc = si.getHardware().getProcessor();
        sb.append(proc.toString());
        return sb.toString();
    }

    public static String getDisplay(SystemInfo si) {
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

    public static String getProc() {
        SystemInfo si = new SystemInfo();
        CentralProcessor proc = si.getHardware().getProcessor();
        return proc.toString();
    }

    public static String getInfo(SIModel si) {
        return new Gson().toJson(si);
    }

    public static SIModel getSIModelfromJson(String json) {
        return new Gson().fromJson(json, SIModel.class);
    }
}
