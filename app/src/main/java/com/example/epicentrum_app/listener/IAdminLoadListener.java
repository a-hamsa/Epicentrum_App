package com.example.epicentrum_app.listener;

import com.example.epicentrum_app.model.AdminModel;

import java.util.List;

public interface IAdminLoadListener {
    void onAdminLoadSuccess(List<AdminModel> adminModelList);
    void onAdminLoadFailed(String message);
}
