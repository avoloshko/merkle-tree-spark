package crypto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MerkleTreeVerifierTest {
  private static Supplier<MerkleTreeVerifier<String>> SUBJECT = MerkleTreeVerifier::keccak256HexMerkleTreeVerifier;

  private List<String> proof = Arrays.asList("a8982c89d80987fb9a510e25981ee9170206be21af3c8e0eb312ef1d3382e761",
    "4a79d984cf5ac0e4a2e1d4e5e51c36df5c940f26174acbd0b12fce98ce6b1b57",
    "0a46541b0dc0031ecd8a8911dcc535a7347fdc248f8550d46000a012911ee3d4");

  private String root = "fc39d20543bc8933d6b075429f649ecfc67a7a1eef63ae51f907a2a1f8dddd71";

  private String leaf = "3ac225168df54212a25c1c01fd35bebfea408fdac2e31ddd6f80a4bbf9a5f1cb";

  @Test
  void testCall()  {
    assertTrue(SUBJECT.get().apply(proof, leaf, root));
  }
}
