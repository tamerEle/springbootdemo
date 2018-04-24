package com.zjpavt.socket.device.connect;

import io.netty.channel.Channel;

import java.util.UUID;

public class DeviceConnect {
    private UUID connectID;
    private UUID deviceID;
    private long connectTime;
    private Channel channel;
    private String deviceSerial;

    public DeviceConnect() {
        this.connectID = UUID.randomUUID();
        this.connectTime = System.currentTimeMillis();
    }

    public UUID getConnectID() {
        return connectID;
    }

    public UUID getDeviceID() {
        return deviceID;
    }

    public void _setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void _setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }
}
