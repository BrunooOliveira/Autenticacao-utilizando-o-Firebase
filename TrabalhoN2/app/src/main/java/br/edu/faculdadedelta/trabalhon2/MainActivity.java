package br.edu.faculdadedelta.trabalhon2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1;
    List<AuthUI.IdpConfig> providers;
    Button btn_sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sair = findViewById(R.id.btn_sair);
        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Deslogar
                    AuthUI.getInstance()
                            .signOut(MainActivity.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btn_sair.setEnabled(false);
                            showSignInOptions();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    ""+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        // Init provider
        providers = Arrays.asList
                (new AuthUI.IdpConfig.EmailBuilder().build(), // Builder do email
                new AuthUI.IdpConfig.PhoneBuilder().build(), // Builder do telefone
                new AuthUI.IdpConfig.FacebookBuilder().build(), // Builder do facebook
                new AuthUI.IdpConfig.GoogleBuilder().build()  // Builder do Google
                );
        
        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MeuTema)
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                // Pega o usuário
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Mostra o email no Toast
                Toast.makeText(this,
                        ""+user.getEmail(),
                        Toast.LENGTH_LONG).show();
                // Setar o botão de sair
                btn_sair.setEnabled(true);
            }
            else {
                Toast.makeText(this,
                        ""+response.getError().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
