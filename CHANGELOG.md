# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Unreleased
### Added:
- TimestampSerializer with UTC based outputs (2019-11-24T00:38:00Z).
- StreamingCubeGeneratorTask is tested and integration tests added.
- Compound generation service and integration tests.
- Predictable and deterministic compound id.
- BatchSegmentGeneratorTask is tested and integration tests added.
- Batch compound generation task
- StreamingCompoundGeneratorTask and integration tests.
- BatchCubeGeneratorTask is tested and integration tests added.
- `MapFactory` for metrics maps.
- Improves BatchCompoundGeneratorTask by reducing the number of partitions before processing.
- Merkle tree infrastructure in crypto package
- Skeleton for `StateRootGeneratorTask`
- Adds support to list files in batch segment generator
- Stats for execution time of root and segment batch tasks
- view_through_conversion type
- Add `deviceType, bundleId, storeUrl, storeId, propertyType` to dimensions
- click_through_conversion type

### Changed:
- Remove `Date` and `SimpleDateFormat` usage with `Timestamp` and `TimestampExtractor`. 
- Proper way of using checkpointing in `StreamingCubeGeneratorTask`
- Ensure that `EventExtractor` sets `computedAt` and `routedAt` in a proper way
- Unify date time format
- Unify partition directories names to use always two digits for month, day, hour and minute.

### Fixed
- Ensure `SimpleDateFormat` is used in a thread safe way.
- Make sure default output compression for cubes is the same as default input compression for
  segments
- Ensure `QueryStringMapEncoder` generates predictable output. 
  This makes cube id and any calculation based on dimensions fully predictable.

### Removed
- Direct Injection
