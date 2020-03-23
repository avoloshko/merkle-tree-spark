package crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class MerkleTree<T> {
  public static MerkleTree<byte[]> keccak256ByteMerkleTree(List<byte[]> leaves) {
    return new MerkleTree<>(leaves, new KeccakBiHashFunc());
  }

  public static MerkleTree<String> keccak256HexMerkleTree(List<String> leaves) {
    return new MerkleTree<>(leaves, new KeccakHexBiHashFunc());
  }

  final private BiHashFunction<T> hashFunc;
  final private List<List<T>> layers;

  public MerkleTree(List<T> leaves, BiHashFunction<T> hashFunc) {
    this.hashFunc = hashFunc;

    layers = createLayers(leaves);
  }

  public T getRoot() {
    return layers.get(layers.size() - 1).get(0);
  }

  public List<T> getProof(int index) {
    if (index < 0 || index >= layers.get(0).size()) {
      throw new IndexOutOfBoundsException(String.format("Element for index '%d' does not exist", index));
    }

    List<T> result = new ArrayList<>(layers.size());
    for (List<T> layer : layers) {
      T pairElement = pair(index, layer);
      if (pairElement != null) {
        result.add(pairElement);
      }
      index = index / 2;
    }
    return result;
  }


  public List<T> getProof(T leaf) {
    int index = layers.get(0).indexOf(leaf);
    if (index == -1) {
      throw new NoSuchElementException(String.format("Element '%s' does not exist", leaf));
    }

    return getProof(index);
  }

  private List<List<T>> createLayers(List<T> leaves) {
    if (leaves.size() == 0) {
      return Collections.singletonList(Collections.singletonList(null));
    }

    List<List<T>> layers = new ArrayList<>();
    layers.add(leaves);

    while (layers.get(layers.size() - 1).size() > 1) {
      layers.add(nextLayer(layers.get(layers.size() - 1)));
    }

    return layers;
  }

  private List<T> nextLayer(List<T> elements) {
    List<T> result = new ArrayList<>();

    for (int i = 1; i <= elements.size(); i += 2) {
      result.add(i < elements.size() ? hashFunc.hash(elements.get(i - 1), elements.get(i)) : elements.get(i - 1));
    }
    return result;
  }

  private T pair(int index, List<T> layer) {
    int pairIndex = index % 2 == 0 ? index + 1 : index - 1;
    return pairIndex < layer.size() ? layer.get(pairIndex) : null;
  }
}
