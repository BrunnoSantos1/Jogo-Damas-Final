package com.damas.controller;

import com.damas.dto.MovimentoDTO;
import com.damas.model.Tabuleiro;
import com.damas.service.HistoricoService;
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

    @Autowired
    private HistoricoService historicoService;

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

        System.out.println("================================");
        System.out.println("JOGADA RECEBIDA PELO CONTROLLER");

        System.out.println(
                "Origem: "
                        + movimento.getOrigemLinha()
                        + ","
                        + movimento.getOrigemColuna());

        System.out.println(
                "Destino: "
                        + movimento.getDestinoLinha()
                        + ","
                        + movimento.getDestinoColuna());

        System.out.println(
                "Captura obrigatoria: "
                        + jogoService.getLinhaCapturaObrigatoria()
                        + ","
                        + jogoService.getColunaCapturaObrigatoria());

        System.out.println("================================");

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

        System.out.println("================================");
        System.out.println("JOGADA DO USUARIO");
        System.out.println(
                "Origem: "
                        + movimento.getOrigemLinha()
                        + ","
                        + movimento.getOrigemColuna());

        System.out.println(
                "Destino: "
                        + movimento.getDestinoLinha()
                        + ","
                        + movimento.getDestinoColuna());
        System.out.println("================================");

        boolean capturaEmSequencia = jogoService.getLinhaCapturaObrigatoria() != null;

        boolean valido = movimentoService.movimentoValido(
                tabuleiro,
                movimento.getOrigemLinha(),
                movimento.getOrigemColuna(),
                movimento.getDestinoLinha(),
                movimento.getDestinoColuna(),
                jogoService.getJogadorAtual(),
                capturaEmSequencia);

        if (!valido) {

            System.out.println(
                    "MOVIMENTO REJEITADO");

            return tabuleiro.getTabuleiro();
        }

        historicoService.salvarEstado(tabuleiro);

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

        System.out.println(
                "CAPTURA OBRIGATORIA -> "
                        + jogoService.getLinhaCapturaObrigatoria()
                        + ","
                        + jogoService.getColunaCapturaObrigatoria());

        if (jogoService.getLinhaCapturaObrigatoria() == null) {

            return new int[] { -1, -1 };
        }

        return new int[] {
                jogoService.getLinhaCapturaObrigatoria(),
                jogoService.getColunaCapturaObrigatoria()
        };
    }

    // =====================================================
    // DESFAZER JOGADA
    // =====================================================

    @PostMapping("/desfazer")
    public int[][] desfazer() {

        Tabuleiro tabuleiro = jogoService.getTabuleiro();

        // 1. Restaura a matriz do tabuleiro para a última foto
        historicoService.desfazer(tabuleiro);

        // 2. Volta o turno para o outro jogador (já que desfez a jogada)
        jogoService.trocarTurno();

        // 3. Limpa qualquer captura obrigatória que tenha ficado presa na jogada desfeita
        jogoService.limparCapturaObrigatoria();

        System.out.println("JOGADA DESFEITA COM SUCESSO");

        return tabuleiro.getTabuleiro();
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