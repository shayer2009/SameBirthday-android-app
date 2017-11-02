<?php

class Chat_model extends CI_Model {

    function update($data) {
        try {
            $this->db->where('id', $data['id']);
            $this->db->delete('messages');
            return true;
        } catch (Exception $e) {
            return false;
        }
    }

    function update_read($UserID) {
        try {
            //$this->db->where("sender_id = '{$UserID}' OR receiver_id = '{$UserID}'");
            $this->db->update('messages',array("is_read"=>1),"sender_id = '{$UserID}'");
            return true;
        } catch (Exception $e) {
            return false;
        }
    }

    function get_all() {
        $where = "messages.IsDelete = '0'";
        $this->db->select('*');
        $this->db->from('messages');
        $this->db->where($where);
        $this->db->order_by("id", "desc");
        $query = $this->db->get();
        return $query->result_array();
    }

    function send($data) {
        $data['add_date'] = date("Y-m-d H:i:s", time());
        $this->db->insert("messages", $data);
        $id = $this->db->insert_id();
        return $id;
    }

    function get_message($data, $last_id = NULL) {
        $this->db->select('*');
        $this->db->from('messages');
        if (!empty($last_id)) {
            $this->db->where("(receiver_id=" . $data['receiver_id'] . " And sender_id=" . $data['sender_id'] . " AND id >" . $last_id . ")");
            $this->db->or_where("(receiver_id=" . $data['sender_id'] . " And sender_id=" . $data['receiver_id'] . " AND id >" . $last_id . ")");
        } else {
            $this->db->where("(receiver_id=" . $data['receiver_id'] . " And sender_id=" . $data['sender_id'] . ")");
            $this->db->or_where("(receiver_id=" . $data['sender_id'] . " And sender_id=" . $data['receiver_id'] . ")");
        }

        $this->db->order_by("id", "desc");
        $this->db->limit('50');
        $query = $this->db->get();
        $result = $query->result_array();
        if (!empty($result)) {
            return $result;
        } else {
            return false;
        }
    }

    function get_message_history($user_id) {

        $this->db->select('*');
        $this->db->from('messages');
        //$this->db->where("sender_id IN (SELECT sender_id FROM `sb_messages` WHERE sender_id='".$user_id."' ORDER BY ID DESC)", NULL, FALSE);
        //$this->db->or_where("receiver_id in(SELECT receiver_id FROM `sb_messages` WHERE receiver_id='".$user_id."' ORDER BY ID DESC)", NULL, FALSE);
        $this->db->where("(sender_id='" . $user_id . "')");
        $this->db->or_where("(receiver_id='" . $user_id . "')");
        $this->db->order_by("id", "desc");
        //$this->db->distinct();
        //$this->db->group_by('sender_id');
        //$this->db->group_by('receiver_id');
        $this->db->limit('50');

        $query = $this->db->get();
        $result = $query->result_array();
        if (!empty($result)) {
            return $result;
        } else {
            return array();
        }
    }

}
