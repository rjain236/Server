package com.dreamer.wanderer.data.dataobjects;

import com.dreamer.wanderer.bo.beandefinitions.bo1;
import com.dreamer.wanderer.bo.beandefinitions.bo2;
import com.dreamer.wanderer.data.annotation.HibernateBeanAnnotation;
import com.dreamer.wanderer.data.annotation.NeoBeanAnnotation;
import com.dreamer.wanderer.data.annotation.NormalAnnotation;
import com.dreamer.wanderer.data.neo.NeoBean;

/**
 * Created by rjain236 on 25/7/15.
 */
public class do3 extends NeoBean {

    @NormalAnnotation(identifier = "f")
    private String field1;

    @HibernateBeanAnnotation(type = bo1.class,identifier = "hbo")
    private Long bo1Field;

    @NeoBeanAnnotation(type = bo2.class,identifier = "nbo",query = "cypher query")
    private Long bo2Inner;
}
