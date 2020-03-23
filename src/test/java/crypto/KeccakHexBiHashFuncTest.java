package crypto;

import org.junit.jupiter.api.Test;
import org.spongycastle.util.encoders.DecoderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeccakHexBiHashFuncTest {
  private static BiHashFunction<String> SUBJECT = new KeccakHexBiHashFunc();

  @Test
  void testEmpty() {
    assertEquals("c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470",
            SUBJECT.hash("", ""));
  }

  @Test
  void testOne() {
    assertEquals("1f1f158e1cf921d82112c89c50778e37ba1a459656b83e00841e6e2a101b2d86",
            SUBJECT.apply("a8982c89d80987fb9a510e25981ee9170206be21af3c8e0eb312ef1d3382e761", ""));
  }

  @Test
  void testMerge1() {
    assertEquals("2d70ff3ba26a3f04409c6648088f1cc29eebe94ef074046abf9966f0777edaf1",
            SUBJECT.apply(
                    "3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb",
                    "fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71"));
  }

  @Test
  void testMerge2() {
    assertEquals("8270be3ef3248882b3acff5ed8ab5496c41bca871509201b9bd5b37050eb9ab3",
            SUBJECT.apply(
                    "fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71",
                    "3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb"));
  }

  @Test
  void testInvalidHex() {
    assertThrows(DecoderException.class,
            () -> SUBJECT.apply("z5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470", ""));
  }

  @Test
  void testHash() {
    assertEquals("2d70ff3ba26a3f04409c6648088f1cc29eebe94ef074046abf9966f0777edaf1",
            SUBJECT.hash(
                    "3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb",
                    "fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71"));
  }

  @Test
  void testHash2() {
    assertEquals("2d70ff3ba26a3f04409c6648088f1cc29eebe94ef074046abf9966f0777edaf1",
            SUBJECT.hash(
                    "fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71",
                    "3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb"));
  }
}