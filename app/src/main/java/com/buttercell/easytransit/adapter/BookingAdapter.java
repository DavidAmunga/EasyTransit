package com.buttercell.easytransit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.model.Booking;

import java.util.ArrayList;
import java.util.List;

class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    private static final String TAG = "BookingViewHolder";
    TextView txtUsername, txtDocNo;

    public BookingViewHolder(View itemView) {
        super(itemView);

        txtUsername = itemView.findViewById(R.id.txtUsername);
        txtDocNo = itemView.findViewById(R.id.txtDocNo);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder> {
    public List<Booking> listData = new ArrayList<>();
    private Context context;


    public BookingAdapter(List<Booking> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View itemView = inflater.inflate(R.layout.booking_layout, parent, false);

        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        holder.txtUsername.setText(listData.get(position).getName());
        holder.txtDocNo.setText(listData.get(position).getDocNo());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
