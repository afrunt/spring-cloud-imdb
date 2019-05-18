package com.afrunt.scimdb.dto;

import java.io.Serializable;

/**
 * @author Andrii Frunt
 */
public class Dto implements Serializable {
    @SuppressWarnings("unchecked")
    public <T extends Dto> T cast() {
        return (T) this;
    }

    public <T extends Dto> T casr(Class<T> type) {
        return type.cast(this);
    }
}
