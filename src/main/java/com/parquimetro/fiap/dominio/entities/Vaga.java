package com.parquimetro.fiap.dominio.entities;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_vagas")
@EnableAutoConfiguration
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saidaAte;

    public Vaga(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vaga vaga = (Vaga) o;
        return id.equals(vaga.id) && placa.equals(vaga.placa) && entrada.equals(vaga.entrada) && saidaAte.equals(vaga.saidaAte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa, entrada, saidaAte);
    }
}
