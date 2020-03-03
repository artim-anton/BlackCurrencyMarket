package com.artimanton.blackcurrencymarket.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.artimanton.blackcurrencymarket.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class NavigationActivity extends TabActivity implements BillingProcessor.IBillingHandler {
    public Spinner spinner_city;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_CITY = "city";
    private SharedPreferences mSettings;
    TabHost tabHost;
    TabHost.TabSpec tabSpec;
    DollarActivity dollarActivity;

    private BillingProcessor bp;
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjwSzeEgAXyQBBKCp724PVqTDbIzY9CGhkTdXFrPNmxpwmLjUyHB9oR3rlkOQIbCPRgU1nWaWqD27qhijbnx1dm3sNZ136EnB2tzbMZjTU88p9Meizz8YfkAhD0csKiEzYk7tzbhO9EWfIprDylbD6UpJsa8OJS//8xQHykcgt2r/DKICxoRoR3hxNczgQY9dtJOPcdrMwKKB402lkqqdEAEyN1t5E0vxBQpqU6Ouufjx3aUrI12GovTnn1kOyT4UUYt20UQz9E9M9GRaBoHgxPstZB8rY6ffkkqaKmmmqjFM5g8hY9OxNF8jkApcjgAtvq03t4j6YOiFUetI+4yc5wIDAQAB"; // PUT YOUR MERCHANT KEY HERE;
    private static final String PRODUCT_ID = "com.artimanton.infovesele";
    private static final String SUBSCRIPTION_ID = "com.artimanton.infovesele.subs1";
    //авторизацыя
    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    Button btn_sign_out;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        bp = new BillingProcessor(this, LICENSE_KEY, this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        final String[] data_city = getResources().getStringArray(
                R.array.data_city);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_city.setAdapter(adapter);
        // заголовок
        spinner_city.setPrompt("Title");
        // выделяем элемент
        spinner_city.setSelection(mSettings.getInt(APP_PREFERENCES_COUNTER, 0));
        // устанавливаем обработчик нажатия
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_COUNTER, position);
                editor.putString(APP_PREFERENCES_CITY, spinner_city.getSelectedItem().toString());
                editor.apply();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // получаем TabHost
       tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно



        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Доллары");
        tabSpec.setContent(new Intent(this, DollarActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Евро");
        tabSpec.setContent(new Intent(this, EuroActivity.class));
        tabHost.addTab(tabSpec);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bp.isSubscribed(SUBSCRIPTION_ID)) {
                    Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                    startActivity(intent);
                }


                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        ///АВТОРИЗАЦИЯ
        btn_sign_out =(Button) findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout
                AuthUI.getInstance()
                        .signOut(NavigationActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                //Init providers
                                providers = Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                        //new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                );
                                showSingInOptions();
                            }
                        }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NavigationActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Toast.makeText(NavigationActivity.this, "onAuthStateChanged:signed_in:" + user,Toast.LENGTH_SHORT).show();
            btn_sign_out.setEnabled(true);
        } else {
            //Init providers
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            showSingInOptions();
            // User is signed out
            Toast.makeText(NavigationActivity.this, "onAuthStateChanged:signed_out",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {
        bp.loadOwnedPurchasesFromGoogle();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showSingInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email in Toast
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_LONG).show();
                //Set button sign_out
                btn_sign_out.setEnabled(true);
            }
            else{
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
