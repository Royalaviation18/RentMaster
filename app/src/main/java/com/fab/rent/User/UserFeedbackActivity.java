package com.fab.rent.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserFeedbackActivity extends AppCompatActivity {
    TextView tvFeedback;
    RatingBar rbStars;
    private String feedback = "0", feedbacktemp = "0";
    EditText etfeedback;
    private String pid, phone, date, feedbackid;
    private String saveCurrentDate, saveCurrentTime, description, starratings;
    private Button sendfeedback;
    DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);


        tvFeedback = findViewById(R.id.tvFeedback);
        rbStars = findViewById(R.id.rbStars);
        etfeedback = findViewById(R.id.et_feedback);
        sendfeedback = findViewById(R.id.feedback_final);


        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    feedback = ("Very Dissatisfied");

                } else if (rating == 1) {
                    feedback = ("Dissatisfied");

                } else if (rating == 2) {
                    feedback = ("OK");


                } else if (rating == 3) {
                    feedback = ("Satisfied");

                } else if (rating == 4) {
                    feedback = ("Very Satisfied");

                } else if (rating == 5) {
                    feedback = ("Excellent");

                }

//                else
//                {
//
//                }
                tvFeedback.setText(feedback);
                feedbacktemp = String.valueOf(feedback);

                if (feedbacktemp.equals("Very Dissatisfied")) {
                    starratings = "0";
                }
                if (feedbacktemp.equals("Dissatisfied")) {
                    starratings = "1";
                }
                if (feedbacktemp.equals("OK")) {
                    starratings = "2";
                }
                if (feedbacktemp.equals("Satisfied")) {
                    starratings = "3";
                }
                if (feedbacktemp.equals("Very Satisfied")) {
                    starratings = "4";
                }
                if (feedbacktemp.equals("Excellent")) {
                    starratings = "5";
                }
            }

        });

        pid = getIntent().getStringExtra("pid");
        phone = getIntent().getStringExtra("phone");




        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat(" MM dd,yyyy ");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a ");
        saveCurrentTime = currentTime.format(calendar.getTime());

        feedbackid = saveCurrentDate + saveCurrentTime;
        //
        sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = etfeedback.getText().toString();

                feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        HashMap<String, Object> feedbackDetailsMap = new HashMap<>();
                        feedbackDetailsMap.put("pid", pid);
                        feedbackDetailsMap.put("phone", phone);
                        feedbackDetailsMap.put("date", saveCurrentDate);
                        feedbackDetailsMap.put("feedbackid", feedbackid);
                        feedbackDetailsMap.put("ratings", starratings);
                        feedbackDetailsMap.put("description", description);


//


                        feedbackRef.child(feedbackid).updateChildren(feedbackDetailsMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserFeedbackActivity.this, "Thank You for your feedback", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UserFeedbackActivity.this, UserHome.class);
                                            startActivity(intent);
                                            //  intent.setFlags()
                                        } else {

                                            Toast.makeText(UserFeedbackActivity.this, "Something went Wrong,Please try again later!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
}
