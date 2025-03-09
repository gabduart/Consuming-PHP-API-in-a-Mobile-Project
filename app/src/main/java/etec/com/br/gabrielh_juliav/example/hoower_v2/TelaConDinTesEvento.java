package etec.com.br.gabrielh_juliav.example.hoower_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaConDinTesEvento extends AppCompatActivity {
    EditText edtNome;
    ListView lstEventos;
    String nome;

    private List<Evento> listaEvento = new ArrayList<>();
    private ArrayAdapter<Evento> adaptadorEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_con_din_tes_evento);

        edtNome = findViewById(R.id.edtDinNome);
        lstEventos = findViewById(R.id.lstDin);

        adaptadorEvento =new ArrayAdapter<Evento>(TelaConDinTesEvento.this, android.R.layout.simple_list_item_1, listaEvento);
        lstEventos.setAdapter(adaptadorEvento);

        preencheLista(MainActivity.caminho + "consultar_json");

        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(TelaConDinTesEvento.this, "Valor Digitado " + charSequence, Toast.LENGTH_SHORT).show();
                nome = charSequence.toString();
                buscaNome(MainActivity.caminho + "retorna_nome_din");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void preencheLista(String endereco){
        listaEvento.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                endereco,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Codigo se a execução deu certo
                        //listaAgendaPessoal.clear();
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                Evento objPessoa = new Evento();

                                objPessoa.setEventoId(obj.getInt("eventoId"));
                                objPessoa.setNome(obj.getString("nome"));
                                objPessoa.setLocal(obj.getString("local"));
                                objPessoa.setData(obj.getString("data"));
                                objPessoa.setDescricao(obj.getString("descricao"));
                                listaEvento.add(objPessoa);
                            }
                            adaptadorEvento = new ArrayAdapter<Evento>(TelaConDinTesEvento.this,
                                    android.R.layout.simple_list_item_1,
                                    listaEvento);
                            lstEventos.setAdapter(adaptadorEvento);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //se der algo errado
                Toast.makeText(TelaConDinTesEvento.this, "Não foi possível carregar" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                return params;
            }
        };
        jsonArrayRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(TelaConDinTesEvento.this);
        queue.getCache().clear();
        queue.add(jsonArrayRequest);
    }

    private void buscaNome(String endereco) {
        listaEvento.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("teste", response);
                            JSONArray objArray = new JSONArray(response);
                            for (int i = 0; i < objArray.length(); i++) {
                                JSONObject obj = (JSONObject) objArray.get(i);

                                Evento objPessoa = new Evento();
                                objPessoa.setEventoId(obj.getInt("eventoId"));
                                objPessoa.setNome(obj.getString("nome"));
                                objPessoa.setLocal(obj.getString("local"));
                                objPessoa.setData(obj.getString("data"));
                                objPessoa.setDescricao(obj.getString("descricao"));
                                listaEvento.add(objPessoa);
                            }
                            adaptadorEvento.notifyDataSetChanged();
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TelaConDinTesEvento.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(strRequest);
    }
}