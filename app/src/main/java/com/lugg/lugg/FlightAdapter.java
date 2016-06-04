package com.lugg.lugg;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by kanke on 04/06/2016.
 */

public class FlightAdapter extends RecyclerView.Adapter<FlightViewHolder> {

    private List<FlightInfo> flightList;

    public FlightAdapter(List<FlightInfo> flightList) {
        this.flightList = flightList;
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new FlightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FlightViewHolder holder, int position) {

        FlightInfo ci = flightList.get(position);
        holder.vName.setText(ci.name);
        holder.vSurname.setText(ci.surname);
        holder.vEmail.setText(ci.email);
        holder.vTitle.setText(ci.name + " " + ci.surname);

    }

}

