<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Auth extends CI_Controller {

    public function __construct() {
        parent::__construct();
        $this->load->library("mylib");
        $this->load->model("auth_model", "auth", TRUE);
    }

    public function index() {
        $this->load->view('admin/login');
    }

    public function login() {
        $email = $this->input->post('Email');
        $password = $this->input->post('Password');
        if ($email != "" && $password != "") {
            $admin = $this->auth->authenticate($email, $password);

            if (!empty($admin)) {
                //$update['LastLoginAt'] = date("Y-m-d H:i:s", time());
                $update['AdminID'] = $admin['AdminID'];
                $this->auth->update($update['AdminID'], $update);
                $this->session->set_userdata($admin);
                $url = base_url() . 'dashboard';
                redirect($url);
            } else {
                $this->session->set_flashdata('error', 'Invalid email address or password.');
                $url = base_url() . 'auth';
                redirect($url);
            }
        } else {

            $url = base_url() . 'auth';
            redirect($url);
        }
    }

    function logout() {
        $this->auth->isLogin();
        $this->session->unset_userdata();
        $this->session->sess_destroy();
        session_destroy();
        $this->session->set_flashdata('success', 'Log Out Successfully. ');
        $url = base_url() . 'auth';
        redirect($url);
    }

}
