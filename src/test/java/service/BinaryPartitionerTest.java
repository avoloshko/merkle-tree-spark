package service;

import org.junit.jupiter.api.Test;
import services.BinaryPartitioner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryPartitionerTest {
  @Test
  void test() throws Exception  {
    assertEquals(4, BinaryPartitioner.getPartitionMaxItemsCount(14, 4));
    assertEquals(131072, BinaryPartitioner.getPartitionMaxItemsCount(1000000, 8));
    assertEquals(4, BinaryPartitioner.getPartitionMaxItemsCount(40, 7));
    assertEquals(1024, BinaryPartitioner.getPartitionMaxItemsCount(1024, 1));
    assertEquals(512, BinaryPartitioner.getPartitionMaxItemsCount(1024, 2));
    assertEquals(512, BinaryPartitioner.getPartitionMaxItemsCount(1023, 1));
    assertEquals(1024, BinaryPartitioner.getPartitionMaxItemsCount(1025, 1));
  }
}
