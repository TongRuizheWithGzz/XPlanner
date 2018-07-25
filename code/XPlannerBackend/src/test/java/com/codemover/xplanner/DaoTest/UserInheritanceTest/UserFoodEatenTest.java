package com.codemover.xplanner.DaoTest.UserInheritanceTest;


import com.codemover.xplanner.DAO.UserFoodEatenRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Model.Entity.UserFoodEaten;
import com.codemover.xplanner.Service.Impl.KeeperService;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFoodEatenTest {

    @Autowired
    private KeeperService keeperService;

    @Autowired
    private UserFoodEatenRepository userFoodEatenRepository;

    @Autowired
    private UserRepository userRepository;

    //@Before
    public void setup(){
        DataFactory df = new DataFactory();
        String[] usernames = {"tongruizhe","lihu"};

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));

        for(int i=0;i<30;i++){
            UserFoodEaten userFoodEaten = new UserFoodEaten();
            userFoodEaten.setUser(userRepository.findByUserName(df.getItem(usernames)));

            Random random = new Random();
            int ran = random.nextInt(2*24*60)+1;

            Calendar eaten_time = Calendar.getInstance();
            eaten_time.setTime(calendar.getTime());
            eaten_time.add(Calendar.MINUTE,ran);;
            userFoodEaten.setAteTime(keeperService.Calendar2Timestamp(eaten_time));
            userFoodEaten.setCalorie(df.getNumberBetween(30,500));
            userFoodEaten.setFoodName(df.getAddress());
            userFoodEatenRepository.save(userFoodEaten);
        }

    }

    //find food today by user
    @Test
    public void FindEatenTest(){
        System.out.println(keeperService.getTodayFoodEaten("tongruizhe").size());
    }



}
