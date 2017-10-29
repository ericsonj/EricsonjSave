package net.ericsonj.save;

import java.util.LinkedList;

/**
 *
 * @author ejoseph
 * @param <E>
 */
public class LinkedListRunnable<E> implements RunnableBatch<E> {

    private final LinkedList<E> list;
    private int count;

    public LinkedListRunnable(LinkedList<E> list) {
        this.list = list;
        this.count = 0;
    }

    @Override
    public boolean hasMoreElements() {
        return count <= (list.size() - 1);
    }

    @Override
    public E getEntity() {
        E element = list.get(count);
        count++;
        return element;
    }

}
