package com.masthuggis.boki.model;

import org.junit.Test;

public class UserFactoryTest {

    private User user = new User("email","displayName","userID");

    @Test
    public void testCreateUser() {
        iUser testUser = UserFactory.createUser(user.getEmail(),user.getDisplayName(),user.getId());
        assert (user.getEmail().equals(testUser.getEmail()));
        assert (user.getDisplayName().equals(testUser.getDisplayName()));
        assert (user.getId().equals(testUser.getId()));
    }
}
