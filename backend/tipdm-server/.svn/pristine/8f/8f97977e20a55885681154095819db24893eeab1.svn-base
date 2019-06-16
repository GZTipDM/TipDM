package com.tipdm.framework.service.dmserver.impl;

import com.alibaba.fastjson.JSON;
import com.tipdm.framework.common.utils.FileKit;
import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.mse.ModelVersion;
import com.tipdm.framework.dmserver.utils.Constants;
import com.tipdm.framework.model.dmserver.*;
import com.tipdm.framework.repository.dmserver.ComponentRepository;
import com.tipdm.framework.repository.dmserver.ModelRepository;
import com.tipdm.framework.repository.dmserver.ModelTreeRepository;
import com.tipdm.framework.service.AbstractBaseServiceImpl;
import com.tipdm.framework.service.dmserver.ModelService;
import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.*;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
@SuppressWarnings("all")
@Transactional
@Service("modelService")
public class ModelServiceImpl extends AbstractBaseServiceImpl<Model, Long> implements ModelService {

    private final static Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelTreeRepository modelTreeRepository;

    @Autowired
    private ComponentRepository componentRepository;


    @Override
    public Page<Model> findOwnModels(Map<String, Object> params, Pageable pageable) {

        Specification<Model> specification = new Specification<Model>() {
            @Override
            public Predicate toPredicate(Root<Model> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicates = null;
                Long creatorId = (Long)params.get("creatorId");

                if(null != creatorId){
                    predicates = criteriaBuilder.equal(root.get("creatorId").as(Long.class), creatorId);
                    criteriaQuery.where(predicates);
                }

                String modelName = (String)params.get("modelName");

                if(StringKit.isNotBlank(modelName)){
                    Predicate condition = criteriaBuilder.like(root.get("modelName").as(String.class), "%" + modelName + "%");
                    if(null == predicates){
                        predicates = criteriaBuilder.and(condition);
                    } else {
                        predicates = criteriaBuilder.and(predicates, condition);
                    }
                }

                Date beginTime = null;
                Date endTime = null;
                try {
                    beginTime = (Date)params.get("beginTime");
                    endTime = (Date)params.get("endTime");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(beginTime != null && endTime != null){
                    Predicate condition = criteriaBuilder.between(root.get("deployTime").as(Date.class), beginTime, endTime);

                    if(null == predicates){
                        predicates = criteriaBuilder.and(condition);
                    } else {
                        predicates = criteriaBuilder.and(predicates, condition);
                    }
                }
                return predicates;
            }
        };

        return modelRepository.findAll(specification, pageable);
    }


    @Override
    public Long deployModel(Model model) throws IOException {
        ModelVersion modelVersion = (ModelVersion) RedisUtils.getFromMap(Constants.MODEL_VERSION, model.getNodeId());
        if(null == modelVersion) {
            modelVersion = new ModelVersion();
        } else {
            modelVersion.increment();
        }
        Integer version = modelVersion.getLatest();
        String modelDir = RedisUtils.get(Constants.MODEL_DIR, String.class);
        File file = new File(modelDir, model.getModelPath());
        File destFile = new File(modelDir,  StringKit.substring(model.getModelPath(), 0, model.getNodeId().length()) + "_" + version + StringKit.substringAfterLast(model.getModelPath(), model.getNodeId()));
        FileKit.copyFile(file, destFile);
        model.setModelPath(destFile.getName());
        model.setVersion(version);
        modelRepository.save(model);
        RedisUtils.putToMap(Constants.MODEL_VERSION, model.getNodeId(), modelVersion);
        return model.getId();

    }

    @Override
    public Long redeployModel(Model model, Integer version) throws IOException {
        Model existsModel = modelRepository.findByNodeIdAndVersion(model.getNodeId(), version);

        if(null == existsModel){
            throw new NullPointerException("can not found model");
        }
        existsModel.setTarget(JSON.toJSONString(model.getTarget()));
        existsModel.setFeatures(JSON.toJSONString(model.getFeatures()));
        existsModel.setOutputs(JSON.toJSONString(model.getOutputs()));
        existsModel.setDeployTime(Calendar.getInstance().getTime());

        String modelDir = RedisUtils.get(Constants.MODEL_DIR, String.class);
        File file = new File(modelDir, model.getModelPath());
        File destFile = new File(modelDir,  StringKit.substring(model.getModelPath(), 0, model.getNodeId().length()) + "_" + version + StringKit.substringAfterLast(model.getModelPath(), model.getNodeId()));
        FileKit.copyFile(file, destFile);
        existsModel.setVersion(version);
        update(existsModel);
        return existsModel.getId();
    }

    @Override
    public void updateModel(Model model) {
        this.update(model);
    }

    @SuppressWarnings("all")
    @Override
    public Long createNode(ModelTree node) {

        List<String> paths = new ArrayList<>();
        getVirtualPath(node.getParentId(), paths);
        modelTreeRepository.save(node);
        paths.add(node.getId().toString());
        paths.add(0, "0");
        node.setPath(StringKit.join(paths, "/") + "/");

        return node.getId();
    }

    @Override
    public void deleteNode(Long nodeId, String contextPath) throws IOException {

        ModelTree node = modelTreeRepository.findOne(nodeId);
        File parentDir = new File(contextPath + "/" + getRealPathByNodeId(node.getParentId()));

        if(node.getLeaf()){
            File file = new File(parentDir, node.getModel().getModelPath());
            modelTreeRepository.delete(nodeId);
            file.delete();
        } else {
            File documentDir = new File(parentDir, node.getName());
            modelTreeRepository.deleteByPath(node.getPath() + "%");
            FileUtils.deleteDirectory(documentDir);
        }
    }

    @Override
    public void deleteModel(Long modelId, String contextPath) {
        ModelTree node = findNodeByModelId(modelId);
        File parentDir = new File(contextPath + "/" + getRealPathByNodeId(node.getParentId()));
        File file = new File(parentDir, node.getModel().getModelPath());
        modelTreeRepository.delete(node.getId());
        file.delete();
    }

    @Override
    public void deleteModel(Long modelId, Long creatorId) throws IllegalAccessException {
        Model model = findOne(modelId);
        if(model == null){
            throw new NullPointerException("模型不存在或已被删除");
        }
        if(model.getCreatorId().equals(creatorId)){
            modelRepository.delete(model);
        } else {
            throw new IllegalAccessException("您不能删除他人部署的模型");
        }
    }

    @Override
    public String getRealPathByNodeId(Long nodeId) {
        List<String> paths = new ArrayList<>();

        getPath(nodeId, paths);

        return StringKit.join(paths, "/");
    }

    @Override
    public List<ModelTree> getChild(Long nodeId, Long creatorId) {
        return modelTreeRepository.findByParentIdAndCreatorId(nodeId, creatorId);
    }

    @Override
    public ModelTree findNodeByModelId(Long modelId) {
        return modelTreeRepository.findByModelId(modelId);
    }


    private Map<String, Object> loadModel(Long modelId)throws FileNotFoundException{
        Model model = findOne(modelId);
        if(model == null){
            logger.error("can not found model");
            throw new FileNotFoundException("model file not found");
        }
        String modelDir = RedisUtils.get(Constants.MODEL_DIR, String.class);
        File modelFile = new File(modelDir, model.getModelPath());
        if (!modelFile.exists()) {
            logger.error("can not found model file, file path:{}", modelFile.getAbsolutePath());
            throw new FileNotFoundException("model file not found");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("model", model);
        result.put("modelFile", modelFile);
        return result;
    }

    private void getVirtualPath(Long nodeId, List<String> path){

        ModelTree node = modelTreeRepository.findOne(nodeId);

        if(node != null){
            path.add(0, node.getId().toString());
            getVirtualPath(node.getParentId(), path);
        }
    }

    private void getPath(Long nodeId, List<String> path){

        ModelTree node = modelTreeRepository.findOne(nodeId);

        if(node != null){
            path.add(0, node.getName());
            getPath(node.getParentId(), path);
        }
    }
}
