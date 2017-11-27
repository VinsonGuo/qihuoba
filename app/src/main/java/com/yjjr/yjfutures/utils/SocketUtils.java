package com.yjjr.yjfutures.utils;

import com.yjjr.yjfutures.utils.http.HttpConfig;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

/**
 * Created by dell on 2017/9/29.
 */

public class SocketUtils {

    public static final String HIS_DATA = "getHistoryData";
    public static final String SYMBOL_LIST = "getSymbolList";
    public static final String HIS_TICKS = "topHistoricalTicks";

    private static Socket sSocket;

    public static void init() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;
            options.transports = new String[]{WebSocket.NAME};
            //创建连接
            sSocket = IO.socket(HttpConfig.SOCKET_URL, options);
            sSocket.connect();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    public static Socket getSocket() {
        return sSocket;
    }


    public static void disconnect() {
        if (sSocket != null && sSocket.connected()) {
            sSocket.disconnect();
        }
    }
}
