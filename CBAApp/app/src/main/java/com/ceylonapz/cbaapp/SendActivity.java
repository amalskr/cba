package com.ceylonapz.cbaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ceylonapz.cbaapp.model.User;
import com.ceylonapz.cbaapp.utils.Util;
import com.ceylonapz.cbaapp.utils.Validator;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;

public class SendActivity extends AppCompatActivity {

    private User user;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("USER"), User.class);
    }

    public void sendNow(View view) {

        EditText eml_et = (EditText) findViewById(R.id.emailEt);
        String email = eml_et.getText().toString();

        if (Validator.isValidEmail(email)) {
            sendEmail(email);
        } else {
            Util.showToast(this, "Invalid email! Your name and domain name should have minimum 3 letters.(abc@abc.lk) ");
        }
    }

    private void sendEmail(String email) {
        imageUri = Uri.parse(user.getSignatureImagePath());

        String body = "NAME : " + user.getName() + "\n" +
                "AMOUNT : " + user.getAmount() + "\n" +
                "CODE : " + user.getCode();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "CBA payment - " + user.getName());
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        File fdelete = new File(imageUri.getPath());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + imageUri.getPath());
            } else {
                System.out.println("file not Deleted :" + imageUri.getPath());
            }
        }
    }
}
