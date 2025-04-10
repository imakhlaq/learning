package com.sharefile.securedoc.function;

/*

Function<String,Integer>  -> it represents a function that takes one parameter(String) and returns a value(Integer)
BiFunction<String,Integer,Double>  -> it represents a function that takes two parameter(String,Integer) and returns a value(Double)

Consumer<String> -> it represent a Function that takes one parameter and return void
Consumer<String,String> -> it represent a Function that takes two parameter and return void

Producer<

 */

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);

//    default Consumer<T, U, V> andThen (TriConsumer<? super T> after) {
//        Objects.requireNonNull(after);
//        return (T t, U u, V v) -> {accept(t, u, v); after.accept(t,u,v );};
//    }
}