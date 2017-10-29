package net.ericsonj.save;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ejoseph
 */
public class SaveTransaction implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(SaveTransaction.class.getName());

    private Session session;
    private Transaction tx;

    public SaveTransaction() throws SaveTransactionException {
        initOperation();
    }

    private void initOperation() throws SaveTransactionException {
        try {
            session = DBSaveUtil.getSessionFactory().openSession();
            this.tx = session.beginTransaction();
        } catch (HibernateException ex) {
            throw new SaveTransactionException("Error in init SaveTransaction", ex);
        }
    }

    @Override
    public void close() throws SaveTransactionException {
        try {
            if (session != null) {
                session.close();
                LOG.log(Level.INFO, "Close Session open in SaveTransaction");
            }
        } catch (HibernateException ex) {
            throw new SaveTransactionException("Error in close SaveTransaction", ex);
        }
    }

    public void commit() throws HibernateException {
        try {
            if (tx != null) {
                LOG.log(Level.INFO, "Commit transactions in SaveTransaction");
                tx.commit();
            }
        } catch (Throwable e) {
            throw new HibernateException(e);
        }
    }

    public void rollback() {
        if (tx != null) {
            tx.rollback();
            LOG.log(Level.SEVERE, "Rollback in SaveTransaction");
        }
    }

    public Session getSession() {
        return session;
    }

}
