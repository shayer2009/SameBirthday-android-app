<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Welcome extends CI_Controller {

    public function __construct() {
        parent::__construct();
        $this->load->database();
        $this->load->library("mylib");
        $this->load->model("users_model", "users", true);
        //$this->load->model("auth_model", "auth", true);
    }

    public function index() 
	{
		$this->load->view("welcome_message");   
    }

    
}
