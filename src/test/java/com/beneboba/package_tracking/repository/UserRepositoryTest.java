package com.beneboba.package_tracking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.helper.dummy.DummyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUserByUsername() {
        User user = DummyUser.newUser();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findUserByUsername(DummyUser.USER_USERNAME);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(DummyUser.USER_USERNAME);
    }

    @Test
    public void testFindUserByUsernameNotFound() {
        Optional<User> foundUser = userRepository.findUserByUsername("notfound");

        assertThat(foundUser).isNotPresent();
    }
}
