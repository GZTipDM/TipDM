package com.tipdm.framework.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tipdm.framework.model.IdEntity;
import com.tipdm.framework.persist.BaseRepository;

@Transactional
public abstract class AbstractBaseServiceImpl<T extends IdEntity<PK>, PK extends Serializable> {
	
	@Autowired
	protected BaseRepository<T, PK> baseRepository;// 将子类注入

	public List<T> findAll() {
		return (List<T>) baseRepository.findAll();
	}

	public T findOne(PK id) {
		return baseRepository.findOne(id);
	}

	public void save(T obj) {
		baseRepository.save(obj);
	}

	public void save(List<T> objs) {
		baseRepository.save(objs);
	}

	public void update(T obj) {
		baseRepository.save(obj);
	}

	@SuppressWarnings("unchecked")
	public void delete(PK... ids) {
		for (PK id : ids)
			baseRepository.delete(id);

	}

	public void deleteAll() {
		baseRepository.deleteAll();
	}

	public long getCount() {
		return baseRepository.count();
	}

	public Page<T> findAll(Pageable pageable) {
		return baseRepository.findAll(pageable);
	}

	public List<T> find(@SuppressWarnings("unchecked") PK... ids) {
		List<PK> idList = new ArrayList<PK>();

		for (PK id : ids) {
			idList.add(id);
		}
		return (List<T>) baseRepository.findAll(idList);
	}

	public BaseRepository<T, PK> getBaseRepository() {
		return baseRepository;
	}

	public void setBaseRepository(BaseRepository<T, PK> baseRepository) {
		this.baseRepository = baseRepository;
	}

}
