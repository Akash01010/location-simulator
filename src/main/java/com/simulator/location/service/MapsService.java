package com.simulator.location.service;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public interface MapsService {

    List<LatLng> getPathBetweenMarkers(LatLng start, LatLng end) throws ApiException, InterruptedException, IOException;

    List<LatLng> getPathBetweenMarkersAtFixedDistance(LatLng start, LatLng end, Double step) throws IOException, InterruptedException, ApiException;
}
