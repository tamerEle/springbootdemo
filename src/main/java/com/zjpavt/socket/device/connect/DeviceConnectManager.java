package com.zjpavt.socket.device.connect;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyc
 */
@Service
//@Slf4j
public class DeviceConnectManager implements IDeviceConnectManager {
    final private Map<UUID,DeviceConnect> connectMap = new ConcurrentHashMap();
    /**
     * if device exist the value mapping the HashMap is not null
     */
    final private Map<UUID, List<DeviceConnect>> deviceMap = new ConcurrentHashMap<>();

    @Override
    public DeviceConnect getByConnectID(UUID connectID) {
        //no need consider about the specify key is null
        return connectMap.get(connectID);
    }

    @Override
    public List<DeviceConnect> getByDeviceID(UUID deviceID) {
        return deviceMap.get(deviceID);
    }

    @Override
    public DeviceConnect addConnect(Channel channel) {
        DeviceConnect deviceConnect = new DeviceConnect();
        deviceConnect._setChannel(channel);
        connectMap.put(deviceConnect.getConnectID(),deviceConnect);
        return deviceConnect;
    }

    @Override
    public DeviceConnect removeConnect(UUID connectID) {
        return connectMap.remove(connectID);
    }

    @Override
    public DeviceConnect login(DeviceConnect deviceConnect, UUID deviceID, String deviceSerial) {
        if (deviceConnect.getDeviceID() != null) {
            logout(deviceConnect);
        }
        deviceConnect.setDeviceSerial(deviceSerial);
        deviceConnect._setDeviceID(deviceID);
        deviceMap.compute(deviceID,(deviceIDTemp,v) -> {
            List<DeviceConnect> deviceConnectList = this.deviceMap.get(deviceIDTemp);
            // meaning the list is not in the map
            if (deviceConnectList == null) {
                deviceConnectList = new ArrayList<>();
                deviceConnectList.add(deviceConnect);
            } else {
                deviceConnectList.add(deviceConnect);
            }
            return deviceConnectList;
        });
        return null;
    }

    /**
     *  can't judge by the object returned to decide if the device is existed in the connect Map.
     * @param deviceConnect the connect object of deivce
     * @return DeviceConnect
     */
    @Override
    public DeviceConnect logout(DeviceConnect deviceConnect) {
        /* can't know whether the Key is contained in the map,and can't know whether what do in the lambdas;
         * do nothing if the device not logined. */
        if (deviceConnect.getDeviceID() == null) {
            String msg = "A logout connection without deviceID";
            //throw new SocketConnectException("A logout connection without deviceID");

        }
        deviceMap.computeIfPresent(deviceConnect.getDeviceID(),(deviceID,loginedDeviceList) ->{
            if (loginedDeviceList == null) {
                throw new NullPointerException("this is null");
            }
            if (loginedDeviceList.size() > 1) {
                for (DeviceConnect deviceConnectByDeviceID : loginedDeviceList) {
                    if (deviceConnectByDeviceID.getConnectID().equals(deviceConnect.getConnectID())) {
                        loginedDeviceList.remove(deviceConnect);
                        return loginedDeviceList;
                    }
                }
                /*if the size is zero or the logout device is the only one in the Map, remove the key
                the second expression ignore the size is 1.*/
            } else if(loginedDeviceList.size() == 0 || (loginedDeviceList.get(0).getConnectID().equals(deviceConnect.getConnectID()))){
                /*for the deviceMap return null is meaning the map will remove the key.*/
                return null;
            }
            return loginedDeviceList;
        });
        return deviceConnect;
    }

    @Override
    public int countTotalConnect() {
        return this.connectMap.size();
    }

    @Override
    public int countTotalLoginedDevice() {
        return this.deviceMap.size();
    }

    public class SocketConnectException extends Exception {
        public SocketConnectException(String casue) {
            super(casue);
        }

        public SocketConnectException() {
            super();
        }

    }
}
