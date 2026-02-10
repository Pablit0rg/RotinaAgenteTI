package com.example.demo;

import com.example.demo.service.Rotina;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Bean: O Spring gerencia esse método
    // CommandLineRunner: Executa logo após o servidor subir
    @Bean
    public CommandLineRunner executarRotina(Rotina rotina) {
        return args -> {
            System.out.println("==========================================");
            System.out.println("🚀 SERVIDOR INICIADO - EXECUTANDO ROTINA");
            System.out.println("==========================================");
            
            // Passo 1: Acordar
            rotina.acordar(6);
            
            System.out.println("\n--- MONITORAMENTO DE TRÁFEGO ---");
            
            // Passo 2: Monitorar Ônibus
            rotina.verificarOnibus("Interbairros II"); // Teste negativo
            rotina.verificarOnibus("Vila Palmital 010"); // Teste positivo (Vai gerar SQL)
            
            System.out.println("==========================================");
            System.out.println("🏁 FIM DO PROCESSO DIÁRIO");
            System.out.println("==========================================");
        };
    }
}