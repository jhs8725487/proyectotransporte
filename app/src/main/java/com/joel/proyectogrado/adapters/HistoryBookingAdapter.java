package com.joel.proyectogrado.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.proyectogrado.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryBookingAdapter extends RecyclerView.Adapter<HistoryBookingAdapter.ViewHolderHistory> {

    ArrayList<Historial> listaHistorial;
    private Context mContext;

    public HistoryBookingAdapter(ArrayList<Historial> listaHistorial, Context context) {
        this.listaHistorial = listaHistorial;
        mContext=context;
    }

    @NonNull
    @Override
    public HistoryBookingAdapter.ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history_booking, null,false);
        return new ViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBookingAdapter.ViewHolderHistory holder, int position) {
        holder.textViewName.setText(listaHistorial.get(position).getNombre());
        holder.textViewOrigen.setText(listaHistorial.get(position).getOrigen());
        holder.textViewDestino.setText(listaHistorial.get(position).getDestino());
        holder.textViewTelefono.setText(listaHistorial.get(position).getTelefono());
        Picasso.with(mContext).load(listaHistorial.get(position).getFoto()).into(holder.imageViewHistoyBooking);
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }



    public class ViewHolderHistory extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewOrigen;
        private TextView textViewDestino;
        private TextView textViewTelefono;
        private ImageView imageViewHistoyBooking;
        public ViewHolderHistory(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.textViewNombre);
            textViewOrigen=itemView.findViewById(R.id.textViewOrigen);
            textViewDestino=itemView.findViewById(R.id.textViewDestino);
            textViewTelefono=itemView.findViewById(R.id.textViewTelefono);
            imageViewHistoyBooking=itemView.findViewById(R.id.imageViewHistoryBooking);
        }
    }
}
