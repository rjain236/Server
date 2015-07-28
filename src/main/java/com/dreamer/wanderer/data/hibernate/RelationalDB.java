package com.dreamer.wanderer.data.hibernate;

import com.dreamer.wanderer.bo.Snap;
import com.dreamer.wanderer.data.DataBaseInstance;
import com.dreamer.wanderer.data.converters.Serialiser;
import com.dreamer.wanderer.data.converters.SerialiserFactory;
import com.dreamer.wanderer.exceptions.SpringTransactionalException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rjain236 on 29/7/15.
 */
@Service("RelationalDB")
public class RelationalDB extends HibernateDaoSupport implements DataBaseInstance {


    private static final Logger logger = Logger.getLogger(RelationalDB.class);

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private SerialiserFactory serialiserFactory;

    public RelationalDB() {
    }

    @PostConstruct
    public void init() {
        setSessionFactory(sessionFactory);
    }

    @Override
    public <B extends Snap> Long save(B bean) throws SpringTransactionalException {
        Serialiser serialiser = serialiserFactory.getSerialiserInstance(bean.getClass());
        Long id = (Long) getHibernateTemplate().save(serialiser.serialise(bean));
        return id;
    }


    @Override
    public <B extends Snap> B saveOrUpdate(B bean) throws SpringTransactionalException {
//            populateStandardFields((BasePersistenceBean)bean, "System", new Date(),
//                    new HashSet<BasePersistenceBean>());
        Serialiser serialiser = serialiserFactory.getSerialiserInstance(bean.getClass());
        bean = (B) getHibernateTemplate().merge(serialiser.serialise(bean));
        getHibernateTemplate().flush();
        return bean;
    }

    public <T> List<T> findByQuery(String queryString) throws SpringTransactionalException {
        logger.debug("finding all instances");
        return (List<T>) getHibernateTemplate().find(queryString);
    }


    @SuppressWarnings("rawtypes")
    public List findAll(String beanName) throws SpringTransactionalException {
        logger.debug("findAll " + beanName);
        String queryString = "select from " + beanName + " model";
        List list = getHibernateTemplate().find(queryString);
            /*logger.debug("Find all " + beanName
					+ " from the application database successful");*/
        return list;
    }


    @SuppressWarnings("rawtypes")
    public List findDistinctProperties(String beanName, String propertyName)
            throws SpringTransactionalException {

        String queryString = "select distinct model." + propertyName
                + " from " + beanName + " as model";
        List list = getHibernateTemplate().find(queryString);
        logger.debug("finding Distinct instances of property: "
                + propertyName + " successful");
        return list;
    }

    @SuppressWarnings("rawtypes")
    public <B extends Snap> List findByExample(B bean) throws SpringTransactionalException {
        List list = getHibernateTemplate().findByExample(bean);
        logger.debug("findByExample of bean : " + bean
                + " successful");
        return list;
    }

    @SuppressWarnings("rawtypes")
    public List findByProperty(String beanName, String propertyName,
                               Object value) throws SpringTransactionalException {

        String queryString = "from " + beanName + " as model where model."
                + propertyName + "= ? ";
        List list = getHibernateTemplate().find(queryString, value);

        logger.debug("finding bean instance instance for " + beanName
                + " with property: " + propertyName + ", value: " + value
                + " successful");

        return list;
    }

    @SuppressWarnings("rawtypes")
    public <T> List<T> executeQueryWithProperty(String query,
                                                Object value) throws SpringTransactionalException {

        List list = getHibernateTemplate().find(query, value);

        logger.debug("finding bean instance instance for " + query
                + " value: " + value
                + " successful");

        return list;
    }

    @SuppressWarnings("rawtypes")
    public List findByProperties(String beanName, String propertyName,
                                 String values) throws SpringTransactionalException {

        String queryString = "from " + beanName + " as model where model."
                + propertyName + " in ( " + values + ")";
        List list = getHibernateTemplate().find(queryString, values);

        logger.debug("finding bean instance instance for " + beanName
                + " with property: " + propertyName + ", value: " + values
                + " successful");

        return list;
    }


