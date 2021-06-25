package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logica.Juego;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Juego jue;
    private LinearLayout separadorApp;
    private LinearLayout info;
    private LinearLayout dificultad;
    private TextView banderas;
    private TextView tiempo;
    private Button dif10;
    private Button dif20;
    private Button reiniciar;
    private Button casillas[][];
    private GridLayout gridCasillas;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.separadorApp = findViewById(R.id.separadorApp);
        this.separadorApp.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.setMargins(30, 0, 30, 0);
        parametros.gravity = Gravity.CENTER;

        //this.dificultad = findViewById(R.id.dificultad);
        this.dificultad = new LinearLayout(this);
        this.dificultad.setOrientation(LinearLayout.HORIZONTAL);
        this.dificultad.setLayoutParams(parametros);
        this.separadorApp.addView(this.dificultad);

        //this.info = findViewById(R.id.info);
        this.info = new LinearLayout(this);
        this.info.setOrientation(LinearLayout.HORIZONTAL);
        this.info.setLayoutParams(parametros);
        this.separadorApp.addView(this.info);

        //this.gridCasillas = findViewById(R.id.plantilla);
        this.gridCasillas = new GridLayout(this);
        this.separadorApp.addView(this.gridCasillas);

        //this.dif10 = findViewById(R.id.dif10);
        this.dif10 = new Button(this);
        this.dif10.setText("Normal");
        this.dif10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarMatriz(10);
            }
        });
        this.dificultad.addView(this.dif10, parametros);

        //this.dif20 = findViewById(R.id.dif10);
        this.dif20 = new Button(this);
        this.dif20.setText("Difícil");
        this.dif20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarMatriz(15);
            }
        });
        this.dificultad.addView(this.dif20, parametros);

        //this.banderas = findViewById(R.id.banderas);
        this.banderas = new TextView(this);
        this.banderas.setBackgroundResource(R.drawable.bandera);
        this.banderas.setTextColor(Color.BLACK);
        this.banderas.setGravity(Gravity.CENTER_HORIZONTAL);
        this.info.addView(this.banderas, parametros);
        this.banderas.setVisibility(View.GONE);

        //this.reiniciar = findViewById(R.id.reiniciar);
        this.reiniciar = new Button(this);
        this.reiniciar.setText("Reiniciar");
        this.reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarJuego();
            }
        });
        this.info.addView(this.reiniciar, parametros);
        this.reiniciar.setVisibility(View.GONE);

        //this.tiempo = findViewById(R.id.tiempo);
        this.tiempo = new TextView(this);
        this.tiempo.setBackgroundResource(R.drawable.tiempo);
        this.tiempo.setTextColor(Color.MAGENTA);
        this.tiempo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        this.info.addView(this.tiempo, parametros);
        this.tiempo.setVisibility(View.GONE);
    }

    private void generarMatriz(int tamano) {
        this.dif10.setVisibility(View.GONE);
        this.dif20.setVisibility(View.GONE);
        this.banderas.setVisibility(View.VISIBLE);
        this.reiniciar.setVisibility(View.VISIBLE);
        this.tiempo.setVisibility(View.VISIBLE);

        this.jue = new Juego(tamano);
        this.casillas = new Button[this.jue.getTamano()][this.jue.getTamano()];
        this.banderas.setText(String.valueOf(this.jue.getCantidadBanderas()));
        this.tiempo.setText(String.format("%02d:%02d", this.jue.getTiempo() / 60, this.jue.getTiempo() % 60));

        temporizarJuego();

        this.gridCasillas.setColumnCount(this.jue.getTamano());
        this.gridCasillas.setRowCount(this.jue.getTamano());

        Display pantalla = getWindowManager().getDefaultDisplay();
        Point dimension = new Point();
        pantalla.getSize(dimension);
        int ancho = dimension.x;
        int alto = dimension.y;


        for(int i = 0; i < this.jue.getTamano(); i++) {
            for(int j = 0; j < this.jue.getTamano(); j++) {
                this.casillas[i][j] = new Button(this);
                this.casillas[i][j].setText(i +  " " + j);
                this.casillas[i][j].setTextColor(Color.TRANSPARENT);
                this.casillas[i][j].setLayoutParams(new LinearLayout.LayoutParams(ancho / this.jue.getTamano(), ancho / this.jue.getTamano()));
                this.casillas[i][j].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                this.gridCasillas.addView(this.casillas[i][j]);
                this.casillas[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button casilla = (Button) v;
                        verificarCasilla(casilla);
                        verificarVictoria();
                    }
                });
                this.casillas[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Button casilla = (Button) v;
                        accionarBandera(casilla);
                        return true;
                    }
                });
            }
        }
    }

    private void verificarVictoria() {
        if(this.jue.verificarVictoria()) {
            inhabilitarJuego();
            marcarBanderas();
            this.timer.cancel();
            Toast.makeText(getApplicationContext(),"(¬‿¬)", Toast.LENGTH_SHORT).show();
        }
    }

    private void marcarBanderas() {
        for(int i = 0; i < this.jue.getTamano(); i++) {
            for(int j = 0; j < this.jue.getTamano(); j++) {
                if(this.jue.getMinas()[i][j]) {
                    this.casillas[i][j].getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
        this.banderas.setText("0");
    }

    private void inhabilitarJuego() {
        for(int i = 0; i < this.jue.getTamano(); i++) {
            for(int j = 0; j < this.jue.getTamano(); j++) {
                this.casillas[i][j].setEnabled(false);
            }
        }
    }

    private void temporizarJuego() {
        if(this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contabilizarTiempo();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void reiniciarJuego() {
        this.dif10.setVisibility(View.VISIBLE);
        this.dif20.setVisibility(View.VISIBLE);
        this.banderas.setVisibility(View.GONE);
        this.reiniciar.setVisibility(View.GONE);
        this.tiempo.setVisibility(View.GONE);
        this.gridCasillas.removeAllViews();
    }

    private void contabilizarTiempo() {
        this.jue.correrTiempo();
        this.tiempo.setText(String.format("%02d:%02d", this.jue.getTiempo() / 60, this.jue.getTiempo() % 60));
    }

    private void accionarBandera(Button casilla) {
        String[] coor = casilla.getText().toString().split(" ");
        int i = Integer.parseInt(coor[0]);
        int j = Integer.parseInt(coor[1]);
        if(this.jue.getCantidadBanderas() != 0 || this.jue.getBanderas()[i][j]) {
            this.jue.poner_quitar(i, j);
            if(this.jue.getBanderas()[i][j]) {
                this.casillas[i][j].getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            }
            else {
                this.casillas[i][j].getBackground().clearColorFilter();
            }
            this.banderas.setText(String.valueOf(this.jue.getCantidadBanderas()));
        }
        else {
            Toast.makeText(getApplicationContext(),"No te quedan más banderas", Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarCasilla(Button casilla) {
        String[] coor = casilla.getText().toString().split(" ");
        int i = Integer.parseInt(coor[0]);
        int j = Integer.parseInt(coor[1]);
        if(this.jue.isPrimero()) {
            this.jue.generarMatrices(i, j);
        }
        if(!this.jue.getBanderas()[i][j]) {
            if(jue.getMinas()[i][j]) {
                abrirMinas();
                this.casillas[i][j].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                inhabilitarJuego();
                this.timer.cancel();
                Toast.makeText(getApplicationContext(),"ಥ_ಥ", Toast.LENGTH_SHORT).show();
            }
            else {
                abrirCasilla(i, j);
            }
        }
    }

    private void abrirMinas() {
        for(int i = 0; i < this.jue.getTamano(); i++) {
            for(int j = 0; j < this.jue.getTamano(); j++) {
                if(this.jue.getMinas()[i][j]) {
                    this.casillas[i][j].setText("■");
                    this.casillas[i][j].setTextColor(Color.BLACK);
                    if(!this.jue.getBanderas()[i][j]) {
                        this.casillas[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        }
    }

    private void abrirCasilla(int i, int j) {
        this.jue.revelado(i, j);
        if(this.jue.getNumeros()[i][j] == 0) {
            if(i == 0 && j == 0) {
                if(this.jue.getRevelado()[0][1] && !this.jue.getBanderas()[0][1]) {
                    abrirCasilla(0 ,1);
                }
                if(this.jue.getRevelado()[1][0] && !this.jue.getBanderas()[1][0]) {
                    abrirCasilla(1 ,0);
                }
                if(this.jue.getRevelado()[1][1] && !this.jue.getBanderas()[1][1]) {
                    abrirCasilla(1 ,1);
                }
            }
            else if(i == this.jue.getTamano() - 1 && j == this.jue.getTamano() - 1) {
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][this.jue.getTamano() - 2] && !this.jue.getBanderas()[this.jue.getTamano() - 2][this.jue.getTamano() - 2]) {
                    abrirCasilla(this.jue.getTamano() - 2 ,this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][this.jue.getTamano() - 1] && !this.jue.getBanderas()[this.jue.getTamano() - 2][this.jue.getTamano() - 1]) {
                    abrirCasilla(this.jue.getTamano() - 2 ,this.jue.getTamano() - 1);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 1][this.jue.getTamano() - 2] && !this.jue.getBanderas()[this.jue.getTamano() - 1][this.jue.getTamano() - 2]) {
                    abrirCasilla(this.jue.getTamano() - 1 ,this.jue.getTamano() - 2);
                }
            }
            else if(i == 0 && j == this.jue.getTamano() - 1) {
                if(this.jue.getRevelado()[0][this.jue.getTamano() - 2] && !this.jue.getBanderas()[0][this.jue.getTamano() - 2]) {
                    abrirCasilla(0, this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[1][this.jue.getTamano() - 2] && !this.jue.getBanderas()[1][this.jue.getTamano() - 2]) {
                    abrirCasilla(1, this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[1][this.jue.getTamano() - 1] && !this.jue.getBanderas()[1][this.jue.getTamano() - 1]) {
                    abrirCasilla(1, this.jue.getTamano() - 1);
                }
            }
            else if(i == this.jue.getTamano() - 1 && j == 0) {
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][0] && !this.jue.getBanderas()[this.jue.getTamano() - 2][0]) {
                    abrirCasilla(this.jue.getTamano() - 2, 0);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][1] && !this.jue.getBanderas()[this.jue.getTamano() - 2][1]) {
                    abrirCasilla(this.jue.getTamano() - 2, 1);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 1][1] && !this.jue.getBanderas()[this.jue.getTamano() - 1][1]) {
                    abrirCasilla(this.jue.getTamano() - 1, 1);
                }
            }
            else if(i == 0) {
                if(this.jue.getRevelado()[0][j - 1] && !this.jue.getBanderas()[0][j - 1]) {
                    abrirCasilla(0 ,j - 1);
                }
                if(this.jue.getRevelado()[0][j + 1] && !this.jue.getBanderas()[0][j + 1]) {
                    abrirCasilla(0 ,j + 1);
                }
                if(this.jue.getRevelado()[1][j - 1] && !this.jue.getBanderas()[1][j - 1]) {
                    abrirCasilla(1 ,j - 1);
                }
                if(this.jue.getRevelado()[1][j] && !this.jue.getBanderas()[1][j]) {
                    abrirCasilla(1 , j);
                }
                if(this.jue.getRevelado()[1][j + 1] && !this.jue.getBanderas()[1][j + 1]) {
                    abrirCasilla(1 ,j + 1);
                }
            }
            else if(i == this.jue.getTamano() - 1) {
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][j - 1] && !this.jue.getBanderas()[this.jue.getTamano() - 2][j - 1]) {
                    abrirCasilla(this.jue.getTamano() - 2, j - 1);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][j] && !this.jue.getBanderas()[this.jue.getTamano() - 2][j]) {
                    abrirCasilla(this.jue.getTamano() - 2, j);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 2][j + 1] && !this.jue.getBanderas()[this.jue.getTamano() - 2][j + 1]) {
                    abrirCasilla(this.jue.getTamano() - 2, j + 1);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 1][j - 1] && !this.jue.getBanderas()[this.jue.getTamano() - 1][j - 1]) {
                    abrirCasilla(this.jue.getTamano() - 1, j - 1);
                }
                if(this.jue.getRevelado()[this.jue.getTamano() - 1][j + 1] && !this.jue.getBanderas()[this.jue.getTamano() - 1][j + 1]) {
                    abrirCasilla(this.jue.getTamano() - 1, j + 1);
                }
            }
            else if(j == 0) {
                if(this.jue.getRevelado()[i - 1][0] && !this.jue.getBanderas()[i - 1][0]) {
                    abrirCasilla(i - 1, 0);
                }
                if(this.jue.getRevelado()[i - 1][1] && !this.jue.getBanderas()[i - 1][1]) {
                    abrirCasilla(i - 1, 1);
                }
                if(this.jue.getRevelado()[i][1] && !this.jue.getBanderas()[i][1]) {
                    abrirCasilla(i, 1);
                }
                if(this.jue.getRevelado()[i + 1][0] && !this.jue.getBanderas()[i + 1][0]) {
                    abrirCasilla(i + 1, 0);
                }
                if(this.jue.getRevelado()[i + 1][1] && !this.jue.getBanderas()[i + 1][1]) {
                    abrirCasilla(i + 1, 1);
                }
            }
            else if(j == this.jue.getTamano() - 1) {
                if(this.jue.getRevelado()[i - 1][this.jue.getTamano() - 2] && !this.jue.getBanderas()[i - 1][this.jue.getTamano() - 2]) {
                    abrirCasilla(i - 1, this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[i - 1][this.jue.getTamano() - 1] && !this.jue.getBanderas()[i - 1][this.jue.getTamano() - 1]) {
                    abrirCasilla(i - 1, this.jue.getTamano() - 1);
                }
                if(this.jue.getRevelado()[i][this.jue.getTamano() - 2] && !this.jue.getBanderas()[i][this.jue.getTamano() - 2]) {
                    abrirCasilla(i, this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[i + 1][this.jue.getTamano() - 2] && !this.jue.getBanderas()[i + 1][this.jue.getTamano() - 2]) {
                    abrirCasilla(i + 1, this.jue.getTamano() - 2);
                }
                if(this.jue.getRevelado()[i + 1][this.jue.getTamano() - 1] && !this.jue.getBanderas()[i + 1][this.jue.getTamano() - 1]) {
                    abrirCasilla(i + 1, this.jue.getTamano() - 1);
                }
            }
            else {
                if(this.jue.getRevelado()[i - 1][j - 1] && !this.jue.getBanderas()[i - 1][j - 1]) {
                    abrirCasilla(i - 1, j - 1);
                }
                if(this.jue.getRevelado()[i - 1][j] && !this.jue.getBanderas()[i - 1][j]) {
                    abrirCasilla(i - 1, j);
                }
                if(this.jue.getRevelado()[i - 1][j + 1] && !this.jue.getBanderas()[i - 1][j + 1]) {
                    abrirCasilla(i - 1, j + 1);
                }
                if(this.jue.getRevelado()[i][j - 1] && !this.jue.getBanderas()[i][j - 1]) {
                    abrirCasilla(i, j - 1);
                }
                if(this.jue.getRevelado()[i][j + 1] && !this.jue.getBanderas()[i][j + 1]) {
                    abrirCasilla(i, j + 1);
                }
                if(this.jue.getRevelado()[i + 1][j - 1] && !this.jue.getBanderas()[i + 1][j - 1]) {
                    abrirCasilla(i + 1, j - 1);
                }
                if(this.jue.getRevelado()[i + 1][j] && !this.jue.getBanderas()[i + 1][j]) {
                    abrirCasilla(i + 1, j);
                }
                if(this.jue.getRevelado()[i + 1][j + 1] && !this.jue.getBanderas()[i + 1][j + 1]) {
                    abrirCasilla(i + 1, j + 1);
                }
            }
        }
        else {
            this.casillas[i][j].setText(String.valueOf(this.jue.getNumeros()[i][j]));
            int color = Color.parseColor(jue.getColores()[i][j]);
            this.casillas[i][j].setTextColor(color);
        }
        this.casillas[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        this.casillas[i][j].setEnabled(false);
    }
}