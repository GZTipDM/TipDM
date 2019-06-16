package com.tipdm.framework.repository.dmserver;

import com.tipdm.framework.model.dmserver.Model;
import com.tipdm.framework.persist.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
public interface ModelRepository extends BaseRepository<Model, Long> {

    public Model findByNodeIdAndVersion(String nodeId, Integer version);

    public Model findByModelNameAndCreatorId(String modelName, Long creatorId);

}
