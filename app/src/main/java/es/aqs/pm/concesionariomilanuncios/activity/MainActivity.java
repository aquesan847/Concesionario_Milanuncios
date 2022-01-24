package es.aqs.pm.concesionariomilanuncios.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import es.aqs.pm.concesionariomilanuncios.R;
import es.aqs.pm.concesionariomilanuncios.adapter.CocheAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "jdbc:mariadb://146.59.237.189/dam208_aqsconcesionario";
    private static final String USER = "dam208_aqs";
    private static final String PASSWORD = "dam208_aqs";

    public Context context; //Context para la consulta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        new InfoAsyncTask().execute();

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        setTitle(getString(R.string.tiendaNombre));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, ArrayList<Coche>> {
        @Override
        protected ArrayList<Coche> doInBackground(Void... voids) {
            ArrayList<Coche> info = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT * FROM coches";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int carRef = resultSet.getInt("ref");
                    String carTitle = resultSet.getString("titulo");
                    String carDesc = resultSet.getString("descripcion");
                    String carPrice = resultSet.getString("precio");
                    String color = resultSet.getString("color");
                    String combustible = resultSet.getString("combustible");
                    String cambio = resultSet.getString("cambio");
                    String anno = resultSet.getString("anno");
                    String puertas = resultSet.getString("npuertas");
                    String carCategories = getString(R.string.Blank) + color + getString(R.string.miniBlank)+combustible + getString(R.string.miniBlank)
                            + "Cambio:"+cambio + getString(R.string.miniBlank) +"Año:"+anno + getString(R.string.miniBlank) + "Puertas:" + puertas;
                    String carImages = resultSet.getString("imagenes");

                    Coche coche = new Coche(carRef, carTitle, carDesc, carPrice, carCategories, carImages);
                    info.add(coche);
                }
                return info;
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading car information", e);
            }

            return info;
        }

        @Override
        protected void onPostExecute(ArrayList<Coche> result) {
            if (!result.isEmpty()) {
                RecyclerView rv = findViewById(R.id.recyclerView);
                rv.setLayoutManager(new LinearLayoutManager(context));
                    CocheAdapter carAdapter = new CocheAdapter(context);
                rv.setAdapter(carAdapter);
                carAdapter.setList(result);
            }
        }
    }
}
