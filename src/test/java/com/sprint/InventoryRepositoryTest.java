package com.sprint;

import com.sprint.Entities.Inventory;
import com.sprint.Repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    public void testFindByStoreId_returnsStoreMatches() {
        Page<Inventory> page = inventoryRepository.findByStore_StoreId(1L, PageRequest.of(0, 5));

        assertFalse(page.isEmpty());
        assertEquals(1L, page.getContent().get(0).getStore().getStoreId());
    }

    @Test
    public void testFindByStoreId_noResults() {
        Page<Inventory> page = inventoryRepository.findByStore_StoreId(999999L, PageRequest.of(0, 5));

        assertTrue(page.isEmpty());
    }
}
