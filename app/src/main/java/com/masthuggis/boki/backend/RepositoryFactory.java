package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.DataModel;

public class RepositoryFactory {

    public static  Repository createRepository(iBackend backend,DataModel dataModel){
        return new Repository(backend,dataModel);
    }

    public static UserRepository createUserRepository(iBackend backend, DataModel dataModel){
        return new UserRepository(backend,dataModel);
    }
}
