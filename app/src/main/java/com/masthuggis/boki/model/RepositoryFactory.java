package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.iBackend;

/**
 * Used to create objects of Repository-types.
 * Used by Repository and DataModel.
 * Written by masthuggis
 */
class RepositoryFactory {

    static iRepository createRepository(iBackend backend) {
        return new Repository(backend);
    }

    static AdvertRepository createAdvertRepository(iBackend backend) {
        return new AdvertRepository(backend);
    }

    static UserRepository createUserRepository(iBackend backend) {
        return new UserRepository(backend);
    }

}
