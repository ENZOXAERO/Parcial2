package com.example.parcial2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Service {

    private String api = "http://192.168.1.138:90/ServiceAndroid/public/task?";

    public Service(){
    }

    public String getTasks(String state){
        String response = "";
        try {
            URL url = new URL(api + "method=tasks&args0=" + state );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader( connection.getInputStream());
            int data = reader.read();
            while (data != -1) {
                response += (char) data;
                data = reader.read();
            }
            return response;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    public String getTask(String code){
        String response = "";
        try {
            URL url = new URL(api + "method=getTask&args0=" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader( connection.getInputStream());
            int data = reader.read();
            while (data != -1) {
                response += (char) data;
                data = reader.read();
            }
            return response;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    public String saveTask(String name, String description){
        String response = "";
        try {
            URL url = new URL(api + "method=save&args0=" + name + "&args1=" + description + "&args2=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream());
            writer.write("");
            writer.flush();
            InputStreamReader reader = new InputStreamReader( connection.getInputStream());
            int data = reader.read();
            while (data != -1) {
                response += (char) data;
                data = reader.read();
            }
            return response;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    public String updateContact(String code, String name, String description, int state){
        String response = "";
        try {
            URL url = new URL(api + "method=update&args0=" + code + "&args1=" + name + "&args2=" + description + "&args3=" + state);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream());
            writer.write("");
            writer.flush();
            InputStreamReader reader = new InputStreamReader( connection.getInputStream());
            int data = reader.read();
            while (data != -1) {
                response += (char) data;
                data = reader.read();
            }
            return response;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    public String deleteTask(String code){
        String response = "";
        try {
            URL url = new URL(api + "method=delete&args0=" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream());
            writer.write("");
            writer.flush();
            InputStreamReader reader = new InputStreamReader( connection.getInputStream());
            int data = reader.read();
            while (data != -1) {
                response += (char) data;
                data = reader.read();
            }
            return response;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }
}
