package com.tipdm.framework.repository.dmserver;

import com.tipdm.framework.model.dmserver.ModelTree;
import com.tipdm.framework.persist.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
public interface ModelTreeRepository extends BaseRepository<ModelTree, Long> {

    public List<ModelTree> findByParentIdAndCreatorId(Long id, Long creatorId);

    @Query("from ModelTree where model.id = ?1")
    public ModelTree findByModelId(Long modelId);

    @Modifying
    @Query("delete from ModelTree where path like ?1")
    public void deleteByPath(String path);
}
