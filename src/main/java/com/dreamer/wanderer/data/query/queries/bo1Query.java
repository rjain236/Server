package com.dreamer.wanderer.data.query.queries;

import com.dreamer.wanderer.bo.beandefinitions.bo1;
import com.dreamer.wanderer.data.query.Query;
import com.dreamer.wanderer.data.query.QueryExecutor;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by rjain236 on 29/7/15.
 */
public class bo1Query implements Query<bo1>{

    public Collection<bo1> fetch(QueryExecutor executor) {
        return executor.executeForBo1(this);
    }
}
