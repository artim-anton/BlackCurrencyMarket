package com.artimanton.blackcurrencymarket.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.adapter.RecordAdapter;
import com.artimanton.blackcurrencymarket.model.RecordModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EuroActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_CITY = "city";
    public static final String APP_PREFERENCES_COUNTRY = "country";
    private SharedPreferences mSettings;

    private RecyclerView recyclerView;
    private List<RecordModel> result;
    private RecordAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euro);
        load_city();


    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(RecordModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                RecordModel record = dataSnapshot.getValue(RecordModel.class);
                int index = getItemIndex(record);
                result.set(index, record);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                RecordModel bus = dataSnapshot.getValue(RecordModel.class);
                int index = getItemIndex(bus);
                result.remove(index);
                adapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(RecordModel record){
        int index = -1;
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).key.equals(record.key)){
                index = i;
                break;
            }

        }
        return index;
    }

    public void load_city(){
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String setting_city = mSettings.getString(APP_PREFERENCES_CITY, String.valueOf(0));
        String setting_country = mSettings.getString(APP_PREFERENCES_COUNTRY, String.valueOf(0));
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(setting_country + "/" + setting_city + "/" + "evro");

        result = new ArrayList<>();
        recyclerView =  findViewById(R.id.record_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecordAdapter(result);
        recyclerView.setAdapter(adapter);

        updateList();

    }
}
