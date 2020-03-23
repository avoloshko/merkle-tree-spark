package crypto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MerkleTreeStream<T> {
  public static MerkleTreeStream<byte[]> keccak256ByteMerkleTreeStream() {
    return new MerkleTreeStream<>(new KeccakBiHashFunc());
  }

  public static MerkleTreeStream<String> keccak256HexMerkleTreeStream() {
    return new MerkleTreeStream<>(new KeccakHexBiHashFunc());
  }

  final private BiHashFunction<T> hashFunc;
  final private List<T> layers = new ArrayList<>();

  public MerkleTreeStream(BiHashFunction<T> hashFunc) {
    this.hashFunc = hashFunc;
  }

  public T getRoot() {
    T res = null;

    for (T item : layers) {
      if (item != null) {
        res = res == null ? item : hashFunc.hash(res, item);
      }
    }

    return res;
  }

  public MerkleTreeStream<T> write(Iterable<T> item) {
    item.forEach(this::write);
    return this;
  }

  public MerkleTreeStream<T> write(Iterator<T> item) {
    while (item.hasNext()) {
      write(item.next());
    }
    return this;
  }

  public MerkleTreeStream<T> write(T item) {
    int index = 0;
    while (true) {
      if (layers.size() == index) {
        layers.add(null);
      }

      if (layers.get(index) == null) {
        layers.set(index, item);
        break;
      }

      item = hashFunc.hash(layers.get(index), item);
      layers.set(index, null);
      index += 1;
    }

    return this;
  }
}
