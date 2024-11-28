package com.example.automessageone;

import java.util.HashMap;

public class AutoMessage {

    public static boolean messageCreated = false;
    private int type; // 1 for AutoMessage || 2 for StopRing
    private String message;
    private int numberOfCalls;
    private HashMap<String, Integer> callersDetails;

    public AutoMessage(int type, String message, int numberOfCalls) {
        this.type = type;
        this.message = message;
        this.numberOfCalls = numberOfCalls;

        callersDetails = new HashMap<>();

        messageCreated = true;
    }

    public void addCaller(String callerPhoneNumber) {

        if(callersDetails.containsKey(callerPhoneNumber)) {
            for(HashMap.Entry<String, Integer> caller: callersDetails.entrySet()) {
                if(callerPhoneNumber.equals(caller.getKey())) {
                    int tem = caller.getValue();
                    tem += 1;
                    caller.setValue(tem);

                }
            }
        }
        else {
            callersDetails.put(callerPhoneNumber, 1);
        }

    }

    public int getHowManyMissCalls(String phoneNumber) {

        for (HashMap.Entry<String, Integer> caller : callersDetails.entrySet()) {
            if (phoneNumber.equals(caller.getKey())) {
                return caller.getValue();
            }
        }
        return 0;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public int getType() {return this.type;}

    public void setType(int type) {this.type = type;}

    public HashMap<String, Integer> getCallersDetails() {
        return callersDetails;
    }

    public void setCallersDetails(HashMap<String, Integer> callersDetails) {
        this.callersDetails = callersDetails;
    }
}
