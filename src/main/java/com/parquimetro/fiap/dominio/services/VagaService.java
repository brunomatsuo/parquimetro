package com.parquimetro.fiap.dominio.services;

import com.parquimetro.fiap.dominio.dto.enums.Status;
import com.parquimetro.fiap.dominio.dto.VagaDTO;
import com.parquimetro.fiap.dominio.entities.Vaga;
import com.parquimetro.fiap.dominio.repository.IVagaRepository;
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
public class VagaService {

    @Autowired
    private IVagaRepository repository;

    public List<VagaDTO> findAll() {
        List<VagaDTO> dtos = new ArrayList<>();
        List<Vaga> list = repository.findAll();
        list.forEach(vaga -> {
            dtos.add(mapperEntityToDto(vaga));
        });
        return dtos;
    }

    public VagaDTO findByPlaca(String placa) {
        return mapperEntityToDto(repository.findByPlacaOrderByIdDesc(placa).stream().findFirst().orElse(new Vaga()));
    }

    public VagaDTO insert(VagaDTO vagaDTO){
        try {
            Vaga vaga = new Vaga();
            vaga.setPlaca(vagaDTO.getPlaca());
            vaga.setEntrada(LocalDateTime.now());
            //cada ticket adiciona 30 minutos na saída;
            vaga.setSaidaAte(LocalDateTime.now().plusMinutes(vagaDTO.getTicket() * 30));
            repository.save(vaga);

            return mapperEntityToDto(vaga);
        }
        catch (EntityNotFoundException e){
            throw new ControllerNotFoundException("Erro ao cadastrar veículo na vaga.");
        }
    }

    public VagaDTO update(Long id, VagaDTO vagaDTO){
        try{
            Vaga vaga = repository.getReferenceById(id);

            vaga.setSaidaAte(LocalDateTime.now().plusMinutes(vagaDTO.getTicket() * 30));
            repository.save(vaga);

            return mapperEntityToDto(vaga);
        }
        catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Veículo não encontrado na vaga.");
        }
    }

    public Status getStatus(String placa){
        VagaDTO vagaDTO = findByPlaca(placa);

        if(vagaDTO.getPlaca() == null)
        {
            return Status.NAO_ENCONTRADO;
        }

        Duration duration = Duration.between(LocalDateTime.now(), vagaDTO.getSaidaAte());
        double durationMinutes = duration.toMinutes();

        if(durationMinutes <= 0) {
            return Status.EXPIRADO;
        }
        else return Status.ATIVO;
    }

    private VagaDTO mapperEntityToDto(Vaga vaga){
        VagaDTO vagaDTO = new VagaDTO();
        vagaDTO.setId(vaga.getId());
        vagaDTO.setPlaca(vaga.getPlaca());
        vagaDTO.setEntrada(vaga.getEntrada());
        vagaDTO.setSaidaAte(vaga.getSaidaAte());

        return vagaDTO;
    }
}
