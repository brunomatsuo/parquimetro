package com.parquimetro.fiap.dominio.controller;

import com.parquimetro.fiap.dominio.dto.enums.Status;
import com.parquimetro.fiap.dominio.dto.VagaDTO;
import com.parquimetro.fiap.dominio.services.VagaService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Validator;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estaciona")
public class EstacionaController {

    @Autowired
    private VagaService service;

    @Autowired
    private Validator validator;

    @GetMapping
    public ResponseEntity<List<VagaDTO>> getVagas(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{placa}")
    public ResponseEntity findByPlaca(@PathVariable String placa){
        VagaDTO vagaDTO = service.findByPlaca(placa);
        if (vagaDTO.getPlaca() != null){
            return ResponseEntity.ok(vagaDTO);
        }
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{placa}")
    public ResponseEntity<Status> getStatus(@PathVariable String placa) {
        return ResponseEntity.ok(service.getStatus(placa));
    }

    @PostMapping
    public ResponseEntity registrarVeiculoVaga(@RequestBody VagaDTO vagaDTO){
        Map<Path, String> violacoesToMap = validar(vagaDTO);

        if(!violacoesToMap.isEmpty()){
            return ResponseEntity.badRequest().body(violacoesToMap);
        }

        VagaDTO dto = service.insert(vagaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((dto.getId())).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateVeiculoVaga(@PathVariable Long id, @RequestBody VagaDTO vagaDTO){
        Map<Path, String> violacoesToMap = validar(vagaDTO);

        if(!violacoesToMap.isEmpty()){
            return ResponseEntity.badRequest().body(violacoesToMap);
        }

        return ResponseEntity.ok(service.update(id, vagaDTO));
    }

    private <T> Map<Path, String> validar(T dto){
        Set<ConstraintViolation<T>> violacoes = validator.validate(dto);
        Map<Path, String> violacoesToMap = violacoes.stream().collect(Collectors.toMap(
                violacao -> violacao.getPropertyPath(), violacao -> violacao.getMessage()
        ));
        return violacoesToMap;
    }

}
