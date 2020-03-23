package crypto;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MerkleTreeTest {
  private static Function<List<String>, MerkleTree<String>> SUBJECT = MerkleTree::keccak256HexMerkleTree;

  private static Function<String, String> toKeccakHex = new KeccakHexHashFunc();

  private static Function<String, String> converter = value -> toKeccakHex.apply(Hex.toHexString(value.getBytes()));

  private List<String> leaves = Stream.of("a", "b", "c", "d", "e", "f", "g").map(converter).sorted().collect(Collectors.toList());

  @Test
  void testRoot() {
    assertEquals("fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71", SUBJECT.apply(leaves).getRoot());
  }

  @Test
  void testProof() {
    assertEquals(Arrays.asList("a8982c89d80987fb9a510e25981ee9170206be21af3c8e0eb312ef1d3382e761",
      "4a79d984cf5ac0e4a2e1d4e5e51c36df5c940f26174acbd0b12fce98ce6b1b57",
      "0a46541b0dc0031ecd8a8911dcc535a7347fdc248f8550d46000a012911ee3d4"), SUBJECT.apply(leaves).getProof(converter.apply("a")));
  }

  @Test
  void testInvalidLeaf() {
    assertThrows(NoSuchElementException.class, () -> SUBJECT.apply(leaves).getProof(converter.apply("1")));
  }

  @Test
  void testInvalidLeafIndex() {
    assertThrows(IndexOutOfBoundsException.class, () -> SUBJECT.apply(leaves).getProof(8));
  }

  @Test
  void testEmpty() {
    assertNull(SUBJECT.apply(Collections.emptyList()).getRoot());
  }

  @Test
  void testSingle() {
    assertEquals("3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb",
      SUBJECT.apply(Arrays.asList(converter.apply("a"))).getRoot());
  }
}
