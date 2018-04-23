package com.zjpavt.socket.device.connect;

import io.netty.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface IDeviceConnectManager {
    DeviceConnect getByConnectID(UUID connectID);

    List<DeviceConnect> getByDeviceID(UUID deviceID);

    DeviceConnect addConnect(Channel channel);

    DeviceConnect removeConnect(UUID connectID);

    DeviceConnect login(DeviceConnect deviceConnect, UUID deviceID, String deviceSerial);

    DeviceConnect logout(DeviceConnect deviceConnect);

    int countTotalConnect();

    int countTotalLoginedDevice();

}
