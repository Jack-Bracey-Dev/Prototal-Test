package com.jackbracey.prototaltest.Repositories;

import com.jackbracey.prototaltest.Models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByEmail(String email);
}
