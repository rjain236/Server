package com.dreamer.wanderer.data.query.executors;

import com.dreamer.wanderer.bo.CachePersistableBean;
import com.dreamer.wanderer.bo.beandefinitions.bo1;
import com.dreamer.wanderer.data.query.Query;
import com.dreamer.wanderer.data.query.QueryExecutor;
import com.dreamer.wanderer.data.query.queries.bo1Query;
import com.dreamer.wanderer.exceptions.SpringTransactionalException;

import java.util.Collection;

/**
 * Created by rjain236 on 29/7/15.
 */
public class HazelCastQueryExecutor implements QueryExecutor{

    public <RESULT extends CachePersistableBean> Collection<RESULT> execute(Query<RESULT> query) throws SpringTransactionalException {
        throw new SpringTransactionalException("Hazelcast query not implemented for the query");
    }

    public Collection<bo1> executeForBo1(bo1Query query) throws SpringTransactionalException {
        return null;
    }
}
