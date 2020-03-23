package service;

import crypto.KeccakHashFunc;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.spongycastle.util.encoders.Hex;
import services.MerkleLeafFileReader;
import services.MerkleRootCalculator;

import java.io.*;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MerkleRootCalculatorTest {
  private SparkSession spark;

  private Function<byte[], byte[]> hashFunc = new KeccakHashFunc();

  @InjectMocks
  private MerkleRootCalculator merkleRootCalculator;

  @BeforeEach
  void setUp() {
    spark = SparkSession.builder()
            .master("local[*]")
            .getOrCreate();

    MockitoAnnotations.initMocks(this);
  }

  @AfterEach
  void tearDown() {
    spark.stop();
  }

  @Test
  void test10K() throws Exception {
    File tempFile = File.createTempFile("temp", Long.toString(System.nanoTime()));
    genFile(tempFile.getAbsolutePath(), 10000);

    testLocal("4c9932afd931af797388a765acd2b117b6d7987369fd07e10e3b1d3c48690ceb",
            tempFile.getAbsolutePath());

    testDistributed("4c9932afd931af797388a765acd2b117b6d7987369fd07e10e3b1d3c48690ceb",
            tempFile.getAbsolutePath());
  }

  void genFile(String filePath, int itemsCount) throws IOException {
    try (Writer writer = new BufferedWriter(new FileWriter(filePath))) {
      IntStream.range(0, itemsCount).forEach(i -> {
        try {
          writer.write(Hex.toHexString(hashFunc.apply(("" + i).getBytes())));
          writer.write(System.lineSeparator());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  void testDistributed(String root, String filePath) throws Exception {
    FileSystem fs = LocalFileSystem.get(spark.sparkContext().hadoopConfiguration());
    int itemsCount = (int) (fs.getFileStatus(new Path(filePath)).getLen() / 65);

    //JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
    //JavaRDD<String> leaves = sc.textFile(filePath);

    Dataset<String> leaves = new MerkleLeafFileReader().call(spark,
            Collections.singletonList(filePath));

    String result = merkleRootCalculator.call(leaves, itemsCount,
            Runtime.getRuntime().availableProcessors());

    assertEquals(root, result);
  }

  void testLocal(String root, String filePath) throws Exception {
    Dataset<String> leaves = new MerkleLeafFileReader().call(spark,
            Collections.singletonList(filePath));

    String result = merkleRootCalculator.call(leaves);

    assertEquals(root, result);
  }
}
