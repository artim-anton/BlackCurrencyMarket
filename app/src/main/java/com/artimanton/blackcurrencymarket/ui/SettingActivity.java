package com.artimanton.blackcurrencymarket.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.artimanton.blackcurrencymarket.R;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences mSettings;
    public Spinner spinner_city,spinner_country;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_COUNTER_COUNTRY = "counter_country";
    public static final String APP_PREFERENCES_CITY = "city";
    public static final String APP_PREFERENCES_COUNTRY = "country";
    public static final String APP_PREFERENCES_CURRENCY = "currency";
    public static final String APP_PREFERENCES_PATH = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SpinnerCity(false);
        SpinnerCountry();


    }

    public void backBtn(View view) {
        Intent intent = new Intent(SettingActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private void SpinnerCity(boolean bool) {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        int int_country = mSettings.getInt(APP_PREFERENCES_COUNTER_COUNTRY, 0);
        String[] data_city=null;
        if (int_country == 0){
            data_city = getResources().getStringArray(R.array.data_city_ru);
        }
        else if (int_country == 1){
            data_city = getResources().getStringArray(R.array.data_city_ua);
        }
        else if (int_country == 2){
            data_city = getResources().getStringArray(R.array.data_city_by);
        }
        else if (int_country == 3){
            data_city = getResources().getStringArray(R.array.data_city_kg);
        }
        else if (int_country == 4){
            data_city = getResources().getStringArray(R.array.data_city_kz);
        }
        else if (int_country == 5){
            data_city = getResources().getStringArray(R.array.data_city_md);
        }



        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_city.setAdapter(adapter);
        // заголовок
        spinner_city.setPrompt("Title");
        // выделяем элемент
        int setting_counter = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
        if (bool) {setting_counter = 0;}
        spinner_city.setSelection(setting_counter);
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
    }

    public void SpinnerCountry() {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String[] data_country = getResources().getStringArray(
                R.array.data_country);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country.setAdapter(adapter);
        // заголовок
        spinner_country.setPrompt("Title");
        // выделяем элемент
        spinner_country.setSelection(mSettings.getInt(APP_PREFERENCES_COUNTER_COUNTRY, 0));
        // устанавливаем обработчик нажатия
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                boolean bool;
                if (mSettings.getInt(APP_PREFERENCES_COUNTER_COUNTRY, 0)!=position) {bool=true;} else {bool=false;}
                //Toast.makeText(getBaseContext(), mSettings.getInt(APP_PREFERENCES_COUNTER_COUNTRY, 0)+"Position = " + position, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_COUNTER_COUNTRY, position);
                editor.putString(APP_PREFERENCES_COUNTRY, spinner_country.getSelectedItem().toString());
                editor.apply();
                SpinnerCity(bool);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void apllyBtn(View view) {
        Intent intent = new Intent(SettingActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
