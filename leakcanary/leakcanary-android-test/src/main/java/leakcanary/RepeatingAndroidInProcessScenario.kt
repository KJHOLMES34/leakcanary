package leakcanary

import shark.HeapDiff
import shark.ObjectGrowthDetector
import shark.RepeatingScenarioObjectGrowthDetector
import shark.forAndroidHeap

/**
 * Creates a [RepeatingScenarioObjectGrowthDetector] suitable for Android in process tests, such
 * as Espresso tests.
 *
 * Dumps the heap by leveraging Android APIs, running an in process GC right before dumping.
 *
 * @see [RepeatingScenarioObjectGrowthDetector.findRepeatedlyGrowingObjects]
 */
fun HeapDiff.Companion.repeatingAndroidInProcessScenario(
  objectGrowthDetector: ObjectGrowthDetector = ObjectGrowthDetector.forAndroidHeap(),
  heapDumpDirectoryProvider: HeapDumpDirectoryProvider = TargetContextHeapDumpDirectoryProvider(
    heapDumpDirectoryName = "heap_dumps_object_growth"
  ),
  heapDumper: HeapDumper = HeapDumper.forAndroidInProcess()
    .withGc(gcTrigger = GcTrigger.inProcess())
    .withDetectorWarmup(objectGrowthDetector, androidHeap = true),
  heapDumpDeletionStrategy: HeapDumpDeletionStrategy = HeapDumpDeletionStrategy.DeleteOnHeapDumpClose(),
): RepeatingScenarioObjectGrowthDetector {
  return repeatingDumpingTestScenario(
    objectGrowthDetector = objectGrowthDetector,
    heapDumpDirectoryProvider = heapDumpDirectoryProvider,
    heapDumper = heapDumper,
    heapDumpDeletionStrategy = heapDumpDeletionStrategy,
  )
}
