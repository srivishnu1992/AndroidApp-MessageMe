package com.example.srivi.messageme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Details extends AppCompatActivity {

    TextView tvFromDetail;
    TextView tvEmailDetail;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Email email;
    static String TAG_TEMP = "tagtemp";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu2, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reply:
                Intent intent = new Intent( Details.this, Compose.class );
                intent.putExtra( TAG_TEMP, email );
                startActivity( intent );
                break;
            case R.id.i_delete:
                String userId = firebaseAuth.getCurrentUser().getUid();
                databaseReference.child( "mails" ).child(userId).child( email.emailKey ).removeValue();
                finish();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );
        setTitle( "Read Message" );
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setLogo( R.drawable.ic_launcher );
        getSupportActionBar().setDisplayUseLogoEnabled( true );
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            email = (Email) getIntent().getExtras().getSerializable( Inbox.EMAIL_KEY);
            tvFromDetail = findViewById( R.id.tvFromDetail );
            tvFromDetail.setText( "From : "+email.senderName );
            tvEmailDetail = findViewById( R.id.tvEmailDetail );
            tvEmailDetail.setText( email.text );
        }
    }
}
