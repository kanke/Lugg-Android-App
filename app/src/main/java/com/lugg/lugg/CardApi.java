package com.lugg.lugg;

import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kanke on 04/06/2016.
 */

public interface CardApi {

    @POST("/lugg/register")
    Call<String> sendToken(@Body TransactionDetails transactionDetails);

    @POST("/lugg/orderAuthorisation")
    Call<String> authorize(@Body Authorization authorization);

    @POST("/lugg/dropoff")
    Call<String> dropOff(@Body DropOffLugg dropOffLugg);

    @POST("/lugg/received")
    Call<String> received(@Body ReceivedLugg receivedLugg);
}
