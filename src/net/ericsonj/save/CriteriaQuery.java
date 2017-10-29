package net.ericsonj.save;

import org.hibernate.Criteria;

/**
 *
 * @author ejoseph
 */
public interface CriteriaQuery {
    public abstract void addCriterion(Criteria cr);
}
