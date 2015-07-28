package com.dreamer.wanderer.data.query.executors;

import com.dreamer.wanderer.bo.CachePersistableBean;
import com.dreamer.wanderer.bo.beandefinitions.bo1;
import com.dreamer.wanderer.data.converters.SerialiserFactory;
import com.dreamer.wanderer.data.converters.SerialiserSchemaBasedBean;
import com.dreamer.wanderer.data.query.Query;
import com.dreamer.wanderer.data.query.QueryExecutor;
import com.dreamer.wanderer.data.query.queries.bo1Query;
import com.dreamer.wanderer.exceptions.SpringTransactionalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by rjain236 on 29/7/15.
 */
@Service("HibernateQueryExecutor")
public class HibernateQueryExecutor implements QueryExecutor {

    @Autowired
    private SerialiserSchemaBasedBean serialiserSchemaBasedBean;

    @Autowired
    private SerialiserFactory serialiserFactory;


    public <RESULT extends CachePersistableBean> Collection<RESULT> execute(Query<RESULT> query) throws SpringTransactionalException {
        throw new SpringTransactionalException("Hibernate query not implemented for the query");
    }

    public Collection<bo1> executeForBo1(bo1Query query) throws SpringTransactionalException {
        return null;
    }
}
