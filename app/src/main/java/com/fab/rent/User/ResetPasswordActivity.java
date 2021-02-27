package com.fab.rent.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.R;
import com.fab.rent.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView pageTitle,titlequestions;
    private EditText phoneno,question1,question2;
    private Button verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");

        pageTitle=findViewById(R.id.page_title);

        titlequestions=findViewById(R.id.tv_title);
        phoneno=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_one);
        question2=findViewById(R.id.question_two);
        verifybtn=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneno.setVisibility(View.GONE);


        if(check.equals("settings"))
        {
            pageTitle.setText("Set Security Questions");

            titlequestions.setText("Please Set the Answers for the following Security Questions");
            verifybtn.setText("Set");
            DisplayPreviousans();
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setAnswers();



                }
            });
        }
        else if(check.equals("login"))
        {
            phoneno.setVisibility(View.VISIBLE);
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers()
    {
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if(answer1.equals(""))
        {
            question1.setError("This Field is required");
            question1.requestFocus();
            return;
        }
        if(answer2.equals(""))
        {
            question2.setError("This Field is required");
            question2.requestFocus();
            return;
        }

        else
        {
            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);


            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this,"Security Question's answers have been updated",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ResetPasswordActivity.this, UserHome.class);
                        startActivity(intent);
                    }
                }
            });


        }
    }

    private void DisplayPreviousans()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String ans1=snapshot.child("answer1").getValue().toString();
                    String ans2=snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void verifyUser() {
        String phone = phoneno.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (phone.equals("")) {
            phoneno.setError("Please Enter your phone no");
            phoneno.requestFocus();
            return;
        }
        if (answer1.equals("")) {
            question1.setError("Please Enter the Answer");
            question1.requestFocus();
            return;
        }
        if (answer2.equals("")) {
            question2.setError("Please Enter the Answer");
            question2.requestFocus();
            return;
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String mPhone=snapshot.child("phone").getValue().toString();
                        if (snapshot.hasChild("Security Questions"))
                        {
                            String ans1=snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2=snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if(!ans1.equals(answer1))
                            {
                                Toast.makeText(ResetPasswordActivity.this,"Your first answer is wrong",Toast.LENGTH_LONG).show();
                            }
                            else if(!ans2.equals(answer2))
                            {
                                Toast.makeText(ResetPasswordActivity.this,"Your second answer is wrong",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword= new EditText(ResetPasswordActivity.this);
                                newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                newPassword.setHint("Enter a new Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(ResetPasswordActivity.this,"Password has been changed Successfully",Toast.LENGTH_LONG).show();

                                                        Intent intent=new Intent(ResetPasswordActivity.this, loginActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                        }

                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this,"You have not set the security Questions.",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"This Phone no doesn't Exists",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}