package com.example.parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Create extends AppCompatActivity {

    ProgressDialog progressDialog;

    EditText name;
    EditText description;

    String response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        name = findViewById(R.id.txtName);
        description = findViewById(R.id.txtDescription);

        Button btnNew = findViewById(R.id.btnSave);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new saveData().execute();
            }
        });


        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create.super.onBackPressed();
            }
        });
    }

    public class saveData extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if(response.equals("202")){
                Toast.makeText(getApplicationContext(), "Contact has been saved.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Contact has not been saved.", Toast.LENGTH_SHORT).show();
            }
            name.setText("");
            description.setText("");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Create.this);
            progressDialog.setMessage("Sending data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new Service();

            String json = service.saveTask(name.getText().toString(), description.getText().toString());
            if(json != null){
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray data = object.getJSONArray("data");
                    response = data.getJSONObject(0).getString("response");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "JsonParsing Error", Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "JsonParsing Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;

        }
    }
}