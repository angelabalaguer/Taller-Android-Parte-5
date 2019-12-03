package com.example.taller5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MiNuevoAdaptador adaptador;
    private Vector<String> misdatos;
    public Vector<String> valor;
    private String res;
    HttpURLConnection conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        misdatos = new Vector<String>();
        misdatos.add("123000 Wilson Callisaya");
        misdatos.add("123000 Pepito Domingez");
        adaptador = new MiNuevoAdaptador(this,
                misdatos);
        adaptador = new MiNuevoAdaptador(this,
                ListaClientes(8));
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    //USANDO JSON
    public Vector<String> ListaClientes(int cantidad) {
        List<Cliente> clientes;
        clientes= leerJSon(conseguirstring());
        valor=new Vector<>();
        Vector<String> salida = new Vector<>();
        for (Cliente cliente : clientes) {
            salida.add(cliente.getNombre()+ " " + cliente.getApellido());
            valor.add(cliente.getcodigo());
        }
        Log.i("mierror",salida.toString());
        return salida;
    }
    public String conseguirstring() {
        try {
            String miurl= this.getString(R.string.dominio)+this.getString(R.string.vercliente);
            URL url=new URL(miurl);
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                res=linea;
            } else {
                Log.e("mierror", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            return res="";
        } finally {
            if (conexion!=null) conexion.disconnect();
        }
        return res;
    }
    private List<Cliente> leerJSon(String string) {
        List<Cliente> Clientes = new ArrayList<>();
        try {
            JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                Clientes.add(new Cliente(objeto.getString("Cod_persona"), objeto.getString("Nombre"),objeto.getString("Apellidos")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Clientes;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insertar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_insertar:
                startActivity(new Intent(this, InsertarCliente.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}