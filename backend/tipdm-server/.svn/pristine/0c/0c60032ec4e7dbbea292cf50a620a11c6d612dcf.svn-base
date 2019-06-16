package com.tipdm.framework.service.dmserver;

import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.service.BaseService;

import java.util.List;

/**
 * Created by TipDM on 2017/2/22.
 * E-mail:devp@tipdm.com
 */
public interface UDAService extends BaseService<Component, Long>{


    public void update(Long id, Component component);

    public List<Component> findList(Long parentId, Long creatorId);

    public void deleteUDA(Long id, Long creatorId) throws IllegalAccessException;
}
