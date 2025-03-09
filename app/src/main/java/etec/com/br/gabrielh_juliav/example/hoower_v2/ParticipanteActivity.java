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

public class ParticipanteActivity extends AppCompatActivity {
    Button btnCad, btnExc, btnEdt,btnCon, btnLim;
    EditText edtNome, edtTelefone, edtEmail, edtIdade;
    ListView lstParticipante;

    Participante pessoa = new Participante();

    private List<Participante> listaParticipante = new ArrayList<>();
    private ArrayAdapter<Participante> adaptadorParticipante;
    private static String ip = "192.168.1.28";

    int codigo;

    public static String  caminho = "http://"+ ip +"/hoower/controller/participantescontroller.php?acao=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participante);

        edtEmail = findViewById(R.id.edtEmailPart);
        edtNome = findViewById(R.id.edtNomePart);
        edtTelefone = findViewById(R.id.edtTelefonePart);
        edtIdade = findViewById(R.id.edtIdadePart);

        btnCad = findViewById(R.id.btnCadastrarPart);
        btnCon = findViewById(R.id.btnConsultarPart);
        btnEdt = findViewById(R.id.btnEditarPart);
        btnExc = findViewById(R.id.btnExcluirPart);
        btnLim = findViewById(R.id.btnLimparPart);

        btnExc.setEnabled(false);
        btnEdt.setEnabled(false);
        btnCad.setEnabled(true);
        btnLim.setEnabled(true);
        btnCon.setEnabled(true);

        lstParticipante = findViewById(R.id.lstDados);

        lstParticipante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoa = (Participante) parent.getItemAtPosition(position);
                edtNome.setText(pessoa.getNome());
                edtTelefone.setText(pessoa.getTelefone());
                edtEmail.setText(pessoa.getEmail());
                edtIdade.setText(pessoa.getIdade());
                codigo = pessoa.getParticipanteId();
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
                pessoa.setTelefone(edtTelefone.getText().toString());
                pessoa.setEmail(edtEmail.getText().toString());
                pessoa.setIdade(edtIdade.getText().toString());
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
                pessoa.setTelefone(edtTelefone.getText().toString());
                pessoa.setEmail(edtEmail.getText().toString());
                pessoa.setIdade(edtIdade.getText().toString());

                alterar(caminho + "atualizar");
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
                Intent itConsulta = new Intent(ParticipanteActivity.this, TelaConDinTesPart.class);
                startActivity(itConsulta);
            }
        });
    }

    public void preencheLista(String endereco){
        listaParticipante.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                endereco,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Codigo se a execução deu certo
                        //listaParticipante.clear();
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                Participante objPessoa = new Participante();

                                objPessoa.setParticipanteId(obj.getInt("participanteId"));
                                objPessoa.setNome(obj.getString("nome"));
                                objPessoa.setTelefone(obj.getString("telefone"));
                                objPessoa.setEmail(obj.getString("email"));
                                objPessoa.setIdade(obj.getString("idade"));
                                listaParticipante.add(objPessoa);
                            }
                            adaptadorParticipante = new ArrayAdapter<Participante>(ParticipanteActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    listaParticipante);
                            lstParticipante.setAdapter(adaptadorParticipante);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //se der algo errado
                Toast.makeText(ParticipanteActivity.this, "Não foi possível carregar" + error, Toast.LENGTH_SHORT).show();
            }
        }

        ){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                return params;
            }
        };
        jsonArrayRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(ParticipanteActivity.this);
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
                            Toast.makeText(ParticipanteActivity.this, "Dados Cadastrados com Sucesso!", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParticipanteActivity.this, "Não foi possível Cadastrar " + error, Toast.LENGTH_LONG).show();
                    }
                }
        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("nome",pessoa.getNome());
                params.put("telefone",pessoa.getTelefone());
                params.put("email",pessoa.getEmail());
                params.put("idade",pessoa.getIdade());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ParticipanteActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private void alterar(String endereco){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ok")){
                            Log.e("Teste",response);
                            Toast.makeText(ParticipanteActivity.this, "Dados Atualizados", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParticipanteActivity.this, "Erro ao Atualizar - " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("participanteId", String.valueOf(codigo));
                params.put("nome",pessoa.getNome());
                params.put("email",pessoa.getEmail());
                params.put("telefone",pessoa.getTelefone());
                params.put("idade",pessoa.getIdade());
                Log.e("parametro", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ParticipanteActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void limparDados() {
        edtNome.setText(null);
        edtTelefone.setText(null);
        edtIdade.setText(null);
        edtEmail.setText(null);
        btnExc.setEnabled(false);
        btnEdt.setEnabled(false);
        btnCad.setEnabled(true);
    }

    private void excluir(String endereco) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, endereco,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("ok")){
                            Toast.makeText(ParticipanteActivity.this, "Excluido com Sucesso", Toast.LENGTH_SHORT).show();
                            preencheLista(caminho + "consultar_json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParticipanteActivity.this, "Erro ao Excluir: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("participanteId",String.valueOf(codigo));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ParticipanteActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}