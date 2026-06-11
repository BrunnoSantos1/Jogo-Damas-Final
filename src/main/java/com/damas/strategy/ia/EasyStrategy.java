package com.damas.strategy.ia;

import com.damas.model.Tabuleiro;
import com.damas.model.TipoPeca;
import com.damas.strategy.movimento.CapturaStrategy;

import java.util.Random;

public class EasyStrategy
        implements IAStrategy {

    private CapturaStrategy capturaStrategy = new CapturaStrategy();

    @Override
    public void fazerJogada(
            Tabuleiro tabuleiro) {

        int[][] matriz = tabuleiro.getTabuleiro();

        // =====================================================
        // CAPTURA OBRIGATÓRIA
        // =====================================================

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                if (matriz[linha][coluna] == TipoPeca.PRETA) {

                    if (capturaStrategy
                            .capturaEmCadeia(
                                    tabuleiro,
                                    linha,
                                    coluna)) {

                        return;
                    }
                }

                if (matriz[linha][coluna] == TipoPeca.DAMA_PRETA) {

                    if (capturaStrategy
                            .capturaDama(
                                    tabuleiro,
                                    linha,
                                    coluna)) {

                        return;
                    }
                }
            }
        }

        // =====================================================
        // MOVIMENTO ALEATÓRIO
        // =====================================================

        Random random = new Random();

        boolean jogou = false;

        while (!jogou) {

            int linha = random.nextInt(8);

            int coluna = random.nextInt(8);

            int valor = matriz[linha][coluna];

            if (valor == TipoPeca.PRETA) {

                int[][] movimentos = {
                        { -1, -1 },
                        { -1, 1 }
                };

                for (int[] mov : movimentos) {

                    int novaLinha = linha + mov[0];

                    int novaColuna = coluna + mov[1];

                    if (novaLinha >= 0 &&
                            novaLinha < 8 &&
                            novaColuna >= 0 &&
                            novaColuna < 8 &&
                            matriz[novaLinha][novaColuna]

                                    ==

                                    TipoPeca.VAZIO) {

                        tabuleiro.moverPeca(
                                linha,
                                coluna,
                                novaLinha,
                                novaColuna);

                        jogou = true;

                        break;
                    }
                }
            }
        }
    }
}
