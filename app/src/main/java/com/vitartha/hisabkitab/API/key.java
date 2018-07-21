package com.vitartha.hisabkitab.API;

public interface key {

    interface server {
        String rooturl = "http://local.hisabkitab.in/api/";
        String key_status = "status_code";
        String key_token = "token";
        String key_data = "data";
        String key_paginator = "paginator";
    }

    interface user_api {
        String user_endpoint = "users/";
        String key_username = "username";
        String key_pwd = "password";
        String key_fullname = "name";
        String key_mob = "mobile";
        String key_mail = "email";
        String key_otp = "value";
        String key_otpvalue = "otp";
        String key_newpwd = "new_password";
        String login_endpoint = server.rooturl + user_endpoint + "login/";
        String registration_endpoint = server.rooturl + user_endpoint + "register/";
        String otp_endpoint = server.rooturl + user_endpoint + "loginotp/";
        String change_pwd_endpoint = server.rooturl + user_endpoint + "changepassword/";
        String update_profile_endpoint = server.rooturl + user_endpoint + "updateprofile/";
    }

    interface transactions {
        String transaction_endpoint = "transactions/";
        String show_url = server.rooturl + transaction_endpoint + "show/";
        String add_url = server.rooturl + transaction_endpoint + "add/";
    }
}
