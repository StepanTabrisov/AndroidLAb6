package com.example.lab5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MainScene extends Activity implements View.OnClickListener {
    TextView userName;
    Button exit;
    Button deleteUser;
    Button changePassword;
    Button accept;
    EditText newPassword;
    TextView errorMessage;

    Intent intent;

    DatabaseHandler db = new DatabaseHandler(this);
    User user = new User();

    FutureTask<Boolean> future;
    FutureTask<Void> future1;
    Callable task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_scene);
        userName = findViewById(R.id.userName);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            user = (User) arguments.getSerializable("user");
        }
        userName.setText(user.getLogin());

        exit = findViewById(R.id.exitButton);
        exit.setOnClickListener(this);
        deleteUser = findViewById(R.id.deleteUserButton);
        deleteUser.setOnClickListener(this);
        changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        accept = findViewById(R.id.acceptNewPassword);
        accept.setOnClickListener(this);

        newPassword = findViewById(R.id.newPassword);
        errorMessage = findViewById(R.id.errorNewPassword);

        intent = new Intent(this, InitScene.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exitButton:
                startActivity(intent);
                break;

            case R.id.deleteUserButton:

                task = () -> {
                    System.out.println("new thread: delete user from db");
                    db.deleteUserFromDB(user);
                    db.close();
                    return null;
                };
                future1 = new FutureTask<>(task);
                new Thread(future1).start();
                startActivity(intent);
                break;

            case R.id.changePassword:
                newPassword.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                break;

            case R.id.acceptNewPassword:

                String newPwd = newPassword.getText().toString();
                //System.out.println("new " + newPwd);
                //System.out.println(user.getPass());
                if(newPwd.equals("")){
                    errorMessage.setText("Введите новый пароль!");
                    errorMessage.setVisibility(View.VISIBLE);
                }else if(newPwd.equals(user.getPass())){
                    errorMessage.setText("Пароли совпадают!");
                    errorMessage.setVisibility(View.VISIBLE);
                }else {
                    errorMessage.setText("Пароль успешно изменен!");
                    errorMessage.setVisibility(View.VISIBLE);
                    newPassword.setVisibility(View.INVISIBLE);
                    accept.setVisibility(View.INVISIBLE);


                    task = () -> {
                        System.out.println("new thread: update user password db");
                        db.updateUserPassword(user, newPwd);
                        db.close();
                        Thread.sleep(10000);
                        return null;
                    };
                    future1 = new FutureTask<>(task);
                    new Thread(future1).start();
                }
                break;
            default:
                break;
        }
    }
}
