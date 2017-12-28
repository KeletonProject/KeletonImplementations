package org.kucro3.keleton.impl.home;

import org.kucro3.keleton.world.home.exception.HomeStorageException;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Helper {
    public static <T, R> Function<T, R> wrapStorageException(FunctionWithException<T, R> func)
    {
        return wrapStorageException(func, null);
    }

    public static <T, R> Function<T, R> wrapStorageException(FunctionWithException<T, R> func, String message)
    {
        return wrapException(func, (e, m) -> {
            throw m == null ? new HomeStorageException(e) : new HomeStorageException(m, e);
        }, message);
    }

    public static <T, R> Function<T, R> wrapException(FunctionWithException<T, R> func, BiFunction<Exception, String, R> exceptionConsumer)
    {
        return wrapException(func, exceptionConsumer, null);
    }

    public static <T, R> Function<T, R> wrapException(FunctionWithException<T, R> func, BiFunction<Exception, String, R> exceptionConsumer, String message)
    {
        return (t) -> {
            try {
                return func.apply(t);
            } catch (Exception e) {
                return exceptionConsumer.apply(e, message);
            }
        };
    }

    public static interface FunctionWithException<T, R>
    {
        R apply(T t) throws Exception;
    }
}
