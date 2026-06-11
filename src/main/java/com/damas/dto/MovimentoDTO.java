package com.damas.dto;

public class MovimentoDTO {

    private int origemLinha;
    private int origemColuna;

    private int destinoLinha;
    private int destinoColuna;

    public int getOrigemLinha() {
        return origemLinha;
    }

    public void setOrigemLinha(int origemLinha) {
        this.origemLinha = origemLinha;
    }

    public int getOrigemColuna() {
        return origemColuna;
    }

    public void setOrigemColuna(int origemColuna) {
        this.origemColuna = origemColuna;
    }

    public int getDestinoLinha() {
        return destinoLinha;
    }

    public void setDestinoLinha(int destinoLinha) {
        this.destinoLinha = destinoLinha;
    }

    public int getDestinoColuna() {
        return destinoColuna;
    }

    public void setDestinoColuna(int destinoColuna) {
        this.destinoColuna = destinoColuna;
    }
}