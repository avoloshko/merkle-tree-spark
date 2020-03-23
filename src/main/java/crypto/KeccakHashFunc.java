package crypto;

import org.spongycastle.crypto.digests.KeccakDigest;

public final class KeccakHashFunc implements HashFunction<byte[]> {
  private final KeccakDigest digest;
  private final int fixedOutputLength;

  public KeccakHashFunc(int bitLength) {
    digest = new KeccakDigest(bitLength);
    fixedOutputLength = bitLength / 8;
  }

  public KeccakHashFunc() {
    this(256);
  }

  @Override
  public byte[] apply(byte[] bytes) {
    digest.update(bytes, 0, bytes.length);
    byte[] out = new byte[fixedOutputLength];
    digest.doFinal(out, 0);
    return out;
  }
}
