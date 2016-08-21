package br.com.gdgabc.firebasemessagingdojo;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btEnviar;
    private EditText etMessage;
    private ListView lvMessages;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String TAG_MESSAGE=MessageActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btEnviar = (Button) findViewById(R.id.button_enviar);
        etMessage = (EditText) findViewById(R.id.edit_message);
        lvMessages = (ListView) findViewById(R.id.list_messages);

        btEnviar.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("messages");
        /*Query messages = databaseReference.child("messages");
        messages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("TAG","messages.addValueEventListener"+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG_MESSAGE, "Value is: " + value);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG_MESSAGE, "Failed to read value.", error.toException());
            }
        });*/
        final FirebaseListAdapter<Message> adapter =
                new FirebaseListAdapter<Message>(
                        this,
                        Message.class,
                        android.R.layout.simple_list_item_1,
                        databaseReference
                ) {
                    @Override
                    protected void populateView(View v, Message model, int position) {
                        TextView text = (TextView) v.findViewById(android.R.id.text1);
                        text.setText(model.mensagem);
                    }
                };

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                lvMessages.smoothScrollToPosition(adapter.getCount());
            }
        });


        lvMessages.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        Message message = new Message();
        message.mensagem = etMessage.getText().toString();
        message.nome="Teste";
        databaseReference.push()
                .setValue(message);
        etMessage.setText("");
    }
}
