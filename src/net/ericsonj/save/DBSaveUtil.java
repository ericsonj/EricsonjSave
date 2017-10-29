package net.ericsonj.save;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author ejoseph
 */
public class DBSaveUtil {

    private static final Logger LOG = Logger.getLogger(DBSaveUtil.class.getName());

    private static final SessionFactory SESSION_FACTORY;
    private static final ServiceRegistry SERVICE_REGISTRY;

    static {
        try {

            long start = System.currentTimeMillis();

            ScannerClass sc = new ScannerClass();
            List<Class> entities = sc.getClasses();

//            System.out.println((System.currentTimeMillis() - start) + "ms");
            LOG.log(Level.INFO, "EricsonHORM {0} entities found in {1}ms", new Object[]{entities.size(), System.currentTimeMillis() - start});

            Configuration configuration = new Configuration().configure();

            for (Class clz : entities) {
                configuration.addAnnotatedClass(clz);
            }

            SERVICE_REGISTRY = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            // Create the SessionFactory from hibernate.cfg.xml
            SESSION_FACTORY = configuration.buildSessionFactory(SERVICE_REGISTRY);
//            sessionFactory = new Configuration().configure().buildSessionFactory();  
            LOG.log(Level.INFO, " ==== INIT Hibernate SessionFactory ==== ");

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed  
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void closeSessionFactory() {
        if (SESSION_FACTORY instanceof SessionFactoryImpl) {
            SessionFactoryImpl sf = (SessionFactoryImpl) SESSION_FACTORY;
            ConnectionProvider conn = sf.getConnectionProvider();
            if (conn instanceof C3P0ConnectionProvider) {
                ((C3P0ConnectionProvider) conn).stop();
                LOG.log(Level.INFO, " ==== Close C3P0ConnectionProvider ==== ");
            }
        }
        SESSION_FACTORY.close();
        LOG.log(Level.INFO, " ==== Close Hibernate SessionFactory ==== ");

    }

    public static void runInConnection(Work jdbcWork) {
        Session session = getSessionFactory().openSession();
        session.doWork(jdbcWork);
        session.close();
    }

}
