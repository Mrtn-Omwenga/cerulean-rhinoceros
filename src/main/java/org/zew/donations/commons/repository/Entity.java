package org.zew.donations.commons.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Entity {

    @JsonIgnore
    String getId();

}
