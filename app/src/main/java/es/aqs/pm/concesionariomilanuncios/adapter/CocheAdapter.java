package es.aqs.pm.concesionariomilanuncios.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import es.aqs.pm.concesionariomilanuncios.R;
import es.aqs.pm.concesionariomilanuncios.activity.CarInfo;
import es.aqs.pm.concesionariomilanuncios.activity.Coche;

public class CocheAdapter extends RecyclerView.Adapter<CocheAdapter.CocheViewHolder> {
    ArrayList<Coche> list;
    Context context;

    public CocheAdapter(Context context){this.context = context;}

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CocheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        Coche coche = list.get(position);
        holder.coche = coche;
        holder.tvTitle.setText(coche.title);
        String desc = coche.description.substring(0, Math.min(coche.description.length(), 150));
        holder.tvDesc.setText(desc+"...");
        holder.tvPrice.setText(coche.price + "€");
        String imgUrl = coche.imagesUrls.get(0);
        String[] imgUrls = imgUrl.split(";");
        Glide.with(context).load(imgUrls[0].trim()).into(holder.ivCar);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public void setList(ArrayList<Coche> cars){
        list = cars;
    }

    class CocheViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDesc, tvPrice;
        ImageView ivCar;
        Coche coche;

        public CocheViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivCar = itemView.findViewById(R.id.ivCar);

            itemView.setOnClickListener((View v) -> {
                Log.v("xyzyx", "View Holder");
                Intent intent = new Intent(context, CarInfo.class);
                intent.putExtra("car", coche);
                context.startActivity(intent);
            });
        }
    }

}

