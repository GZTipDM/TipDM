package com.tipdm.framework.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tipdm.framework.model.IdEntity;

public interface BaseService<T extends IdEntity<PK>, PK extends Serializable> {

	public List<T> findAll();

	public T findOne(PK id);

	public void save(T obj);

	public void save(List<T> objs);

	public List<T> find(Long... ids);

	public void delete(@SuppressWarnings("unchecked") PK... ids);

	public void deleteAll();

	public long getCount();

	public Page<T> findAll(Pageable pageable);
}
