package com.example.juego_3enraya;

import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_FALSE;
import static com.example.juego_3enraya.Model.DefaultConstants.CONNECTION_TRUE;
import static com.example.juego_3enraya.Model.DefaultConstants.MOVE;
import static com.example.juego_3enraya.Model.DefaultConstants.START_GAME;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    //Attributes
    TextView txtIp;
    TextView txtPort;
    TextView txtTurn;

    TextView txtResult;
    Button btnStart;
    Button btnConnec;
    Button[][] buttons;

    MainActivity instance;
    Boolean turn = false;

    Socket socketS;

    //Methods
    public Boolean geTurn(){
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Build an array with 9 buttons in horizontal order
        buttons = new Button[3][3];
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
         btnConnec = findViewById(R.id.btnConnec);

         txtTurn = findViewById(R.id.txtTurn);

         btnStart = findViewById(R.id.btnStart);
         btnStart.setEnabled(false);
         btnStart.setOnClickListener(
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                        iniciarPartida();
                     }
                 }
         );
        //Build menu textView
        txtIp = findViewById(R.id.txtIp);
        txtPort = findViewById(R.id.txtPort);
        //TxtResult
        txtResult = findViewById(R.id.txtResult);

        btnConnec.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String ip = txtIp.getText().toString();
                        int port = Integer.valueOf(txtPort.getText().toString());

                        if (!ip.equals("") && port!=0){
                            ThreadConnection conn = new ThreadConnection(ip, port, instance);
                            Log.i("Hola","Funciona " +ip );
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
            case START_GAME:
                txtTurn.setText(turn.toString());
                initGame();
                break;
            case MOVE:

        }

    }

    public void initGame(){
        if (turn){
            changeButtonsState(turn);
        }else{

        }
    }
    public void changeButtonsState(boolean state){
        for (int i = 0; i < buttons.length ; i++) {
            for (int u = 0; u < buttons[i].length ; u++) {
                buttons[i][u].setEnabled(state);
            }
        }
    }

    public void iniciarPartida(){
        String ip = txtIp.getText().toString();
        int port = Integer.valueOf(txtPort.getText().toString());
        ThreadStartGame start = new ThreadStartGame(ip, port, instance, socketS);
        start.execute();
    }


   public void getClickPosition(View view){
        String tag = view.getTag().toString();

   }


}