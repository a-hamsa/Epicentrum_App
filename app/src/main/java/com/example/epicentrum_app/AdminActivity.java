package com.example.epicentrum_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.epicentrum_app.adapter.MyAdminAdapter;
import com.example.epicentrum_app.listener.IAdminLoadListener;
import com.example.epicentrum_app.model.AdminModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity implements IAdminLoadListener {

    @BindView(R.id.recycler_admin)
    RecyclerView recyclerAdmin;
    @BindView(R.id.adminLayout)
    RelativeLayout adminLayout;

    Button btnSignOut;
    IAdminLoadListener adminLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnSignOut = (Button) findViewById(R.id.signOutBtn);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
        loadAdminFromFirebase();
    }
    private void loadAdminFromFirebase(){
        List<AdminModel> adminModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot adminSnapshot : snapshot.getChildren()){
                        AdminModel adminModel = adminSnapshot.getValue(AdminModel.class);
                        adminModel.setNama(adminModel.getNama());
                        adminModels.add(adminModel);
                    }
                    adminLoadListener.onAdminLoadSuccess(adminModels);
                } else{
                    adminLoadListener.onAdminLoadFailed("Orders empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adminLoadListener.onAdminLoadFailed(error.getMessage());
            }
        });
    }

    private void init(){
        ButterKnife.bind(this);
        adminLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerAdmin.setLayoutManager(layoutManager);
        recyclerAdmin.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

    }

    @Override
    public void onAdminLoadSuccess(List<AdminModel> adminModelList) {
        MyAdminAdapter adapter = new MyAdminAdapter(this, adminModelList);
        recyclerAdmin.setAdapter(adapter);
    }

    @Override
    public void onAdminLoadFailed(String message) {
        Snackbar.make(adminLayout, message, Snackbar.LENGTH_LONG).show();
    }
}