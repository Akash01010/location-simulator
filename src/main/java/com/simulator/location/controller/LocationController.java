package com.simulator.location.controller;

import com.google.common.flogger.FluentLogger;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.simulator.location.service.LineService;
import com.simulator.location.service.MapsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {

    @Autowired
    MapsServiceImpl mapsService;

    @Autowired
    LineService lineService;

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @GetMapping("/getpath/{data}")
    public List<LatLng> getPathCoordinates(@PathVariable(value = "data") String data) throws IOException, InterruptedException, ApiException {
        String[] coordinates = data.split(",");
        List<LatLng> sumInit = mapsService.getPathBetweenMarkers(new LatLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])), new LatLng(Double.parseDouble(coordinates[2]), Double.parseDouble(coordinates[3])));
        List<LatLng> sumFinal = mapsService.getPathBetweenMarkersAtFixedDistance(new LatLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])), new LatLng(Double.parseDouble(coordinates[2]), Double.parseDouble(coordinates[3])));

        verifyResult(sumInit, sumFinal);
        return sumFinal;
    }

    private void verifyResult(List<LatLng> path, List<LatLng> finalPath) {
        double sumInit = 0.0;
        double sumFinal = 0.0;
        for (int j = 0; j < path.size() - 1; j++) {
            double haversineDistanceBetweenMarkers = lineService.haversineDistanceBetweenMarkers(path.get(j), path.get(j + 1));
            sumInit += haversineDistanceBetweenMarkers;
        }
        for (int j = 0; j < finalPath.size() - 1; j++) {
            double haversineDistanceBetweenMarkers = lineService.haversineDistanceBetweenMarkers(finalPath.get(j), finalPath.get(j + 1));
//            logger.atInfo().log("Distance: " + haversineDistanceBetweenMarkers);
            sumFinal += haversineDistanceBetweenMarkers;
        }

        logger.atInfo().log("Distance between source and destination using library coordinates: %f kms", sumInit/1000);
        logger.atInfo().log("Distance between source and destination using interpolated coordinates: %f kms", sumFinal/1000);
        logger.atInfo().log("Approx number of points expected source and destination after interpolation %f", sumInit/50);
        logger.atInfo().log("Total number of points between source and destination after interpolation %d", finalPath.size());
    }
}
