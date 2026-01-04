package com.ttknp.api.service;

import java.util.HashMap;
import java.util.List;

public interface ModelService <T> {
    List<T> getAll();
    List<T> getAllByKey(Object key);
    <U> List<U> getColumnByKey(Object key);
    T getById(Object id);
    Boolean deleteById(Object id);
    Boolean update(T t);
    Boolean add(T t);
    HashMap<String, byte[]> getGadgetHasMapReport(String fileType);
    HashMap<String, byte[]> getGadgetHasMapReport(String fileType,String key);
}
