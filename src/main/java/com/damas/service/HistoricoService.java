package com.damas.service;

import com.damas.model.Tabuleiro;
import com.damas.model.TabuleiroMemento;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class HistoricoService {
    private Stack<TabuleiroMemento> historico = new Stack<>();

    // Chame isso ANTES de mover a peça
    public void salvarEstado(Tabuleiro tabuleiro) {
        historico.push(tabuleiro.salvar());
    }

    // Chame isso quando o usuário clicar no botão "Desfazer"
    public void desfazer(Tabuleiro tabuleiro) {
        if (!historico.isEmpty()) {
            TabuleiroMemento ultimaFoto = historico.pop();
            tabuleiro.restaurar(ultimaFoto);
        }
    }
}
