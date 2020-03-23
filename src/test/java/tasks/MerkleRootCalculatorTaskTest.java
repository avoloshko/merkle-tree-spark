package tasks;

import com.typesafe.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import services.MerkleLeafFileReader;
import services.MerkleRootCalculator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MerkleRootCalculatorTaskTest {
  private String outputPath = ".output/" + MerkleRootCalculatorTaskTest.class.getSimpleName();

  private List<String> paths = Arrays.asList("a", "b", "c");

  private Integer partitionsCount = 10;

  @Mock
  private MerkleRootCalculator merkleRootCalculator;

  @Mock
  private MerkleLeafFileReader merkleLeafFileReader;

  @Mock
  private Config config;

  @Spy
  @InjectMocks
  private MerkleRootCalculatorTask merkleRootCalculatorTask;

  private SparkSession spark;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate();

    doReturn(spark).when(merkleRootCalculatorTask).buildSparkSession();
    when(config.getString("tasks.merkle-root-calculator.output-path")).thenReturn(outputPath);
    when(config.getString("tasks.merkle-root-calculator.paths")).thenReturn(String.join(",", paths));
    when(config.getInt("tasks.merkle-root-calculator.partitions")).thenReturn(partitionsCount);
  }

  @AfterEach
  void tearDown() throws IOException {
    FileUtils.deleteDirectory(new File(outputPath));
  }

  @Test
  void testOutput() throws IOException {
    String output = UUID.randomUUID().toString();

    Dataset<String> dataset = spark.emptyDataset(Encoders.STRING());

    when(merkleLeafFileReader.call(spark, paths)).thenReturn(dataset);

    when(merkleRootCalculator.call(dataset, 0L, partitionsCount)).thenReturn(output);

    merkleRootCalculatorTask.run();

    String fileContent = FileUtils.readFileToString(
      FileUtils.iterateFiles(new File(outputPath), new String[]{"txt"}, false).next()).trim();

    assertEquals(output, fileContent);

    verify(merkleRootCalculatorTask, times(1)).buildSparkSession();
  }
}
