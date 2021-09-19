package com.simulator.location.service;

import com.google.maps.model.LatLng;

public interface LineService {
    LatLng section(LatLng start, LatLng end, Double dist);
    double haversineDistanceBetweenMarkers(LatLng start, LatLng end);
}
