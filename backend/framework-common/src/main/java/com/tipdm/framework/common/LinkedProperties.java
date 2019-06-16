package com.tipdm.framework.common;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by zhoulong on 2017/4/5.
 * E-mail:zhoulong8513@gmail.com
 */
public class LinkedProperties extends Properties {

    private static final long serialVersionUID = 1L;

    private Map<Object, Object> linkMap = new LinkedHashMap<Object, Object>();

    @Override
    public synchronized Enumeration keys() {
        List<Object> keys = new ArrayList<Object>();
        for (Map.Entry<Object, Object> entry : linkMap.entrySet()) {
            keys.add(entry.getKey());
        }
        return Collections.enumeration(keys);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        super.put(key, value);
        return linkMap.put(key, value);
    }

    @Override
    public synchronized boolean contains(Object value) {
        return linkMap.containsValue(value);
    }

    @Override
    public boolean containsValue(Object value) {
        return linkMap.containsValue(value);
    }

    @Override
    public synchronized Enumeration<Object> elements() {
        throw new UnsupportedOperationException(
                "Enumerations are so old-school, don't use them, "
                        + "use keySet() or entrySet() instead");
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return linkMap.entrySet();
    }

    @Override
    public synchronized void clear() {
        super.clear();
        linkMap.clear();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return linkMap.containsKey(key);
    }

    public void store(Writer writer, String comments)
            throws IOException {
        super.putAll(linkMap);
        super.store(writer, comments);
    }

}
