package blog.javamastery.datastructures.common;

import java.util.stream.Stream;

public interface Streamable<T> {
    Stream<T> stream();
}
