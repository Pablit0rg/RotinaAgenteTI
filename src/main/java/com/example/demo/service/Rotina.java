package com.example.demo.service;

import com.example.demo.model.Assento;
import com.example.demo.model.HistoricoViagem;
import com.example.demo.repository.HistoricoRepository;
import com.example.demo.equipment.FoneBluetooth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Random;

@Service
public class Rotina {
    
    private final HistoricoRepository repository;

    // Lendo a URL do application.properties
    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    // Ferramentas para Web
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules(); // Cria o JSON

    private int horaDespertar = 6;
    private boolean alarmeAtivo = true;
    private final String MEU_ONIBUS = "Vila Palmital 010";

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

    public Rotina(HistoricoRepository repository) {
        this.repository = repository;
        
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
        
        System.out.println("🏁 Chegada ao Terminal Guadalupe.");
        String statusFinal = sentado ? "SENTADO" : "EM PE";
        int bateriaFinal = meusFones.getNivelBateria();
        
        System.out.println("💾 Salvando log no Banco de Dados...");
        HistoricoViagem log = new HistoricoViagem(statusFinal, bateriaFinal);
        repository.save(log);
        System.out.println("✅ Log salvo: " + log);

        // --- DISPARO PARA O N8N ---
        enviarRelatorioN8N(log);
    }

    private void enviarRelatorioN8N(HistoricoViagem log) {
        try {
            System.out.println("📡 Enviando dados para n8n...");
            
            // 1. Converte Objeto Java -> JSON String
            String jsonPayload = objectMapper.writeValueAsString(log);
            System.out.println("📦 Payload JSON: " + jsonPayload);

            // 2. Cria a Requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(n8nWebhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // 3. Envia e espera a resposta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("🚀 Sucesso! n8n recebeu os dados.");
            } else {
                System.out.println("⚠️ n8n retornou erro: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao conectar com n8n: " + e.getMessage());
            // Não paramos o programa, apenas logamos o erro (Resiliência)
        }
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