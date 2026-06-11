package com.damas.view;

import com.damas.model.Tabuleiro;
import javax.swing.*;
import java.awt.*;
import com.damas.strategy.ia.EasyStrategy;
import com.damas.strategy.ia.IAStrategy;
import com.damas.strategy.ia.MediumStrategy;
import com.damas.strategy.ia.HardStrategy;
import com.damas.service.MovimentoService;
import com.damas.service.CapturaService;
import com.damas.strategy.movimento.CapturaStrategy;
import com.damas.service.JogoService;

public class TelaJogo extends JFrame {

    private JButton[][] botoes = new JButton[8][8];

    private Tabuleiro tabuleiro;

    private int linhaSelecionada = -1;
    private int colunaSelecionada = -1;

    private int jogadorAtual = 1;

    private JLabel turnoLabel;
    private boolean contraMaquina;

    private IAStrategy estrategiaIA;

    private MovimentoService movimentoService = new MovimentoService();

    private CapturaService capturaService = new CapturaService();

    private CapturaStrategy capturaStrategy = new CapturaStrategy();

    private JogoService jogoService = new JogoService();

    private ImageIcon pecaVermelha;
    private ImageIcon pecaPreta;

    private ImageIcon damaVermelha;
    private ImageIcon damaPreta;

    public TelaJogo(String dificuldade, boolean contraMaquina) {

        tabuleiro = new Tabuleiro();
        this.contraMaquina = contraMaquina;

        pecaVermelha = new ImageIcon(
                getClass().getResource("/img/vermelho.png"));

        pecaPreta = new ImageIcon(
                getClass().getResource("/img/preto.png"));

        damaVermelha = new ImageIcon(
                getClass().getResource("/img/dama_vermelho.png"));

        damaPreta = new ImageIcon(
                getClass().getResource("/img/dama_preta.png"));

        if (dificuldade.equals("facil")) {

            estrategiaIA = new EasyStrategy();

        } else if (dificuldade.equals("medio")) {

            estrategiaIA = new MediumStrategy();

        } else {

            estrategiaIA = new HardStrategy();
        }

        setTitle("Jogo de Damas");
        setSize(700, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelTabuleiro = new JPanel(new GridLayout(8, 8));

        turnoLabel = new JLabel("Turno: Jogador Vermelho");

        add(turnoLabel, BorderLayout.SOUTH);

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                JButton botao = new JButton();

                botao.setBorderPainted(false);
                botao.setFocusPainted(false);
                botao.setContentAreaFilled(false);
                botao.setOpaque(true);

                botao.setFont(new Font("Arial", Font.BOLD, 30));

                if ((linha + coluna) % 2 == 0) {

                    botao.setBackground(Color.WHITE);

                } else {

                    botao.setBackground(Color.GRAY);
                }

                int linhaFinal = linha;
                int colunaFinal = coluna;

                botao.addActionListener(e -> clicarCasa(linhaFinal, colunaFinal));

                botoes[linha][coluna] = botao;

                painelTabuleiro.add(botao);
            }
        }

        atualizarTabuleiro();

        add(painelTabuleiro);

