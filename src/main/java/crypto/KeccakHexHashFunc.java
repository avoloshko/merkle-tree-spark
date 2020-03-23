package crypto;

import org.spongycastle.util.encoders.Hex;

public final class KeccakHexHashFunc implements HashFunction<String> {
  private KeccakHashFunc keccakBiHashFunc = new KeccakHashFunc();

  @Override
  public String apply(String hex) {
    return Hex.toHexString(keccakBiHashFunc.apply(Hex.decode(hex)));
  }
}
