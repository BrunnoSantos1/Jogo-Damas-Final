package com.damas.service;

import com.damas.model.Tabuleiro;
import org.springframework.stereotype.Service;

@Service
public class LeiMaioriaService {

    // =====================================================
    // MAIOR SEQUÊNCIA DE CAPTURA
    // =====================================================

    private int[][] copiarMatriz(
            int[][] original) {

        int[][] copia = new int[8][8];

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                copia[linha][coluna] = original[linha][coluna];
            }
        }

        return copia;
    }

    private Tabuleiro simularCaptura(
            Tabuleiro tabuleiro,
            int origemLinha,
            int origemColuna,
            int destinoLinha,
            int destinoColuna,
            int linhaCapturada,
            int colunaCapturada) {

        Tabuleiro copia = new Tabuleiro(
                copiarMatriz(
                        tabuleiro.getTabuleiro()));

        copia.removerPeca(
                linhaCapturada,
                colunaCapturada);

        copia.moverPeca(
                origemLinha,
                origemColuna,
                destinoLinha,
                destinoColuna);

        return copia;
    }

    public int calcularMaiorSequenciaCaptura(
            Tabuleiro tabuleiro,
            int linha,
            int coluna) {

        int[][] matriz = tabuleiro.getTabuleiro();

        int peca = matriz[linha][coluna];

        // sem peça
        if (peca == 0) {

            return 0;
        }

        int maior = 0;

        int[][] direcoes = {
                { -2, -2 },
                { -2, 2 },
                { 2, -2 },
                { 2, 2 }
        };

        for (int[] dir : direcoes) {

            int destinoLinha = linha + dir[0];

            int destinoColuna = coluna + dir[1];

            int linhaMeio = linha + dir[0] / 2;

            int colunaMeio = coluna + dir[1] / 2;

            if (destinoLinha < 0 ||
                    destinoLinha >= 8 ||
                    destinoColuna < 0 ||
                    destinoColuna >= 8) {

                continue;
            }

            if (matriz[destinoLinha][destinoColuna] != 0) {

                continue;
            }

            int inimigo = matriz[linhaMeio][colunaMeio];

            boolean captura = false;

            if (peca == 1 || peca == 3) {

                captura = inimigo == 2 ||
                        inimigo == 4;
            }

            if (peca == 2 || peca == 4) {

                captura = inimigo == 1 ||
                        inimigo == 3;
            }

            if (!captura) {

                continue;
            }

            Tabuleiro copia = simularCaptura(
                    tabuleiro,
                    linha,
                    coluna,
                    destinoLinha,
                    destinoColuna,
                    linhaMeio,
                    colunaMeio);

            int sequencia = 1 +
                    calcularMaiorSequenciaCaptura(
                            copia,
                            destinoLinha,
                            destinoColuna);

            maior = Math.max(
                    maior,
                    sequencia);
        }

        return maior;
    }

    public int maiorCapturaJogador(
            Tabuleiro tabuleiro,
            int jogador) {

        int maior = 0;

        int[][] matriz = tabuleiro.getTabuleiro();

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                int peca = matriz[linha][coluna];

                if (jogador == 1 &&
                        peca != 1 &&
                        peca != 3) {

                    continue;
                }

                if (jogador == 2 &&
                        peca != 2 &&
                        peca != 4) {

                    continue;
                }

                maior = Math.max(
                        maior,
                        calcularMaiorSequenciaCaptura(
                                tabuleiro,
                                linha,
                                coluna));
            }
        }

        return maior;
    }

    public boolean possuiMaiorCaptura(
            Tabuleiro tabuleiro,
            int jogador,
            int linha,
            int coluna) {

        int maior = maiorCapturaJogador(
                tabuleiro,
                jogador);

        int capturas = calcularMaiorSequenciaCaptura(
                tabuleiro,
                linha,
                coluna);

        return capturas == maior;
    }
}