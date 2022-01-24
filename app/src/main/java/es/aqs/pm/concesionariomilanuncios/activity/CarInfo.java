package es.aqs.pm.concesionariomilanuncios.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;

import es.aqs.pm.concesionariomilanuncios.R;

public class CarInfo extends AppCompatActivity implements View.OnClickListener {

    Coche coche;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info);

        this.coche = getIntent().getParcelableExtra("car");

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(coche.title);

        String[] images = coche.imagesUrls.toArray(new String[0]);

        // Mostramos las imagenes más los botones para ir a las demás
        ImageView imageView = findViewById(R.id.image_cars);
        String imgUrl = coche.imagesUrls.get(0);
        String[] imgUrls = imgUrl.split(";");
        Glide.with(getApplicationContext()).load(imgUrls[cont].trim()).into(imageView);

        Button btAtras = findViewById(R.id.btPrevious);
        Button btSiguiente = findViewById(R.id.btNext);
        btAtras.setOnClickListener(this);
        btSiguiente.setOnClickListener(this);


        // Ponemos titulo, precio e info
        TextView tvTitle = findViewById(R.id.tvTitleDetail);
        tvTitle.setText(coche.title);
        TextView tvPrice = findViewById(R.id.tvPriceDetail);
        tvPrice.setText(coche.price+"€");
        TextView tvDesc = findViewById(R.id.tvDescDetail);
        tvDesc.setText(coche.description);


        // Creamos y añadimos las tags
        View linearLayout =  findViewById(R.id.tagsContainer);

        for (String tag : coche.categories) {
            TextView valueTV = new TextView(this);
            valueTV.setText(tag);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            valueTV.setLayoutParams(params);


            ((LinearLayout) linearLayout).addView(valueTV);
        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuWeb:
                intent.setData(Uri.parse("https://www.milanuncios.com"));
                startActivity(intent);
                return true;
            case R.id.menuPrivacidad:
                intent.setData(Uri.parse("https://www.milanuncios.com/legal/politica-privacidad"));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
            ImageView imageView = findViewById(R.id.image_cars);
            String imgUrl = coche.imagesUrls.get(0);
            String[] imgUrls = imgUrl.split(";");
            switch (view.getId()) {
                case R.id.btPrevious:
                    cont--;
                    if (cont < 0) {
                        cont = 0;
                    }
                    if (cont == 0) {
                        Glide.with(getApplicationContext()).load(imgUrls[cont].trim()).into(imageView);
                    }
                    if (cont > 0) {
                        Glide.with(getApplicationContext()).load(imgUrls[cont].trim()).into(imageView);
                    }
                    break;

                case R.id.btNext:
                    System.out.println(cont);
                    cont++;
                    if (cont > imgUrls.length) {
                        cont = imgUrls.length;
                    }
                    if (cont < imgUrls.length) {
                        Glide.with(getApplicationContext()).load(imgUrls[cont].trim()).into(imageView);
                    }
                    break;
            }
    }
}