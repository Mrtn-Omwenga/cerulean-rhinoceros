package org.zew.donations.converter;

import org.zew.donations.commons.repository.Entity;
import org.zew.donations.model.response.EntityResponse;

public class EntityConverter {

    public static EntityResponse fromEntityToResponse(Entity entity) {
        return EntityResponse
                .builder()
                .id(entity.getId())
                .build();
    }

}
