package com.dacs.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommonService<E> {
	
	public Optional<E> getById(Long id);
	
	public Boolean existById(Long id);
	
	public void delete(Long id);
	
	public E save(E entity);
		
	public E getBy(Map<String,Object> filter);

	public List<E> getAll();

}
