package etec.com.br.gabrielh_juliav.example.hoower_v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button btnCad, btnExc, btnEdt,btnCon,btnLim;
    EditText edtNome, edtTelefone, edtEmail, edtDescricao, edtData;
    ListView lstEvento;

    Evento pessoa = new Evento();

    private List<Evento> listaAgendaPessoal = new ArrayList<>();
    private ArrayAdapter<Evento> adaptadorAgenda;

    private static String ip = "192.168.1.28";

    int codigo;
    public static String  caminho = "http://"+ ip +"/hoower/controller/eventoscontroller.php?acao=";
    public static String  caminhoPart = "http://"+ ip +"/hoower/controller/participantescontroller.php?acao=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtDescricao = findViewById(R.id.edtDesc);

        btnCad = findViewById(R.id.btnCadastrar);
        btnCon = findViewById(R.id.btnConsultar);
        btnEdt = findViewById(R.id.btnEditar);
        btnExc = findViewById(R.id.btnExcluir);
        btnLim = findViewById(R.id.btnLimpar);

        lstEvento = findViewById(R.id.lstDados);

        btnExc.setEnabled(false);
        btnEdt.setEnabled(false);
        btnCad.setEnabled(true);
        btnLim.setEnabled(true);
        btnCon.setEnabled(true);

        lstEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoa = (Evento) parent.getItemAtPosition(position);
                edtNome.setText(pessoa.getNome());
                edtTelefone.setText(pessoa.getLocal());
                edtEmail.setText(pessoa.getData());
                edtDescricao.setText(pessoa.getDescricao());
                codigo = pessoa.getEventoId();
                btnExc.setEnabled(true);
                btnEdt.setEnabled(true);
                btnCad.setEnabled(false);
            }
        });
        preencheLista(caminho + "consultar_json");

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recebe os valores digitados pelo usuário
                pessoa.setNome(edtNome.getText().toString());
                pessoa.setLocal(edtTelefone.getText().toString());
                pessoa.setData(edtEmail.getText().toString());
                pessoa.setDescricao(edtDescricao.getText().toString());
                //chama o método cadastrar. Utiliza o atributo caminho que criamos acima junto com a ação que desejamos no servidor. CADASTRAR.
                cadastrar(caminho + "cadastrar");
                //Depois de realizar o cadastro dos dados, atualiza a lista do aplicativo.
                preencheLista(caminho + "consultar_json");
                //limpa os dados das caixas de texto.
                limparDados();
            }
        });

        btnEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pessoa.setNome(edtNome.getText().toString());
                pessoa.setData(edtEmail.getText().toString());
                pessoa.setDescricao(edtDescricao.getText().toString());
                pessoa.setLocal(edtTelefone.getText().toString());

                alterar(caminho + "atualizar");

                limparDados();
            }
        });

        btnLim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparDados();
            }
        });

        btnExc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluir(caminho+"excluir");
                preencheLista(caminho + "consultar_json");
                limparDados();
            }
        });

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itConsulta = new Intent(MainActivity.this, TelaConDinTesEvento.class);
                startActivity(itConsulta);
            }
        });
    }
    public void preencheLista(String endereco){
        listaAgendaPessoal.clear();
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
                                listaAgendaPessoal.add(objPessoa);
                            }
                            adaptadorAgenda = new ArrayAdapter<Evento>(MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    listaAgendaPessoal);
                            lstEvento.setAdapter(adaptadorAgenda);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //se der algo errado
                Toast.makeText(MainActivity.this, "Não foi possível carregar" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String>params = new HashMap<>();
                return params;
            }
        };
        jsonArrayRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.getCache().clear();
        queue.add(jsonArrayRequest);
    }

    public void cadastrar(String endereco){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ok")) {
                            Log.e("Teste",response);
                            Toast.makeText(MainActivity.this, "Dados Cadastrados com Sucesso!", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Não foi possível Cadastrar " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("nome",pessoa.getNome());
                params.put("local",pessoa.getLocal());
                params.put("data",pessoa.getData());
                params.put("descricao",pessoa.getDescricao());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private void limparDados() {
        edtNome.setText(null);
        edtTelefone.setText(null);
        edtDescricao.setText(null);
        edtEmail.setText(null);
        btnExc.setEnabled(false);
        btnEdt.setEnabled(false);
        btnCad.setEnabled(true);
    }

    private void alterar(String endereco){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ok")){
                            Log.e("Teste",response);
                            Toast.makeText(MainActivity.this, "Dados Atualizados", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erro ao Atualizar - " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("eventoId", String.valueOf(codigo));
                params.put("nome",pessoa.getNome());
                params.put("local",pessoa.getLocal());
                params.put("data",pessoa.getData());
                params.put("descricao",pessoa.getDescricao());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void excluir(String endereco) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ok")){
                            Toast.makeText(MainActivity.this, "Excluido com Sucesso", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erro ao Excluir: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("eventoId",String.valueOf(codigo));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}