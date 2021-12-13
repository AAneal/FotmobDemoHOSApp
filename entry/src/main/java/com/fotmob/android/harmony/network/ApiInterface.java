package com.fotmob.android.harmony.network;

import com.fotmob.android.harmony.network.model.Live;
import com.fotmob.android.harmony.network.model.XmlResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


import java.util.Map;

public interface ApiInterface {

    //Get All matches of the day
    @GET(Const.MATCHES)
    Call<Live> getMatchData(@QueryMap Map<String, String> map);

    //Get Match details
    @GET
    Call<ResponseBody> getMatchDetail(@Url String url);
}
