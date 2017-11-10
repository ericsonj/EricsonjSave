package net.ericsonj.save;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Table;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

/**
 *
 * @author ejoseph
 * @param <E>
 * @param <PK>
 */
public class ElementTransactionLogicFacade<E, PK extends Serializable> implements LogicFacade<E, PK> {

    protected Session session;
    protected Transaction tx;
    protected Class<E> elementType;
    protected boolean isTransactional;

    public ElementTransactionLogicFacade() {
        initTypes();
        this.isTransactional = false;
    }

    public ElementTransactionLogicFacade(SaveTransaction sf) {
        initTypes();
        this.isTransactional = true;
        this.session = sf.getSession();
    }

    private void initTypes() {
        this.elementType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected void initOperation() throws HibernateException {
        try {
            session = DBSaveUtil.getSessionFactory().openSession();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public PK create(E Entity) throws HibernateException {
        PK entityId = null;
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = this.session.beginTransaction();
                entityId = (PK) session.save(Entity);
                tx.commit();
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
            return entityId;
        }

        try {
            entityId = (PK) session.save(Entity);
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

        return entityId;
    }

    @Override
    public void edit(E Entity) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                session.update(Entity);
                tx.commit();
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
            return;
        }

        try {
            session.update(Entity);
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public E find(PK id) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                E result = (E) session.get(this.elementType, id);
                tx.commit();
                return result;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }

        try {
            E result = (E) session.get(this.elementType, id);
            return result;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void remove(E Entity) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                session.delete(Entity);
                tx.commit();
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
            return;
        }

        try {
            session.delete(Entity);
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public List<E> findAllByCriteria(CriteriaQuery crQuery) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                Criteria cr = session.createCriteria(elementType);
                if (crQuery != null) {
                    crQuery.addCriterion(cr);
                }
                List<E> entitys = cr.list();
                tx.commit();
                return entitys;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }

        try {
            Criteria cr = session.createCriteria(elementType);
            if (crQuery != null) {
                crQuery.addCriterion(cr);
            }
            List<E> entitys = cr.list();
            return entitys;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public E findByCriteria(CriteriaQuery crQuery) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                Criteria cr = session.createCriteria(elementType);
                if (crQuery != null) {
                    crQuery.addCriterion(cr);
                }
                E entitys = (E) cr.uniqueResult();
                tx.commit();
                return entitys;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }

        try {
            Criteria cr = session.createCriteria(elementType);
            if (crQuery != null) {
                crQuery.addCriterion(cr);
            }
            E entitys = (E) cr.uniqueResult();
            return entitys;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public void executeQuery(String query) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                SQLQuery sqlQuery = session.createSQLQuery(query);
                sqlQuery.executeUpdate();
                tx.commit();
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
            return;
        }
        try {
            SQLQuery sqlQuery = session.createSQLQuery(query);
            sqlQuery.executeUpdate();
        } catch (Throwable e) {
            throw new HibernateException(e);
        }
    }

    public long getCount() {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                Criteria cr = session.createCriteria(elementType);
                cr.setProjection(Projections.rowCount());
                long count = (Long) cr.uniqueResult();
                tx.commit();
                return count;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }

        try {
            Criteria cr = session.createCriteria(elementType);
            cr.setProjection(Projections.rowCount());
            long count = (Long) cr.uniqueResult();
            return count;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

    /**
     * Find by string query.
     * <pre>
     * {@code
     * List<User> users = facade.findByquery(getSelectQuery() + " WHERE localization = ? ","Bogota")
     * }
     * </pre>
     *
     * @param query
     * @param params
     * @return
     */
    public List<E> findByQuery(String query, Object... params) {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                SQLQuery sqlQuery = session.createSQLQuery(query);
                sqlQuery.addEntity(elementType);

                int i = 0;
                for (Object param : params) {
                    sqlQuery.setParameter(i, param);
                    i++;
                }

                List<E> entitys = sqlQuery.list();
                tx.commit();
                return entitys;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }

        try {
            SQLQuery sqlQuery = session.createSQLQuery(query);
            sqlQuery.addEntity(elementType);

            int i = 0;
            for (Object param : params) {
                sqlQuery.setParameter(i, param);
                i++;
            }

            List<E> entitys = sqlQuery.list();
            return entitys;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }
    }

    public long getCountQuery(String query, Object... params) {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();
                SQLQuery sqlQuery = session.createSQLQuery(query);
                
                int i = 0;
                for (Object param : params) {
                    sqlQuery.setParameter(i, param);
                    i++;
                }

                long count = ((BigInteger) sqlQuery.uniqueResult()).longValue();
                tx.commit();
                return count;
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
        }
        try {
            SQLQuery sqlQuery = session.createSQLQuery(query);
            sqlQuery.addEntity(elementType);

            int i = 0;
            for (Object param : params) {
                sqlQuery.setParameter(i, param);
                i++;
            }

            long count = ((BigInteger) sqlQuery.uniqueResult()).longValue();
            return count;
        } catch (Throwable e) {
            throw new HibernateException(e);
        }
    }

    /**
     * Get count query, SELECT count(*) FROM table
     *
     * @return
     */
    public String getCountQuery() {
        return " SELECT count(*) FROM " + getTableName() + " ";
    }

    /**
     * Get select query, SELECT * FROM table
     *
     * @return
     */
    public String getSelectQuery() {
        return " SELECT * FROM " + getTableName() + " ";
    }

    /**
     * Get table name
     *
     * @return
     */
    public String getTableName() {
        Table m = elementType.getAnnotation(Table.class);
        if (m != null) {
            return m.name();
        }
        return elementType.getCanonicalName();
    }

    /**
     * Insert in batch mode from RunnableBatch implementation.
     *
     * @param batchSize
     * @param runnableBatch
     * @throws HibernateException
     */
    @Override
    public void insertBatch(int batchSize, RunnableBatch<E> runnableBatch) throws HibernateException {
        if (!isTransactional) {
            try {
                this.initOperation();
                tx = session.beginTransaction();

                int i = 0;
                while (runnableBatch.hasMoreElements()) {
                    E entity = runnableBatch.getEntity();
                    if (entity != null) {
                        session.save(entity);
                    }
                    i++;
                    if (i % batchSize == 0) { // Same as the JDBC batch size
                        //flush a batch of inserts and release memory:
                        session.flush();
                        session.clear();
                    }
                }
                tx.commit();
            } catch (Throwable e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new HibernateException(e);
            } finally {
                session.close();
            }
            return;
        }

        try {
            int i = 0;
            while (runnableBatch.hasMoreElements()) {
                E entity = runnableBatch.getEntity();
                if (entity != null) {
                    session.save(entity);
                }
                i++;
                if (i % batchSize == 0) { // Same as the JDBC batch size
                    //flush a batch of inserts and release memory:
                    session.flush();
                    session.clear();
                }
            }
        } catch (Throwable e) {
            throw new HibernateException(e);
        }

    }

}
