package de.immomio.broker.queue.listener.additionalLogic;

public interface AdditionalLogic<T> {

    boolean doLogic(T obj);

}
