package com.georegistration.service;

import com.georegistration.domain.GeoLocation;
import com.georegistration.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class RegistrationService {

    @Value("${ip.geolocation.api.url}")
    private String ipGeolocationApiUrl;

    private RestTemplate restTemplate;

    public RegistrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * call IP geolocation API to get location information
     * @param user
     * @return
     */
    public ResponseEntity<?> registration(User user){
        // call IP geolocation API to get location information
        GeoLocation geoLocation = restTemplate.getForObject(ipGeolocationApiUrl + user.getIpAddress(), GeoLocation.class);

        // check if the user is eligible to register
        if (!"CA".equalsIgnoreCase(geoLocation.getCountryCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not eligible to register");
        }

        // generate a random UUID and return a welcome message with the username and city name
        String uuid = UUID.randomUUID().toString();
        String message = "Welcome " + user.getUsername() + ", you are registered from " + geoLocation.getCity();
        return ResponseEntity.ok().body(Map.of("uuid", uuid, "message", message));

    }

}
