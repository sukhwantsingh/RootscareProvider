
package com.rootscare.data.model.request.sendschedule;

import androidx.annotation.Keep;

import java.util.List;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class SendScheduleRequest {

    @SerializedName("service_type")
    @Expose
    public String serviceType;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("accept_booking")
    @Expose
    public String acceptBooking;
    @SerializedName("slots")
    @Expose
    public List<Slot> slots = null;
    @SerializedName("slot_type")
    @Expose
    public String slot_type;

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
