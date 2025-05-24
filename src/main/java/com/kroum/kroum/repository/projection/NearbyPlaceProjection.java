package com.kroum.kroum.repository.projection;

public interface NearbyPlaceProjection {
    double getLatitude();
    double getLongitude();
    String getFirstImageUrl();
    String getPlaceName();
    String getDescription();
    String getAddress();
    Long getPlaceId(); // bookmarked 처리할 때 필요
    double getDistance(); // ST_Distance_Sphere() 결과
}