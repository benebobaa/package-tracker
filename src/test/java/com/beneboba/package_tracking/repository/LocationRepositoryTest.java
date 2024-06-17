package com.beneboba.package_tracking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.helper.dummy.DummyLocation;
import com.beneboba.package_tracking.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testFindFirstByOrderByIdDesc() {
        Location location1 = DummyLocation.newLocation();
        Location location2 = DummyLocation.newLocation(2L, "gdng-001", "Tebet Utara");
        locationRepository.save(location1);
        locationRepository.save(location2);

        Location foundLocation = locationRepository.findFirstByOrderByIdDesc();

        assertThat(foundLocation).isNotNull();
        assertThat(foundLocation.getCodeLocation()).isEqualTo("gdng-001");
    }

    @Test
    public void testFindFirstByOrderByIdDescNotFound() {
        Location foundLocation = locationRepository.findFirstByOrderByIdDesc();

        assertThat(foundLocation).isNull();
    }

    @Test
    public void testFindFirstByCodeLocation() {
        Location location = DummyLocation.newLocation();
        locationRepository.save(location);

        Optional<Location> foundLocation = locationRepository.findFirstByCodeLocation(DummyLocation.CODE_LOCATION);

        assertThat(foundLocation).isPresent();
        assertThat(foundLocation.get().getCodeLocation()).isEqualTo(DummyLocation.CODE_LOCATION);
        assertThat(foundLocation.get().getName()).isEqualTo(DummyLocation.NAME_LOCATION);
    }

    @Test
    public void testFindFirstByCodeLocationNotFound() {
        Optional<Location> foundLocation = locationRepository.findFirstByCodeLocation("nofoundlocation");

        assertThat(foundLocation).isNotPresent();
    }
}
