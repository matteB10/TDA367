package com.masthuggis.boki.injectors;

import com.masthuggis.boki.model.DataModel;

public class DependencyInjector {

    public static DataModel injectDataModel(){
        return DataModel.getInstance();
    }
}
