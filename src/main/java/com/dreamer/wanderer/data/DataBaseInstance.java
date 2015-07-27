package com.dreamer.wanderer.data;

/**
 * Created by rjain236 on 25/7/15.
 */
public interface DataBaseInstance<T extends DataBaseInstance, B extends PersistableBean> {

    public Long save(B bean);

    public Long saveOrUpdate(B bean);

    public void delete(B bean);

}
