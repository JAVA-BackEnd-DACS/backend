package com.dacs.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.PersonalDto;
import com.dacs.backend.dto.ServicioDto;
import com.dacs.backend.mapper.CirugiaMapper;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.EquipoMedico;
import com.dacs.backend.model.entity.Personal;
import com.dacs.backend.model.entity.Servicio;
import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.EquipoMedicoRepository;
import com.dacs.backend.model.repository.PacienteRepository;
import com.dacs.backend.model.repository.PersonalRepository;
import com.dacs.backend.model.repository.ServicioRepository;

@Service
public class CirugiaServiceImpl implements CirugiaService {

    @Autowired
    CirugiaRepository cirugiaRepository;
    @Autowired
    private CirugiaMapper cirugiaMapper;

    @Autowired
    private EquipoMedicoRepository equipoMedicoRepository;
    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TurnoService turnoService;

    @Override
    public Optional<Cirugia> getById(Long id) {
        return cirugiaRepository.findById(id);
    }

    @Override
    @Transactional
    public CirugiaDTO.Response createCirugia(CirugiaDTO.Create request) {
        // mapear request -> entidad (resuelve relaciones dentro del mapper)
        Long servicioId = request.getServicioId();
        Long quirofanoId = request.getQuirofanoId();
        LocalDateTime fechaHoraInicio = request.getFechaHoraInicio();
        LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(servicioRepository.findById(servicioId).get().getDuracionMinutos()); // suposición
        
      //  System.out.println("FechaHoraInicio: " + fechaHoraFin);
        System.out.println("FechaHoraFin: " + fechaHoraFin);
        System.out.println("QuirofanoId: " + quirofanoId);
        System.out.println("fechaHoraInicioss: " + fechaHoraInicio);
        Boolean disponibilidad = turnoService.verificarDisponibilidadTurno(quirofanoId, fechaHoraInicio, fechaHoraFin);
        if (!disponibilidad) {  
            throw new IllegalArgumentException("No hay turnos disponibles para el quirófano en la fecha y hora solicitadas.");
        }
        
        Cirugia entity = cirugiaMapper.toEntity(request);
        Cirugia saved = cirugiaRepository.save(entity);
        
        turnoService.asignarTurno(saved.getId(), quirofanoId, fechaHoraInicio, fechaHoraFin);
        
        // mapear entidad -> response DTO
        return cirugiaMapper.toResponseDto(saved);
    }

    @Override
    public Cirugia save(Cirugia entity) {
        return cirugiaRepository.save(entity);
    }

    @Override
    public Boolean existById(Long id) {
        return cirugiaRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        Optional<Cirugia> cirugia = getById(id);
        cirugiaRepository.delete(cirugia.get());
    }

    @Override
    public Cirugia getBy(Map<String, Object> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CirugiaDTO.Response updateCirugia(Long id, CirugiaDTO.Update requestDto) {
        if (!cirugiaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cirugia no encontrada id=" + id);
        }
        Cirugia entity = cirugiaMapper.toEntity(requestDto);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad Cirugia no puede ser null");
        }
        return cirugiaMapper.toResponseDto(cirugiaRepository.save(entity));
    }

    @Override
    public List<Cirugia> getAll() {
        return cirugiaRepository.findAll();
    }

