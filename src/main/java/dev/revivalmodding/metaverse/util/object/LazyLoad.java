package dev.revivalmodding.metaverse.util.object;

import java.util.function.Supplier;

/**
 * Loads the held object once {@link #get()} is called
 * @param <T> - held type
 *
 * @author Toma
 */
public class LazyLoad<T> implements Supplier<T> {

    private T t;
    private Supplier<T> supplier;

    public LazyLoad(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if(t == null) {
            t = supplier.get();
            supplier = null;
        }
        return t;
    }
}
