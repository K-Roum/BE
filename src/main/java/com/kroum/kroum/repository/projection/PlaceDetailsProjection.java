package com.kroum.kroum.repository.projection;

import lombok.Getter;

public interface PlaceDetailsProjection {
    String getFirstImageUrl();
    String getPlaceName();
    String getDescription();
    String getAddress();
    Long getPlaceId();
}

