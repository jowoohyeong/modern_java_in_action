package chapter07;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class chapter07Main {
    static final String SENTENCE =
            " Nel    mezzo del cammin di nostra vita " +
                    "mi ritrovai in una selva oscura " +
                    "ch la dritta via era  smarrita ";
    public static void main(String[] args) {

        // 7.1.3 병렬 스트림의 올바른 사용법
        //System.out.println("SideEffect parallel sum done in: " + measurePerf(ParallelStreams::sideEffectParallelSum, 10_000_000L) + "msecs");

        // 7.1.4 병렬 스트림 효과적으로 사용하기
        /*
            1. 순차 스트림, 병렬 스트림 무엇을 선택할지 고민된다면 벤치마크로 직접 성능을 측정하는 것이 바람직하다.
            2. 자동 박싱과 언박싱은 성능을 크게 저하시킬 수 있기 때문에 기본형 특화 스트림(IntStream, LongStream, DoubleStream)을 사용하자
            3. 병렬 스트림보다 순차 스트림이 빠른 연산이 있다. 특히 limit, findFirst
            4. 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하자.
                처리 해야할 요소 수 : N, 하나의 요소 처리 비용 : Q, 전체 스트림 파이프라인 처리 비용 : N * Q
                Q가 높아질 수록 병렬 스트림으로 성능 개선 확률 높아짐
            5. 소량의 데이터인 경우 병렬 스트림은 도움되지 않는다.
            6. 스트림을 구성하는 자료 구조 확인
                ArrayList > LinkedList 효율적
                LinkedList는 모든 요소를 탐색해야지 분할 가능하지만, ArrayList는 그렇지 않아도 분할 가능
            7. 스트림의 중간 연산에 따라 분해 과정의 성능이 달라질 수 있다.
                filter를 사용할 경우 스트림의 길이를 예측할 수 없기 때문에 병렬 처리 가능 유무 확인 어려움
            8. 최종 연산의 병합 과정 비용을 살펴보자. (Collector의 combiner 메서드)
                병합 과정의 비용이 비싸다면 병렬 스트림으로 얻은 성능 이익이 서브 스트림의 부분 결과를 합치는 과정에서 상쇄될 수 있다.

                소스                분해성
            ArrayList               훌륭함
            LinkedList              나쁨
            IntStream.range         훌륭함
            Stream.iterate          나쁨
            HashSet                 좋음
            TreeSet                 좋음
        */

        // 7.2.1  RecursiveTask 활용
        //System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000L)+ " msecs");

        // 7.2.2 포크/조인 프레임워크를 제대로 사용하는 방법

        // 7.3.2 커스텀 Spliterator 구현하기
        System.out.println("Found " + WordCounter.countWordsIteratively(SENTENCE) +" Words");
        System.out.println("Found " + countWords() +" Words");

    }

    private static int countWords() {
        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);

        return countWords(stream);
    }

    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter =
                stream.parallel().reduce(
                        new WordCounter(0, true),
                        WordCounter::accumlate,
                        WordCounter::combine);
        return wordCounter.getCounter();
    }
    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
          long start = System.nanoTime();
          R result = f.apply(input);
          long duration = (System.nanoTime() - start) / 1_000_000;
          System.out.println("Result: " + result);
          if (duration < fastest) {
            fastest = duration;
          }
        }
        return fastest;
    }
}

