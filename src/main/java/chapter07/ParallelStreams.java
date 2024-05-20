package chapter07;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

  public static void main(String ... args){
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");

        // 소요 시간 체크, iterativceSum, sequentialSum, parallelSum
        runningTimeCheck();

  }

  public static long iterativeSum(long n) {
    long result = 0;
    for (long i = 0; i <= n; i++) {
      result += i;
    }
    return result;
  }

  public static long sequentialSum(long n) {
    return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
  }

  public static long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
  }

  public static long rangedSum(long n) {
    return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
  }

  public static long parallelRangedSum(long n) {
    return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
  }

  public static long sideEffectSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).forEach(accumulator::add);
    return accumulator.total;
  }

  public static long sideEffectParallelSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
    return accumulator.total;
  }

  public static class Accumulator {

    private long total = 0;

    public void add(long value) {
      total += value;
    }

  }
  public static void runningTimeCheck(){
        long n = 242231;
        long startTime = System.nanoTime();
        System.out.println(ParallelStreams.iterativeSum(n));
        long endTime = System.nanoTime();        // 종료 시간 측정
        long duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)
        System.out.println("소요된 시간 (iterativeSum): " + duration / 1000000);


        startTime = System.nanoTime();
        System.out.println(ParallelStreams.sequentialSum(n));
        endTime = System.nanoTime();        // 종료 시간 측정
        duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)
        System.out.println("소요된 시간 (sequentialSum): " + duration / 1000000);

        startTime = System.nanoTime();
        System.out.println(ParallelStreams.parallelSum(n));
        endTime = System.nanoTime();        // 종료 시간 측정
        duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)
        System.out.println("소요된 시간 (parallelSum): " + duration / 1000000);
    }
}