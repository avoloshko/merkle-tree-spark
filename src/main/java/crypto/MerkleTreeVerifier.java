package crypto;

import java.util.List;

public class MerkleTreeVerifier<T> {
  public static MerkleTreeVerifier<byte[]> keccak256ByteMerkleTreeVerifier() {
    return new MerkleTreeVerifier<>(new KeccakBiHashFunc());
  }

  public static MerkleTreeVerifier<String> keccak256HexMerkleTreeVerifier() {
    return new MerkleTreeVerifier<>(new KeccakHexBiHashFunc());
  }

  final private BiHashFunction<T> hashFunc;

  public MerkleTreeVerifier(BiHashFunction<T> hashFunc) {
    this.hashFunc = hashFunc;
  }

  public boolean apply(List<T> proof, T leaf, T root) {
    T computedRoot = leaf;
    for (T item : proof) {
      computedRoot = hashFunc.hash(computedRoot, item);
    }

    return hashFunc.compare(computedRoot, root) == 0;
  }
}
