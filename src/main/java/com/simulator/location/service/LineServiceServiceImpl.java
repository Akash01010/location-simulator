package com.simulator.location.service;

import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

@Service
public class LineServiceServiceImpl implements LineService {

    public LatLng section(LatLng start, LatLng end, Double dist) {
        double x1 = start.lat;
        double x2 = end.lat;
        double y1 = start.lng;
        double y2 = end.lng;
        double m = dist;
        double n = haversineDistanceBetweenMarkers(start, end) - dist;

        double x = ((n * x1) + (m * x2)) /
                (m + n);
        double y = ((n * y1) + (m * y2)) /
                (m + n);

        return new LatLng(x, y);
    }

    @Override
    public double haversineDistanceBetweenMarkers(LatLng start, LatLng end) {
        double lon1 = Math.toRadians(start.lng);
        double lon2 = Math.toRadians(end.lng);
        double lat1 = Math.toRadians(start.lat);
        double lat2 = Math.toRadians(end.lat);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;

        return (c * r) * 1000;
    }
}

