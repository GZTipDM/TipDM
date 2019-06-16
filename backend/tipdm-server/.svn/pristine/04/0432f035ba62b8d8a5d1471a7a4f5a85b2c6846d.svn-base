package com.tipdm.framework.dmserver.shiro.mgt;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * Created by TipDM on 2016/12/8.
 * E-mail:devp@tipdm.com
 */
public class BearerTokenSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(true);
        return super.createSubject(context);
    }
}
