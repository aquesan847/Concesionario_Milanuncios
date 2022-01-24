package es.aqs.pm.concesionariomilanuncios.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.aqs.pm.concesionariomilanuncios.R;
import es.aqs.pm.concesionariomilanuncios.activity.CarInfo;
import es.aqs.pm.concesionariomilanuncios.activity.Coche;

class CocheViewHolder extends RecyclerView.ViewHolder{
    TextView tvTitle, tvDesc, tvPrice;
    ImageView ivCar;
    Coche coche;
    Context context;

    public CocheViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvDesc = itemView.findViewById(R.id.tvDesc);
        ivCar = itemView.findViewById(R.id.ivCar);

        itemView.setOnClickListener((View v) -> {
            Log.v("xyzyx", "Debo cambiar de escena");
            Intent intent = new Intent(context, CarInfo.class);
            intent.putExtra("car", coche);
            context.startActivity(intent);
        });
    }
}
