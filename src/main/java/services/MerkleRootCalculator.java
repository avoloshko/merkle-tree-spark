package services;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import scala.Tuple2;

import static org.apache.spark.sql.functions.col;

public class MerkleRootCalculator {
  public String call(Dataset<String> leaves) {
    Dataset<String> sortedLeaves = leaves.orderBy(col("value"));
    return new MerkleTreeMapFunction().call(sortedLeaves.toLocalIterator()).next();
  }

  public String call(Dataset<String> leaves, long itemsCount, int partitionsCount) {
    JavaRDD<String> subRoots = leaves
            .orderBy(col("value"))
            .javaRDD()
            .zipWithIndex()
            .mapToPair(Tuple2::swap)
            .partitionBy(new BinaryPartitioner(itemsCount, partitionsCount))
            .map(it -> it._2)
            .mapPartitions(new MerkleTreeMapFunction());

    return new MerkleTreeMapFunction().call(subRoots.toLocalIterator()).next();
  }
 }
