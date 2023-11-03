package com.parquimetro.fiap.dominio.repository;

import com.parquimetro.fiap.dominio.entities.Parquimetro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IParquimetroRepository extends JpaRepository<Parquimetro, Long> {
    List<Parquimetro> findByPlacaOrderByIdDesc(String placa);
}
