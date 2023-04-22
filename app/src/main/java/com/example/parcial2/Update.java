package com.example.parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Update extends AppCompatActivity {

    ProgressDialog progressDialog;

    String code;
    EditText name;
    EditText description;
    LinearLayout mainLayout;

    Switch swtState;

    boolean state;
    String response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Intent intent = getIntent();

        code = intent.getStringExtra("code");
        name = findViewById(R.id.txtName);
        description = findViewById(R.id.txtDescription);
        swtState = findViewById(R.id.swtState);

        new Update.getTask().execute();

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mainLayout = findViewById(R.id.mainLayout);
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                new Update.saveData().execute();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update.super.onBackPressed();
            }
        });

    }

    public class getTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            swtState.setChecked(state);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Update.this);
            progressDialog.setMessage("Sending data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new Service();

            String json = service.getTask(code);
            if(json != null){
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray data = object.getJSONArray("data");
                    name.setText(data.getJSONObject(0).getString("name"));
                    description.setText(data.getJSONObject(0).getString("description"));
                    state = data.getJSONObject(0).getString("state").equals("0") ? false : true;
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Update.this);
            progressDialog.setMessage("Sending data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new Service();

            int state = swtState.isChecked() ? 1 : 0;

            String json = service.updateContact(code, name.getText().toString(), description.getText().toString(), state);
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