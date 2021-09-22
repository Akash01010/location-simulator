package com.simulator.location.service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.simulator.location.exception.AppException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapsServiceImpl implements MapsService {

    @Autowired
    LineService lineService;

    @Autowired
    GeoApiContext geoApiContext;

    @Override
    public List<LatLng> getPathBetweenMarkers(LatLng start, LatLng end) throws ApiException, InterruptedException, IOException {
        DirectionsResult res = getRoutesBetweenMarkers(start, end);
        DirectionsRoute route = res.routes[0];
        return getCoordinatesFromRoute(route);
    }

    @SneakyThrows
    @Override
    public List<LatLng> getPathBetweenMarkersAtFixedDistance(LatLng start, LatLng end, Double step) {

        List<LatLng> path = getPathBetweenMarkers(start, end);
        return interpolatePointsAtFixedDistance(path, step);
    }

    private List<LatLng> interpolatePointsAtFixedDistance(List<LatLng> path, Double step) {
        double sum = 0.0;
        double delta = 0.1;

        LatLng currPoint = path.get(0);
        LatLng nextPoint;

        List<LatLng> finalPath = new ArrayList<>();
        finalPath.add(currPoint);

        int i;
        for (i = 1; i < path.size(); i++) {
            nextPoint = path.get(i);
            double dist = lineService.haversineDistanceBetweenMarkers(currPoint, nextPoint);
            sum += dist;
            if (sum > (step - delta) && sum < (step + delta)) {
                finalPath.add(nextPoint);
                currPoint = nextPoint;
                sum = 0;
            } else if (sum > step + delta) {
                LatLng secPoint = lineService.section(currPoint, nextPoint, dist - (sum - step));
                finalPath.add(secPoint);
                sum = 0;
                currPoint = secPoint;
                i--;
            } else {
                currPoint = nextPoint;
            }
        }
        if (sum > delta) {
            finalPath.add(path.get(path.size() - 1));
        }
        return finalPath;
    }

    private DirectionsResult getRoutesBetweenMarkers(LatLng start, LatLng end) throws ApiException, InterruptedException, IOException {
        DirectionsApiRequest req = DirectionsApi.getDirections(geoApiContext, start.lat + "," + start.lng, end.lat + "," + end.lng);
        DirectionsResult res = req.await();
        if (res.routes != null && res.routes.length > 0) {
            return res;
        } else {
            throw new AppException("No Routes Found!");
        }
    }

    private List<LatLng> getCoordinatesFromRoute(DirectionsRoute route) {
        List<LatLng> path = new ArrayList<>();
        if (route.legs != null) {
            for (int i = 0; i < route.legs.length; i++) {
                DirectionsLeg leg = route.legs[i];
                path.addAll(getCoordinatesFromLeg(leg));
            }
        }
        return path;
    }

    private List<LatLng> getCoordinatesFromLeg(DirectionsLeg leg) {
        List<LatLng> path = new ArrayList<>();
        if (leg.steps != null) {
            for (int j = 0; j < leg.steps.length; j++) {
                DirectionsStep step = leg.steps[j];
                if (step.steps != null && step.steps.length > 0) {
                    for (int k = 0; k < step.steps.length; k++) {
                        DirectionsStep step1 = step.steps[k];
                        path.addAll(getCoordinatesFromStep(step1));
                    }
                } else {
                    path.addAll(getCoordinatesFromStep(step));
                }
            }
        }
        return path;
    }

    private List<LatLng> getCoordinatesFromStep(DirectionsStep step) {
        List<LatLng> path = new ArrayList<>();
        EncodedPolyline points = step.polyline;
        if (points != null) {
            //Decode polyline and add points to list of route coordinates
            List<LatLng> coords = points.decodePath();
            for (LatLng coord : coords) {
                path.add(new LatLng(coord.lat, coord.lng));
            }
        }
        return path;
    }

}