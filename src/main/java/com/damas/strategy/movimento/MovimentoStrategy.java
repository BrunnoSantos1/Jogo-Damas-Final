package com.damas.strategy.movimento;

import com.damas.model.Tabuleiro;

public interface MovimentoStrategy {

    boolean validarMovimento(
            Tabuleiro tabuleiro,
            int origemLinha,
            int origemColuna,
            int destinoLinha,
            int destinoColuna);
}
