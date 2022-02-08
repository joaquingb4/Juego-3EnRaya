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
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadStartGame extends AsyncTask<Void, Void, Boolean> {

    String ip;
    int port;
    Socket socket;

    MainActivity instance;

    public ThreadStartGame(String ip, int port, MainActivity instance, Socket cliente){
        this.ip = ip;
        this.port = port;
        this.instance = instance;
        this.socket = cliente;
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
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Empezar Partida
            //Le envío el tamaño del byte para enviar
            byte[] mensaje = {0x01};
            out.write(mensaje.length);
            //Ahora le envío el array/mensaje
            out.write(mensaje);
            //Tamaño del array de bytes que espero
            int tamaño = in.read();
            Log.i("tamaño : ",""+tamaño);
            //¿Espero quien empieza?
            byte[] answer = new byte[tamaño];
            //lo deposito en answer
            in.read(answer);
            int number = answer[0];//Transformamos en número
            Log.i("respuesta : ",""+number);//<-Estoy aquí, solo tiene un lenght de 1 y no sé que contine
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
        Log.i("final","Estoy esperando aquí Inicio " + resposta);
        if(resposta){
            instance.updateUI(START_GAME);

        }else{
            instance.updateUI(CONNECTION_FALSE);
        }

    }
}