package com.damas.strategy.movimento;

import com.damas.model.Tabuleiro;

public class CapturaStrategy {

    public boolean existeCapturaObrigatoria(
            Tabuleiro tabuleiro,
            int jogador) {

        int[][] matriz = tabuleiro.getTabuleiro();

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                int valor = matriz[linha][coluna];

                // peças normais
                if ((jogador == 1 && valor == 1) ||
                        (jogador == 2 && valor == 2)) {

                    int[][] direcoes = {
                            { 2, -2 },
                            { 2, 2 },
                            { -2, -2 },
                            { -2, 2 }
                    };

                    for (int[] dir : direcoes) {

                        int destinoLinha = linha + dir[0];
                        int destinoColuna = coluna + dir[1];

                        int meioLinha = linha + dir[0] / 2;
                        int meioColuna = coluna + dir[1] / 2;

                        if (destinoLinha >= 0 &&
                                destinoLinha < 8 &&
                                destinoColuna >= 0 &&
                                destinoColuna < 8 &&
                                matriz[destinoLinha][destinoColuna] == 0) {

                            int inimigo = matriz[meioLinha][meioColuna];

                            if (jogador == 1 &&
                                    (inimigo == 2 || inimigo == 4)) {

                                return true;
                            }

                            if (jogador == 2 &&
                                    (inimigo == 1 || inimigo == 3)) {

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean podeContinuarCapturando(
            Tabuleiro tabuleiro,
            int linha,
            int coluna,
            int jogador) {

        int[][] matriz = tabuleiro.getTabuleiro();

        int[][] direcoes = {
                { 2, -2 },
                { 2, 2 },
                { -2, -2 },
                { -2, 2 }
        };

        for (int[] dir : direcoes) {

            int destinoLinha = linha + dir[0];
            int destinoColuna = coluna + dir[1];

            int meioLinha = linha + dir[0] / 2;
            int meioColuna = coluna + dir[1] / 2;

            if (destinoLinha >= 0 &&
                    destinoLinha < 8 &&
                    destinoColuna >= 0 &&
                    destinoColuna < 8 &&
                    matriz[destinoLinha][destinoColuna] == 0) {

                int inimigo = matriz[meioLinha][meioColuna];

                if (jogador == 1 &&
                        (inimigo == 2 || inimigo == 4)) {

                    return true;
                }

                if (jogador == 2 &&
                        (inimigo == 1 || inimigo == 3)) {

                    return true;
                }
            }
        }

        return false;
    }

    public boolean podeContinuarCapturandoDama(
            Tabuleiro tabuleiro,
            int linha,
            int coluna,
            int jogador) {

        int[][] matriz = tabuleiro.getTabuleiro();

        int[][] direcoes = {
                { -1, -1 },
                { -1, 1 },
                { 1, -1 },
                { 1, 1 }
        };

        for (int[] dir : direcoes) {

            int l = linha + dir[0];
            int c = coluna + dir[1];

            while (l >= 0 &&
                    l < 8 &&
                    c >= 0 &&
                    c < 8) {

                int valor = matriz[l][c];

                if ((jogador == 1 &&
                        (valor == 2 || valor == 4)) ||

                        (jogador == 2 &&
                                (valor == 1 || valor == 3))) {

                    int destinoLinha = l + dir[0];
                    int destinoColuna = c + dir[1];

                    if (destinoLinha >= 0 &&
                            destinoLinha < 8 &&
                            destinoColuna >= 0 &&
                            destinoColuna < 8 &&
                            matriz[destinoLinha][destinoColuna] == 0) {

                        return true;
                    }

                    break;
                }

                if ((jogador == 1 &&
                        (valor == 1 || valor == 3)) ||

                        (jogador == 2 &&
                                (valor == 2 || valor == 4))) {

                    break;
                }

                l += dir[0];
                c += dir[1];
            }
        }

        return false;
    }

    // =====================================================
    // CAPTURA EM CADEIA IA
    // =====================================================

    public boolean capturaEmCadeia(
            Tabuleiro tabuleiro,
            int linha,
            int coluna) {

        int[][] matriz = tabuleiro.getTabuleiro();

        int[][] direcoes = {
                { -2, -2 },
                { -2, 2 },
                { 2, -2 },
                { 2, 2 }
        };

        for (int[] dir : direcoes) {

            int destinoLinha = linha + dir[0];

            int destinoColuna = coluna + dir[1];

            int meioLinha = linha + dir[0] / 2;

            int meioColuna = coluna + dir[1] / 2;

            if (destinoLinha >= 0 &&
                    destinoLinha < 8 &&
                    destinoColuna >= 0 &&
                    destinoColuna < 8 &&
                    matriz[destinoLinha][destinoColuna] == 0) {

                int inimigo = matriz[meioLinha][meioColuna];

                if (inimigo == 1 || inimigo == 3) {

                    tabuleiro.removerPeca(
                            meioLinha,
                            meioColuna);

                    tabuleiro.moverPeca(
                            linha,
                            coluna,
                            destinoLinha,
                            destinoColuna);

                    return true;
                }
            }
        }

        return false;
    }

    // =====================================================
    // CAPTURA DAMA IA
    // =====================================================

    public boolean capturaDama(
            Tabuleiro tabuleiro,
            int linha,
            int coluna) {

        int[][] matriz = tabuleiro.getTabuleiro();

        int[][] direcoes = {
                { -1, -1 },
                { -1, 1 },
                { 1, -1 },
                { 1, 1 }
        };

        for (int[] dir : direcoes) {

            int l = linha + dir[0];
            int c = coluna + dir[1];

            while (l >= 0 &&
                    l < 8 &&
                    c >= 0 &&
                    c < 8) {

                int valor = matriz[l][c];

                if (valor == 1 || valor == 3) {

                    int destinoLinha = l + dir[0];

                    int destinoColuna = c + dir[1];

                    if (destinoLinha >= 0 &&
                            destinoLinha < 8 &&
                            destinoColuna >= 0 &&
                            destinoColuna < 8 &&
                            matriz[destinoLinha][destinoColuna] == 0) {

                        tabuleiro.removerPeca(l, c);

                        tabuleiro.moverPeca(
                                linha,
                                coluna,
                                destinoLinha,
                                destinoColuna);

                        return true;
                    }

                    break;
                }

                if (valor == 2 || valor == 4) {

                    break;
                }

                l += dir[0];
                c += dir[1];
            }
        }

        return false;
    }
}
