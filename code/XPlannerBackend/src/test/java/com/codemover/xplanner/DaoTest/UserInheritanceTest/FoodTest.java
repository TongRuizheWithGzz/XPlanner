package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.Service.Impl.KeeperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodTest {
    @Autowired
    private KeeperService keeperService;

    @Test
    public void findfood(){
        System.out.println(keeperService.getFoodList("第五餐厅").size());
    }

}
