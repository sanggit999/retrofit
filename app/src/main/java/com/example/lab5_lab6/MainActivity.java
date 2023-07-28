package com.example.lab5_lab6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String URL = "http://10.0.2.2:3000/";
    private EditText editText1, editText2, editText3, editText4, editText5;
    private Button btn;
    private RecyclerView recyclerView;

    List<User> userList;
    Retrofit retrofit;

    UserAPI userAPI;


    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        recyclerView = findViewById(R.id.recyclerView);
        btn = findViewById(R.id.btn);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getApplicationContext(), userList, new UserItemClickListener() {
            @Override
            public void onEditClicked(User user) {
                AlertEdit(user);
            }

            @Override
            public void onDeleteClicked(User user) {
                AlertDelete(user);
            }
        });

        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        userAPI = retrofit.create(UserAPI.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(userAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostData();
            }
        });

        //Lấy dữ liệu khi post;
        GetData();

    }


    //POST;
    public void PostData() {
        String username = editText1.getText().toString();
        String password = editText2.getText().toString();
        String name = editText3.getText().toString();
        String email = editText4.getText().toString();
        String avatar = editText5.getText().toString();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(name);
        user.setEmail(email);
        user.setAvatar(avatar);


        Call<User> userCall = userAPI.post(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Đã gửi POST  thành công", Toast.LENGTH_SHORT).show();
                    GetData();
                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText4.setText("");
                    editText5.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Đã gửi POST thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("API_CALL", "API call failed: " + t.getMessage());
            }
        });

    }

    //GET;
    private void GetData() {
        Call<List<User>> userCall = userAPI.getAllData();
        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    userList = response.body();
                    userAdapter.setUserList(userList);
                    userAdapter.notifyDataSetChanged();
                    Log.d("API_CALL", "Received user list from API: " + userList.size() + " users");

                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi lấy", Toast.LENGTH_SHORT).show();
                    Log.d("API_CALL", "API call failed, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("API_CALL", "API call failed: " + t.getMessage());
            }
        });
    }

    private void UpdateData(User user) {
        String userId = user.get_id();
        Call<User> userCall = userAPI.edit(userId, user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Xoá thành công, cập nhật lại danh sách
                    GetData();
                    Toast.makeText(MainActivity.this, "Update người dùng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Update người dùng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Có lỗi khi gửi yêu cầu xoá người dùng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void DeleteData(User user) {
        String userId = user.get_id(); // Lấy _id của người dùng
        Call<User> userCall = userAPI.delete(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Xoá thành công, cập nhật lại danh sách
                    GetData();
                    Toast.makeText(MainActivity.this, "Xoá người dùng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Xoá người dùng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Có lỗi khi gửi yêu cầu xoá người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AlertEdit(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("UPDATE");
        builder.setMessage("Vui lòng điền đủ thông tin.");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.update_user, null);
        EditText username = view.findViewById(R.id.editTextText2);
        EditText password = view.findViewById(R.id.editTextText3);
        EditText fullname = view.findViewById(R.id.editTextText4);
        EditText email = view.findViewById(R.id.editTextText5);
        EditText avatar = view.findViewById(R.id.editTextText6);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnHuy = view.findViewById(R.id.btnHuy);


        username.setText(user.getUsername());
        password.setText(user.getPassword());
        fullname.setText(user.getFullname());
        email.setText(user.getEmail());
        avatar.setText(user.getAvatar());

        AlertDialog dialog = builder.setView(view).show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin mới từ EditText
                String updatedUsername = username.getText().toString();
                String updatedPassword = password.getText().toString();
                String updatedFullname = fullname.getText().toString();
                String updatedEmail = email.getText().toString();
                String updatedAvatar = avatar.getText().toString();

                // Tạo đối tượng User mới với thông tin cập nhật
                User updatedUser = new User();
                updatedUser.set_id(user.get_id()); // Đặt _id để xác định người dùng cần cập nhật
                updatedUser.setUsername(updatedUsername);
                updatedUser.setPassword(updatedPassword);
                updatedUser.setFullname(updatedFullname);
                updatedUser.setEmail(updatedEmail);
                updatedUser.setAvatar(updatedAvatar);

                // Gọi phương thức UpdateData để cập nhật thông tin người dùng
                UpdateData(updatedUser);
                dialog.dismiss();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    private void AlertDelete(User user) {
        new AlertDialog.Builder(MainActivity.this).setTitle("Xoá!").setMessage("Bạn có muốn xoá người dùng này?").setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteData(user);
            }
        }).setNegativeButton("Không", null).show();
    }
}