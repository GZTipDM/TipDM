package com.tipdm.framework.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * 基础Dao
 * @author yarn
 *
 * @param <T> 实体类 泛型
 * @param <ID> id
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends
		PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {
	
	public Page<T> findAll(Pageable pageable);

}