    @Override
    public List<MiembroEquipoMedicoDto.Response> getEquipoMedico(Long cirugiaId) {
        List<EquipoMedico> equipo = equipoMedicoRepository.findByCirugiaId(cirugiaId);

        List<Long> personalIds = equipo.stream()
                .map(EquipoMedico::getPersonal)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .map(Personal::getId)
                .collect(Collectors.toList());

        final Map<Long, Personal> personalMap;
        if (!personalIds.isEmpty()) {
            List<Personal> personals = personalRepository.findAllById(personalIds);
            personalMap = personals.stream().collect(Collectors.toMap(Personal::getId, p -> p));
        } else {
            personalMap = Map.of();
        }

        return equipo.stream().map(e -> {
            // mapear de forma explícita igual que en createEquipoMedico
            MiembroEquipoMedicoDto.Response dto = modelMapper.map(e, MiembroEquipoMedicoDto.Response.class);

            // asegurar campos clave
            dto.setRol(e.getRol());
            dto.setCirugiaId(e.getCirugia() != null ? e.getCirugia().getId() : null);
            dto.setFechaAsignacion(e.getFechaAsignacion());

            // rellenar info del personal a partir del personalMap (evita N+1)
            Personal p = (e.getPersonal() != null) ? personalMap.get(e.getPersonal().getId()) : null;
            if (p != null) {
                PersonalDto.Response pDto = modelMapper.map(p, PersonalDto.Response.class);
                dto.setPersonal(pDto);
            } else {
                dto.setPersonal(null);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MiembroEquipoMedicoDto.Response> saveEquipoMedico(Long cirugiaId,
            List<MiembroEquipoMedicoDto.Create> req) {
        // si la intención es reemplazar el equipo, eliminar los existentes primero
        equipoMedicoRepository.deleteByCirugiaId(cirugiaId);

        // validar existencia de la cirugía
        Cirugia cirugia = cirugiaRepository.findById(cirugiaId)
                .orElseThrow(() -> new IllegalArgumentException("Cirugia no encontrada id=" + cirugiaId));

        // obtener ids de personal desde la petición
        List<Long> personalIds = req.stream()
                .map(MiembroEquipoMedicoDto.Create::getPersonalId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // cargar todos los personals en una sola consulta
        final Map<Long, Personal> personalMap;
        if (!personalIds.isEmpty()) {
            List<Personal> personals = personalRepository.findAllById(personalIds);
            personalMap = personals.stream().collect(Collectors.toMap(Personal::getId, p -> p));
        } else {
            personalMap = Map.of();
        }

        List<EquipoMedico> toSave = construirEquipoMedico(req, cirugia, personalMap);
        if (toSave.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // guardar en batch y mapear resultado a DTOs
        List<EquipoMedico> saved = equipoMedicoRepository.saveAll(toSave);
        return mapearEquipoMedicoAResponse(saved);
    }

    private List<EquipoMedico> construirEquipoMedico(List<MiembroEquipoMedicoDto.Create> req, Cirugia cirugia,
            Map<Long, Personal> personalMap) {
        List<EquipoMedico> toSave = new java.util.ArrayList<>();
        for (MiembroEquipoMedicoDto.Create item : req) {
            Personal personal = personalMap.get(item.getPersonalId());
            if (personal == null) {
                throw new IllegalArgumentException("Personal no encontrado id=" + item.getPersonalId());
            }
            EquipoMedico e = new EquipoMedico();
            e.setFechaAsignacion(LocalDateTime.now());
            e.setCirugia(cirugia);
            e.setPersonal(personal);
            e.setRol(item.getRol());
            toSave.add(e);
        }
        return toSave;
    }

    @Override
    public PaginacionDto.Response<CirugiaDTO.Response> getCirugias(int pagina, int tamaño, LocalDate fechaInicio,
            LocalDate fechaFin) {
        System.err.println("asdasdasdsa: " + pagina + ", size: " + tamaño);
        Pageable pageable = PageRequest.of(pagina, tamaño);
        Page<Cirugia> p;

        if (fechaInicio != null && fechaFin != null) {
            p = cirugiaRepository.findByFechaHoraInicioBetween(
                    fechaInicio.atStartOfDay(),
                    fechaFin.atTime(23, 59, 59),
                    pageable);
        } else if (fechaInicio != null) {
            p = cirugiaRepository.findByFechaHoraInicioAfter(
                    fechaInicio.atStartOfDay(),
                    pageable);
        } else if (fechaFin != null) {
            p = cirugiaRepository.findByFechaHoraInicioBefore(
                    fechaFin.atTime(23, 59, 59),
                    pageable);
        } else {
            p = cirugiaRepository.findAll(pageable);
        }

        List<Cirugia> entidades = p.getContent();
        List<CirugiaDTO.Response> dtos = entidades.stream()
                .map(e -> modelMapper.map(e, CirugiaDTO.Response.class))
                .collect(Collectors.toList());

        mapearPacientes(entidades, dtos);
        mapearServicios(entidades, dtos);
        PaginacionDto.Response<CirugiaDTO.Response> resp = new PaginacionDto.Response<CirugiaDTO.Response>();
        resp.setContenido(dtos);
        resp.setPagina(p.getNumber());
        resp.setTamaño(p.getSize());
        resp.setTotalElementos(p.getTotalElements());
        resp.setTotalPaginas(p.getTotalPages());
        return resp;
    }

    private void mapearPacientes(List<Cirugia> entidades, List<CirugiaDTO.Response> dtos) {
        List<Long> pacienteIds = entidades.stream()
                .map(Cirugia::getPaciente)
                .filter(Objects::nonNull)
                .map(ent -> ent.getId())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!pacienteIds.isEmpty()) {
            List<com.dacs.backend.model.entity.Paciente> pacientes = pacienteRepository.findAllById(pacienteIds);
            Map<Long, com.dacs.backend.model.entity.Paciente> pacientesMap = pacientes.stream()
                    .collect(Collectors.toMap(com.dacs.backend.model.entity.Paciente::getId, pc -> pc));

            for (int i = 0; i < entidades.size(); i++) {
                var entidad = entidades.get(i);
                var dto = dtos.get(i);
                if (entidad.getPaciente() != null && entidad.getPaciente().getId() != null) {
                    var pacienteEntity = pacientesMap.get(entidad.getPaciente().getId());
                    if (pacienteEntity != null) {
                        var pacienteDto = modelMapper.map(pacienteEntity, PacienteDTO.Response.class);
                        dto.setPaciente(pacienteDto);
                    }
                }
            }
        }
    }

    private void mapearServicios(List<Cirugia> entidades, List<CirugiaDTO.Response> dtos) {
        List<Long> servicioIds = entidades.stream()
                .map(Cirugia::getServicio)
                .filter(Objects::nonNull)
                .map(s -> s.getId())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!servicioIds.isEmpty()) {
            List<com.dacs.backend.model.entity.Servicio> servicios = servicioRepository.findAllById(servicioIds);
            Map<Long, com.dacs.backend.model.entity.Servicio> serviciosMap = servicios.stream()
                    .collect(Collectors.toMap(com.dacs.backend.model.entity.Servicio::getId, s -> s));

            for (int i = 0; i < entidades.size(); i++) {
                var entidad = entidades.get(i);
                var dto = dtos.get(i);
                if (entidad.getServicio() != null && entidad.getServicio().getId() != null) {
                    var servicioEntity = serviciosMap.get(entidad.getServicio().getId());
                    if (servicioEntity != null) {
                        var servicioDto = modelMapper.map(servicioEntity, ServicioDto.class);
                        dto.setServicio(servicioDto);
                    }
                }
            }
        }
    }

    @Override
    public List<ServicioDto> getServicios( int pagina, int tamaño) {
        Pageable pageable = PageRequest.of(pagina, tamaño);
        return servicioRepository.findAll(pageable)
            .stream()
            .map(s -> modelMapper.map(s, ServicioDto.class))
            .collect(Collectors.toList());
    }

    private List<MiembroEquipoMedicoDto.Response> mapearEquipoMedicoAResponse(List<EquipoMedico> saved) {
        return saved.stream().map(e -> {
            MiembroEquipoMedicoDto.Response dto = modelMapper.map(e, MiembroEquipoMedicoDto.Response.class);
            // asegurar campos explícitos
            dto.setRol(e.getRol());
            if (e.getCirugia() != null)
                dto.setCirugiaId(e.getCirugia().getId());
            dto.setFechaAsignacion(e.getFechaAsignacion());

            // agregar info del personal (objeto)
            Personal p = e.getPersonal();
            if (p != null) {
                PersonalDto.Response pDto = modelMapper.map(p, PersonalDto.Response.class);
                dto.setPersonal(pDto);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}