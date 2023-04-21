package com.georegistration.service;

import com.georegistration.domain.GeoLocation;
import com.georegistration.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static junit.framework.TestCase.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void testRegistration() {
        // Mock the IP geolocation API response
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setCountryCode("CA");
        geoLocation.setCity("Toronto");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(GeoLocation.class)))
                .thenReturn(geoLocation);

        // Create a sample user
        User user = new User();
        user.setIpAddress("127.0.0.1");
        user.setUsername("testuser");

        // Call the registration method
        ResponseEntity<?> responseEntity = registrationService.registration(user);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof Map);

        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertNotNull(responseBody.get("uuid"));
        assertNotNull(responseBody.get("message"));
        assertTrue(responseBody.get("message").contains("testuser"));
        assertTrue(responseBody.get("message").contains("Toronto"));
    }

    @Test
    public void testRegistrationWithForbiddenCountry() {
        // Mock the IP geolocation API response
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setCountryCode("US");
        geoLocation.setCity("New York");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(GeoLocation.class)))
                .thenReturn(geoLocation);

        // Create a sample user
        User user = new User();
        user.setIpAddress("127.0.0.1");
        user.setUsername("testuser");

        // Call the registration method
        ResponseEntity<?> responseEntity = registrationService.registration(user);

        // Verify the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("User is not eligible to register", responseEntity.getBody());
    }
}
