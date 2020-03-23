package crypto;

import org.spongycastle.util.encoders.Hex;

public final class KeccakHexBiHashFunc implements BiHashFunction<String> {
  private KeccakBiHashFunc keccakBiHashFunc = new KeccakBiHashFunc();

  @Override
  public String apply(String hex, String hex2) {
    return Hex.toHexString(keccakBiHashFunc.apply(Hex.decode(hex), Hex.decode(hex2)));
  }

  @Override
  public int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }
}
