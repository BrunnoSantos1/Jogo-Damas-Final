package com.damas.model;

public class Jogada {

    public int origemLinha;
    public int origemColuna;

    public int destinoLinha;
    public int destinoColuna;

    public int pontuacao;

    public Jogada(
            int origemLinha,
            int origemColuna,
            int destinoLinha,
            int destinoColuna,
            int pontuacao) {

        this.origemLinha = origemLinha;
        this.origemColuna = origemColuna;
        this.destinoLinha = destinoLinha;
        this.destinoColuna = destinoColuna;
        this.pontuacao = pontuacao;
    }
}
