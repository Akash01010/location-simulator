package com.simulator.location.controller;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.simulator.location.service.LineService;
import com.simulator.location.service.MapsServiceImpl;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Flogger
@RestController
public class LocationController {

    @Autowired
    MapsServiceImpl mapsService;

    @Autowired
    LineService lineService;

    @GetMapping("/getpath/from/{start}/to/{end}/step/{step}")
    public ResponseEntity<List<LatLng>> getPathCoordinates(@PathVariable(value = "start") String start, @PathVariable(value = "end") String end, @PathVariable(value = "step") String step) throws IOException, InterruptedException, ApiException {

        List<LatLng> sumInit = mapsService.getPathBetweenMarkers(new LatLng(Double.parseDouble(start.split(",")[0]), Double.parseDouble(start.split(",")[1])), new LatLng(Double.parseDouble(end.split(",")[0]), Double.parseDouble(end.split(",")[1])));
        List<LatLng> sumFinal = mapsService.getPathBetweenMarkersAtFixedDistance(new LatLng(Double.parseDouble(start.split(",")[0]), Double.parseDouble(start.split(",")[1])), new LatLng(Double.parseDouble(end.split(",")[0]), Double.parseDouble(end.split(",")[1])), Double.parseDouble(step));

        verifyResult(sumInit, sumFinal);
        return new ResponseEntity<>(sumFinal, HttpStatus.OK);
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
            log.atInfo().log("Coordinate: " + finalPath.get(j).lat + "," + finalPath.get(j).lng);
            sumFinal += haversineDistanceBetweenMarkers;
        }

        log.atInfo().log("Distance between source and destination using library coordinates: %f kms", sumInit / 1000);
        log.atInfo().log("Distance between source and destination using interpolated coordinates: %f kms", sumFinal / 1000);
        log.atInfo().log("Approx number of points expected source and destination after interpolation %f", sumInit / 50);
        log.atInfo().log("Total number of points between source and destination after interpolation %d", finalPath.size());
    }
}
