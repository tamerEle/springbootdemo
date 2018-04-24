package com.zjpavt.util;

public class ConfigUtil {
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**the Server communicate with device.*/
    public static final String SOCKET_CONNECT_DEVICE_ID = "Device ID:";
    public static final String SOCKET_CONNECT_CHARSET = "UTF-8";
    public static final String SOCKET_CONNECT_DEVICE_ID_COMMAND = "X";
    public static final int SOCKET_CONNECT_SEND_PERIOD = 100;
    public static final int SOCKET_CONNECT_SERVER_PORT = 8082;
    //public static final int SOCKET_CONNECT_SEND_PERIOD = 100;

    public static final int SOCKET_CONNECT_CLIENT_PORT = 8082;
    public static final String SOCKET_CONNECT_SERVER_HOST = "127.0.0.1";

    /**
     * used for http range download
     */
    public static final String DOWNLOAD_URL = "http://127.0.0.1:9090/downloadFile";
}
