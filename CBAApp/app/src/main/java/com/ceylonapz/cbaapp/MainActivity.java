package com.ceylonapz.cbaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ceylonapz.cbaapp.utils.Util;
import com.ceylonapz.cbaapp.utils.Validator;

public class MainActivity extends AppCompatActivity {

    private EditText name_et, amount_et, code_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        name_et = (EditText) findViewById(R.id.nameEt);
        amount_et = (EditText) findViewById(R.id.amountEt);
        code_et = (EditText) findViewById(R.id.codeEt);
    }

    //open sign screen
    public void SignNow(View view) {
        String name = name_et.getText().toString();
        String amount = amount_et.getText().toString();
        String code = code_et.getText().toString();

        //validate name
        if (Validator.isAlpha(name)) {
            //validate amount
            if (Validator.isAmount(amount)) {
                //validate code
                if (Validator.isNumber(code)) {

                    Double dAmount = Double.parseDouble(amount);
                    Integer iCode = Integer.parseInt(code);

                    //open signature
                    Intent signatureIntent = new Intent(getApplicationContext(), SignatureActivity.class);
                    signatureIntent.putExtra("NAME", name);
                    signatureIntent.putExtra("AMOUNT", dAmount);
                    signatureIntent.putExtra("CODE", iCode);
                    startActivity(signatureIntent);

                } else {
                    Util.showToast(this, "Invalid code!");
                }
            } else {
                Util.showToast(this, "Invalid amount!");
            }
        } else {
            Util.showToast(this, "Invalid Name! Please use letters.");
        }

    }

    //clear all fields
    public void clearNow(View view) {
        name_et.setText("");
        amount_et.setText("");
        code_et.setText("");
    }
}
