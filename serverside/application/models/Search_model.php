<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Search_model extends MY_Model {

    public $_table = 'users'; // you MUST mention the table name
    public $primary_key = 'UserID'; // you MUST mention the primary key
    //public $fillable = array('Password'); // If you want, you can set an array with the fields that can be filled by insert/update
    public $protected_attributes = array(); // ...Or you can set an array with the fields that cannot be filled by insert/update
    public $before_create = array('timestamps_create');
    public $before_update = array('timestamps_update');

    //public $soft_delete=false;

    protected function timestamps_create($data) {
        $data['CreatedAt'] = date('Y-m-d H:i:s', time());
        $data['UpdatedAt'] = date('Y-m-d H:i:s', time());
        return $data;
    }

    protected function timestamps_update($data) {
        $data['UpdatedAt'] = date('Y-m-d H:i:s', time());
        return $data;
    }

    public function __construct() {
        parent::__construct();
    }

    /**
     * Search User By nearest lat long.
     */

    function search($UserID,$lat,$log,$Birthdate,$StartAge,$EndAge,$Gender,$Distance){
        $this->db->select("UserID,Username,Email,Birthdate,Gender,UpdatedAt,( 3959 * acos( cos( radians('{$lat}') ) * cos( radians( Latitude ) ) * cos( radians( Longitude ) - radians('{$log}') ) + sin( radians('{$lat}') ) * sin( radians( Latitude ) ) ) ) AS Distance");
        $this->db->from('users');
        //$this->db->having('')
        if(!empty($Gender) && ($Gender=="Male" || $Gender=="Female")){
            $this->db->where("Gender",$Gender);
        }
        if(!empty($Birthdate)){
            $this->db->like("Birthdate",$Birthdate,'both');
        }
        
        if(!empty($Distance)){
            $this->db->having('distance < ',$Distance);
        }
        if(!empty($StartAge)){
            $this->db->where('Birthdate <',date("Y-m-d",  strtotime("- ".$StartAge." year" )));
        }
        if(!empty($EndAge)){
            $this->db->where('Birthdate >',date("Y-m-d",  strtotime("- ".$EndAge." year" )));
        }
        
        $this->db->where("UserID !=",$UserID);
        $this->db->where("IsDeleted","0");
        $this->db->order_by('distance', "ASC");
        $this->db->limit(25);
        $query = $this->db->get();
        $result = $query->result_array();
        return $result;
    }
function searchsamebirthday($UserID,$lat,$log,$Distance){
        $this->db->select("UserID,Username,Email,Birthdate,Gender,UpdatedAt,( 3959 * acos( cos( radians('{$lat}') ) * cos( radians( Latitude ) ) * cos( radians( Longitude ) - radians('{$log}') ) + sin( radians('{$lat}') ) * sin( radians( Latitude ) ) ) ) AS Distance");
        $this->db->from('users');
        
        
        if(!empty($Distance)){
            $this->db->having('distance < ',$Distance);
        }
        $this->db->where("DATE_FORMAT(Birthdate, '%m%d') IN ( SELECT DATE_FORMAT(Birthdate, '%m%d') FROM sb_users WHERE IsDeleted = '0' GROUP BY DATE_FORMAT(Birthdate, '%m%d') HAVING COUNT(`UserID`) > 1 )", NULL, FALSE);
        
        $this->db->where("UserID !=",$UserID);
        $this->db->where("IsDeleted","0");
        $this->db->order_by("DATE_FORMAT(Birthdate, '%m%d')", "ASC",FALSE);
        $this->db->limit(25);
        $query = $this->db->get();
        $result = $query->result_array();
        return $result;
    }


}
