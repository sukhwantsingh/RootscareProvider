
package com.rootscare.data.model.response.fetchscheduleresponse;

import java.util.List;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("slots")
    @Expose
    public List<Slot> slots = null;
    @SerializedName("accept_booking")
    @Expose
    public String acceptBooking;

    @SerializedName("service_address")
    @Expose
    public String service_address;

    @SerializedName("service_lat")
    @Expose
    public String service_lat;

    @SerializedName("service_long")
    @Expose
    public String service_long;

    @SerializedName("service_radius")
    @Expose
    public String service_radius;
}
