package com.damas.model;

public class TabuleiroMemento {
    private final int[][] estadoSalvo;

    public TabuleiroMemento(int[][] matrizAtual) {
        estadoSalvo = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                estadoSalvo[i][j] = matrizAtual[i][j]; // Cópia peça por peça!
            }
        }
    }

    public int[][] getEstadoSalvo() {
        return estadoSalvo;
    }
}
