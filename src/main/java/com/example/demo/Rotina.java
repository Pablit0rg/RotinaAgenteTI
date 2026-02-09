package com.example.demo;

public class Rotina {
    // Atributos de estado (Encapsulamento)
    private int horaDespertar = 6;
    private boolean alarmeAtivo = true;
    private final String MEU_ONIBUS = "Vila Palmital 010";

    // Constantes de tempo (Imutabilidade - Foco na Banca)
    private final int LIMITE_CASA = 40; 
    private final int TEMPO_CAMINHADA_PONTO = 10; 
    private final int HORARIO_ONIBUS = 54; 

    // Detalhamento da preparação (Steps do Pipeline)
    private final int TEMPO_VESTIR = 5;
    private final int TEMPO_CAFE = 15;
    private final int TEMPO_DENTES = 5;
    private final int TEMPO_TENIS = 5;
    private final int TEMPO_MOCHILA = 5;
    private final int TEMPO_FECHAR_CASA = 2;
    private final int TEMPO_CAFE_BONUS = 3;

    public void acordar(int horaAtual) {
        if (horaAtual == horaDespertar && alarmeAtivo) {
            System.out.println("⏰ 06:00! Sistema iniciado.");
            encerrarAlarme();
            
            int tempoGastoEmCasa = calcularTempoCasa();
            
            // Validação de Saída (Caminho Crítico)
            if (tempoGastoEmCasa <= LIMITE_CASA) {
                String msg = (tempoGastoEmCasa == LIMITE_CASA) ? "em ponto. Eficiência máxima." : "adiantado.";
                System.out.println("🔒 Portão trancado às 06:" + tempoGastoEmCasa + " " + msg);
                irParaOPonto(tempoGastoEmCasa);
            } else {
                System.out.println("🚨 CRITICAL ERROR: Saída atrasada às 06:" + tempoGastoEmCasa);
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
            System.out.println("⏳ Buffer de segurança: " + tempoEspera + " minutos.");
            System.out.println("📱 Revisando Java e Estatística até o " + MEU_ONIBUS + " chegar...");
        }
    }

    public void verificarOnibus(String linhaQueParou) {
        // Importante: .equals() compara o conteúdo, == compararia o endereço de memória
        if (linhaQueParou.equals(MEU_ONIBUS)) { 
            System.out.println("🚌 O " + linhaQueParou + " chegou! Partiu.");
            embarcar();
        } else {
            System.out.println("❌ Passou o " + linhaQueParou + ". Ignorando linha incorreta...");
        }
    }

    public void embarcar() {
        System.out.println("💳 Cartão passado. Transação autorizada via NFC.");
        System.out.println("🎯 Objetivo: Chegar ao Banco do Brasil.");
    }

    public void encerrarAlarme() {
        this.alarmeAtivo = false;
    }

    public static void main(String[] args) {
        Rotina minhaRotina = new Rotina();
        
        // Fase 1: Manhã em casa e deslocamento
        minhaRotina.acordar(6);
        
        // Fase 2: Seleção de pacotes (linhas de ônibus) no ponto
        System.out.println("\n--- Monitorando Tráfego no Ponto ---");
        minhaRotina.verificarOnibus("Interbairros II"); 
        minhaRotina.verificarOnibus("Vila Palmital 010"); 
    }
}