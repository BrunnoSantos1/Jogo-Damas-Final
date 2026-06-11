package com.damas.strategy.ia;

import com.damas.model.Jogada;
import com.damas.model.Tabuleiro;
import com.damas.model.TipoPeca;
import com.damas.strategy.movimento.CapturaStrategy;

import java.util.ArrayList;
import java.util.List;

public class HardStrategy
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

                if (matriz[linha][coluna] == TipoPeca.DAMA_PRETA) {

                    if (capturaStrategy
                            .capturaDama(
                                    tabuleiro,
                                    linha,
                                    coluna)) {

                        return;
                    }
                }

                if (matriz[linha][coluna] == TipoPeca.PRETA) {

                    if (capturaStrategy
                            .capturaEmCadeia(
                                    tabuleiro,
                                    linha,
                                    coluna)) {

                        return;
                    }
                }
            }
        }

        // =====================================================
        // IA INTELIGENTE
        // =====================================================

        List<Jogada> jogadas = gerarJogadas(tabuleiro);

        if (jogadas.isEmpty()) {

            return;
        }

        Jogada melhor = escolherMelhorJogada(jogadas);

        tabuleiro.moverPeca(
                melhor.origemLinha,
                melhor.origemColuna,
                melhor.destinoLinha,
                melhor.destinoColuna);
    }

    // =====================================================
    // GERAR JOGADAS
    // =====================================================

    private List<Jogada> gerarJogadas(
            Tabuleiro tabuleiro) {

        List<Jogada> jogadas = new ArrayList<>();

        int[][] matriz = tabuleiro.getTabuleiro();

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                if (matriz[linha][coluna] == TipoPeca.PRETA) {

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

                            int pontuacao = 10;

                            if (novaLinha == 0) {

                                pontuacao += 100;
                            }

                            jogadas.add(
                                    new Jogada(
                                            linha,
                                            coluna,
                                            novaLinha,
                                            novaColuna,
                                            pontuacao));
                        }
                    }
                }
            }
        }

        return jogadas;
    }

    // =====================================================
    // MELHOR JOGADA
    // =====================================================

    private Jogada escolherMelhorJogada(
            List<Jogada> jogadas) {

        Jogada melhor = jogadas.get(0);

        for (Jogada jogada : jogadas) {

            if (jogada.pontuacao > melhor.pontuacao) {

                melhor = jogada;
            }
        }

        return melhor;
    }
}
