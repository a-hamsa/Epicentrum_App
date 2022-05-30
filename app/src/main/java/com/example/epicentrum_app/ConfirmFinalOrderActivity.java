package com.example.epicentrum_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, tableEditText;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        submitButton = findViewById(R.id.submitBtn);
        nameEditText = findViewById(R.id.txtNameConfirm);
        tableEditText = findViewById(R.id.txtTableConfirm);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

    }

    private void Check(){
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please Provide customer full name!", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(tableEditText.getText().toString())){
            Toast.makeText(this, "Please Provide customer table number!", Toast.LENGTH_SHORT).show();
        } else {
            confirmOrder();
        }
    }

    private void confirmOrder(){
        final String saveCurrentDate, saveCurrentTime;
        final DatabaseReference orders = FirebaseDatabase.getInstance().getReference("Orders").child(nameEditText.getText().toString());

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("Nama", nameEditText.getText().toString());
        ordersMap.put("Table", tableEditText.getText().toString());
        ordersMap.put("Date", saveCurrentDate);
        ordersMap.put("Time", saveCurrentTime);
        ordersMap.put("State", "Process");

        orders.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("Cart").child("UNIQUE_USER_ID").removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "The order has been placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });




    }
}