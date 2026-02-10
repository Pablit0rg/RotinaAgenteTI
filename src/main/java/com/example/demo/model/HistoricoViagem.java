package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class HistoricoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;
    private String statusAssento;
    private int bateriaFinal;
    private boolean incidenteSeguranca;

    public HistoricoViagem() {}

    public HistoricoViagem(String statusAssento, int bateriaFinal) {
        this.dataHora = LocalDateTime.now();
        this.statusAssento = statusAssento;
        this.bateriaFinal = bateriaFinal;
        this.incidenteSeguranca = false;
    }

    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getStatusAssento() { return statusAssento; }
    public int getBateriaFinal() { return bateriaFinal; }
    public boolean isIncidenteSeguranca() { return incidenteSeguranca; }

    // --- AQUI ESTÁ A CORREÇÃO: APENAS UM TOSTRING ---
    @Override
    public String toString() {
        return "Log SQL [id=" + id + ", data=" + dataHora + 
               ", status=" + statusAssento + ", bat=" + bateriaFinal + 
               "%, incidente=" + incidenteSeguranca + "]";
    }
}