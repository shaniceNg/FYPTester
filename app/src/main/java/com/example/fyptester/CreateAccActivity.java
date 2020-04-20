package com.example.fyptester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText userName, userPassword, userEmail;
    private Button regButtton;
    private ImageButton closeButton;
    private FirebaseAuth firebaseAuth;
    private String email, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        setupUIViews();


        firebaseAuth=FirebaseAuth.getInstance();

        regButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //register to firebase
                    String user_email=userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                               //sendEmailVerification(); //to verify each person but can remove if dw, then no nid below code here
                                sendUserData();
                                Toast.makeText(CreateAccActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                               // firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(CreateAccActivity.this, MainActivity.class)); //to here
                            }
                            else{
                                Toast.makeText(CreateAccActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(CreateAccActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews(){
        userName = findViewById(R.id.editTextCreateName);
        userEmail = findViewById(R.id.editTextCreateEmail);
        userPassword=findViewById(R.id.editTextCreatePassword);
        regButtton=findViewById(R.id.btnCreateAcc);
        closeButton =findViewById(R.id.btnClose);
    }

    private Boolean validate(){
        Boolean result = false;

        name= userName.getText().toString();
        password = userPassword.getText().toString();
        email= userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result=true;
        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //copy all of here to createuserwithemailandpassword if dw to verify email
                        sendUserData();
                        Toast.makeText(CreateAccActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(CreateAccActivity.this, MainActivity.class));
                        //to here
                    }else{
                        Toast.makeText(CreateAccActivity.this, "Verification email not sent / Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile=new UserProfile(name,email);
        myRef.setValue(userProfile);
    }

    //spinner from here
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text= parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //to here
}
