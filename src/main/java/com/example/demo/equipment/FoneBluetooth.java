package com.example.demo.equipment;

public class FoneBluetooth {
    private int nivelBateria;
    private boolean conectado;
    private final int TAXA_DESCARGA = 10;

    public FoneBluetooth(int cargaInicial) {
        this.nivelBateria = Math.min(100, Math.max(0, cargaInicial));
        this.conectado = this.nivelBateria > 0;
    }

    public void consumirBateria() {
        if (conectado) {
            this.nivelBateria -= TAXA_DESCARGA;
            if (this.nivelBateria <= 0) {
                this.nivelBateria = 0;
                this.conectado = false;
                System.out.println("🪫 Bateria Esgotada! O som parou.");
            } else {
                System.out.println("🔋 Bateria Fones: " + this.nivelBateria + "%");
            }
        }
    }

    public boolean isTocando() {
        return conectado && nivelBateria > 0;
    }

    public String getStatus() {
        return isTocando() ? "🎵 Tocando Trap/Hip-Hop (Lofi Focus)" : "🔇 Silêncio (Sem Bateria)";
    }
    
    public int getNivelBateria() {
        return nivelBateria;
    }
}