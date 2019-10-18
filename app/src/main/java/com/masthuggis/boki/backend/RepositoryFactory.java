package com.masthuggis.boki.backend;

public class RepositoryFactory {

    public static iRepository createRepository(iBackend backend) {
        return new Repository(backend);
    }

    public static AdvertRepository createAdvertRepository(iBackend backend) {
        return new AdvertRepository(backend);
    }

    public static UserRepository createUserRepository(iBackend backend) {
        return new UserRepository(backend);
    }

}
