import DTO.Dish;
import DTO.Transaction;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static DTO.Dish.dishTags;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.reducing;

public class test06 extends dataStore{
    public test06(){
        // 예제 6-1
        System.out.println(menu);
        System.out.println("---------------------------------------");

        // collect() groupingBy key값으로 묶기 -> 각 키와 그에 대응하는 요소 리스트로 구성된 맵으로 반환
        // collector()
        // reducing62()
        // Quiz1();
        // grouping63();

        // Paritioning64();
        // Quiz2();

        // Collector



    }

    public Map<Boolean, List<Integer>> partitionPrimes(int n){
        return IntStream.rangeClosed(2, n).boxed().collect(
                partitioningBy(this::isPrime)
        );
    }
    public boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt((double)candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i-> candidate % i == 0 );
    }

    public static <A> List<A> takeWhile(List<A> list, Predicate p){
        int i=0;
        for(A item : list){
            if(!p.test(item)){
                return list.subList(0,i);
            }
            i++;
        }
        return list;
    }
    public boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return primes.stream()
                .takeWhile(i-> i<=candidateRoot)
                .noneMatch(i-> candidate % i == 0);
    }
  /*  public boolean isPrime2(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i-> i <= candidateRoot)
                .stream()
                .noneMatch(i-> candidate % i == 0);
    }*/
    public enum CaloricLevel{ DIET, NORMAL, FAT}

    void Paritioning64(){
        Map<Boolean, List<Dish>> partitionedMenu =
                menu.stream().collect(partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);
        System.out.println(partitionedMenu.get(true));

        List<Dish> vegetarianDishes =
                menu.stream().filter(Dish::isVegetarian).collect(toList());
        System.out.println(vegetarianDishes);

        //6.4.1
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
                menu.stream().collect(
                        partitioningBy(Dish::isVegetarian,
                            groupingBy(Dish::getType)));
        System.out.println(vegetarianDishesByType);

        Map<Boolean, Dish> mostCaloriesPartitionedByVegetarian = menu.stream().collect(
                partitioningBy(Dish::isVegetarian,
                        collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get))
        );
        System.out.println(mostCaloriesPartitionedByVegetarian);

        // 6.4.2
        System.out.println(partitionPrimes(10));
        System.out.println(partitionPrimes(15));
        System.out.println(partitionPrimes(23));
    }

    void grouping63(){
        System.out.println(menu.stream().collect(groupingBy(Dish::getType)));

        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish-> {
                    if(dish.getCalories() <= 400 ) return CaloricLevel.DIET;
                    else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
        System.out.println(dishesByCaloricLevel);

        Map<Dish.Type, List<Dish>> caloricDishesByType =
            menu.stream().filter(dish-> dish.getCalories() >= 500 )
                            .collect(groupingBy(Dish::getType));
        System.out.println(caloricDishesByType);

        Map<Dish.Type, List<Dish>> caloricDishesByType2 =
            menu.stream()
                .collect(groupingBy(Dish::getType,
                         filtering(dish -> dish.getCalories() > 500, toList())));
        System.out.println(caloricDishesByType2);

        Map<Dish.Type, List<String>> caloricDishesByType3 =
            menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        System.out.println(caloricDishesByType3);

        Map<Dish.Type, Set<String>> dishNamesByType =
                menu.stream().collect(groupingBy(Dish::getType,
                        flatMapping(dish-> dishTags.get(dish.getName()).stream(), toSet())
                ));
        System.out.println(dishNamesByType);


        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishNamesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish-> {
                                    if(dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })
                        ));
        System.out.println(dishNamesByTypeCaloricLevel);

        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting()));

        System.out.println(typesCount);

        Map<Dish.Type, Optional<Dish>> mostCaloricByType =
                       menu.stream().collect(
                           groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));

        System.out.println(mostCaloricByType);

        // 예제 6-3
        Map<Dish.Type, Dish> mostCaloricByType2 =
               menu.stream().collect(
                   groupingBy(Dish::getType,
                           collectingAndThen(
                                   maxBy(comparingInt((Dish::getCalories))), Optional::get
                           )));
        System.out.println(mostCaloricByType2);

        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream().collect(
               groupingBy(Dish::getType,
                       summingInt(Dish::getCalories))
        );
        System.out.println(totalCaloriesByType);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType = menu.stream().collect(
               groupingBy(Dish::getType,
                       mapping(dish->{
                           if(dish.getCalories()<= 400)    return CaloricLevel.DIET;
                           else if(dish.getCalories()<= 700) return CaloricLevel.NORMAL;
                           else return CaloricLevel.FAT;
                       } , toSet()
                   ))
        );
        System.out.println(caloricLevelByType);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType2 = menu.stream().collect(
               groupingBy(Dish::getType,
                       mapping(dish->{
                           if(dish.getCalories()<= 400)    return CaloricLevel.DIET;
                           else if(dish.getCalories()<= 700) return CaloricLevel.NORMAL;
                           else return CaloricLevel.FAT;
                       } , toCollection(HashSet::new)
                   ))
        );
        System.out.println(caloricLevelByType2);


    }

    void Quiz1(){
        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
        System.out.println(shortMenu);

        shortMenu = menu.stream().collect(reducing("", Dish::getName, (s1, s2)-> s1+s2));
        System.out.println(shortMenu);

        shortMenu = menu.stream().map(Dish::getName).collect(reducing((s1, s2)-> s1+s2)).get();
        System.out.println(shortMenu);
    }

    void Quiz2(){
        // 1번
        Map<Boolean, Map<Boolean, List<Dish>>> quiz1= menu.stream().collect(partitioningBy(Dish::isVegetarian, partitioningBy(d->d.getCalories() > 500)));
        System.out.println(quiz1);

        // 3번
        Map<Boolean, Long> quiz3 = menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
        System.out.println(quiz3);
    }
    void collector(){
        Map<Currency, List<Transaction>> transMap = new HashMap<>();

        for(Transaction trans : transactionss){
            Currency currency = trans.getCurrency();
            List<Transaction> transForCurrency = transMap.get(currency);
            if(transForCurrency == null){
                transForCurrency = new ArrayList<>();
                transMap.put(currency, transForCurrency);
            }
            transForCurrency.add(trans);
        }
        System.out.println(transMap);

        Map<Currency, List<Transaction>> transMap2 = transactionss.stream().collect(groupingBy(Transaction::getCurrency));
        System.out.println(transMap2);

        List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());

        System.out.println(dishes);
        dishes = menu.stream().collect(toList());
        System.out.println(dishes);

        dishes = menu.stream().collect(
                ArrayList::new,
                List::add,
                List::addAll
        );
        System.out.println(dishes);

        dishes.add(new Dish("newBalance", false, 240, Dish.Type.FISH));
        dishes.add(new Dish("Hoka", false, 190, Dish.Type.FISH));

        System.out.println(dishes);
    }
    void reducing62(){
       // 정렬
       Comparator<Dish> dishCaloriesComparator = comparingInt(Dish::getCalories);
       Optional<Dish> mostCaloriesDish = menu.stream().collect(maxBy(dishCaloriesComparator));
       System.out.println(mostCaloriesDish);

       System.out.println(menu.stream().collect(summingInt(Dish::getCalories)));
       System.out.println(menu.stream().collect(averagingInt(Dish::getCalories)));

       IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
       System.out.println(menuStatistics);

       String shortMenu = menu.stream().map(Dish::getName).collect(joining());
       System.out.println(shortMenu);
       shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
       System.out.println(shortMenu);

       int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i,j) -> i+j));
       System.out.println(totalCalories);
       Optional<Dish> mostCalories = menu.stream().collect(reducing( (d1,d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
       System.out.println(mostCalories);

        totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
        System.out.println(totalCalories);
        totalCalories = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
        System.out.println(totalCalories);
        System.out.println(menu.stream().mapToInt(Dish::getCalories).sum());
   }

    public static List<Transaction> transactionss = Arrays.asList(
        new Transaction(Currency.EUR, 1500.0),
        new Transaction(Currency.USD, 2300.0),
        new Transaction(Currency.GBP, 9900.0),
        new Transaction(Currency.EUR, 1100.0),
        new Transaction(Currency.JPY, 7800.0),
        new Transaction(Currency.CHF, 6700.0),
        new Transaction(Currency.EUR, 5600.0),
        new Transaction(Currency.USD, 4500.0),
        new Transaction(Currency.CHF, 3400.0),
        new Transaction(Currency.GBP, 3200.0),
        new Transaction(Currency.USD, 4600.0),
        new Transaction(Currency.JPY, 5700.0),
        new Transaction(Currency.EUR, 6800.0)
      );

    public static class Transaction {

        private final Currency currency;
        private final double value;

        public Transaction(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " " + value;
        }

    }

    public enum Currency {
        EUR, USD, JPY, GBP, CHF
    }



}
