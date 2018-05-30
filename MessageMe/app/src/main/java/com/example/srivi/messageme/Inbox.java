package com.example.srivi.messageme;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Inbox extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseMail;
    ArrayList<Email> emailArrayList;
    static String EMAIL_KEY = "emailKey";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu1, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_email:
                Intent intent = new Intent( Inbox.this, Compose.class );
                startActivity( intent );
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_inbox );
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseMail = databaseReference.child( "mails" ).child( userId );
        final ListView listView = findViewById( R.id.listView );

        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setLogo( R.drawable.ic_launcher );
        getSupportActionBar().setDisplayUseLogoEnabled( true );

        databaseMail.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailArrayList = new ArrayList<>(  );
                    for(DataSnapshot temp1:dataSnapshot.getChildren()) {
                        Email email = temp1.getValue(Email.class);
                        emailArrayList.add( email );
                    }
                EmailAdapter emailAdapter = new EmailAdapter( Inbox.this, R.layout.list_row, emailArrayList);
                listView.setAdapter( emailAdapter );
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Email tempEmail = emailArrayList.get( i );
                        databaseMail.child( tempEmail.emailKey ).child( "IsRead" ).setValue( false );
                        Intent intent = new Intent( Inbox.this, Details.class );
                        intent.putExtra( EMAIL_KEY, tempEmail );
                        startActivity( intent );
                    }
                } );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }
}
