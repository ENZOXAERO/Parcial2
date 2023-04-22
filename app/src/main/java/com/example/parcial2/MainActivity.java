package com.example.parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<HashMap<String,String>> taskList = new ArrayList<>();

    List<State> states = new ArrayList<State>();

    ProgressDialog progressDialog;

    ListAdapter listAdapter;

    Spinner ddlState;

    private String code;
    private String state = "2";
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ddlState = findViewById(R.id.ddlState);

        states.add(new State(2,"All"));
        states.add(new State(1,"Active"));
        states.add(new State(0,"Inactive"));

       ddlState.setAdapter(new ArrayAdapter<State>(this, android.R.layout.simple_spinner_item, states));
        ddlState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String selected = ddlState.getSelectedItem().toString();
               if(selected.equals("All")){
                   state = "2";
               }else if(selected.equals("Active")){
                   state = "1";
               }else{
                   state = "0";
               }
                taskList = new ArrayList<>();
                listAdapter = null;
                listView.setAdapter(null);
                new getTasks().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        listView = findViewById(R.id.livTask);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> code = (HashMap)(listView.getItemAtPosition(i));
                updateTask(code.get("id"));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> data = (HashMap)(listView.getItemAtPosition(i));
                code = data.get("id");
                new deleteTask().execute();
                return false;
            }
        });

        new getTasks().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        taskList = new ArrayList<>();
        listAdapter = null;
        listView.setAdapter(null);
        new getTasks().execute();
    }

    private void createTask(){
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
    }

    private void updateTask(String code){
        Intent intent = new Intent(this, Update.class);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    private class getTasks extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing())
                progressDialog.dismiss();


            listAdapter = new SimpleAdapter(MainActivity.this, taskList, R.layout.item, new String[]{"name"}, new int[]{R.id.contact});
            listView.setAdapter(listAdapter);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new Service();
            String json = service.getTasks(state);
            if(json != null){
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray data = object.getJSONArray("data");

                    for(int x = 0; x < data.length(); x++){
                        JSONObject jsonObject = data.getJSONObject(x);
                        HashMap<String,String> list = new HashMap<>();
                        list.put("id",jsonObject.getString("code"));
                        list.put("name",jsonObject.getString("name"));
                        taskList.add(list);
                    }
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


    private class deleteTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if(response.equals("202")){
                Toast.makeText(getApplicationContext(), "Contact has been saved.", Toast.LENGTH_SHORT).show();
                taskList = new ArrayList<>();
                listAdapter = null;
                listView.setAdapter(null);
                new getTasks().execute();
            }else{
                Toast.makeText(getApplicationContext(), "Contact has not been saved.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new Service();

            String json = service.deleteTask(code);
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