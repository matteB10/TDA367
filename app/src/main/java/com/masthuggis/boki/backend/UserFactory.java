package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.User;

 class UserFactory {

     static User createUser(String email, String displayname, String userID, DataModel dataModel){
        return new User(email,displayname,userID,dataModel);
    }
    }
