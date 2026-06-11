package com.damas.controller;

import com.damas.dto.MovimentoDTO;
import com.damas.model.Tabuleiro;
import com.damas.service.JogoService;
import com.damas.service.MovimentoService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/jogo")
public class JogoController {

    @Autowired
    private JogoService jogoService;

    @Autowired
    private MovimentoService movimentoService;

    // =====================================================
    // TABULEIRO
    // =====================================================

    @GetMapping("/tabuleiro")
    public int[][] tabuleiro() {

        Tabuleiro tabuleiro = jogoService.getTabuleiro();

        return tabuleiro.getTabuleiro();
    }

    // =====================================================
    // TURNO
    // =====================================================

    @GetMapping("/turno")
    public String turno() {

        if (jogoService.getJogadorAtual() == 1) {

            return "Jogador Vermelho";
        }

        return "Jogador Preto";
    }

    // =====================================================
    // VITÓRIA
    // =====================================================

    @GetMapping("/vitoria")
    public String vitoria() {

        String vencedor = jogoService.verificarVitoria(
                jogoService.getTabuleiro());

        if (vencedor == null) {

            return "Partida em andamento";
        }

        return vencedor;
    }

    // =====================================================
    // MOVER PEÇA
    // =====================================================

    @PostMapping("/mover")
    public int[][] mover(
            @RequestBody MovimentoDTO movimento) {

        Tabuleiro tabuleiro = jogoService.getTabuleiro();

        // =====================================================
        // CAPTURA EM SEQUÊNCIA OBRIGATÓRIA
        // =====================================================

        if (jogoService.getLinhaCapturaObrigatoria() != null) {

            if (movimento.getOrigemLinha() != jogoService.getLinhaCapturaObrigatoria()
                    ||
                    movimento.getOrigemColuna() != jogoService.getColunaCapturaObrigatoria()) {

                return tabuleiro.getTabuleiro();
            }
        }

        boolean valido = movimentoService.movimentoValido(
                tabuleiro,
                movimento.getOrigemLinha(),
                movimento.getOrigemColuna(),
                movimento.getDestinoLinha(),
                movimento.getDestinoColuna(),
                jogoService.getJogadorAtual());

        if (!valido) {

            System.out.println(
                    "MOVIMENTO REJEITADO");

            return tabuleiro.getTabuleiro();
        }

        boolean capturou = movimentoService.capturarPeca(
                tabuleiro,
                movimento.getOrigemLinha(),
                movimento.getOrigemColuna(),
                movimento.getDestinoLinha(),
                movimento.getDestinoColuna());

        movimentoService.mover(
                tabuleiro,
                movimento.getOrigemLinha(),
                movimento.getOrigemColuna(),
                movimento.getDestinoLinha(),
                movimento.getDestinoColuna());

        movimentoService.verificarDama(
                tabuleiro);

        // captura em sequência
        boolean continuar = false;

        if (capturou) {

            continuar = movimentoService.podeCapturarNovamente(
                    tabuleiro,
                    movimento.getDestinoLinha(),
                    movimento.getDestinoColuna());

            if (continuar) {

                jogoService.definirCapturaObrigatoria(
                        movimento.getDestinoLinha(),
                        movimento.getDestinoColuna());
            }
        }

        if (!continuar) {

            jogoService.limparCapturaObrigatoria();

            jogoService.trocarTurno();
        }

        return tabuleiro.getTabuleiro();
    }

    @GetMapping("/captura-obrigatoria")
    public int[] capturaObrigatoria() {

        if (jogoService.getLinhaCapturaObrigatoria() == null) {

            return new int[] { -1, -1 };
        }

        return new int[] {
                jogoService.getLinhaCapturaObrigatoria(),
                jogoService.getColunaCapturaObrigatoria()
        };
    }

    // =====================================================
    // REINICIAR JOGO
    // =====================================================

    @PostMapping("/reiniciar")
    public int[][] reiniciar() {

        jogoService.reiniciarJogo();

        return jogoService
                .getTabuleiro()
                .getTabuleiro();
    }
}