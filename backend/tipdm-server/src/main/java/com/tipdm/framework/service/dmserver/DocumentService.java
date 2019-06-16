package com.tipdm.framework.service.dmserver;
import com.tipdm.framework.model.dmserver.Document;
import com.tipdm.framework.service.BaseService;

import java.util.List;

public interface DocumentService  extends BaseService<Document, Long> {

    public Integer update(Long documentId, String order);

    public List<Document> findChild(Long parentId, Long creatorId);

    public void updateDocument(Document document);
}
