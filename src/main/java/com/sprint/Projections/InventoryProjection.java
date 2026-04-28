package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Film;
import com.sprint.Entities.Inventory;
import com.sprint.Entities.Store;

@Projection(name = "inventoryProjection", types = { Inventory.class })
public interface InventoryProjection {
    Long getInventoryId();
    
    Film getFilm();
    
    default String getFilmName() {
        return getFilm().getTitle();
    }

    Store getStore();
    
    default Long getStoreId() {
        return getStore().getStoreId();
    }
}
