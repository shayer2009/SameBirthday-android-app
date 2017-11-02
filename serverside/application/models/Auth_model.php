<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Auth_model extends MY_Model {

    public $_table = 'admin'; // you MUST mention the table name
    public $primary_key = 'AdminID'; // you MUST mention the primary key
    //public $fillable = array('Email'); // If you want, you can set an array with the fields that can be filled by insert/update
    public $protected_attributes = array(); // ...Or you can set an array with the fields that cannot be filled by insert/update
    public $before_create = array('created_at', 'updated_at');
    public $before_update = array('updated_at');


    public function __construct() {
        parent::__construct();
    }

    function authenticate($email, $password) {

        if ($email != "" && $password != "") {
            $this->db->select("AdminID,Email,UserName")->from("admin");
            $condition = array(
                "Email" => $email,
                "Password" => sha1($password)
            );
            $this->db->where($condition);
            $query = $this->db->get();
            $admin = $query->result_array();
            if (!empty($admin)) {
                return $admin[0];
            } else
                return array();
        } else {
            return array();
        }
    }

    function isLogin() {
        if ($this->session->userdata($this->primary_key)) {
            return true;
        } else {
            $this->session->set_flashdata('notification_type', 'error');
            $this->session->set_flashdata('error', 'Session expired.');
            $url = base_url() . 'auth';
            redirect($url);
            exit();
        }
    }

}
