package com.sprint.Projections;

import org.springframework.data.rest.core.config.Projection;

import com.sprint.Entities.Address;
import com.sprint.Entities.Staff;
import com.sprint.Entities.Store;

@Projection(name = "staffProjection", types = { Staff.class })
public interface StaffProjection {
    Long getStaffId();
    
    String getFirstName();
    
    String getLastName();
    
    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }
    
    String getEmail();
    
    Boolean getActive();
    
    String getUsername();
    
    Address getAddress();
    
    Store getStore();
    
    default Long getAddressId() {
        return getAddress().getAddressId();
    }
    
    default Long getStoreId() {
        return getStore().getStoreId();
    }
}
