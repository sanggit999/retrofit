package com.example.lab5_lab6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<User> userList;

    UserItemClickListener itemClickListener;

    public UserAdapter(Context context, List<User> userList, UserItemClickListener itemClickListener) {
        this.context = context;
        this.userList = userList;
        this.itemClickListener = itemClickListener;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        
        Picasso.get().load(user.getAvatar()).into(holder.imageViewAvatar1);
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewPassword.setText(user.getPassword());
        holder.textViewFullName.setText(user.getFullname());
        holder.textViewEmail.setText(user.getEmail());

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onDeleteClicked(user);
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onEditClicked(user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername, textViewPassword, textViewFullName, textViewEmail;
        private ImageView imageViewAvatar1;
        private ImageButton buttonEdit, buttonDelete;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewPassword = itemView.findViewById(R.id.textViewPassword);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageViewAvatar1 = itemView.findViewById(R.id.imageViewAvatar1);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

}
