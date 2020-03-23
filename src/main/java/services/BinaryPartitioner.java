package services;

import org.apache.spark.Partitioner;

public class BinaryPartitioner extends Partitioner {
  private final int partitionsCount;
  private final int maxItemsCount;

  public BinaryPartitioner(long itemsCount, int partitionsCount) {
    this.maxItemsCount = getPartitionMaxItemsCount(itemsCount, partitionsCount);
    this.partitionsCount = getPartitionsCount(itemsCount, this.maxItemsCount);
  }

  @Override
  public int numPartitions() {
    return partitionsCount;
  }

  @Override
  public int getPartition(Object key) {
    return ((Number) key).intValue() / maxItemsCount;
  }

  public static int getPartitionsCount(long itemsCount, int partitionMaxItemsCount) {
    return (int) (itemsCount / partitionMaxItemsCount + (itemsCount % partitionMaxItemsCount == 0 ? 0 : 1));
  }
  
  public static int getPartitionMaxItemsCount(long itemsCount, int partitionsCount) {
    int partitionMaxItemsCount = 1 << (63 - Long.numberOfLeadingZeros(itemsCount));
    while (getPartitionsCount(itemsCount, partitionMaxItemsCount) < partitionsCount) {
      partitionMaxItemsCount /= 2;
    }
    return partitionMaxItemsCount;
  }
}