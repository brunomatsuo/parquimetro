    package com.parquimetro.fiap.dominio.services;

    import com.parquimetro.fiap.dominio.dto.enums.Status;
    import com.parquimetro.fiap.dominio.dto.ParquimetroDTO;
    import com.parquimetro.fiap.dominio.entities.Parquimetro;
    import com.parquimetro.fiap.dominio.repository.IParquimetroRepository;
    import com.parquimetro.fiap.exception.service.ControllerNotFoundException;
    import jakarta.persistence.EntityNotFoundException;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
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

        private static final int MINUTOS_ACRESCIMO = 30;

        public Page<ParquimetroDTO> findAll(Pageable pageable) {
            List<ParquimetroDTO> dtos = new ArrayList<>();
            Page<Parquimetro> list = repository.findAll(pageable);
            return list.map(this::mapperEntityToDto);
        }

        public ParquimetroDTO getByPlaca(String placa) {
            return mapperEntityToDto(findByPlaca(placa));
        }

            public Parquimetro findByPlaca(String placa) {
                    return repository.findByPlacaOrderByIdDesc(placa)
                       .stream().findFirst()
                       .orElseThrow(() -> {
                            throw new ControllerNotFoundException("Veículo não encontrado.");
                       });
            }

        public ParquimetroDTO insert(ParquimetroDTO parquimetroDTO){
            try {
                Parquimetro parquimetro = new Parquimetro();
                parquimetro.setPlaca(parquimetroDTO.getPlaca());
                parquimetro.setEntrada(LocalDateTime.now());
                parquimetro.setSaidaAte(LocalDateTime.now().plusMinutes(parquimetroDTO.getTickets() * MINUTOS_ACRESCIMO));
                repository.save(parquimetro);

                return mapperEntityToDto(parquimetro);
            }
            catch (Exception e){
                throw new ControllerNotFoundException("Erro ao cadastrar veículo na vaga.");
            }
        }

        public ParquimetroDTO update(ParquimetroDTO parquimetroDTO){
            Parquimetro parquimetro = findByPlaca(parquimetroDTO.getPlaca());

            LocalDateTime dataAtual = LocalDateTime.now();
            LocalDateTime dataEntrada = parquimetro.getEntrada();

            if (!verificaDataValida(dataAtual, dataEntrada)) {
                throw new ControllerNotFoundException("Veículo não encontrado na vaga na data de hoje.");
            }

            int tickets = parquimetroDTO.getTickets();
            LocalDateTime dataSaida = parquimetro.getSaidaAte();
            parquimetro.setSaidaAte(calcularNovaSaida(dataAtual, dataSaida, tickets));

            repository.save(parquimetro);

            return mapperEntityToDto(parquimetro);

            }

        public Status getStatus(String placa){

            try {
                Parquimetro parquimetro = findByPlaca(placa);
                Long durationMinutes = getDurationBetween(LocalDateTime.now(), parquimetro.getSaidaAte());

                Status status = durationMinutes <= 0 ? Status.EXPIRADO : Status.ATIVO;
                return status;

            } catch (EntityNotFoundException e) {
                return Status.NAO_ENCONTRADO;
            }
        }

        private long getDurationBetween(LocalDateTime dataAtual, LocalDateTime dataSaida){
            return Duration.between(dataAtual, dataSaida).toMinutes();

        }

        private Boolean verificaDataValida(LocalDateTime dataAtual, LocalDateTime dataEntrada){
            return dataEntrada.toLocalDate().isEqual(dataAtual.toLocalDate());
        }

        private LocalDateTime calcularNovaSaida(LocalDateTime dataAtual, LocalDateTime dataSaida, int tickets)
        {
            int minutosExtra = tickets * MINUTOS_ACRESCIMO;
            return dataSaida.isAfter(dataAtual) ? dataSaida.plusMinutes(minutosExtra) : dataAtual.plusMinutes(minutosExtra);
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
