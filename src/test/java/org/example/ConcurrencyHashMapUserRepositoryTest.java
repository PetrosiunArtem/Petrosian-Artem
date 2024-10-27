package org.example;

import org.example.users.ConcurrencyHashMapUserRepository;
import org.example.users.User;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class ConcurrencyHashMapUserRepositoryTest {

  @Test
  public void findByMsisdn() {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    User user = new User("Даниил", "Кузьмич");
    String msisdn = "89151003040";
    repository.updateUserByMsisdn(msisdn, user);
    User currentResult = repository.findByMsisdn(msisdn);
    User extendedResult = user;
    assertEquals(currentResult, extendedResult);
  }

  @Test
  public void updateUserByMsisdn() {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    User userFirst = new User("Фаина", "Приходько");
    User userSecond = new User("Эльдар", "Дарханович");
    String msisdnFirst = "89151429440";
    String msisdnSecond = "89121013040";
    repository.updateUserByMsisdn(msisdnFirst, userFirst);
    repository.updateUserByMsisdn(msisdnSecond, userSecond);
    ConcurrentHashMap<String, User> currentResult = repository.getHashMap();
    ConcurrentHashMap<String, User> extendedResult =
        new ConcurrentHashMap<>() {
          {
            put(msisdnFirst, userFirst);
            put(msisdnSecond, userSecond);
          }
        };
    assertEquals(currentResult, extendedResult);
  }
}
