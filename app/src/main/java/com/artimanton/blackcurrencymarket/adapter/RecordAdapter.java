package com.artimanton.blackcurrencymarket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.model.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> list;

    public RecordAdapter(List<Record> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        final Record record = list.get(position);
        holder.etData.setText(record.data);
        holder.etCurrency.setText(record.currency);
        holder.etPrice.setText(record.price);
        holder.etKol.setText(record.kol);
        holder.etPhone.setText(record.phone);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecordViewHolder extends RecyclerView.ViewHolder{
        private TextView etData, etCurrency, etPrice, etKol, etPhone;

        private RecordViewHolder(View itemView) {
            super(itemView);
            etData = itemView.findViewById(R.id.et_data);
            etCurrency = itemView.findViewById(R.id.et_currency);
            etPrice = itemView.findViewById(R.id.et_price);
            etKol = itemView.findViewById(R.id.et_kol);
            etPhone = itemView.findViewById(R.id.et_phone);
        }
    }
}
