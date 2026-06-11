package com.damas.strategy.movimento;

import com.damas.model.Tabuleiro;
import com.damas.model.TipoPeca;

public class MovimentoComumStrategy
        implements MovimentoStrategy {

    @Override
    public boolean validarMovimento(
            Tabuleiro tabuleiro,
            int origemLinha,
            int origemColuna,
            int destinoLinha,
            int destinoColuna) {

        int[][] matriz = tabuleiro.getTabuleiro();

        if (destinoLinha < 0 ||
                destinoLinha >= 8 ||
                destinoColuna < 0 ||
                destinoColuna >= 8) {

            return false;
        }

        if (matriz[destinoLinha][destinoColuna] != TipoPeca.VAZIO) {

            return false;
        }

        int diferencaLinha = destinoLinha - origemLinha;

        int diferencaColuna = Math.abs(destinoColuna -
                origemColuna);

        return Math.abs(diferencaLinha) == 1
                && diferencaColuna == 1;
    }
}