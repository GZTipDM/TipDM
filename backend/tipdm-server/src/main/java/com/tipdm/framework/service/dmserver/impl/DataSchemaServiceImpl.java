package com.tipdm.framework.service.dmserver.impl;

import com.tipdm.framework.model.dmserver.DataSchema;
import com.tipdm.framework.repository.dmserver.DataSchemaRepository;
import com.tipdm.framework.service.dmserver.DataSchemaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhoulong on 2017/5/9.
 */
@Service("dataSchemaService")
public class DataSchemaServiceImpl implements DataSchemaService {

    @Autowired
    private DataSchemaRepository repository;

    @Override
    public DataSchema findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public void createSchema(String schema) {
        boolean existsSchema = repository.existsSchema(schema) > 0;
        boolean existsUser = repository.existsUser(schema) > 0;
        DataSchema dataSchema = repository.findByName(schema);
        if (!existsSchema) {
            //生成随机密码
            String password = RandomStringUtils.randomAlphabetic(8);
            repository.createSchema(schema, password);
            if (dataSchema == null) {
                DataSchema newSchema = new DataSchema(schema, password);
                repository.save(newSchema);
            } else {
                dataSchema.setPassword(password);
            }
        } else {
            if (dataSchema == null) {
                String password = RandomStringUtils.randomAlphabetic(8);
                DataSchema newSchema = new DataSchema(schema, password);
                repository.save(newSchema);
            } else {
                if (!existsUser) {
                    repository.updateSchema(dataSchema.getName(), dataSchema.getPassword());
                }
            }
        }
    }
}
