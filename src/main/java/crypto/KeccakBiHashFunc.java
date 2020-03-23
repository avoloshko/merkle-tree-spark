package crypto;

import org.spongycastle.crypto.digests.KeccakDigest;
import org.spongycastle.util.Arrays;

public final class KeccakBiHashFunc implements BiHashFunction<byte[]> {
  private final KeccakDigest digest;
  private final int fixedOutputLength;

  public KeccakBiHashFunc(int bitLength) {
    digest = new KeccakDigest(bitLength);
    fixedOutputLength = bitLength / 8;
  }

  public KeccakBiHashFunc() {
    this(256);
  }

  @Override
  public byte[] apply(byte[] bytes, byte[] bytes2) {
    digest.update(bytes, 0, bytes.length);
    digest.update(bytes2, 0, bytes2.length);
    byte[] out = new byte[fixedOutputLength];
    digest.doFinal(out, 0);
    return out;
  }

  @Override
  public int compare(byte[] first, byte[] second) {
    return Arrays.compareUnsigned(first, second);
  }
}
