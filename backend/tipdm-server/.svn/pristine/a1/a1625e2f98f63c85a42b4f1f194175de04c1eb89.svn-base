package com.tipdm.framework.service.dmserver.impl;
import com.tipdm.framework.model.dmserver.Document;
import com.tipdm.framework.repository.dmserver.DocumentRepository;
import com.tipdm.framework.service.AbstractBaseServiceImpl;
import com.tipdm.framework.service.dmserver.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Transactional
@Service("documentService")
public class DocumentServiceImpl extends AbstractBaseServiceImpl<Document, Long> implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public Integer update(Long documentId, String order){

        Document doc = documentRepository.findOne(documentId);
        if(doc.getSequence() == null){
            doc.setSequence(0);
            return 0;
        }

        List<Document> child = documentRepository.findByParentId(doc.getParentId(), doc.getCreatorId());
        Integer sequence = doc.getSequence();
        if("up".equals(order)){
            if(sequence == 0){
                return sequence;
            }
            Document pre = child.get(sequence-1);
            doc.setSequence(sequence-1);
            pre.setSequence(sequence);
        } else {
            if(sequence == child.size()-1){
                return sequence;
            }
            Document next = child.get(sequence+1);
            next.setSequence(sequence);
            doc.setSequence(sequence+1);
        }
        doc.setUpdateTime(Calendar.getInstance().getTime());
        return doc.getSequence();
    }

    public List<Document> findChild(Long parentId, Long creatorId){
        return documentRepository.findByParentId(parentId, creatorId);
    }

    public void updateDocument(Document document){
       Document doc = documentRepository.findOne(document.getId());
        if(document.getSequence()!=null){
            doc.setSequence(document.getSequence());
        }
    }
}
