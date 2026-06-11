package com.damas.strategy.movimento;

import com.damas.model.Tabuleiro;

public class MovimentoDamaStrategy
        implements MovimentoStrategy {

    @Override
    public boolean validarMovimento(
            Tabuleiro tabuleiro,
            int origemLinha,
            int origemColuna,
            int destinoLinha,
            int destinoColuna) {

        return Math.abs(destinoLinha -
                origemLinha)

                ==

                Math.abs(destinoColuna -
                        origemColuna);
    }
}
