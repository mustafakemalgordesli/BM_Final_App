package tr.com.mustafagordesli.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText pass, emailTxt, firstname, lastname;
    Button btnSend;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        pass= (EditText) findViewById(R.id.editPass);
        emailTxt = (EditText) findViewById(R.id.editEmail);
        firstname = (EditText) findViewById(R.id.editFirstname);
        lastname = (EditText) findViewById(R.id.editLastname);
        btnSend = (Button) findViewById(R.id.btnSend);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = firstname.getText().toString().trim();
                String email = emailTxt.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String lname = lastname.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if(task.isSuccessful()) {
                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("fname", name);
                                userData.put("lname", lname);
                                firestore.collection("users").document(firebaseAuth.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        });
            }
        });

    }
}