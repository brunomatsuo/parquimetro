package com.parquimetro.fiap.dominio.repository;

import com.parquimetro.fiap.dominio.entities.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByPlacaOrderByIdDesc(String placa);
}
