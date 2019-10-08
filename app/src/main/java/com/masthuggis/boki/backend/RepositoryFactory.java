package com.masthuggis.boki.backend;

public class RepositoryFactory {

    public static  Repository createRepository(iBackend backend){
        return new Repository(backend);
    }

    public static UserRepository createUserRepository(iBackend backend){
        return new UserRepository(backend);
    }
}
