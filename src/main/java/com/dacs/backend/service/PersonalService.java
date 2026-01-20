package com.dacs.backend.service;

import java.util.List;

import com.dacs.backend.dto.PaginationDto;
import com.dacs.backend.dto.PersonalDto;

public interface PersonalService {
    PersonalDto.Response create(PersonalDto.Create request);

    PersonalDto.Response update(Long id, PersonalDto.Update request);

    void delete(Long id);

    PaginationDto<PersonalDto.Response> getAll(int page, int size, String search);


}
