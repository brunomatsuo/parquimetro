package com.parquimetro.fiap.dominio.controller;

import com.parquimetro.fiap.dominio.dto.enums.Status;
import com.parquimetro.fiap.dominio.dto.ParquimetroDTO;
import com.parquimetro.fiap.dominio.services.ParquimetroService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private ParquimetroService service;

    @Autowired
    private Validator validator;

    @GetMapping
    public ResponseEntity<Page<ParquimetroDTO>> getVagas(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                                                         @RequestParam(value = "quatidade", defaultValue = "30") Integer quantidade,
                                                         @RequestParam(value = "direcao", defaultValue = "DESC") String direcao,
                                                         @RequestParam(value = "ordenacao", defaultValue = "id") String ordenacao)
    {
        PageRequest pageRequest = PageRequest.of(pagina, quantidade, Sort.Direction.valueOf(direcao), ordenacao);
        return ResponseEntity.ok(service.findAll(pageRequest));
    }

    @GetMapping("/{placa}")
    public ResponseEntity findByPlaca(@PathVariable String placa){
        return ResponseEntity.ok(service.getByPlaca(placa));
    }

    @GetMapping("/status/{placa}")
    public ResponseEntity<Status> getStatus(@PathVariable String placa) {
        return ResponseEntity.ok(service.getStatus(placa));
    }

    @PostMapping
    public ResponseEntity registrarVeiculoVaga(@RequestBody ParquimetroDTO parquimetroDTO){
        Map<Path, String> violacoesToMap = validar(parquimetroDTO);

        if(!violacoesToMap.isEmpty()){
            return ResponseEntity.badRequest().body(violacoesToMap);
        }

        ParquimetroDTO dto = service.insert(parquimetroDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((dto.getId())).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping
    public ResponseEntity updateVeiculoVaga(@RequestBody ParquimetroDTO parquimetroDTO){
        Map<Path, String> violacoesToMap = validar(parquimetroDTO);

        if(!violacoesToMap.isEmpty()){
            return ResponseEntity.badRequest().body(violacoesToMap);
        }

        return ResponseEntity.ok(service.update(parquimetroDTO));
    }

    private <T> Map<Path, String> validar(T dto){
        Set<ConstraintViolation<T>> violacoes = validator.validate(dto);
        Map<Path, String> violacoesToMap = violacoes.stream().collect(Collectors.toMap(
                violacao -> violacao.getPropertyPath(), violacao -> violacao.getMessage()
        ));
        return violacoesToMap;
    }

}
