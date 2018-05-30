package com.example.srivi.messageme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitle( "MessageMe!" );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setLogo( R.drawable.ic_launcher );
        getSupportActionBar().setDisplayUseLogoEnabled( true );
       // getSupportActionBar().
        editEmail = findViewById( R.id.editEmail );
        editPassword = findViewById( R.id.editPassword );
        firebaseAuth = FirebaseAuth.getInstance();
        findViewById( R.id.btnSignUp ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity( intent );
            }
        } );
        findViewById( R.id.btnLogin ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText()==null)
                    editEmail.setError( "Please enter email" );
                else if(editPassword.getText()==null)
                    editPassword.setError( "Please enter password" );
                else {
                    String email = editEmail.getText().toString();
                    String password = editPassword.getText().toString();
                    login(email, password);
                }
            }
        } );
    }
    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } );
    }
    public void updateUI(FirebaseUser user) {
        if(user!=null) {
            editPassword.setText( "" );
            Intent intent = new Intent( MainActivity.this, Inbox.class );
            startActivity( intent );
        }
    }
}
