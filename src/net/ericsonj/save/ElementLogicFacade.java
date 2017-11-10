package net.ericsonj.save;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ejoseph
 * @param <E>
 * @param <PK>
 */
public class ElementLogicFacade<E, PK extends Serializable> extends ElementTransactionLogicFacade<E, PK> {

    public ElementLogicFacade() {
        super();
    }

    public ElementLogicFacade(SaveTransaction sf) {
        super(sf);
    }

    /**
     * Insert in batch mode from LinkedList
     *
     * @param batchSize
     * @param list
     */
    public void insertBatch(int batchSize, LinkedList<E> list) {
        LinkedListRunnable<E> llr = new LinkedListRunnable<>(list);
        insertBatch(batchSize, llr);
    }

    /**
     * Find all elements in table.
     *
     * @return
     * @throws HibernateException
     */
    public List<E> findAll() throws HibernateException {
        return findAllByCriteria(null);
    }

    /**
     * Find all elements of table by property equals.
     * <pre>
     * {@code
     * List<User> users = facade.findByProperty("localization","Bogota");
     * }
     * </pre>
     *
     * @param property
     * @param obj
     * @return
     */
    public List<E> findAllByProperty(String property, Object obj) {
        return findAllByCriteria(new CriteriaQuery() {
            @Override
            public void addCriterion(Criteria cr) {
                cr.add(Restrictions.eq(property, obj));
            }
        });
    }

    /**
     * Find a single elements of table by property equals.
     * <pre>
     * {@code
     * User user = facade.findByProperty("localization","Bogota");
     * }
     * </pre>
     *
     * @param property
     * @param obj
     * @return
     */
    public E findByProperty(String property, Object obj) {
        return findByCriteria(new CriteriaQuery() {
            @Override
            public void addCriterion(Criteria cr) {
                cr.add(Restrictions.eq(property, obj));
            }
        });
    }

}
