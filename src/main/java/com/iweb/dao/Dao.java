package com.iweb.dao;

import java.util.Collection;

public interface Dao<T> {
    Collection<T> listAll();
    void insert(T t);
    void delete(T t);
    void update(T t);
    String detail(T t);

}
