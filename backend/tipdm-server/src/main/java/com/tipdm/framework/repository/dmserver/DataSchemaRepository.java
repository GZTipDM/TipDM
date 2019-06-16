package com.tipdm.framework.repository.dmserver;

import com.tipdm.framework.model.dmserver.DataSchema;
import com.tipdm.framework.persist.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by TipDM on 2016/12/15.
 * E-mail:devp@tipdm.com
 */
@Transactional
public interface DataSchemaRepository extends BaseRepository<DataSchema, Long> {

    /**
     * 根据schema名称查找
     * @param schemaName
     * @return
     */
    public DataSchema findByName(String schemaName);

    @Query(value = "SELECT count(schema_name) FROM information_schema.schemata where schema_name = ?1", nativeQuery = true)
    public Integer existsSchema(String schemaName);

    /**
     * 新建schema
     * @param schema
     */
    public void createSchema(String schema, String password);
}
