package com.example.srivi.messageme;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Compose extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseUsers;
    ArrayList<User> users = new ArrayList<>(  );
    TextView tvTo;
    CharSequence[] userNames;
    EditText editMessage;
    int senderId;
    String toEmail="";
    Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_compose );
        setTitle( "Compose Message" );
        tvTo = findViewById( R.id.tvTo );
        editMessage = findViewById( R.id.editMessage );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setLogo( R.drawable.ic_launcher );
        getSupportActionBar().setDisplayUseLogoEnabled( true );
        findViewById( R.id.btnSend ).setVisibility( View.VISIBLE );
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            email = (Email) getIntent().getExtras().getSerializable( Details.TAG_TEMP);
            findViewById( R.id.ibContact ).setVisibility( View.INVISIBLE );
            tvTo.setText( "To : "+email.senderName );
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder( Compose.this )
                .setTitle( "Users" );
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseUsers = databaseReference.child( "users" );
        databaseUsers.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot temp:dataSnapshot.getChildren()) {
                    User tempUser = temp.getValue(User.class);
                    users.add( tempUser );
                }
                Log.d("demo", String.valueOf( users.size()));
                userNames = new CharSequence[users.size()];
                for(int i=0;i<users.size();i++)
                    userNames[i] = users.get( i ).firstName+" "+users.get( i ).lastName;

                builder.setItems( userNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        senderId = i;
                        toEmail=users.get( i ).email;
                        tvTo.setText( "To : "+userNames[i] );
                    }
                } );
                final AlertDialog alertDialog = builder.create();

                findViewById( R.id.ibContact ).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.show();
                    }
                } );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        } );
        findViewById( R.id.btnSend ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMessage.getText()==null)
                    Toast.makeText( Compose.this, "Please enter text", Toast.LENGTH_SHORT ).show();
                else {
                    String message = editMessage.getText().toString();
                    Date date = new Date();
                    String userId = users.get( senderId ).userId;
                    String senderName = users.get(senderId).firstName+" "+users.get( senderId ).lastName;
                    Email email = new Email( message, date.toString(), userId, senderName, false );
                    email.emailKey = databaseReference.child( "mails" ).child( userId ).push().getKey();
                    databaseReference.child( "mails" ).child( userId ).child(email.emailKey).setValue( email );
                    Toast.makeText( Compose.this, "Message sent", Toast.LENGTH_SHORT ).show();
                    finish();
                }
            }
        } );

    }
}
