package com.tipdm.framework.service.dmserver.impl;

import com.tipdm.framework.dmserver.exception.DuplicateException;
import com.tipdm.framework.dmserver.exception.ElementNotFoundException;
import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.Widget;
import com.tipdm.framework.repository.dmserver.ComponentRepository;
import com.tipdm.framework.repository.dmserver.ElementRepository;
import com.tipdm.framework.repository.dmserver.WidgetRepository;
import com.tipdm.framework.service.AbstractBaseServiceImpl;
import com.tipdm.framework.service.dmserver.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
@Transactional
@Service("componentService")
public class ComponentServiceImpl extends AbstractBaseServiceImpl<Component, Long> implements ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Override
    public List<Component> findChild(Long parentId, Boolean inBuilt) {
        List<Component> components = componentRepository.findByParentId(parentId, inBuilt);
        long count = components.stream().filter(x -> x.getSequence() == null || x.getSequence() < 0).count();//统计排序值为null的组件
        if(count > 0){
            IntStream.range(0, components.size()).forEach(index -> {
                components.get(index).setSequence(index);
            });
        }
        return components;
    }

    @Override
    public List<Component> findChild(Long parentId,Long creatorId) {

        List<Component> components = componentRepository.findByParentId(parentId, true);
        long count = components.stream().filter(x -> x.getSequence() == null || x.getSequence() < 0).count();//统计排序值为null的组件
        if(count > 0){
            IntStream.range(0, components.size()).forEach(index -> {
                components.get(index).setSequence(index);
            });
        }
        return components;
    }

    @Override
    public List<Component> findAllInBuiltComponent() {
        return componentRepository.findByInBuiltTrue();
    }


    @Override
    public void update(Long id, Component component) {
        component.setId(id);
        component.setUpdateTime(Calendar.getInstance().getTime());
        componentRepository.merge(component);
    }

    @Transactional
    @Override
    public void modifyCatName(Long catId, String catName) {
        Component component = componentRepository.findOne(catId);
        if(component == null){
            throw new ElementNotFoundException("组件分类不存在");
        }
        component.setName(catName);
    }

    @Override
    public void deleteElement(Long eleId) {
        elementRepository.delete(eleId);
    }

    @Override
    public List<Widget> findWidgetList() {

        Specification<Widget> specification = new Specification<Widget>(){
            @Override
            public Predicate toPredicate(Root<Widget> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
        return widgetRepository.findAll(specification);
    }

    @Override
    public void saveWidget(Widget widget) {

        widgetRepository.save(widget);
    }

    @Transactional
    @Override
    public void save(Component component) {
        Component comp = componentRepository.findByParentIdAndNameAndCreatorIdAndInBuilt(component.getParentId(), component.getName(), component.getCreatorId(), true);
        if(comp != null) {
            String exceptionMsg = comp.isComponent() ? "组件名已存在！" : "组件分类已存在！";
            throw new DuplicateException(exceptionMsg);
        }
        componentRepository.save(component);
    }

}
