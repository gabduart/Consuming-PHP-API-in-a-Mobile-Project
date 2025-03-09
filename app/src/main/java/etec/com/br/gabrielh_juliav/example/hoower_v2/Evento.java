package etec.com.br.gabrielh_juliav.example.hoower_v2;

import androidx.annotation.NonNull;

public class Evento {
    private int eventoId;

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    private String nome;
    private String data;
    private String local;
    private String descricao;

    @NonNull
    @Override
    public String toString() {
        return "Nome: " + nome + "\nlocal: " + local;
    }
}
