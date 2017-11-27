package com.yjjr.yjfutures.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;


/**
 * Created by dell on 2017/9/29.
 */

public class BizSocketUtils {


    public static final String TRADE_RECORD = "singleTopSendRecord";

    private static Socket sSocket;

    private static Gson sGson = new Gson();

    public static void init() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;
            options.transports = new String[]{WebSocket.NAME};
            //创建连接
            sSocket = IO.socket(HttpConfig.DOMAIN + ":17070", options);
            sSocket.connect();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    public static Socket getSocket() {
        return sSocket;
    }

    public static Observable<BizResponse<List<Holds>>> getHolding(final boolean isDemo) {
        return Observable.create(new ObservableOnSubscribe<BizResponse<List<Holds>>>() {
            @Override
            public void subscribe(final ObservableEmitter<BizResponse<List<Holds>>> e) throws Exception {
                try {
                    final String event = isDemo ? "simulationHoldingListSocket" : "holdingListSocket";
                    sSocket.once(event, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            try {
                                LogUtils.d("%s -> %s", event, args[0]);
                                final List<Holds> holds = sGson.fromJson(args[0].toString(), new TypeToken<List<Holds>>() {
                                }.getType());
                                BizResponse<List<Holds>> response = new BizResponse<>();
                                response.setRcode(0);
                                response.setResult(holds);
                                response.setRmsg("ok");
                                e.onNext(response);
                            } catch (Throwable t) {
                                e.onError(t);
                            }
                        }
                    });
                    sSocket.emit(event, BaseApplication.getInstance().getAccount());
                } catch (Throwable t) {
                    e.onError(t);
                }
            }
        });
    }

    public static void disconnect() {
        if (sSocket != null && sSocket.connected()) {
            sSocket.disconnect();
        }
    }

}
