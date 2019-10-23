package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.iBackend;

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
