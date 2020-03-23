package crypto;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.Hex;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MerkleTreeStreamTest {
  private static Supplier<MerkleTreeStream<String>> SUBJECT = MerkleTreeStream::keccak256HexMerkleTreeStream;

  private static Function<String, String> toKeccakHex = new KeccakHexHashFunc();

  private static Function<String, String> converter = value -> toKeccakHex.apply(Hex.toHexString(value.getBytes()));

  private List<String> leaves = Stream.of("a", "b", "c", "d", "e", "f", "g").map(converter).sorted().collect(Collectors.toList());

  @Test
  void testRoot() {
    assertEquals("fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71",
      SUBJECT.get().write(leaves).getRoot());
  }

  @Test
  void testEmpty() {
    assertNull(SUBJECT.get().getRoot());
  }

  @Test
  void testSingle() {
    assertEquals("3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb",
      SUBJECT.get().write(converter.apply("a")).getRoot());
  }
}
