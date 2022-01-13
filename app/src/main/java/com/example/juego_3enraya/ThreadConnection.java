package com.example.juego_3enraya;

import android.os.AsyncTask;

import java.net.Socket;

public class ThreadConnection extends AsyncTask <Void,Void, Boolean> {
    //Attriubtes
    String ip;
    int port;
    Socket socket;
    MainActivity instance;

    public ThreadConnection(String ip, int port, MainActivity instance) {
        this.ip = ip;
        this.port = port;
        this.instance = instance;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return false;
    }
}
