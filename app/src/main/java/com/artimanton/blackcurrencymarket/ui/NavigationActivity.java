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

import com.artimanton.blackcurrencymarket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class NavigationActivity extends TabActivity {
    public Spinner spinner_city;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

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
                Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