    @SuppressWarnings("rawtypes")
    public <T> T findById(String beanName, String identifierProperty, Object identifier) throws SpringTransactionalException {

        String queryString = "from " + beanName + " as model where model."
                + identifierProperty + "= ? ";
        List list = getHibernateTemplate().find(queryString, identifier);

        logger.debug("finding bean instance instance for " + beanName
                + " with property: " + identifierProperty + ", value: " + identifier
                + " successful");

        if (CollectionUtils.isEmpty(list)) return null;
        return (T) list.get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> findByPropertyMap(String beanName, Map<String, Object> propertyMap) throws SpringTransactionalException {

        StringBuffer sb = new StringBuffer("from ").append(beanName).append(" as model where ");
        String[] paramNames = new String[propertyMap.keySet().size()];
        Object[] values = new Object[propertyMap.keySet().size()];

        int icount = 0;
        for (Iterator<String> iterator = propertyMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object value = propertyMap.get(key);
            String parameterIdentifier = key + "_" + icount;
            paramNames[icount] = parameterIdentifier;
            values[icount] = value;
            if (icount > 0) {
                sb.append(" and ");
            }
//				sb.append(" model.").append(key).append(" = :").append(parameterIdentifier);
            if (value instanceof String) {
                sb.append(" lower(model.").append(key).append(") = lower(?)");
            } else {
                sb.append(" model.").append(key).append(" = ?");
            }

            // changed query string due to some unknown bugs
            icount++;

        }

//			List list =  getHibernateTemplate().find(sb.toString(), paramNames, values);
        List list = getHibernateTemplate().find(sb.toString(), values);

        return (List<T>) list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> findByQuery(String query, Object[] values) throws SpringTransactionalException {

        List list = getHibernateTemplate().find(query, values);
        return (List<T>) list;
    }

    /* (non-Javadoc)
     * @see com.finmechanics.fmconverge.dao.AppDAO#delete(com.finmechanics.fmconverge.persistence.BasePersistenceBean)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <B extends Snap> void delete(B snap) {
        Serialiser serialiser = serialiserFactory.getSerialiserInstance(snap.getClass());

        getHibernateTemplate().delete(serialiser.serialise(snap));

    }


    //    protected void populateStandardFields(BasePersistenceBean bean,
//                                          String createdBy, Date created, Set<BasePersistenceBean> beans) {
//        if (beans.contains(bean)) {
//            return;
//        }
//        bean.setCreatedBy(createdBy);
//        bean.setCreated(created);
//        bean.setLastUpdatedBy(createdBy);
//        bean.setLastUpdated(created);
//        beans.add(bean);
//        PropertyDescriptor[] propertyDescriptors = PropertyUtils
//                .getPropertyDescriptors(bean);
//        try {
//            for (int i = 0; i < propertyDescriptors.length; i++) {
//                PropertyDescriptor descriptor = propertyDescriptors[i];
//                Object value = PropertyUtils.getProperty(bean,
//                        descriptor.getName());
//                if (value instanceof BasePersistenceBean) {
//                    populateStandardFields((BasePersistenceBean) value,
//                            createdBy, created, beans);
//                }
//                if (value instanceof Collection) {
//                    Collection collection = (Collection) value;
//                    for (Object object : collection) {
//                        if (object instanceof BasePersistenceBean) {
//                            populateStandardFields(
//                                    (BasePersistenceBean) object, createdBy,
//                                    created, beans);
//                        }
//                    }
//                }
//            }
//        } catch (IllegalAccessException | NoSuchMethodException
//                | InvocationTargetException e) {
//            FmLibException fmLibException = new FmLibException("Could not get nested object",
//                    "Error while saving the object" + bean, e, true);
//            logger.error("error saving" + bean + ErrPrettyPrint.prettyPrint(e, true));
//            throw fmLibException;
//        }catch(Exception re){
//            FmLibException fmLibException = new FmLibException("Save error",
//                    "Error while saving the object" + bean, re, true);
//            logger.error("error saving" + bean + ErrPrettyPrint.prettyPrint(re, true));
//            throw fmLibException;
//        }
//    }
//
    public <T extends Snap> List<T> executeCriteria(
            DetachedCriteria criteria) {
        return (List<T>) getHibernateTemplate().findByCriteria(criteria);
    }


}