        setVisible(true);
    }

    private void clicarCasa(int linha, int coluna) {

        int valor = tabuleiro.getTabuleiro()[linha][coluna];

        // Selecionar peça
        if (valor == jogadorAtual ||
                (jogadorAtual == 1 && valor == 3) ||
                (jogadorAtual == 2 && valor == 4)) {

            linhaSelecionada = linha;
            colunaSelecionada = coluna;

            return;
        }

        // Movimentar
        if (linhaSelecionada != -1) {

            moverPeca(linha, coluna);
        }
    }

    private void moverPeca(int destinoLinha, int destinoColuna) {

        int diferencaLinha = destinoLinha - linhaSelecionada;
        int diferencaColuna = destinoColuna - colunaSelecionada;

        int pecaSelecionada = tabuleiro.getTabuleiro()[linhaSelecionada][colunaSelecionada];

        // =========================
        // PEÇA VERMELHA NORMAL
        // =========================
        if (pecaSelecionada == 1) {

            // Movimento normal
            if (!capturaStrategy.existeCapturaObrigatoria(
                    tabuleiro,
                    jogadorAtual) &&

                    diferencaLinha == 1 &&
                    Math.abs(diferencaColuna) == 1 &&
                    tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                movimentoService.mover(
                        tabuleiro,
                        linhaSelecionada,
                        colunaSelecionada,
                        destinoLinha,
                        destinoColuna);

                verificarVitoria();
                trocarTurno();
            }

            // Captura
            if (Math.abs(diferencaLinha) == 2 &&
                    Math.abs(diferencaColuna) == 2 &&
                    tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                int linhaInimigo = (linhaSelecionada + destinoLinha) / 2;

                int colunaInimigo = (colunaSelecionada + destinoColuna) / 2;

                int inimigo = tabuleiro.getTabuleiro()[linhaInimigo][colunaInimigo];

                if (inimigo == 2 || inimigo == 4) {

                    capturaService.remover(
                            tabuleiro,
                            linhaInimigo,
                            colunaInimigo);

                    movimentoService.mover(
                            tabuleiro,
                            linhaSelecionada,
                            colunaSelecionada,
                            destinoLinha,
                            destinoColuna);

                    // Captura em cadeia
                    if (capturaStrategy.podeContinuarCapturando(
                            tabuleiro,
                            destinoLinha,
                            destinoColuna,
                            jogadorAtual)) {

                        linhaSelecionada = destinoLinha;
                        colunaSelecionada = destinoColuna;

                    } else {

                        verificarVitoria();
                        trocarTurno();
                    }
                }
            }
        }

        // =========================
        // PEÇA PRETA NORMAL
        // =========================
        if (pecaSelecionada == 2) {

            // Movimento normal
            if (!capturaStrategy.existeCapturaObrigatoria(
                    tabuleiro,
                    jogadorAtual) &&

                    diferencaLinha == -1 &&
                    Math.abs(diferencaColuna) == 1 &&
                    tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                movimentoService.mover(
                        tabuleiro,
                        linhaSelecionada,
                        colunaSelecionada,
                        destinoLinha,
                        destinoColuna);

                verificarVitoria();
                trocarTurno();
            }

            // Captura
            if (Math.abs(diferencaLinha) == 2 &&
                    Math.abs(diferencaColuna) == 2 &&
                    tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                int linhaInimigo = (linhaSelecionada + destinoLinha) / 2;

                int colunaInimigo = (colunaSelecionada + destinoColuna) / 2;

                int inimigo = tabuleiro.getTabuleiro()[linhaInimigo][colunaInimigo];

                if (inimigo == 1 || inimigo == 3) {

                    capturaService.remover(
                            tabuleiro,
                            linhaInimigo,
                            colunaInimigo);

                    movimentoService.mover(
                            tabuleiro,
                            linhaSelecionada,
                            colunaSelecionada,
                            destinoLinha,
                            destinoColuna);

                    // Captura em cadeia
                    if (capturaStrategy.podeContinuarCapturando(
                            tabuleiro,
                            destinoLinha,
                            destinoColuna,
                            jogadorAtual)) {

                        linhaSelecionada = destinoLinha;
                        colunaSelecionada = destinoColuna;

                    } else {

                        verificarVitoria();
                        trocarTurno();
                    }
                }
            }
        }

        // =========================
        // MOVIMENTO DAS DAMAS
        // =========================
        if (pecaSelecionada == 3 || pecaSelecionada == 4) {

            if (Math.abs(diferencaLinha) == Math.abs(diferencaColuna)) {

                int passoLinha;

                if (diferencaLinha > 0) {

                    passoLinha = 1;

                } else {

                    passoLinha = -1;
                }

                int passoColuna;

                if (diferencaColuna > 0) {

                    passoColuna = 1;

                } else {

                    passoColuna = -1;
                }

                int linhaAtual = linhaSelecionada + passoLinha;
                int colunaAtual = colunaSelecionada + passoColuna;

                boolean caminhoLivre = true;

                int inimigos = 0;

                int linhaInimigo = -1;
                int colunaInimigo = -1;

                while (linhaAtual != destinoLinha &&
                        colunaAtual != destinoColuna) {

                    int valor = tabuleiro.getTabuleiro()[linhaAtual][colunaAtual];

                    // Dama vermelha
                    if (pecaSelecionada == 3) {

                        if (valor == 1 || valor == 3) {

                            caminhoLivre = false;
                            break;
                        }

                        if (valor == 2 || valor == 4) {

                            inimigos++;

                            linhaInimigo = linhaAtual;
                            colunaInimigo = colunaAtual;
                        }
                    }

                    // Dama preta
                    if (pecaSelecionada == 4) {

                        if (valor == 2 || valor == 4) {

                            caminhoLivre = false;
                            break;
                        }

                        if (valor == 1 || valor == 3) {

                            inimigos++;

                            linhaInimigo = linhaAtual;
                            colunaInimigo = colunaAtual;
                        }
                    }

                    linhaAtual += passoLinha;
                    colunaAtual += passoColuna;
                }

                // Movimento simples
                if (!capturaStrategy.existeCapturaObrigatoria(
                        tabuleiro,
                        jogadorAtual) &&

                        caminhoLivre &&
                        inimigos == 0 &&
                        tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                    movimentoService.mover(
                            tabuleiro,
                            linhaSelecionada,
                            colunaSelecionada,
                            destinoLinha,
                            destinoColuna);

                    verificarVitoria();
                    trocarTurno();
                }

                // Captura da dama
                if (caminhoLivre &&
                        inimigos == 1 &&
                        tabuleiro.getTabuleiro()[destinoLinha][destinoColuna] == 0) {

                    capturaService.remover(
                            tabuleiro,
                            linhaInimigo,
                            colunaInimigo);

                    movimentoService.mover(
                            tabuleiro,
                            linhaSelecionada,
                            colunaSelecionada,
                            destinoLinha,
                            destinoColuna);

                    // Atualiza posição da dama
                    linhaSelecionada = destinoLinha;
                    colunaSelecionada = destinoColuna;

                    // Verifica se continua captura
                    if (!capturaStrategy.podeContinuarCapturandoDama(
                            tabuleiro,
                            destinoLinha,
                            destinoColuna,
                            jogadorAtual)) {

                        verificarVitoria();
                        trocarTurno();
                    }
                }
            }

            // =========================
            // CONTINUAÇÃO DE CAPTURA
            // =========================

            boolean continuar = false;

            // Dama
            if (pecaSelecionada == 3 || pecaSelecionada == 4) {

                continuar = capturaStrategy.podeContinuarCapturandoDama(
                        tabuleiro,
                        destinoLinha,
                        destinoColuna,
                        jogadorAtual);

            }
            // Peça comum
            else {

                continuar = capturaStrategy.podeContinuarCapturando(
                        tabuleiro,
                        destinoLinha,
                        destinoColuna,
                        jogadorAtual);
            }

            // Se não houver mais captura
            if (!continuar) {

                linhaSelecionada = -1;
                colunaSelecionada = -1;
            }
        }
        movimentoService.verificarDama(tabuleiro);

        atualizarTabuleiro();

    }

    private void trocarTurno() {

        if (jogadorAtual == 1) {

            jogadorAtual = 2;

            // =========================
            // CONTRA MÁQUINA
            // =========================
            if (contraMaquina) {

                turnoLabel.setText("Turno: Máquina");

                estrategiaIA.fazerJogada(tabuleiro);

                movimentoService.verificarDama(tabuleiro);

                atualizarTabuleiro();

                verificarVitoria();

                jogadorAtual = 1;

                turnoLabel.setText("Turno: Jogador Vermelho");

            } else {

                turnoLabel.setText("Turno: Jogador Preto");
            }

        } else {

            jogadorAtual = 1;

            turnoLabel.setText("Turno: Jogador Vermelho");
        }
    }

    private void atualizarTabuleiro() {

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                int valor = tabuleiro.getTabuleiro()[linha][coluna];

                JButton botao = botoes[linha][coluna];

                // limpa
                botao.setText("");
                botao.setIcon(null);

                // =========================
                // PEÇA VERMELHA
                // =========================

                if (valor == 1) {

                    botao.setIcon(pecaVermelha);
                }

                // =========================
                // PEÇA PRETA
                // =========================

                if (valor == 2) {

                    botao.setIcon(pecaPreta);
                }

                // =========================
                // DAMA VERMELHA
                // =========================

                if (valor == 3) {

                    botao.setIcon(damaVermelha);
                }

                // =========================
                // DAMA PRETA
                // =========================

                if (valor == 4) {

                    botao.setIcon(damaPreta);
                }
            }
        }
    }

    private void verificarVitoria() {

        String vencedor = jogoService.verificarVitoria(
                tabuleiro);

        if (vencedor != null) {

            String[] opcoes = {
                    "Novo Jogo",
                    "Fechar"
            };

            int escolha = JOptionPane.showOptionDialog(
                    this,
                    vencedor,
                    "Fim de Jogo",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            dispose();

            if (escolha == 0) {

                this.dispose();

                reiniciarJogo();

            } else {

                this.dispose();

                System.exit(0);
            }
        }
    }

    private void reiniciarJogo() {

        SwingUtilities.invokeLater(() -> {

            TelaInicial novaTela = new TelaInicial();

            novaTela.setVisible(true);
        });
    }
}
