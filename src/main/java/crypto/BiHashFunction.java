package crypto;

import java.util.Comparator;
import java.util.function.BiFunction;

public interface BiHashFunction<T> extends BiFunction<T, T, T>, Comparator<T> {
  default T hash(T first, T second) {
    if (compare(first, second) < 0) {
      return apply(first, second);
    }
    return apply(second, first);
  }
}
