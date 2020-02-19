package com.artimanton.blackcurrencymarket.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.model.Record;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    public EditText etCity, etData, etPrice, etKol, etPhone, etKey;
    public String etCurrency, path;
    public Spinner spinner;
    private String dateText, timeText;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String[] data = {"$", "€"};


        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
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
        etCurrency = spinner.getSelectedItem().toString();
        if (etCurrency == "$") {path = "dollar";}
        if (etCurrency == "€") {path = "evro";}
        reference = database.getReference(etCity.getText().toString()+"/"+path);

        String id = reference.push().getKey();
        Record newAdvert = new Record(dateText, timeText, etCurrency, etPrice.getText().toString(), etKol.getText().toString(), etPhone.getText().toString(), id);

        Map<String, Object> advertValue = newAdvert.toMap();
        Map<String, Object> record = new HashMap<>();
        record.put(id, advertValue);
        reference.updateChildren(record);
    }
}
