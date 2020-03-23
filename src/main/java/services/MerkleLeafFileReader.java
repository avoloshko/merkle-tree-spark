package services;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import scala.collection.JavaConverters;

import java.util.List;

public class MerkleLeafFileReader {
  public Dataset<String> call(SparkSession spark, List<String> paths) {
    return spark.read()
            .format("text")
            .load(JavaConverters.asScalaIteratorConverter(paths.iterator()).asScala().toSeq())
            .as(Encoders.STRING());
  }
}