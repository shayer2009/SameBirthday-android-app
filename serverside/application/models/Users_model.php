<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Users_model extends MY_Model {

    public $_table = 'users'; // you MUST mention the table name
    public $primary_key = 'UserID'; // you MUST mention the primary key
    //public $fillable = array('Password'); // If you want, you can set an array with the fields that can be filled by insert/update
    public $protected_attributes = array(); // ...Or you can set an array with the fields that cannot be filled by insert/update
    public $before_create = array('created_at', 'updated_at');
    public $before_update = array('updated_at');
    //public $after_get = array('test');

    //public $soft_delete=false;
    public function __construct() {
        parent::__construct();
    }

    protected function test($data) {
        if (empty($data["Email"])) {
            $data['Email'] = "a@a.com";
            //$data['UpdatedAt'] = date('Y-m-d H:i:s', time());
        }
        return $data;
    }

    /**
     * Check user Is Authenticated Or Not.
     */
    function authenticate($email, $password) {
        if ($email != "" && $password != "") {
            $this->db->select("UserID")->from($this->_table);
            $condition = array(
                "Email" => $email,
                "Password" => sha1($password)
            );
            $this->db->where($condition);
            $query = $this->db->get();
            $admin = $query->result_array();
            if (!empty($admin)) {
                return $admin[0]['UserID'];
            } else
                return $admin;
        } else {
            return FALSE;
        }
    }

    /**
     * Fetch a single record based on the primary key. Returns an object.
     */
    function IsValidSessionID($uniqueKey) {
        $this->db->select($this->primary_key);
        $this->db->from($this->_table);
        $this->db->limit('1');
        $this->db->where("SessionID", $uniqueKey);
        $query = $this->db->get();
        $result = $query->result_array();
        if (empty($result)) {
            return true;
        }
        return false;
    }

    function getUserIDbySessionID($SessionID) {
        if (!empty($SessionID)) {
            $this->db->select('*');
            $this->db->from('users');
            $this->db->limit('1');
            $this->db->where("SessionID", $SessionID);
            $query = $this->db->get();
            $result = $query->result_array();
            if (!empty($result)) {
                return $result[0];
            }
        }
        return false;
    }

    function search($UserID, $lat, $log, $Birthdate, $StartAge, $EndAge, $Gender, $Distance) {
        $this->db->select("UserID,Username,Email,Birthdate,Gender,( 3959 * acos( cos( radians('{$lat}') ) * cos( radians( Latitude ) ) * cos( radians( Longitude ) - radians('{$log}') ) + sin( radians('{$lat}') ) * sin( radians( Latitude ) ) ) ) AS distance");
        $this->db->from('users');
        $query = $this->db->get();
        $result = $query->result_array();

        return $result;
    }
	
	function get_by_id($id) {
        $this->db->select('*');
        $this->db->from('users');
        $this->db->limit('1');
        $this->db->where("UserID", $id);
        $query = $this->db->get();
        $result = $query->result_array();
        if (!empty($result)) {
            return $result['0'];
        } else {
            return false;
        }
    }

}
