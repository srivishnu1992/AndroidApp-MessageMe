package com.example.srivi.messageme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Signup extends AppCompatActivity {

    EditText editName;
    EditText editCompanyName;
    EditText editEmail;
    EditText editChoose;
    EditText editRepeat;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        setTitle( "MessageMe! (Sign Up)" );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setLogo( R.drawable.ic_launcher );
        getSupportActionBar().setDisplayUseLogoEnabled( true );

        editName = findViewById( R.id.editFirstName );
        editCompanyName = findViewById( R.id.editLastName );
        editEmail = findViewById( R.id.editEmailSign );
        editChoose = findViewById( R.id.editChooseSign );
        editRepeat = findViewById( R.id.editRepeatSign );
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        findViewById( R.id.btnCancel ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        findViewById( R.id.btnSignSign ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText()==null)
                    editName.setError( "Please enter name" );
                else if(editCompanyName.getText()==null)
                    editCompanyName.setError( "Please company name" );
                else if(editEmail.getText()==null)
                    editEmail.setError( "Please enter email" );
                else if(editChoose.getText()==null)
                    editChoose.setError( "Please enter password" );
                else if(editRepeat.getText()==null)
                    editRepeat.setError( "Please repeat password" );
                else {
                    String firstName = editName.getText().toString();
                    String lastName = editCompanyName.getText().toString();
                    String email = editEmail.getText().toString();
                    String choose = editChoose.getText().toString();
                    String repeat = editRepeat.getText().toString();
                    if(choose.equals( repeat ))
                        signup(firstName, lastName, email, choose);
                    else
                        Toast.makeText( Signup.this, "Passwords doesn't match", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }
    public void signup(final String firstName, final String lastName, final String email, String choose) {
        firebaseAuth.createUserWithEmailAndPassword( email, choose )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            User user = new User( firstName, lastName, email, userId);
                            databaseReference.child( "users" ).child( userId ).setValue( user );
                            editChoose.setText( "" );
                            editRepeat.setText( "" );
                            Intent intent = new Intent( Signup.this, Inbox.class );
                            startActivity( intent );
                        }  else {
                            Toast.makeText( Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }
}
