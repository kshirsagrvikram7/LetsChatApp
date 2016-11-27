package vksagar.letschatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText username_signup, username_login;
    Button signup, login;
    public static String username_editext;
    Firebase firebase_regusers = null;
    Firebase firebase_onlineusers = null;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_signup = (EditText) findViewById(R.id.editText1);
        username_login = (EditText) findViewById(R.id.editText2);
        signup = (Button) findViewById(R.id.button1);


        login = (Button) findViewById(R.id.button3);

        Firebase.setAndroidContext(this);
        firebase_regusers = new Firebase("https://letschatapp-d31cf.firebaseio.com/Registered_Users");
        firebase_onlineusers = new Firebase("https://letschatapp-d31cf.firebaseio.com/Online_Users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernamestring = username_signup.getText().toString().trim();


                Toast.makeText(MainActivity.this, "Successfully registered. Please Log In to continue", Toast.LENGTH_SHORT).show();
                firebase_regusers.push().setValue(usernamestring);
                username_signup.setText("");


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase_regusers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            username_editext = username_login.getText().toString().trim();
                            username = snapshot.getValue(String.class);

                            if (username_editext.equals(username)) {

                                Intent intent = new Intent(MainActivity.this, OnlineUsersActivity.class);
                                intent.putExtra("FROM_USER", username);
                                intent.putExtra("LOG_IN_USER",username_editext);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "You are Online", Toast.LENGTH_SHORT).show();
                                firebase_onlineusers.push().setValue(username_editext);
                                username_login.setText("");

                            } else {
                                Toast.makeText(MainActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
}
