package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FanCodeTest {
    @Test
    public void verifyFanCodeUsersHaveMoreThan50PercentCompletedTodos() throws JsonProcessingException {
        FanCodeValidator fanCodeValidator=new FanCodeValidator();
        fanCodeValidator.validateUser();
       Assert.assertTrue( fanCodeValidator.printUserData());

    }
}
