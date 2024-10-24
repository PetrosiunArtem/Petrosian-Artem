package org.example.users;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrencyHashMapUserRepository implements UserRepository {

  private final ConcurrentHashMap<String, User> hashMap = new ConcurrentHashMap<>();

  @Override
  public User findByMsisdn(String msisdn) {
    return this.hashMap.get(msisdn);
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    this.hashMap.put(msisdn, user);
  }
}
