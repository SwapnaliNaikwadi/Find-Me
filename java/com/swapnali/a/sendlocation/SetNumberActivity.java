package com.swapnali.a.sendlocation;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetNumberActivity extends AppCompatActivity {

    TextView contact1,contact2,contact3,add1,add2,add3;

    Button save;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_number);

        session = new SessionManager(getApplicationContext());


        contact1=(TextView)findViewById(R.id.contact1);
        contact2=(TextView)findViewById(R.id.contact2);
        contact3=(TextView)findViewById(R.id.contact3);

        add1=(TextView)findViewById(R.id.add1);
        add2=(TextView)findViewById(R.id.add2);
        add3=(TextView)findViewById(R.id.add3);

        save=(Button)findViewById(R.id.saveNo);

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SetNumberActivity.this);
                dialog.setContentView(R.layout.set_number);
                dialog.setTitle("Add Number");

                // get the Refferences of views
                final EditText editTextNumber=(EditText)dialog.findViewById(R.id.edtNumber);

                Button btnSave=(Button)dialog.findViewById(R.id.save);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String no=editTextNumber.getText().toString();

                        contact1.setText(no);

                        add1.setText("Edit");


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SetNumberActivity.this);
                dialog.setContentView(R.layout.set_number);
                dialog.setTitle("Add Number");

                // get the Refferences of views
                final EditText editTextNumber=(EditText)dialog.findViewById(R.id.edtNumber);

                Button btnSave=(Button)dialog.findViewById(R.id.save);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String no=editTextNumber.getText().toString();

                        contact2.setText(no);

                        add2.setText("Edit");


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SetNumberActivity.this);
                dialog.setContentView(R.layout.set_number);
                dialog.setTitle("Login");

                // get the Refferences of views
                final EditText editTextNumber=(EditText)dialog.findViewById(R.id.edtNumber);

                Button btnSave=(Button)dialog.findViewById(R.id.save);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String no=editTextNumber.getText().toString();

                        contact3.setText(no);

                        add3.setText("Edit");


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.createLoginSession(contact1.getText().toString(),contact2.getText().toString(),contact3.getText().toString());

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
