
package com.esp8266.location.mapMyIndia.safemate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentRouteState {

    @SerializedName("is_active")
    @Expose
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
