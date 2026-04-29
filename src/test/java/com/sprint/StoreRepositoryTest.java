package com.sprint;

import com.sprint.Entities.Store;
import com.sprint.Repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void testFindByAddressCityIgnoreCase_returnsMatchingCity() {
        Page<Store> page = storeRepository.findByAddress_City_CityIgnoreCase(
                "Lethbridge",
                PageRequest.of(0, 5)
        );

        assertFalse(page.isEmpty());
        assertEquals("Lethbridge", page.getContent().get(0).getAddress().getCity().getCity());
    }

    @Test
    public void testFindByAddressCityIgnoreCase_noResults() {
        Page<Store> page = storeRepository.findByAddress_City_CityIgnoreCase(
                "NoSuchCity",
                PageRequest.of(0, 5)
        );

        assertTrue(page.isEmpty());
    }
}
