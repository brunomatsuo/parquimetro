package com.parquimetro.fiap.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class ParquimetroDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    @NotBlank(message = "A placa é obrigatória e não pode ser nula.")
    private String placa;
    @JsonProperty
    private LocalDateTime entrada;
    @JsonProperty
    private LocalDateTime saidaAte;
    @JsonProperty
    @Positive(message = "Insira um número de tickets válido.")
    private int tickets;

    public ParquimetroDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaidaAte() {
        return saidaAte;
    }

    public void setSaidaAte(LocalDateTime saidaAte) {
        this.saidaAte = saidaAte;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }
}
