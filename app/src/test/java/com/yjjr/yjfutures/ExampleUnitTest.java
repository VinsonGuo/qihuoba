package com.yjjr.yjfutures;

import android.support.v4.media.MediaMetadataCompat;

import com.yjjr.yjfutures.model.AccountInfo;
import com.yjjr.yjfutures.model.Exchange;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.junit.Test;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGetExchangeList() throws Exception {
        HttpManager.getHttpService().getUserExchange("5ff73cf4-1c90-4fb0-8147-7a7c7eb7f0e7","test001")
                .subscribe(new Consumer<List<Exchange>>() {
                    @Override
                    public void accept(@NonNull List<Exchange> exchanges) throws Exception {

                    }
                });
    }

    @Test
    public void testGetUserInfo() throws Exception {
        HttpManager.getHttpService().getAccountInfo("5ff73cf4-1c90-4fb0-8147-7a7c7eb7f0e7")
                .subscribe(new Consumer<AccountInfo>() {
                    @Override
                    public void accept(@NonNull AccountInfo s) throws Exception {

                    }
                });
    }
}