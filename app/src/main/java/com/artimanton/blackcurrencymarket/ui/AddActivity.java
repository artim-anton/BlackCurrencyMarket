package com.artimanton.blackcurrencymarket.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.model.RecordModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    public EditText  etCity, etData, etPrice, etKol, etPhone, etKey;
    public String etSellBuy, etRegion, etCurrency, path;
    public Spinner spinner_sell, spinner_currency, spinner_city;
    private String dateText, timeText;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    private SharedPreferences mSettings;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        String[] data_cell = {"КУПЛЮ", "ПРОДАМ"};


        // адаптер
        ArrayAdapter<String> adapter_sell = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_cell);
        adapter_sell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(R.layout.spiner_item);

        spinner_sell = (Spinner) findViewById(R.id.spinner_sell_buy);
        spinner_sell.setAdapter(adapter_sell);
        // заголовок
        spinner_sell.setPrompt("Title");
        // выделяем элемент
        spinner_sell.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner_sell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        String[] data_currency = {"$", "€","₽"};


        // адаптер
        ArrayAdapter<String> adapter_currency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_currency);
        adapter_currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(R.layout.spiner_item);

        spinner_currency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_currency.setAdapter(adapter_currency);
        // заголовок
        spinner_currency.setPrompt("Title");
        // выделяем элемент
        spinner_currency.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



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
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        database = FirebaseDatabase.getInstance();

        etCity = (EditText) findViewById(R.id.etCity);
        //etData = (EditText) findViewById(R.id.etData);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etKol = (EditText) findViewById(R.id.etKol);
        etPhone = (EditText) findViewById(R.id.etPhone);

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeText = timeFormat.format(currentDate);

    }

    public void addBtn(View view) {
        etSellBuy = spinner_sell.getSelectedItem().toString();
        etRegion = spinner_city.getSelectedItem().toString();
        etCurrency = spinner_currency.getSelectedItem().toString();
        if (etCurrency == "$") {path = "dollar";}
        if (etCurrency == "€") {path = "evro";}
        reference = database.getReference(etRegion+"/"+path);

        String id = reference.push().getKey();
        RecordModel newAdvert = new RecordModel(dateText, timeText, etSellBuy, etCity.getText().toString(), etCurrency, etPrice.getText().toString(), etKol.getText().toString(), etPhone.getText().toString(), id);

        Map<String, Object> advertValue = newAdvert.toMap();
        Map<String, Object> record = new HashMap<>();
        record.put(id, advertValue);
        reference.updateChildren(record);
        this.finish();
    }
}
