package com.damas.view;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {

        setTitle("Jogo de Damas");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel();

        painel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton pvp = new JButton("Jogador vs Jogador");

        pvp.addActionListener(e -> {

            dispose();

            new TelaJogo("facil", false);
        });
        JButton pvc = new JButton("Jogador vs Máquina");
        pvc.addActionListener(e -> {

            String[] opcoes = {
                    "Fácil",
                    "Médio",
                    "Difícil"
            };

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha a dificuldade",
                    "Dificuldade da IA",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            dispose();

            if (escolha == 0) {

                new TelaJogo("facil", true);

            } else if (escolha == 1) {

                new TelaJogo("medio", true);

            } else {

                new TelaJogo("dificil", true);
            }
        });
        JButton historico = new JButton("Histórico");
        JButton regras = new JButton("Regras");
        JButton sair = new JButton("Sair");

        painel.add(pvp);
        painel.add(pvc);
        painel.add(historico);
        painel.add(regras);
        painel.add(sair);

        add(painel);

        sair.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}