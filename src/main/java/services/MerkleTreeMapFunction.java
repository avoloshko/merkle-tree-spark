package services;

import crypto.MerkleTreeStream;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;

public class MerkleTreeMapFunction implements FlatMapFunction<Iterator<String>, String>, Serializable {
  private static final Logger logger = LoggerFactory.getLogger(MerkleTreeMapFunction.class);

  @Override
  public Iterator<String> call(Iterator<String> input) {
    logger.info("Thread: " + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());

    MerkleTreeStream<String> merkleTreeStream = MerkleTreeStream.keccak256HexMerkleTreeStream();
    merkleTreeStream.write(input);
    return Collections.singleton(merkleTreeStream.getRoot()).iterator();
  }
}
