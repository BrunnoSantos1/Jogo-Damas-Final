package com.damas.service;

import com.damas.model.Tabuleiro;
import org.springframework.stereotype.Service;

@Service
public class CapturaService {

    public void remover(
            Tabuleiro tabuleiro,
            int linha,
            int coluna) {

        tabuleiro.removerPeca(
                linha,
                coluna);
    }
}
