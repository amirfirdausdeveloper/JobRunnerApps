package com.jobrunner.apps.Class;

public class FindJobClass {
    private String date_created;
    private String job_id;
    private String job_type;
    private String employee_job_id;
    private String employer_id;
    private String employer_name;
    private String job_name;
    private String date_from;
    private String date_to;
    private String working_time_from;
    private String working_time_to;
    private String working_hour;
    private String job_location_address;
    private String job_location_latlng;
    private String job_details;
    private String working_job_salary_hourday;
    private String job_salary_day;
    private String job_salary_total;
    private String job_salary_hour;
    private String status_payment;
    private String job_total_comission;

    public FindJobClass(String date_created,String job_id,String job_type,String employee_job_id,String employer_id,String employer_name,String job_name,
                        String date_from,String date_to,String working_time_from,String working_time_to,String working_hour,String job_location_address,String job_location_latlng,String job_details,
                        String working_job_salary_hourday,String job_salary_day,String job_salary_total,String job_salary_hour,String status_payment,String job_total_comission){
        this.date_created = date_created;
        this.job_id = job_id;
        this.job_type = job_type;
        this.employee_job_id  = employee_job_id;
        this.employer_id = employer_id;
        this.employer_name = employer_name;
        this.job_name = job_name;
        this.date_from = date_from;
        this.date_to = date_to;
        this.working_time_from = working_time_from;
        this.working_time_to = working_time_to;
        this.working_hour = working_hour;
        this.job_location_address = job_location_address;
        this.job_location_latlng = job_location_latlng;
        this.job_details = job_details;
        this.working_job_salary_hourday = working_job_salary_hourday;
        this.job_salary_day = job_salary_day;
        this.job_salary_total  = job_salary_total;
        this.job_salary_hour  = job_salary_hour;
        this.status_payment  = status_payment;
        this.job_total_comission  = job_total_comission;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getEmployee_job_id() {
        return employee_job_id;
    }

    public String getEmployer_id() {
        return employer_id;
    }

    public String getEmployer_name() {
        return employer_name;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getJob_details() {
        return job_details;
    }

    public String getJob_location_address() {
        return job_location_address;
    }

    public String getJob_location_latlng() {
        return job_location_latlng;
    }

    public String getJob_name() {
        return job_name;
    }

    public String getJob_type() {
        return job_type;
    }

    public String getWorking_hour() {
        return working_hour;
    }

    public String getJob_salary_day() {
        return job_salary_day;
    }

    public String getJob_salary_hour() {
        return job_salary_hour;
    }

    public String getJob_salary_total() {
        return job_salary_total;
    }

    public String getWorking_time_from() {
        return working_time_from;
    }

    public String getWorking_time_to() {
        return working_time_to;
    }

    public String getJob_total_comission() {
        return job_total_comission;
    }

    public String getStatus_payment() {
        return status_payment;
    }

    public String getWorking_job_salary_hourday() {
        return working_job_salary_hourday;
    }
}
