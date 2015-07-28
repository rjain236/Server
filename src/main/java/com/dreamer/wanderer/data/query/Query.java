package com.dreamer.wanderer.data.query;

import com.dreamer.wanderer.bo.CachePersistableBean;

import java.util.Collection;

/**
 * Created by rjain236 on 29/7/15.
 */
public interface Query<R extends CachePersistableBean> {

    Collection<R> fetch(QueryExecutor executor);

}
