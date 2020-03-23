package crypto;

        import org.junit.jupiter.api.Test;
        import org.spongycastle.util.encoders.DecoderException;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeccakHexHashFuncTest {
  private static HashFunction<String> SUBJECT = new KeccakHexHashFunc();

  @Test
  void testEmpty() {
    assertEquals("c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470",
            SUBJECT.hash(""));
  }

  @Test
  void testOne() {
    assertEquals("1f1f158e1cf921d82112c89c50778e37ba1a459656b83e00841e6e2a101b2d86",
            SUBJECT.apply("a8982c89d80987fb9a510e25981ee9170206be21af3c8e0eb312ef1d3382e761"));
  }

  @Test
  void testInvalidHex() {
    assertThrows(DecoderException.class,
            () -> SUBJECT.apply("z5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470"));
  }
}
