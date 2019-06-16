package com.tipdm.framework.repository.dmserver;

import com.tipdm.framework.model.dmserver.Document;
import com.tipdm.framework.persist.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by TipDM on 2016/12/15.
 * E-mail:devp@tipdm.com
 */
public interface DocumentRepository extends BaseRepository<Document, Long> {

    public Integer countByParentId(Long parentId);

    @Query("from Document where parentId=?1 and creatorId=?2 order by sequence asc, updateTime desc")
    public List<Document> findByParentId(Long documentId, Long creatorId);

    @Query("from Document where project.id = ?1")
    public Document findByProjectId(Long projectId);

    @Modifying
    @Query("delete from Document where path like ?1")
    public void deleteByPath(String path);
}
