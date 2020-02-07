package com.artimanton.blackcurrencymarket.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
    public EditText etCity, etData, etCurrency, etPrice, etKol, etPhone, etKey;
    private String dateText, timeText;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        database = FirebaseDatabase.getInstance();
        etCity = (EditText) findViewById(R.id.etCity);
        etData = (EditText) findViewById(R.id.etData);
        etCurrency = (EditText) findViewById(R.id.etСurrency);
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
        reference = database.getReference(etCity.getText().toString());
        String id = reference.push().getKey();
        Record newAdvert = new Record(dateText, timeText, etCurrency.getText().toString(), etPrice.getText().toString(), etKol.getText().toString(), etPhone.getText().toString(), id);

        Map<String, Object> advertValue = newAdvert.toMap();
        Map<String, Object> record = new HashMap<>();
        record.put(id, advertValue);
        reference.updateChildren(record);
    }
}
