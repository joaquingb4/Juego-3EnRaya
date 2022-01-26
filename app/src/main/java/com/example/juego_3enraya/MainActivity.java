package com.example.juego_3enraya;

import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_FALSE;
import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_TRUE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Attributes
    private static final int SERVERPORT = 5000;
    private static final String ADDRESS = "192.168.1.121";
    TextView txtResult;


    Button btnStart, btnConnec;

    MainActivity instance;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Build an array with 9 buttons in horizontal order
        Button[][] buttons = new Button[3][3];
        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);
        //The buttons are not enable
        for (int i = 0; i < buttons.length ; i++) {
            for (int u = 0; u < buttons[i].length; u++){
                buttons[i][u].setEnabled(false);
            }
        }
        //take a instance of this activity;
        instance = this;


        //Build menu buttons
        Button btnCon = findViewById(R.id.btnConnec);
        Button btnStart = findViewById(R.id.btnStart);
        //Build menu textView
        TextView txtIp = findViewById(R.id.txtIp);
        TextView txtPort = findViewById(R.id.txtPort);

        btnCon.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String ip = txtIp.getText().toString();
                        int port = Integer.parseInt(txtPort.getText().toString());
                        if (!ip.equals("") && port!=0){
                            ThreadConnection conn = new ThreadConnection(ip, port, instance);
                            Log.i("Hola","Funciona");
                            conn.execute();

                        }else {
                            Toast.makeText(getApplicationContext(), "Ip o port", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void updateUI(byte header){
        switch (header){
            case CONNECTION_TRUE:
                txtResult.setText("CONNETED OK");
                btnStart.setEnabled(true);
                break;
            case CONNECTION_FALSE:
                txtResult.setText("CONNECTED KO");
                break;
        }
    }


}


