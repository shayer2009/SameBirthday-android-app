<?php

class MY_Form_validation extends CI_Form_validation {

    ## link
    //  http://stackoverflow.com/questions/27621250/is-unique-in-codeigniter-for-edit-function
   
    
    ## If Soft delete is availabel than IsDelete=0 
    public function is_unique($str, $field) {

        if (substr_count($field, '.') == 3) {
            list($table, $field, $id_field, $id_val) = explode('.', $field);
            $query = $this->CI->db->limit(1)->where($field, $str)->where($id_field . ' != ', $id_val)->where("IsDeleted", "0")->get($table);
        } else {
            list($table, $field) = explode('.', $field);
            $query = $this->CI->db->limit(1)->where("IsDeleted", "0")->get_where($table, array($field => $str));
        }

        return $query->num_rows() === 0;
    }

    /* implementation */
    /*
      function update() {

      $user_id = $this->input->post("user_id");
      $rules = array(array(
      'field' => 'email',
      'label' => 'Email',
      'rules' => 'required|valid_email|is_unique[users.Email.id.' . $user_id . ']'));

      $this->form_validation->set_rules($rules);
      } */

}