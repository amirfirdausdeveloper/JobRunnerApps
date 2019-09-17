package com.jobrunner.apps.Connection;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class UrlLink {
    private JSONParser2 jsonParser;

    public UrlLink() {
        jsonParser = new JSONParser2();
    }

    public static String url_path = "https://jobrunnerofficial.000webhostapp.com/api/employee/";

    public static String loginURL = url_path + "login_employee.php";
    public static String registerURL = url_path + "registration_employee.php";
    public static String getProfileDetailsURL = url_path + "getProfileDetails.php";
    public static String updateProfileURL = url_path + "updateProfile.php";
    public static String getPendingJobURL = url_path + "getListPendingJob.php";
    public static String acceptJobURL = url_path + "acceptJob.php";
    public static String getCurrentJobURL = url_path + "currenJob.php";
    public static String getCurrentHistoryJobURL = url_path + "currentHistoryJob.php";
    public static String getIncomeByMonthURL = url_path + "incomeByMonth.php";
    public static String getIncomeURL = url_path + "getIncome.php";
    public static String getYearIncomeURL = url_path + "getIncomeYear.php";





    public JSONObject getIncomeYear(String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id",jr_id);
        return jsonParser.makeHttpRequest(getYearIncomeURL, "POST", params);
    }

    public JSONObject getIncome(String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id",jr_id);
        return jsonParser.makeHttpRequest(getIncomeURL, "POST", params);
    }

    public JSONObject getIncomeByMonth(String jr_id,String year) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id",jr_id);
        params.put("year",year);
        return jsonParser.makeHttpRequest(getIncomeByMonthURL, "POST", params);
    }

    public JSONObject getCurrentHistoryJob(String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id",jr_id);
        return jsonParser.makeHttpRequest(getCurrentHistoryJobURL, "POST", params);
    }

    public JSONObject getCurrentJob(String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id",jr_id);
        return jsonParser.makeHttpRequest(getCurrentJobURL, "POST", params);
    }

    public JSONObject acceptJob(String job_id,String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("job_id",job_id);
        params.put("jr_id",jr_id);
        return jsonParser.makeHttpRequest(acceptJobURL, "POST", params);
    }

    public JSONObject getPendingJob() {
        HashMap<String, String> params = new HashMap<>();
        return jsonParser.makeHttpRequest(getPendingJobURL, "POST", params);
    }

    public JSONObject updateProfile(String jr_id,String email,String firstname,String lastname,String gender,String address,String postcode,String martial_status,String age
            ,String bank_name,String bank_account,String phone_no,String emergency_no,String additional_skill_one,String additional_skill_two,String additional_skill_three
            ,String profile_url) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id", jr_id);
        params.put("email", email);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("gender", gender);
        params.put("address", address);
        params.put("postcode", postcode);
        params.put("martial_status", martial_status);
        params.put("age", age);
        params.put("bank_name", bank_name);
        params.put("bank_account", bank_account);
        params.put("phone_no", phone_no);
        params.put("emergency_no", emergency_no);
        params.put("additional_skill_one", additional_skill_one);
        params.put("additional_skill_two", additional_skill_two);
        params.put("additional_skill_three", additional_skill_three);
        params.put("profile_url", profile_url);

        return jsonParser.makeHttpRequest(updateProfileURL, "POST", params);
    }

    public JSONObject getProfileJrId(String jr_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("jr_id", jr_id);
        return jsonParser.makeHttpRequest(getProfileDetailsURL, "POST", params);
    }

    public JSONObject loginEmailAndPasswordAndToken(String email, String password,String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("token", token);
        return jsonParser.makeHttpRequest(loginURL, "POST", params);
    }

    public JSONObject registerEmailAndPassword(String email, String password,String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("token", token);
        return jsonParser.makeHttpRequest(registerURL, "POST", params);
    }
}
