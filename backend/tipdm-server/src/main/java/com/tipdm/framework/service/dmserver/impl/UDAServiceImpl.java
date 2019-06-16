package com.tipdm.framework.service.dmserver.impl;


import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.repository.dmserver.ComponentRepository;
import com.tipdm.framework.repository.dmserver.DataTableRepository;
import com.tipdm.framework.service.AbstractBaseServiceImpl;
import com.tipdm.framework.service.dmserver.UDAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by TipDM on 2017/2/22.
 * E-mail:devp@tipdm.com
 */
@Service("udaService")
public class UDAServiceImpl extends AbstractBaseServiceImpl<Component, Long> implements UDAService {

    private final static Logger logger = LoggerFactory.getLogger(UDAServiceImpl.class);

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private DataTableRepository userTableRepository;


    @Override
    public List<Component> findList(Long parentId, Long creatorId) {
        List<Component> list = componentRepository.findByParentId(parentId, creatorId, Boolean.FALSE);
//        Map<String, Object> map = RedisUtils.getMap(com.tipdm.framework.dmserver.utils.Constants.FILE_UPLOAD_ID);
//        list.parallelStream().forEach(x -> {
//            if (StringKit.isBlank(x.getExtra().getValue()) && x.getExtra().getType() == ComponentExtra.ExtraType.FILE) {
//                Optional optional = map.values().stream().filter(o -> x.getId().equals(((UploadInfo) o).getId()) && ((UploadInfo) o).getCategory() == UploadInfo.Category.UDC).findFirst();
//                if (optional.isPresent()) {
//                    x.setUploadId(((UploadInfo) optional.get()).getUploadId());
//                }
//            }
//        });
        return list;
    }

    @Override
    public void update(Long id, Component component) {
        Component modifyObj = componentRepository.findOne(id);
        BeanUtils.copyProperties(component, modifyObj, new String[]{"id"});
        modifyObj.setUpdateTime(Calendar.getInstance().getTime());
        componentRepository.merge(modifyObj);
    }

    @Override
    public void deleteUDA(Long id, Long creatorId) throws IllegalAccessException {
        Component component = componentRepository.findOne(id);
        if (component == null) {
            throw new NullPointerException("组件不存在或已被删除");
        }
        if (creatorId.equals(component.getCreatorId()) && !component.getInBuilt()) {
            componentRepository.delete(component);
        } else {
            throw new IllegalAccessException("对不起，您不能执行此操作");
        }

    }
}
