package com.artimanton.blackcurrencymarket.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.artimanton.blackcurrencymarket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                    Intent intent = new Intent(NavigationActivity.this, BillingActivity.class);
                    startActivity(intent);
                }


                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
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
}
