package com.example.bmiProject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;

import java.util.List;


public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private Context context;
    private List<Food> foodList;
    FirebaseFirestore firebaseFirestore;
    private static ClickListener listener;

    public FoodListAdapter(Context context, List<Food> results) {
        this.context = context;
        this.foodList = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        holder.tvCategory.setText(foodList.get(position).getFoodNameCategory());
        holder.tvCalorie.setText(foodList.get(position).getFoodCalorie());
        holder.tvFoodName.setText(foodList.get(position).getFoodName());
        Glide.with(context).load(foodList.get(position).getFbUri()).into(holder.foodImg);
        int positions =  position;
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("food").document(foodList.get(positions).getFoodDocId()).delete().
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,
                                    "Item deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                            foodList.remove(positions);
                         context.startActivity(new Intent(context,
                                 FoodListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvCalorie, tvFoodName;
        ImageView foodImg, deleteImg;
        Button editBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCalorie = itemView.findViewById(R.id.tvCalorie);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            foodImg = itemView.findViewById(R.id.foodImg2);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            editBtn = itemView.findViewById(R.id.editBtn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(foodList.get(getAdapterPosition()));
                }
            });
        }
    }

    public void OnItemClickListener(ClickListener listener) {
        FoodListAdapter.listener = listener;
    }

    public interface ClickListener {
        void onClick(Food result);
    }
}







