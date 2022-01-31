package com.example.juego_3enraya;

import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_FALSE;
import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_TRUE;
import static com.example.juego_3enraya.Model.DefaultConstants.START_GAME;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadStartGame extends AsyncTask<Void, Void, Boolean> {

    String ip;
    int port;
    Socket socket;

    MainActivity instance;

    public ThreadStartGame(String ip, int port, MainActivity instance){
        this.ip = ip;
        this.port = port;
        this.instance = instance;
    }

    /**
     * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
     * */
    ProgressDialog progressDialog;

    /**
     * muestra una ventana emergente
     * */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = new ProgressDialog(instance);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Connecting to server");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            //Se conecta al servidor
            InetAddress serverAddr = InetAddress.getByName(ip);
            Log.i("I/TCP Client", ip+ " "+" Connecting...");
            socket = new Socket(serverAddr, port);
            Log.i("I/TCP Client", "Connected to server");
            return true;
        }catch (UnknownHostException ex) {
            Log.e("E/TCP Client", "" + ex.getMessage());
            return false;
        } catch (IOException ex) {
            Log.e("E/TCP Client", "" + ex.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean resposta){
        progressDialog.dismiss();
        Log.i("final","Estoy esperando aquí " + resposta);
        if(resposta == true){
            instance.updateUI(START_GAME);
        }else{
            instance.updateUI(CONNECTION_FALSE);
        }

    }
}