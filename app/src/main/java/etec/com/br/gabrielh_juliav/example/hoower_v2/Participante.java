package etec.com.br.gabrielh_juliav.example.hoower_v2;

import androidx.annotation.NonNull;

public class Participante {
    private int participanteId;
    private String nome;
    private String email;

    private String telefone;
    private String idade;

    public int getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(int participanteId) {
        this.participanteId = participanteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    @NonNull
    @Override
    public String toString() {
        return "Nome: " + nome + "\nIdade: " + idade;
    }
}
