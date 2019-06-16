package com.tipdm.framework.repository.dmserver;

import com.tipdm.framework.model.dmserver.Template;
import com.tipdm.framework.persist.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
public interface TemplateRepository extends BaseRepository<Template, Long>{

    @Query("from Template where project.id = ?1")
    public Template findByProjectId(Long projectId);

    @Modifying
    @Query("delete Template where project.id = ?1")
    public void deleteByProjectId(Long projectId);

    public Page<Template> findTemplate(Pageable pageable);
}
