package com.example.demo;

import java.util.Optional;
import java.util.Random;

/**
 * Sistema de Gestão de Rotina Diária (Agente de TI).
 * Versão 2.0: Implementação de Algoritmo de Alocação Dinâmica com Interpolação Linear.
 */
public class Rotina {
    
    // --- ATRIBUTOS DE ESTADO ---
    private int horaDespertar = 6;
    private boolean alarmeAtivo = true;
    private final String MEU_ONIBUS = "Vila Palmital 010";

    // Constantes de Tempo
    private final int LIMITE_CASA = 40; 
    private final int TEMPO_CAMINHADA_PONTO = 10; 
    private final int HORARIO_ONIBUS = 54; 
    private final int DURACAO_VIAGEM = 60; // Tempo total até o Terminal

    // Métricas de Preparação
    private final int TEMPO_VESTIR = 5;
    private final int TEMPO_CAFE = 15;
    private final int TEMPO_DENTES = 5;
    private final int TEMPO_TENIS = 5;
    private final int TEMPO_MOCHILA = 5;
    private final int TEMPO_FECHAR_CASA = 2;
    private final int TEMPO_CAFE_BONUS = 3;

    // --- VARIÁVEIS PROBABILÍSTICAS ---
    private final double CHANCE_INICIAL = 10.0;
    private final double CHANCE_FINAL = 100.0;
    private final Random random = new Random();

    // RECORD (Estrutura de Dados Imutável)
    private record Assento(String tipo, boolean ehPreferencial, boolean estaLivre) {}

    // --- MÉTODOS DE FLUXO ---

    public void acordar(int horaAtual) {
        if (horaAtual == horaDespertar && alarmeAtivo) {
            System.out.println("⏰ 06:00! Sistema iniciado.");
            encerrarAlarme();
            
            int tempoGastoEmCasa = calcularTempoCasa();
            
            if (tempoGastoEmCasa <= LIMITE_CASA) {
                System.out.println("🔒 Portão trancado às 06:" + tempoGastoEmCasa);
                irParaOPonto(tempoGastoEmCasa);
            } else {
                System.out.println("🚨 CRITICAL ERROR: Saída atrasada!");
            }
        }
    }

    private int calcularTempoCasa() {
        return TEMPO_VESTIR + TEMPO_CAFE + TEMPO_DENTES + 
               TEMPO_TENIS + TEMPO_MOCHILA + TEMPO_FECHAR_CASA + TEMPO_CAFE_BONUS;
    }

    public void irParaOPonto(int minutoSaida) {
        int horaChegadaPonto = minutoSaida + TEMPO_CAMINHADA_PONTO;
        int tempoEspera = HORARIO_ONIBUS - horaChegadaPonto;

        System.out.println("🚶 Caminhando para o ponto...");
        System.out.println("📍 Chegada ao ponto: 06:" + horaChegadaPonto);
        
        if (tempoEspera > 0) {
            System.out.println("⏳ Buffer de espera: " + tempoEspera + " min. Revisando Java Records.");
        }
    }

    public void verificarOnibus(String linhaQueParou) {
        if (linhaQueParou.equals(MEU_ONIBUS)) { 
            System.out.println("🚌 Handshake: O " + linhaQueParou + " chegou!");
            iniciarTrajeto(); // Mudamos aqui para iniciar o loop de viagem
        } else {
            System.out.println("❌ Ignorando " + linhaQueParou);
        }
    }

    // --- LÓGICA CORE: ALOCAÇÃO DINÂMICA (Linear Regression Logic) ---

    /**
     * Simula o trajeto minuto a minuto, recalculando as chances de sentar.
     */
    public void iniciarTrajeto() {
        System.out.println("💳 Embarque autorizado. Iniciando trajeto de " + DURACAO_VIAGEM + " minutos.");
        
        boolean sentado = false;

        // Loop de Simulação Temporal (O "Beat" da viagem)
        for (int minutoAtual = 0; minutoAtual <= DURACAO_VIAGEM; minutoAtual += 5) { // Check a cada 5 min para não floodar o log
            
            if (!sentado) {
                // Tenta encontrar assento com a probabilidade atualizada
                Optional<Assento> assento = escanearAmbiente(minutoAtual);
                
                if (assento.isPresent()) {
                    System.out.println("⏱️ Minuto " + minutoAtual + ": 💺 Oportunidade encontrada! Sentando.");
                    sentado = true;
                    viajarSentado();
                } else {
                    System.out.println("⏱️ Minuto " + minutoAtual + ": 🧍 Sem vagas (Chance atual: " + String.format("%.1f", calcularChanceAtual(minutoAtual)) + "%). Segue o jogo.");
                    viajarEmPe();
                }
            } else {
                // Já está sentado, apenas curte a viagem
                // System.out.println("... (Viajando confortavelmente) ..."); 
            }
        }
        
        System.out.println("🏁 Chegada ao Terminal Guadalupe. Fim da execução.");
    }

    /**
     * Calcula a probabilidade baseada no tempo decorrido.
     * Fórmula da Reta: y = ax + b
     * Onde: b = Chance Inicial (10), a = Taxa de Crescimento
     */
    private double calcularChanceAtual(int minutoAtual) {
        // Cálculo da Taxa de Crescimento (Slope)
        // Precisamos crescer 90% (100 - 10) ao longo de 60 minutos.
        // Taxa = 90 / 60 = 1.5% por minuto.
        double taxaCrescimento = (CHANCE_FINAL - CHANCE_INICIAL) / DURACAO_VIAGEM;
        
        double chanceAtual = CHANCE_INICIAL + (taxaCrescimento * minutoAtual);
        
        // Math.min garante que nunca passe de 100% (Defensive Programming)
        return Math.min(chanceAtual, 100.0);
    }

    private Optional<Assento> escanearAmbiente(int minutoAtual) {
        double chanceReal = calcularChanceAtual(minutoAtual);
        
        // Gera um número double entre 0.0 e 100.0
        double sorteio = random.nextDouble() * 100;

        // Se o número sorteado for menor que a chance atual, BINGO!
        if (sorteio < chanceReal) {
            return Optional.of(new Assento("UNICO", false, true));
        }
        return Optional.empty();
    }

    private void viajarSentado() {
        // Lógica simplificada para o log não ficar gigante
    }

    private void viajarEmPe() {
        // Lógica simplificada
    }

    private void encerrarAlarme() {
        this.alarmeAtivo = false;
    }

    public static void main(String[] args) {
        Rotina minhaRotina = new Rotina();
        System.out.println(">>> LOG DIÁRIO >>>");
        minhaRotina.acordar(6);
        System.out.println("\n--- No Ponto ---");
        minhaRotina.verificarOnibus("Vila Palmital 010");
    }
}