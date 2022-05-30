package com.example.epicentrum_app.listener;

import com.example.epicentrum_app.model.FoodModel;

import java.util.List;

public interface IFoodLoadListener {
    void onFoodLoadSuccess(List<FoodModel> foodModelList);
    void onFoodLoadFailed(String message);
}
