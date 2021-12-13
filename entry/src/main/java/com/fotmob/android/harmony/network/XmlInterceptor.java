package com.fotmob.android.harmony.network;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class XmlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        String bodyStr = body.string();
        bodyStr.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
        String wrappedBody = "<abc>" + bodyStr + "</abc>";
        return response.newBuilder()
                .body(ResponseBody.create(MediaType.get("text/plain"), bodyStr))
                .build();
    }
}
