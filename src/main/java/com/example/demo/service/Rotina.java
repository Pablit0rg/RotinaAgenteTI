package com.example.demo.service;

import com.example.demo.model.Assento;
import com.example.demo.model.HistoricoViagem; // Import novo
import com.example.demo.repository.HistoricoRepository; // Import novo
import com.example.demo.equipment.FoneBluetooth;
import org.springframework.stereotype.Service; // Import novo
import java.util.Optional;
import java.util.Random;

@Service // Agora o Spring gerencia essa classe!
public class Rotina {
    
    // Injeção de Dependência (O Spring vai preencher isso pra nós)
    private final HistoricoRepository repository;

    private int horaDespertar = 6;
    private boolean alarmeAtivo = true;
    private final String MEU_ONIBUS = "Vila Palmital 010";

    // Constantes (Mantidas)
    private final int LIMITE_CASA = 40; 
    private final int TEMPO_CAMINHADA_PONTO = 10; 
    private final int HORARIO_ONIBUS = 54; 
    private final int DURACAO_VIAGEM = 60; 
    private final int TEMPO_VESTIR = 5;
    private final int TEMPO_CAFE = 15;
    private final int TEMPO_DENTES = 5;
    private final int TEMPO_TENIS = 5;
    private final int TEMPO_MOCHILA = 5;
    private final int TEMPO_FECHAR_CASA = 2;
    private final int TEMPO_CAFE_BONUS = 3;

    private final double CHANCE_INICIAL = 10.0;
    private final double CHANCE_FINAL = 100.0;
    private final Random random = new Random();

    private FoneBluetooth meusFones; 

    // Construtor com Injeção (O Spring chama esse cara e passa o repository)
    public Rotina(HistoricoRepository repository) {
        this.repository = repository; // Guardamos a ferramenta de banco
        
        int cargaSimulada = random.nextInt(50, 101); 
        this.meusFones = new FoneBluetooth(cargaSimulada);
        System.out.println("🎧 Sistema de Áudio inicializado. Carga: " + cargaSimulada + "%");
    }

    public void acordar(int horaAtual) {
        if (horaAtual == horaDespertar && alarmeAtivo) {
            System.out.println("⏰ 06:00! Boot do Sistema.");
            encerrarAlarme();
            
            int tempoGastoEmCasa = calcularTempoCasa();
            
            if (tempoGastoEmCasa <= LIMITE_CASA) {
                System.out.println("🔒 Portão trancado às 06:" + tempoGastoEmCasa);
                irParaOPonto(tempoGastoEmCasa);
            } else {
                System.out.println("🚨 CRITICAL ERROR: Latência alta na saída!");
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

        System.out.println("🚶 Deslocamento para o ponto...");
        System.out.println("📍 Chegada ao ponto: 06:" + horaChegadaPonto);
        
        if (tempoEspera > 0) {
            System.out.println("⏳ Buffer de espera: " + tempoEspera + " min.");
        }
    }

    public void verificarOnibus(String linhaQueParou) {
        if (linhaQueParou.equals(MEU_ONIBUS)) { 
            System.out.println("🚌 Handshake: O " + linhaQueParou + " chegou!");
            iniciarTrajeto(); 
        } else {
            System.out.println("❌ Ignorando pacote: " + linhaQueParou);
        }
    }

    public void iniciarTrajeto() {
        System.out.println("💳 Embarque autorizado. Iniciando processamento de viagem (" + DURACAO_VIAGEM + " min).");
        System.out.println("🎧 Status Inicial: " + meusFones.getStatus());
        
        boolean sentado = false;

        for (int minutoAtual = 0; minutoAtual <= DURACAO_VIAGEM; minutoAtual += 5) {
            
            if (minutoAtual > 0 && minutoAtual % 10 == 0) {
                meusFones.consumirBateria();
            }

            if (!sentado) {
                Optional<Assento> assento = escanearAmbiente(minutoAtual);
                if (assento.isPresent()) {
                    System.out.println("⏱️ Minuto " + minutoAtual + ": 💺 Recurso alocado! Viajando sentado.");
                    sentado = true;
                }
            }

            if (minutoAtual % 10 == 0) {
                String modo = sentado ? "Sentado" : "Em Pé";
                String audio = meusFones.isTocando() ? "ON" : "OFF";
                System.out.println(String.format("   ↳ [%d min] Modo: %s | Áudio: %s", minutoAtual, modo, audio));
            }
        }
        
        // --- PERSISTÊNCIA NO BANCO (NOVO!) ---
        System.out.println("🏁 Chegada ao Terminal Guadalupe.");
        String statusFinal = sentado ? "SENTADO" : "EM PE";
        int bateriaFinal = meusFones.getNivelBateria();
        
        System.out.println("💾 Salvando log no Banco de Dados...");
        
        // Cria o objeto para salvar
        HistoricoViagem log = new HistoricoViagem(statusFinal, bateriaFinal);
        
        // Manda o repositório salvar (INSERT INTO TB_HISTORICO...)
        repository.save(log);
        
        System.out.println("✅ Log salvo com sucesso: " + log);
    }

    private double calcularChanceAtual(int minutoAtual) {
        double taxaCrescimento = (CHANCE_FINAL - CHANCE_INICIAL) / DURACAO_VIAGEM;
        return Math.min(CHANCE_INICIAL + (taxaCrescimento * minutoAtual), 100.0);
    }

    private Optional<Assento> escanearAmbiente(int minutoAtual) {
        if (random.nextDouble() * 100 < calcularChanceAtual(minutoAtual)) {
            return Optional.of(new Assento("UNICO", false, true));
        }
        return Optional.empty();
    }

    private void encerrarAlarme() {
        this.alarmeAtivo = false;
    }
}