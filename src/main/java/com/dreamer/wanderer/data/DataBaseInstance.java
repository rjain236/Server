package com.dreamer.wanderer.data;

import com.dreamer.wanderer.bo.CachePersistableBean;
import com.dreamer.wanderer.bo.Snap;
import com.dreamer.wanderer.exceptions.SpringTransactionalException;

import java.util.Collection;

/**
 * Created by rjain236 on 25/7/15.
 */

public interface DataBaseInstance {

    <B extends Snap> Long save(B bean);

    <B extends Snap> B saveOrUpdate(B bean) throws SpringTransactionalException;

    <B extends Snap> void delete(B bean);

}
