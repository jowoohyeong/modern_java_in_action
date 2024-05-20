import DTO.Dish;
import DTO.Trader;
import DTO.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class test05 extends dataStore{

    public test05(){
        long startTime = System.nanoTime();
        List<Dish> filteredMenu = specialMenu.stream()
                .filter(dish->dish.getCalories()< 320)
                .collect(Collectors.toList());

        //anyWhile();
        //Mapping();
        //Finding();
        //Reducing();
        //Practicing()

        // 7. 숫자형 스트림
        // IntTypeStream()

        // 8. 스트림 만들기
        // makingStream();

        // 실행할 코드

        //예제 5.2.1 중복제거
        //stream512(numbers);

        long endTime = System.nanoTime();        // 종료 시간 측정
        long duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)

        System.out.println("소요된 시간 (밀리초): " + duration / 1000000);
    }
    public void normalReduce(){
        long startTime = System.nanoTime();
        // 실행할 코드
        System.out.println(numbers.stream().reduce(Integer::sum));
        long endTime = System.nanoTime();        // 종료 시간 측정
        long duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)

        System.out.println("소요된 시간 (밀리초): " + duration / 1000000);
    }
    public void parallelReduce(){
        long startTime = System.nanoTime();
        // 실행할 코드
        System.out.println(numbers.parallelStream().reduce(Integer::sum));
        long endTime = System.nanoTime();        // 종료 시간 측정
        long duration = (endTime - startTime);          // 소요된 시간 계산 (나노초 단위)

        System.out.println("소요된 시간 (밀리초): " + duration / 1000000);
    }
    public void anyWhile(){
        // p.158    takeWhile 정렬되어 있을 때 사용, 처음으로 거짓이 되는 지점 전까지의 요소 출력
        List<Dish> slicedMenu1 = specialMenu.stream()
                .takeWhile(dish->dish.getCalories()> 320)
                .collect(Collectors.toList());
        System.out.println(slicedMenu1);

        // dropWhile: takeWhile와 반대 처음으로 거짓이 되는 지점까지의 요소들을 버리고 출력
        List<Dish> slicedMenu2 = specialMenu.stream()
                .dropWhile(dish->dish.getCalories() > 320)
                .collect(Collectors.toList());
        System.out.println(slicedMenu2);

        List<Dish> dishes = specialMenu.stream()
               .dropWhile(dish->dish.getCalories() > 320)
               .skip(2)
               .collect(Collectors.toList());
        System.out.println(dishes);

    }

    public void Mapping(){
        specialMenu.stream().map(Dish::getName).map(String::length).forEach(System.out::println);

        //flat 평면화된 stream 반환
        List<String> words = Arrays.asList("Hello", "World");
        words = words.stream().map(word->word.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        System.out.println(words);
    }

    public void Finding(){
        if(menu.stream().anyMatch(Dish::isVegetarian)){
            System.out.println("The menu (somewhat) vegetarian friendly");
        }

        System.out.println(menu.stream().allMatch(dish -> dish.getCalories() < 1000));
        System.out.println(menu.stream().noneMatch(dish -> dish.getCalories() >= 1000));

        Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();
        System.out.println(dish);
        dish.ifPresent(System.out::println);

        someNumbers.stream()
                .map(x->x*x)
                .filter(x->x%3==0)
                .findFirst()
                .ifPresent(System.out::println);
    }

    void Reducing(){
        System.out.println(numbers.stream().reduce(0, (a, b) -> a + b));
        System.out.println(numbers.stream().reduce(0, Integer::sum));
        System.out.println(numbers.stream().reduce(0, Integer::max));
        System.out.println(numbers.stream().reduce(0, Integer::min));

        normalReduce();
        parallelReduce();

        System.out.println(menu.stream().map(Dish::getCalories).reduce(0, Integer::sum));
    }

    void Practicing(){
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );

        transactions.stream()
                .filter(tran-> tran.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .forEach(System.out::println);

        transactions.stream()
                .map(trans->trans.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);

        transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .forEach(System.out::println);

        System.out.println(transactions.stream().anyMatch(trans -> trans.getTrader().getCity().equals("Milan")));
        transactions.stream()
                .filter(trans-> trans.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        System.out.println(transactions.stream().map(Transaction::getValue).reduce(Integer::max).get());
        System.out.println(transactions.stream().map(Transaction::getValue).reduce(Integer::min).get());

    }

    public void IntTypeStream(){
        //p.181
        OptionalInt maxCal =menu.stream().mapToInt(Dish::getCalories).max();
        System.out.println(maxCal.orElse(1));   //orElse 값이 없을때 1

        IntStream evenNum = IntStream.rangeClosed(1, 100).filter(n-> n%2==0);
        System.out.println(evenNum.count());

        Stream<double[]> pythagoreanTriples =
               IntStream.rangeClosed(1,100)
                       .boxed()
                       .flatMap(a->IntStream.rangeClosed(a, 100)
                       .mapToObj(b-> new double[]{a, b, Math.sqrt(a*a + b*b)})
                       .filter(t-> t[2] % 1 == 0));

       pythagoreanTriples.limit(5)
               .forEach(t -> System.out.println(t[0] +","+ t[1] +","+t[2]));
    }
    void makingStream(){
        Stream<String> stream = Stream.of("modern", "java", "in", "action");
        stream.map(String::toUpperCase).forEach(System.out::println);

        // null stream 생성
        Stream<String> emptyStream = Stream.empty();
        Stream<String> ifNullStream = Stream.ofNullable(System.getProperty("home"));

        Stream<String> values =
                Stream.of("config", "home", "user")
                        .flatMap(key -> Stream.ofNullable(System.getProperty(key)));

        int sum = Arrays.stream(number2).sum();
        System.out.println(sum);

        // 메모장 고유 단어 갯수 확인
        long uniWords = 0;
        try(Stream<String> lines = Files.lines(Paths.get("C:\\Users\\TIGEN0802\\Desktop\\data.txt"), Charset.defaultCharset())){
            uniWords =
                    lines.flatMap(line -> Arrays.stream(line.split(" ")))
                            .distinct()
                            .count();
        } catch(IOException e){
        }
        System.out.println(uniWords);

        // 무한 스트림 or 언바운드 스트림
        Stream.iterate(0, n->n+2)
                .limit(10)
                .forEach(System.out::print);

        // 피보나치 수열
        Stream.iterate(new int[]{0, 1}, t-> new int[]{t[1] , t[0]+t[1]})
             .limit(10)
             .map(t->t[0])
             .forEach(System.out::println);


        IntStream.iterate(0, n->n+4)
            .takeWhile(n-> n< 100)
            // == .iterate(0, n-> n<100,n->n+4)
            .forEach(System.out::println);

        // 랜덤값
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        // 무한 스트림
        IntStream ones = IntStream.generate(()-> 1);
        IntStream twos = IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 2;
            }
        });

        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib)
                .limit(10)
                .forEach(System.out::print);
    }
    public void stream512(List<Integer> numbers){
        numbers.stream()
            .filter(i->i%2==0)
            .distinct()
            .forEach(System.out::println);
    }

}
