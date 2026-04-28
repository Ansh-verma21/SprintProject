package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Address;
import com.sprint.Entities.Staff;
import com.sprint.Entities.Store;

@Projection(name = "storeProjection", types = { Store.class })
public interface StoreProjection {
    Long getStoreId();

    Staff getManagerStaff();   
    
    Address getAddress();
    
    default String getManagerName() {
        return getManagerStaff().getFirstName() 
             + " " + getManagerStaff().getLastName();
    }

    default Long getAddressId() {
        return getAddress().getAddressId();
    }
    
    default Long getManagerStaffId() {
        return getManagerStaff().getStaffId();
    }
}
