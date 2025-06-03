package org.latifah.employeedashboardback.ai;

//siham
public class AIResponse {
    private String responseText;
    private boolean success;
    private String intent; // ADD THIS FIELD

    // ADD NEW CONSTRUCTOR
    public AIResponse(String responseText, boolean success, String intent) {
        this.responseText = responseText;
        this.success = success;
        this.intent = intent;
    }
    public String getResponseText() {
        return responseText;
    }
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }


    // ADD GETTER AND SETTER
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    public AIResponse(String responseText, boolean success) {
        this.responseText = responseText;
        this.success = success;
        this.intent = null; // Initialize intent to null
    }

}