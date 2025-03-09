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
import android.widget.TableLayout;
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

public class TelaConDinTesPart extends AppCompatActivity {
    EditText edtNomeDin;
    ListView lstDin;
    String nome;

    private List<Participante> listaPart = new ArrayList<>();
    private ArrayAdapter<Participante> adaptadorPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_con_din_tes_part);

        edtNomeDin = findViewById(R.id.edtDinNomePart);
        lstDin = findViewById(R.id.lstDin);
        adaptadorPart = new ArrayAdapter<Participante>(TelaConDinTesPart.this, android.R.layout.simple_list_item_1, listaPart);
        lstDin.setAdapter(adaptadorPart);
        preencheLista(MainActivity.caminhoPart + "consultar_json");

        edtNomeDin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(TelaConDinTesPart.this, "Valor Digitado: " + charSequence, Toast.LENGTH_SHORT).show();
                nome = charSequence.toString();
                buscaNome(MainActivity.caminhoPart + "retorna_nome_din");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void preencheLista(String endereco) {
        listaPart.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, endereco, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Codigo se a execução deu certo
                        // listaAgendaPessoal.clear();
                        Log.e("teste", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Participante objPessoa = new Participante();
                                objPessoa.setParticipanteId(obj.getInt("participanteId"));
                                objPessoa.setNome(obj.getString("nome"));
                                objPessoa.setTelefone(obj.getString("telefone"));
                                objPessoa.setEmail(obj.getString("email"));
                                listaPart.add(objPessoa);
                            }

                            adaptadorPart.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //se der algo errado
                Toast.makeText(TelaConDinTesPart.this, "Não foi possível carregar" + error, Toast.LENGTH_SHORT).show();
            }
        }

        );
        RequestQueue queue = Volley.newRequestQueue(TelaConDinTesPart.this);
        queue.getCache().clear();
        queue.add(jsonArrayRequest);
    }

    private void buscaNome(String endereco) {
        listaPart.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray objArray = new JSONArray(response);
                            for (int i = 0; i < objArray.length(); i++) {
                                JSONObject obj = new JSONObject();
                                obj = (JSONObject) objArray.get(i);
                                Participante objPessoa = new Participante();
                                objPessoa.setParticipanteId(obj.getInt("participanteId"));
                                objPessoa.setNome(obj.getString("nome"));
                                objPessoa.setTelefone(obj.getString("telefone"));
                                objPessoa.setEmail(obj.getString("email"));
                                listaPart.add(objPessoa);
                            }
                            adaptadorPart.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("vendo o response", response);
                            Log.e("ta dando erro", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TelaConDinTesPart.this, error.toString(), Toast.LENGTH_SHORT).show();
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