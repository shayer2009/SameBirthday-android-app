<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Users extends REST_Controller {

    function __construct() {
        parent::__construct();
        $this->load->helper('url');
        $this->load->database();
        $this->load->library('Session');
        $this->load->library('email');
        $this->load->library("mylib");
        $this->load->library('form_validation');
        $this->form_validation->set_error_delimiters('', '');
        //Load Model
        //$this->load->model("admin_model", "admin", true);
        $this->load->model("users_model", "users", true);
        $this->load->model("search_model", "search", true);
        $this->load->model("chat_model", "message", true);
    }

    function register_post() {
        $Data = $this->mylib->get_data();

        if ($this->form_validation->run('register') == FALSE) {
            $responce["Result"] = 0;
            //$responce['Message'] = "validation error occure.";
            //$responce["Errors"] = $this->mylib->validationErrors($Data);
            $responce["Message"] = validation_errors();
            $responce["Record"] = $Data;
            $this->response($responce, 200);
        } else {
            $Data["Password"] = sha1($Data["Password"]);
            $Data["Birthdate"] = date("Y-m-d", strtotime($Data["Birthdate"]));
            if ($UserID=$this->users->insert($Data)) {
                $User = $this->users->get($UserID);
                $responce["Result"] = 1;
                $responce['Message'] = "User Registered Successfully.";
                while (1) {
                    $UniqueKey = $this->mylib->GenerateNumber();
                    if ($this->users->IsValidSessionID($UniqueKey)) {
                        $this->users->update($UserID, array("SessionID" => $UniqueKey));
                        $responce['SessionID'] = $UniqueKey;
                        break;
                    }
                }
                
                
                
                $responce["Username"] = $User["Username"];
                $responce["Birthdate"] = date(DATEFORMAT, strtotime($User["Birthdate"]));
                $responce["Gender"] = $User["Gender"];
                $responce["Latitude"] = $Data["Latitude"];
                $responce["Longitude"] = $Data["Longitude"];
                $this->response($responce, 200);
            }
        }

        $responce["Result"] = 0;
        $responce['Message'] = "Internal error occure.";
        $this->response($responce, 200);
    }

    function login_post() {
        $Data = $this->mylib->get_data();

        if ($this->form_validation->run('login') == FALSE) {
            $responce["Result"] = 0;
            $responce['Message'] = "validation error occure.";
            $responce["Errors"] = $this->mylib->validationErrors($Data);
            $responce["Record"] = $Data;
            $this->response($responce, 200);
        } else {

            if ($UserID = $this->users->authenticate($Data["Email"], $Data["Password"])) {
                $User = $this->users->get($UserID);

                $responce["Result"] = 1;
                $responce['Message'] = "User Login Successfully.";
                while (1) {
                    $UniqueKey = $this->mylib->GenerateNumber();
                    if ($this->users->IsValidSessionID($UniqueKey)) {
                        $this->users->update($UserID, array("SessionID" => $UniqueKey, "Latitude" => $Data["Latitude"], "Longitude" => $Data["Longitude"]));
                        $responce['SessionID'] = $UniqueKey;
                        break;
                    }
                }
                $responce["Username"] = $User["Username"];
                $responce["Birthdate"] = date(DATEFORMAT, strtotime($User["Birthdate"]));
                $responce["Gender"] = $User["Gender"];
                $responce["Latitude"] = $Data["Latitude"];
                $responce["Longitude"] = $Data["Longitude"];
                $this->response($responce, 200);
            } else {
                $responce["Result"] = 0;
                $responce['Message'] = "Username or password not valid";
                $responce['Record'] = $Data;
                $this->response($responce, 200);
            }
        }

        $responce["Result"] = 0;
        $responce['Message'] = "Internal error occure.";
        $this->response($responce, 200);
    }

    function search_post() {
        $Data = $this->mylib->get_data();
        $User = $this->users->getUserIDbySessionID($Data["SessionID"]);
        if (empty($User)) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }
        
        $search = $this->search->search($User["UserID"], $Data["Latitude"], $Data["Longitude"], $Data["Birthdate"], $Data["StartAge"], $Data["EndAge"], $Data["Gender"], $Data["Distance"]);
        $sCSV=array();
        $tmp_search=array();
        foreach ($search as $key => $value) {
            $sCSV[]=$value["UserID"];
            if(!empty($Data["UsersCSV"]) && in_array($value["UserID"], explode(",", $Data["UsersCSV"]))){
                
            }else{
                $tmp_search[]=$value;
            }
        }
        
        $responce["Result"] = 1;
        $responce["UsersCSV"] = (!empty($sCSV))?implode(",", $sCSV):""; 
        $responce["Users"] = $tmp_search;

        $this->response($responce, 200);
    }

    function nearby_post() {
        $Data = $this->mylib->get_data();
        $User = $this->users->getUserIDbySessionID($Data["SessionID"]);
        if (empty($User)) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }
        $UsersCSV=(!empty($Data["UsersCSV"]))?$Data["UsersCSV"]:"";
        $BirthdayCSV=(!empty($Data["BirthdayCSV"]))?$Data["BirthdayCSV"]:"";
        unset($Data["SessionID"]);
        unset($Data["UsersCSV"]);
        unset($Data["BirthdayCSV"]);
        $this->users->update($User["UserID"], $Data);

        $search = $this->search->search($User["UserID"], $Data["Latitude"], $Data["Longitude"], NULL, NULL, NULL, NULL, 10);
        $uCSV=array();
        $tmp_search=array();
        foreach ($search as $key => $value) {
            $uCSV[]=$value["UserID"];
            if(!empty($UsersCSV) && in_array($value["UserID"], explode(",", $UsersCSV))){
                unset($search[$key]);
            }else{
                $tmp_search[]=$value;
            }
        }
        
        $bCSV=array();
        $tmp_TodayBirthday=array();
        $TodayBirthday = $this->search->search($User["UserID"], $Data["Latitude"], $Data["Longitude"], date("m-d", strtotime($User["Birthdate"])), NULL, NULL, NULL, 10);
        foreach ($TodayBirthday as $key => $value) {
            $value["Distance_metre"] = "" . (1609.344 * $value["Distance"]);
            $bCSV[]=$value["UserID"];
            if(!empty($BirthdayCSV) && in_array($value["UserID"], explode(",", $BirthdayCSV))){
                unset($TodayBirthday[$key]);
            }else{
                $tmp_TodayBirthday[]=$value;
            }
        }


        $responce["Result"] = 1;
        $responce["UserID"] = $User["UserID"];
        $responce["Today"] = date("Y-m-d A");
        $responce["Users"] = $tmp_search;
        $responce["UsersCSV"] = (!empty($uCSV))?implode(",", $uCSV):"";
        $responce["Birthday"] = $tmp_TodayBirthday;
        $responce["BirthdayCSV"] = (!empty($bCSV))?implode(",", $bCSV):"";

        //$this->response($responce, 200);


        $userID = $User["UserID"];
        $my_all_message = $this->message->get_message_history($userID);

        $tmp = array();
        foreach ($my_all_message as $key => $value) {
            if ($value["sender_id"] == $userID && empty($tmp[$value["receiver_id"]])) {
                $tmp[$value["receiver_id"]] = $value;
            }
            if ($value["receiver_id"] == $userID && empty($tmp[$value["sender_id"]])) {
                $tmp[$value["sender_id"]] = $value;
            }
        }

        if (!empty($my_all_message)) {
            foreach ($tmp as $key => $value) {
                $tmp[$key]['add_date'] = date('d/m/y', strtotime($value['add_date']));
                if ($value['receiver_id'] == $userID) {
                    $UserID = $this->users->get_by_id($value['sender_id']);
                }
                if ($value['sender_id'] == $userID) {
                    $UserID = $this->users->get_by_id($value['receiver_id']);
                }
                $tmp[$key]["User"] = $UserID;
            }

            $tmp2 = array();
            foreach ($tmp as $key => $value) {
                $tmp2[] = $value;
            }
            $responce['Message'] = $tmp2;
            $this->response($responce, 200);
        } else {
            $responce['Message'] = array();
            $this->response($responce, 200);
        }
    }

}
