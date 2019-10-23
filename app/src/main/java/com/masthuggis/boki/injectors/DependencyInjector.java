package com.masthuggis.boki.injectors;

import com.masthuggis.boki.model.DataModel;

/**
 * DependencyInjector is a variant of a Factory class which is used by many views to inject
 * dependencies (currently the only dependency it can inject is the
 * dependency upon DataModel) into their respective presenters.
 * Written by masthuggis
 */
public class DependencyInjector {
    public static DataModel injectDataModel(){
        return DataModel.getInstance();
    }
}
