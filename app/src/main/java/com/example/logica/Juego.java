package com.example.logica;

public class Juego {
    private boolean minas[][];
    private int numeros[][];
    private String colores[][];
    private boolean revelado[][];
    private boolean banderas[][];
    private int cantidadBanderas;
    private int tiempo;
    private int tamano;
    private boolean primero;

    public Juego(int tamano) {
        this.tamano = tamano;
        this.minas = new boolean[this.tamano][this.tamano];
        this.numeros = new int[this.tamano][this.tamano];
        this.colores = new String[this.tamano][this.tamano];
        this.revelado = new boolean[this.tamano][this.tamano];
        this.banderas = new boolean[this.tamano][this.tamano];
        this.cantidadBanderas = this.tamano * this.tamano / 4;
        this.tiempo = 0;
        this.primero = true;
    }

    public boolean isPrimero() {
        return primero;
    }

    public int getTamano() {
        return tamano;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getCantidadBanderas() {
        return cantidadBanderas;
    }

    public boolean[][] getBanderas() {
        return banderas;
    }

    public boolean[][] getRevelado() {
        return revelado;
    }

    public boolean[][] getMinas() {
        return minas;
    }

    public int[][] getNumeros() {
        return numeros;
    }

    public String[][] getColores() {
        return colores;
    }

    public void revelado(int i, int j) {
        this.revelado[i][j] = false;
    }

    public void poner_quitar(int i, int j) {
        if(this.banderas[i][j]) {
            cantidadBanderas++;
        }
        else {
            cantidadBanderas--;
        }
        this.banderas[i][j] = !this.banderas[i][j];
    }

    public void correrTiempo() {
        this.tiempo++;
    }

    public boolean verificarVictoria() {
        for(int i = 0; i < this.tamano; i++) {
            for(int j = 0; j < this.tamano; j++) {
                if(revelado[i][j] != minas[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void generarMatrices(int a, int b) {
        this.primero = false;
        //Generar minas
        for(int k = 0; k < this.cantidadBanderas; k++) {
            int i = (int) (Math.random() * this.tamano - 1);
            int j = (int) (Math.random() * this.tamano - 1);
            while(minas[i][j] || (i == a && j == b) || (i == a - 1 && j == b - 1) || (i == a - 1 && j == b) || (i == a - 1 && j == b + 1) || (i == a && j == b - 1) || (i == a && j == b + 1) || (i == a + 1 && j == b - 1) || (i == a + 1 && j == b) || (i == a + 1 && j == b + 1)) {
                i = (int) (Math.random() * this.tamano - 1);
                j = (int) (Math.random() * this.tamano - 1);
            }
            minas[i][j] = true;
        }
        //Generar números
        for(int i = 0; i < this.tamano; i++) {
            for(int j = 0; j < this.tamano; j++) {
                if(!minas[i][j]) {
                    int cant = 0;
                    if(i == 0 && j == 0) {
                        if(minas[0][1]) {
                            cant++;
                        }
                        if(minas[1][0]) {
                            cant++;
                        }
                        if(minas[1][1]) {
                            cant++;
                        }
                    }
                    else if(i == this.tamano - 1 && j == this.tamano - 1) {
                        if(minas[this.tamano - 2][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[this.tamano - 2][this.tamano - 1]) {
                            cant++;
                        }
                        if(minas[this.tamano - 1][this.tamano - 2]) {
                            cant++;
                        }
                    }
                    else if(i == 0 && j == this.tamano - 1) {
                        if(minas[0][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[1][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[1][this.tamano - 1]) {
                            cant++;
                        }
                    }
                    else if(i == this.tamano - 1 && j == 0) {
                        if(minas[this.tamano - 2][0]) {
                            cant++;
                        }
                        if(minas[this.tamano - 2][1]) {
                            cant++;
                        }
                        if(minas[this.tamano - 1][1]) {
                            cant++;
                        }
                    }
                    else if(i == 0) {
                        if(minas[0][j - 1]) {
                            cant++;
                        }
                        if(minas[0][j + 1]) {
                            cant++;
                        }
                        if(minas[1][j - 1]) {
                            cant++;
                        }
                        if(minas[1][j]) {
                            cant++;
                        }
                        if(minas[1][j + 1]) {
                            cant++;
                        }
                    }
                    else if(i == this.tamano - 1) {
                        if(minas[this.tamano - 2][j - 1]) {
                            cant++;
                        }
                        if(minas[this.tamano - 2][j]) {
                            cant++;
                        }
                        if(minas[this.tamano - 2][j + 1]) {
                            cant++;
                        }
                        if(minas[this.tamano - 1][j - 1]) {
                            cant++;
                        }
                        if(minas[this.tamano - 1][j + 1]) {
                            cant++;
                        }
                    }
                    else if(j == 0) {
                        if(minas[i - 1][0]) {
                            cant++;
                        }
                        if(minas[i - 1][1]) {
                            cant++;
                        }
                        if(minas[i][1]) {
                            cant++;
                        }
                        if(minas[i + 1][0]) {
                            cant++;
                        }
                        if(minas[i + 1][1]) {
                            cant++;
                        }
                    }
                    else if(j == this.tamano - 1) {
                        if(minas[i - 1][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[i - 1][this.tamano - 1]) {
                            cant++;
                        }
                        if(minas[i][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[i + 1][this.tamano - 2]) {
                            cant++;
                        }
                        if(minas[i + 1][this.tamano - 1]) {
                            cant++;
                        }
                    }
                    else {
                        if(minas[i - 1][j - 1]) {
                            cant++;
                        }
                        if(minas[i - 1][j]) {
                            cant++;
                        }
                        if(minas[i - 1][j + 1]) {
                            cant++;
                        }
                        if(minas[i][j - 1]) {
                            cant++;
                        }
                        if(minas[i][j + 1]) {
                            cant++;
                        }
                        if(minas[i + 1][j - 1]) {
                            cant++;
                        }
                        if(minas[i + 1][j]) {
                            cant++;
                        }
                        if(minas[i + 1][j + 1]) {
                            cant++;
                        }
                    }
                    numeros[i][j] = cant;
                }
            }
        }
        //Generar colores
        for(int i = 0; i < this.tamano; i++) {
            for(int j = 0; j < this.tamano; j++) {
                if(numeros[i][j] == 1) {
                    colores[i][j] = "#0000FF";
                }
                if(numeros[i][j] == 2) {
                    colores[i][j] = "#00FF00";
                }
                if(numeros[i][j] == 3) {
                    colores[i][j] = "#FF0000";
                }
                if(numeros[i][j] == 4) {
                    colores[i][j] = "#FFFF00";
                }
                if(numeros[i][j] == 5) {
                    colores[i][j] = "#9900CC";
                }
                if(numeros[i][j] == 6) {
                    colores[i][j] = "#00FFFF";
                }
                if(numeros[i][j] == 7) {
                    colores[i][j] = "#FF6600";
                }
                if(numeros[i][j] == 8) {
                    colores[i][j] = "#FF0099";
                }
            }
        }
        //Rellenar revelado
        for(int i = 0; i < this.tamano; i++) {
            for(int j = 0; j < this.tamano; j++) {
                this.revelado[i][j] = true;
            }
        }
    }
}
