package net.ericsonj.save;

import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author ejoseph
 * @param <E>
 * @param <PK>
 */
public interface LogicFacade<E, PK extends Serializable> {

    PK create(E Entity) throws HibernateException;

    void edit(E Entity) throws HibernateException;

    E find(PK id) throws HibernateException;

    void remove(E Entity) throws HibernateException;

    List<E> findAllByCriteria(CriteriaQuery crQuery) throws HibernateException;

    E findByCriteria(CriteriaQuery crQuery) throws HibernateException;

    void executeQuery(String query) throws HibernateException;

    void insertBatch(int batchSize, RunnableBatch<E> runnableBatch) throws HibernateException;

}
