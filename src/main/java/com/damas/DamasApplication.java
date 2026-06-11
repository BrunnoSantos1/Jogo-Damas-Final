package com.damas;

import com.damas.view.TelaInicial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class DamasApplication {

	public static void main(String[] args) {

		// INICIA SPRING BOOT
		SpringApplication.run(
				DamasApplication.class,
				args);

		// INICIA SWING
		SwingUtilities.invokeLater(() -> {

			new TelaInicial();
		});
	}
}
