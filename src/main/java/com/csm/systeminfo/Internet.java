package com.csm.systeminfo;

import com.csm.model.InternetModel;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;
import oshi.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class Internet {
    private static final String IP_ADDRESS_SEPARATOR = "; ";
    public List<InternetModel> GetInfo(SystemInfo si){
        List<InternetModel> internetModelList = new ArrayList<>();
        for(NetworkIF networkIF : si.getHardware().getNetworkIFs()){
            InternetModel internetModel = new InternetModel();
            internetModel.setName(networkIF.getName());
            internetModel.setIndex(networkIF.getIndex());
            internetModel.setSpeed(networkIF.getSpeed());
            internetModel.setIPV4(getIPAddressesString(networkIF.getIPv4addr()));
            internetModel.setIPV6(getIPAddressesString(networkIF.getIPv6addr()));
            internetModel.setMACAdress(Constants.UNKNOWN.equals(networkIF.getMacaddr()) ? "" : networkIF.getMacaddr());
            internetModelList.add(internetModel);
        }
        return internetModelList;
    }
    private static String getIPAddressesString(String[] ipAddressArr) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String ipAddress : ipAddressArr) {
            if (first) {
                first = false;
            } else {
                sb.append(IP_ADDRESS_SEPARATOR);
            }
            sb.append(ipAddress);
        }

        return sb.toString();
    }

    public static void main(String [] args){
    }
}
