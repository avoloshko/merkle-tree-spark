package tasks;

import com.typesafe.config.Config;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.MerkleLeafFileReader;
import services.MerkleRootCalculator;
import state.Context;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

public class MerkleRootCalculatorTask {
  private static final Logger logger = LoggerFactory.getLogger(MerkleRootCalculatorTask.class);

  public static void main(String[] args) {
    Context.INSTANCE.getInjector().getInstance(MerkleRootCalculatorTask.class).run();
  }

  @Inject
  MerkleRootCalculator merkleRootCalculator;

  @Inject
  MerkleLeafFileReader merkleLeafFileReader;

  @Inject
  Config config;

  public void run() {
    SparkSession spark = buildSparkSession();

    Dataset<String> leaves = merkleLeafFileReader.call(spark,
            Arrays.asList(config.getString("tasks.merkle-root-calculator.paths").split(",")));

    String root = merkleRootCalculator.call(
            leaves,
            leaves.count(),
            config.getInt("tasks.merkle-root-calculator.partitions"));

    saveToFile(spark.createDataset(Collections.singletonList(root), Encoders.STRING()));

    spark.close();
  }

  SparkSession buildSparkSession() {
    return SparkSession.builder()
            .master(config.getString("spark.master"))
            .appName("MerkleRootCalculatorTask")
            .config("spark.sql.session.timeZone", "UTC")
            .getOrCreate();
  }

  void saveToFile(Dataset<String> output) {
    String outputPath = config.getString("tasks.merkle-root-calculator.output-path");
    output
      .write()
      .format("text")
      .mode(SaveMode.Overwrite)
      .save(outputPath);

    logger.info("saved to: " + outputPath);
  }
}