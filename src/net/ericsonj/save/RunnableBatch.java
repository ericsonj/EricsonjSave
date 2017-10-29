package net.ericsonj.save;

/**
 *
 * @author ejoseph
 * @param <E>
 */
public interface RunnableBatch<E> {

    public boolean hasMoreElements();

    public abstract E getEntity();

}
