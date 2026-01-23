package com.dacs.backend.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dacs.backend.dto.PaginationDto;
import com.dacs.backend.dto.PersonalDto;
import com.dacs.backend.model.entity.Personal;
import com.dacs.backend.model.repository.PersonalRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public PersonalDto.Response create(PersonalDto.Create request) {
        Personal entity = modelMapper.map(request, Personal.class);
        Personal saved = personalRepository.save(entity);
        return modelMapper.map(saved, PersonalDto.Response.class);
    }

    @Override
    @Transactional
    public PersonalDto.Response update(Long id, PersonalDto.Update request) {
        Personal existingPersonal = personalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personal not found with id: " + id));

        modelMapper.map(request, existingPersonal);
        Personal updatedPersonal = personalRepository.save(existingPersonal);
        return modelMapper.map(updatedPersonal, PersonalDto.Response.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Personal existingPersonal = personalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personal not found with id: " + id));
        personalRepository.delete(existingPersonal);
    }

   @Override
   public PaginationDto<PersonalDto.Response> getAll(int page, int size, String search) {
       Pageable pageable = PageRequest.of(page, size);
       Page<Personal> personalPage;
       
       if (search != null && !search.trim().isEmpty()) {
           personalPage = personalRepository.findByNombreContainingIgnoreCase(search, pageable);
       } else {
           personalPage = personalRepository.findAll(pageable);
       }
       
       List<PersonalDto.Response> content = personalPage.getContent().stream()
               .map(personal -> modelMapper.map(personal, PersonalDto.Response.class))
               .toList();
       
       PaginationDto.Response<PersonalDto.Response> response = new PaginationDto.Response<>();
       response.setContenido(content);
       response.setTotalElementos(personalPage.getTotalElements());
       response.setTotalPaginas(personalPage.getTotalPages());
       response.setPagina(personalPage.getNumber());
       response.setSize(personalPage.getSize());
       
       return response;
   }
}