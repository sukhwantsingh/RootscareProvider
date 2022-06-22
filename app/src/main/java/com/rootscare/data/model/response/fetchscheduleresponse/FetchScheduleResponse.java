
package com.rootscare.data.model.response.fetchscheduleresponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchScheduleResponse {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("result")
    @Expose
    public Result result;

}
