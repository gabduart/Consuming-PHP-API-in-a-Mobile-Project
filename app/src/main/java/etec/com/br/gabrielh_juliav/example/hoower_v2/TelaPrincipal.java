package etec.com.br.gabrielh_juliav.example.hoower_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaPrincipal extends AppCompatActivity {
    Button btnPart, btnEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        btnPart = findViewById(R.id.btnPart);
        btnEvent = findViewById(R.id.btnEvent);

        btnPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent partActivity = new Intent(TelaPrincipal.this, ParticipanteActivity.class);
                startActivity(partActivity);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventActivity = new Intent(TelaPrincipal.this, MainActivity.class);
                startActivity(eventActivity);
            }
        });
    }
}