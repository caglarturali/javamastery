package blog.javamastery.dsa.common;

import java.util.stream.Stream;

public interface Streamable<T> {
    Stream<T> stream();
}
