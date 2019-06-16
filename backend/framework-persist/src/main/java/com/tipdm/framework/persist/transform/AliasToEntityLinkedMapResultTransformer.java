package com.tipdm.framework.persist.transform;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by zhoulong on 2018/2/26.
 * E-mail:zhoulong8513@gmail.com
 */
public class AliasToEntityLinkedMapResultTransformer extends AliasedTupleSubsetResultTransformer {
    public static final AliasToEntityLinkedMapResultTransformer INSTANCE = new AliasToEntityLinkedMapResultTransformer();

    private AliasToEntityLinkedMapResultTransformer() {
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        HashMap result = new LinkedHashMap<>(tuple.length);

        for(int i = 0; i < tuple.length; ++i) {
            String alias = aliases[i];
            if(alias != null) {
                result.put(alias, tuple[i]);
            }
        }

        return result;
    }

    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
