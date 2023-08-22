package com.gdp.codetest.services.generic;

import java.util.List;

public interface GenericServices<T, ID> {
    public List<T> Get();
    public T Get(ID id);
    public Boolean Save(T model);
    public Boolean Delete(ID id);
}
