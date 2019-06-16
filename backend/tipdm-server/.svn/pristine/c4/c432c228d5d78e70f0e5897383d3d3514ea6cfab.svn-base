package com.tipdm.framework.dmserver.core.scheduling;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by TipDM on 2017/1/11.
 * E-mail:devp@tipdm.com
 */
public final class AtomicEnum<T extends Enum<T>> implements Serializable{

    /**
     * The atomic reference which provides the atomicity internally.
     */
    private final AtomicReference<T> ref;

    /**
     * Constructs a new atomic enumeration object.
     *
     * @param initialValue
     *        the initial value of the enumeration
     */
    public AtomicEnum(final T initialValue) {

        this.ref = new AtomicReference<T>(initialValue);
    }

    /**
     * Sets to the given value.
     *
     * @param newValue
     *        the new value
     */
    public void set(final T newValue) {

        this.ref.set(newValue);
    }

    /**
     * Gets the current value.
     *
     * @return the current value.
     */
    public T get() {

        return this.ref.get();
    }

    /**
     * Sets the given value and returns the old value.
     *
     * @param newValue
     *        the new value
     * @return the previous value
     */
    public T getAndSet(final T newValue) {

        return this.ref.getAndSet(newValue);
    }

    /**
     * Atomically set the value to the given updated value if the current value == the expected value.
     *
     * @param expect
     *        the expected value
     * @param update
     *        the new value
     * @return <code>true</code> if successful, <code>false</code> otherwise
     */
    public boolean compareAndSet(final T expect, final T update) {

        return this.ref.compareAndSet(expect, update);
    }
}
