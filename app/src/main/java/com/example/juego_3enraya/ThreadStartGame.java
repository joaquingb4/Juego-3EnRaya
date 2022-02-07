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

            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dOutput = new DataOutputStream(socket.getOutputStream());
            byte[] message = {0x01};
            dOutput.writeInt(message.length);//<-----Estoy aquí Byte
            dOutput.write(message);

            //Answer
            int lenght = dIn.readInt();
            if (lenght == 0){
                Log.i("Mensaje",": " + lenght);
            }
            byte[] answer = new byte[lenght];
            dIn.readFully(answer, 0, message.length);
            int number = answer[1];
            Log.i("The answer",": " + number);
            instance.setTurn(number==1);
            /*
            byte[] answer = new byte[];
            dIn.readFully(message, 0, message.length);
            */
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