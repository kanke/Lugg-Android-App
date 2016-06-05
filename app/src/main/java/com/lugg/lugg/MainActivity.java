package com.lugg.lugg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CardApi cardApi;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        Simplify.init("sbpb_ZjZlYzk2NzktMzhmZS00ZGVmLWExODktNTZjYTRjMjA1ZjBi");
        initCardEditot();
    }


    private void initCardEditot() {

        final CardEditor cardEditor = (CardEditor) findViewById(R.id.card_editor);

        cardEditor.setIconColor(R.color.abc_background_cache_hint_selector_material_dark);
        cardEditor.setEnabled(true);

        final Button checkoutButton = (Button) findViewById(R.id.checkout_button);
        // add state change listener
        if (cardEditor != null)
            cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {
                @Override
                public void onStateChange(CardEditor cardEditor) {
                    // true: card editor contains valid and complete card information
                    checkoutButton.setEnabled(cardEditor.isValid());
                }
            });

        // add checkout button click listener
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a card token
                Simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback() {
                    @Override
                    public void onSuccess(CardToken cardToken) {
                        // ...
                        Snackbar.make(cardEditor, "Success!", Snackbar.LENGTH_LONG).show();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.12.164:8080/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        cardApi = retrofit.create(CardApi.class);

                        TransactionDetails transactionDetails = new TransactionDetails();

                        transactionDetails.setId(cardToken.getCard().getId()); // Change to customer Id here
                        transactionDetails.setMasterCardId(cardToken.getId());

                        DropOffLugg dropOffLugg = new DropOffLugg();

                        dropOffLugg.setId(cardToken.getCard().getId()); // Change to customer Id here
                        dropOffLugg.setPickup("Heathrow%20Airport,%20Heathrow%20Airport");
                        dropOffLugg.setDropoff("Old Street,%20City%20of%20London");


                        ReceivedLugg receivedLugg = new ReceivedLugg();
                        receivedLugg.setId(cardToken.getCard().getId());

                        Authorization authorization = new Authorization();
                        authorization.setId(cardToken.getCard().getId());
                        authorization.setCustomer("Yacine");


                        //Troubleshooting
//                        Gson gson = new Gson();
//                        String toJsonString = gson.toJson(transactionDetails, TransactionDetails.class);
//
//                        Log.d(TAG,toJsonString);

                        Call call = cardApi.sendToken(transactionDetails);
                        Call call3 = cardApi.authorize(authorization);
                        Call call1 = cardApi.dropOff(dropOffLugg);
                        Call call2 = cardApi.received(receivedLugg);


                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                Log.d(TAG, "Success!" + call.toString() + "Response" + response.message());
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.d(TAG, "Call failed! call"+ call.toString());
                                t.printStackTrace();
                            }
                        });


                        call1.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call1, Response response) {
                                Log.d(TAG, "Success!" + call1.toString() + "Response" + response.message());
                                response.body();
                                System.out.println(response.body());
                            }


                            @Override
                            public void onFailure(Call call1, Throwable t) {
                                Log.d(TAG, "Call failed! call1"+ call1.toString());
                                t.printStackTrace();
                            }
                        });


                        call2.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call2, Response response) {
                                Log.d(TAG, "Success!" + call2.toString() + "Response" + response.message());
                            }

                            @Override
                            public void onFailure(Call call2, Throwable t) {
                                Log.d(TAG, "Call failed! call2"+call2.toString());
                                t.printStackTrace();
                            }
                        });

                        call3.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call3, Response response) {
                                Log.d(TAG, "Success!" + call3.toString() + "Response" + response.message());

                                //Start new activity here if you want
                                final Intent gridIntent = new Intent(getApplicationContext(), FlightActivity.class);
                                startActivity(gridIntent);
                            }

                            @Override
                            public void onFailure(Call call3, Throwable t) {
                                Log.d(TAG, "Call failed! call3"+call3.toString());
                                t.printStackTrace();
                            }
                        });




                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // ...
                    }
                });
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
