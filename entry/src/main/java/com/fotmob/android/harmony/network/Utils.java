package com.fotmob.android.harmony.network;

public class Utils {

    private static ApiInterface apiInterface;

    public static ApiInterface getApiInstance() {
        if (apiInterface == null)
            apiInterface = RetrofitClient.createRetrofitClientForMatches().create(ApiInterface.class);

        return apiInterface;
    }

    /*public static ApiInterface getApiInstanceForMatchFacts() {
        if (apiInterface == null)
            apiInterface = RetrofitClient.createRetrofitClientForMatches().create(ApiInterface.class);

        return apiInterface;
    }*/

}
