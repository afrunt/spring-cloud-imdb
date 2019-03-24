package com.afrunt.scimdb.titlebasics.model;

import ma.glasnost.orika.MapperFacade;

/**
 * @author Andrii Frunt
 */
public interface Mappable {
    default <T> T mapTo(Class<T> type, MapperFacade mapperFacade) {
        return mapperFacade.map(this, type);
    }
}
