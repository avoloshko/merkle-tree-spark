# merkle-tree-spark
## Overview
Computes Merkle Tree root on Spark cluster.

## Setup

### Install Java 1.8
* https://medium.com/w-logs/installing-java-11-on-macos-with-homebrew-7f73c1e9fadf

```
export JAVA_HOME=`/usr/libexec/java_home -v 11.0.6`
```

## Build jar
```
./gradlew build
```

jar can be found in `target/libs`.

## Test

```
./gradlew clean test 
```

## Run

Used for local testing:
1. Use spark 2.4.4 with hadoop 2.7
1. Install scala 2.11 locally

```bash
TASKS_MERKLE_ROOT_CALCULATOR_PATHS="./input" \
TASKS_MERKLE_ROOT_CALCULATOR_OUTPUT_PATH="./output" \
TASKS_MERKLE_ROOT_CALCULATOR_PARTITIONS="4" \
spark-submit \
--class tasks.MerkleRootCalculatorTask \
--master "local[*]" \
./target/libs/merkle-tree-spark.jar
```

## References

