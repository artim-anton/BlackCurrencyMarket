package com.artimanton.blackcurrencymarket.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.adapter.RecordAdapter;
import com.artimanton.blackcurrencymarket.model.Record;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EuroActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Record> result;
    private RecordAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euro);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("zp/evro");

        result = new ArrayList<>();
        recyclerView =  findViewById(R.id.record_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecordAdapter(result);
        recyclerView.setAdapter(adapter);

        updateList();


    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(Record.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                int index = getItemIndex(record);
                result.set(index, record);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Record bus = dataSnapshot.getValue(Record.class);
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

    private int getItemIndex(Record record){
        int index = -1;
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).key.equals(record.key)){
                index = i;
                break;
            }

        }
        return index;
    }
}
