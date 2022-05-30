package com.example.epicentrum_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.epicentrum_app.R;
import com.example.epicentrum_app.eventbus.MyUpdateCartEvent;
import com.example.epicentrum_app.listener.ICartLoadListener;
import com.example.epicentrum_app.listener.IRecyclerViewClickListener;
import com.example.epicentrum_app.model.CartModel;
import com.example.epicentrum_app.model.FoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class MyFoodAdapter extends RecyclerView.Adapter<MyFoodAdapter.MyFoodViewHolder> {

    private Context context;
    private List<FoodModel> foodModelList;
    private ICartLoadListener iCartLoadListener;

    public MyFoodAdapter(Context context, List<FoodModel> foodModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.foodModelList = foodModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFoodViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFoodViewHolder holder, int position) {
        Glide.with(context).load(foodModelList.get(position).getImage()).into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("Rp.").append(foodModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(foodModelList.get(position).getName()));

        holder.setListener((view, adapterPosition) -> {
            addToCart(foodModelList.get(position));
        });
    }

    private void addToCart(FoodModel foodModel) {
        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("Cart").child("UNIQUE_USER_ID");
        userCart.child(foodModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CartModel cartModel =  snapshot.getValue(CartModel.class);
                    cartModel.setQuantity(cartModel.getQuantity()+1);
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("quantity", cartModel.getQuantity());
                    updateData.put("totalPrice", cartModel.getQuantity()*Integer.parseInt(cartModel.getPrice()));

                    userCart.child(foodModel.getKey()).updateChildren(updateData).addOnSuccessListener(aVoid -> {
                        iCartLoadListener.onCartLoadFailed("Add to cart Success");
                    }).addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                }else{
                    CartModel cartModel = new CartModel();
                    cartModel.setName(foodModel.getName());
                    cartModel.setImage(foodModel.getImage());
                    cartModel.setKey(foodModel.getKey());
                    cartModel.setPrice(foodModel.getPrice());
                    cartModel.setQuantity(1);
                    cartModel.setTotalPrice(Integer.parseInt(foodModel.getPrice()));

                    userCart.child(foodModel.getKey()).setValue(cartModel).addOnSuccessListener(aVoid -> {
                        iCartLoadListener.onCartLoadFailed("Add to cart Success");
                    }).addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                }
                EventBus.getDefault().postSticky(new MyUpdateCartEvent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iCartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class MyFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view, getAdapterPosition());
        }
    }

}

