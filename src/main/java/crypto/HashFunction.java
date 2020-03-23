package crypto;

import java.util.function.Function;

public interface HashFunction<T> extends Function<T, T> {
  default T hash(T value) {
    return apply(value);
  }
}
