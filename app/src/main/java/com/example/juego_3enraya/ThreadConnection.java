package com.example.juego_3enraya;

import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_FALSE;
import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_TRUE;
import static com.example.juego_3enraya.Model.DefaultConstants.START_GAME;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadConnection extends AsyncTask<Void, Void, Boolean> {

    String ip;
    int port;
    Socket socketS;

    MainActivity instance;

    public ThreadConnection(String ip, int port, MainActivity instance){
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
        DataInputStream in;
        DataOutputStream out;

        try {
            //Se conecta al servidor
            InetAddress serverAddr = InetAddress.getByName(ip);
            Log.i("I/TCP Client", " Connecting... to " + ip);
            socketS = new Socket(serverAddr, port);
            Log.i("I/TCP Client", "Connected to server");
            //Para leer y escribir
            in = new DataInputStream(socketS.getInputStream());
            out = new DataOutputStream(socketS.getOutputStream());
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
        Log.i("final","Estoy esperando aquí conn " + resposta);
        if(resposta == true){
            instance.updateUI(CONNECTION_TRUE);
            instance.socketS = this.socketS;
        }else{
            instance.updateUI(CONNECTION_FALSE);
        }

    }
}