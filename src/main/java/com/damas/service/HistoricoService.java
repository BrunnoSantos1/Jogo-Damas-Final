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

    // ALTERADO: Agora retorna true se desfez algo, ou false se a pilha estava vazia
    public boolean desfazer(Tabuleiro tabuleiro) {
        if (!historico.isEmpty()) {
            TabuleiroMemento ultimaFoto = historico.pop();
            tabuleiro.restaurar(ultimaFoto);
            return true;
        }
        return false;
    }

    // NOVO: Verifica se o histórico está vazio
    public boolean isVazio() {
        return historico.isEmpty();
    }

    // NOVO: Limpa o histórico (essencial para quando reiniciar o jogo)
    public void limpar() {
        historico.clear();
    }
}
