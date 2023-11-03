package com.parquimetro.fiap.dominio.services;

import com.parquimetro.fiap.dominio.dto.enums.Status;
import com.parquimetro.fiap.dominio.dto.ParquimetroDTO;
import com.parquimetro.fiap.dominio.entities.Parquimetro;
import com.parquimetro.fiap.dominio.repository.IParquimetroRepository;
import com.parquimetro.fiap.exception.service.ControllerNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParquimetroService {

    @Autowired
    private IParquimetroRepository repository;

    public List<ParquimetroDTO> findAll() {
        List<ParquimetroDTO> dtos = new ArrayList<>();
        List<Parquimetro> list = repository.findAll();
        list.forEach(parquimetro -> {
            dtos.add(mapperEntityToDto(parquimetro));
        });
        return dtos;
    }

    public ParquimetroDTO findByPlaca(String placa) {
        return mapperEntityToDto(repository.findByPlacaOrderByIdDesc(placa).stream().findFirst().orElse(new Parquimetro()));
    }

    public ParquimetroDTO insert(ParquimetroDTO parquimetroDTO){
        try {
            Parquimetro parquimetro = new Parquimetro();
            parquimetro.setPlaca(parquimetroDTO.getPlaca());
            parquimetro.setEntrada(LocalDateTime.now());
            //cada ticket adiciona 30 minutos na saída;
            parquimetro.setSaidaAte(LocalDateTime.now().plusMinutes(parquimetroDTO.getTicket() * 30));
            repository.save(parquimetro);

            return mapperEntityToDto(parquimetro);
        }
        catch (EntityNotFoundException e){
            throw new ControllerNotFoundException("Erro ao cadastrar veículo na vaga.");
        }
    }

    public ParquimetroDTO update(Long id, ParquimetroDTO parquimetroDTO){
        try {
            Parquimetro parquimetro = repository.findById(id).orElse(new Parquimetro());
            LocalDateTime dataAgora = LocalDateTime.now();
            int minutosExtra = parquimetroDTO.getTicket() * 30;

            if (parquimetro.getPlaca() != null) {
                if(parquimetro.getEntrada().toLocalDate().isEqual(dataAgora.toLocalDate())) {
                    parquimetro.setSaidaAte(parquimetro.getSaidaAte().isAfter(dataAgora) ?
                            parquimetro.getSaidaAte().plusMinutes(minutosExtra) : dataAgora.plusMinutes(minutosExtra));
                    repository.save(parquimetro);
                    return mapperEntityToDto(parquimetro);
                }
                else {
                    throw new ControllerNotFoundException("Veículo não encontrado na vaga na data de hoje. ");
                }
            }
            else
                throw new ControllerNotFoundException("Veículo não encontrado na vaga.");
        }
        catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Veículo não encontrado na vaga.");
        }
    }

    public Status getStatus(String placa){
        ParquimetroDTO parquimetroDTO = findByPlaca(placa);

        if(parquimetroDTO.getPlaca() == null)
        {
            return Status.NAO_ENCONTRADO;
        }

        Duration duration = Duration.between(LocalDateTime.now(), parquimetroDTO.getSaidaAte());
        double durationMinutes = duration.toMinutes();

        if(durationMinutes <= 0) {
            return Status.EXPIRADO;
        }
        else return Status.ATIVO;
    }

    private ParquimetroDTO mapperEntityToDto(Parquimetro parquimetro){
        ParquimetroDTO parquimetroDTO = new ParquimetroDTO();
        parquimetroDTO.setId(parquimetro.getId());
        parquimetroDTO.setPlaca(parquimetro.getPlaca());
        parquimetroDTO.setEntrada(parquimetro.getEntrada());
        parquimetroDTO.setSaidaAte(parquimetro.getSaidaAte());

        return parquimetroDTO;
    }
}
