package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredMealsWithExceeded = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredMealsWithExceeded.forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesMap = new HashMap<>();
        List<UserMealWithExceed> resultList = new ArrayList<>();

        LocalTime mealTime;
        LocalDate mealDate;

        // Count calories per day
        for (UserMeal meal : mealList) {
            mealDate = LocalDate.from(meal.getDateTime());

            if(caloriesMap.get(mealDate) == null) {
                caloriesMap.put(mealDate, meal.getCalories());
            } else {
                caloriesMap.put(mealDate, caloriesMap.get(mealDate) + meal.getCalories());
            }
        }

        // Filter time, populate calories per day and add to list
        for (UserMeal meal : mealList) {
            mealTime = LocalTime.from(meal.getDateTime());
            mealDate = LocalDate.from(meal.getDateTime());

            if (TimeUtil.isBetween(mealTime, startTime, endTime)) {
                resultList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesMap.get(mealDate) > caloriesPerDay));
            }
        }

        /*
        //Using Stream API HW
       mealList.stream().
                forEach(m -> caloriesMap.put(m.getDateTime().toLocalDate(), Optional.ofNullable(caloriesMap.get(m.getDateTime().toLocalDate())).orElse(0) + m.getCalories()));

        mealList.stream().
                filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime)).
                forEach(m -> resultList.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), (caloriesMap.get(m.getDateTime().toLocalDate()) > caloriesPerDay))));
        */

        /*
        //Using Stream API From Class1

        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), caloriesSumByDate.get(m.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
        */

        return resultList;
    }
}
