<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require APPPATH . '/libraries/REST_Controller.php';

class Message extends REST_Controller {

    function __construct() {
        parent::__construct();
        $this->load->helper('url');
        $this->load->helper('html');
        $this->load->database();
        $this->load->library('Session');
        $this->load->library('email');
        $this->load->library("mylib");
        //Load Model
        $this->load->model("auth_model", "auth", true);
        $this->load->model("users_model", "users", true);
        $this->load->model("chat_model", "message", true);
        //$this->load->model("myfunctions");
    }

    function send_post() {
        $data = $this->mylib->get_data();
        $SenderID=NULL;
        if (empty($data["SessionID"])) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }

        /* GET SENDER ID */
        $User = $this->users->getUserIDbySessionID($data['SessionID']);
        if (empty($User)) {
            
            unset($data["SessionID"]);
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }
        unset($data["SessionID"]);

        if (empty($data['receiver_id'])) {
            unset($data["SessionID"]);
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid receiver id.";
            $this->response($responce, 200);
        }
        $SenderID=$User["UserID"];


        /* ------------ */
        $data['sender_id'] = $SenderID;
        $data['receiver_id'] = $data['receiver_id'];
        $data['message'] = $data['message'];

        $send_message = $this->message->send($data);
        if (!empty($send_message)) {


            $sender = $this->users->get($SenderID);
            $sender['Birthdate'] = date('d/m/y', strtotime($sender["Birthdate"]));
            $sender['CreatedDate'] = ($sender["CreatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($sender["CreatedAt"])) : '00/00/00');
            $sender['UpdatedDate'] = ($sender["UpdatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($sender["UpdatedAt"])) : '00/00/00');

            if (!empty($sender["Photo"])) {
                $sender["Photo"] = base_url() . "application/upload/User/" . $sender["Photo"];
            }

            $receiver = $this->users->get($data['receiver_id']);
            
            
            $receiver['Birthdate'] = date('d/m/y', strtotime($receiver["Birthdate"]));
            $receiver['CreatedDate'] = ($receiver["CreatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($receiver["CreatedAt"])) : '00/00/00');
            $receiver['UpdatedDate'] = ($receiver["UpdatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($receiver["UpdatedAt"])) : '00/00/00');

            if (!empty($receiver["Photo"])) {
                $receiver["Photo"] = base_url() . "application/upload/User/" . $receiver["Photo"];
            }


            $responce["Result"] = 1;
            $responce["Sender"] = $sender;
            $responce["Receiver"] = $receiver;
            $responce['Message_id'] = '' . $send_message;
            $responce['Message'] = "Message send successfully";
            $this->response($responce, 200);
        }

        $responce["Result"] = 0;
        $responce['Message'] = "Moozer can not connect to server.";
        $this->response($responce, 200);
    }

    function get_message_post() {
        $data = $this->mylib->get_data();
        if (empty($data["SessionID"])) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }

        /* GET SENDER ID */
        $User = $this->users->getUserIDbySessionID($data['SessionID']);
        if (empty($User)) {

            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
            unset($data["SessionID"]);
        }
        unset($data["SessionID"]);
        $SenderID=$User["UserID"];
        /* ------------ */

        $data['sender_id'] = $SenderID;
        $data['receiver_id'] = $data['receiver_id'];

        
        if (isset($data['Last_msg_id']) && !empty($data['Last_msg_id'])) {
            $last_msg_id = $data['Last_msg_id'];
            $get_message = $this->message->get_message($data, $last_msg_id);
        } else {
            $get_message = $this->message->get_message($data);
        }
        if (!empty($get_message)) {
            sort($get_message);
            foreach ($get_message as $key => $value) {

                $get_message[$key]['add_date'] = date('d/m/y H:i:s', strtotime($value["add_date"]));
                if ($data["sender_id"] == $value["sender_id"]) {
                    $get_message[$key]["IsSend"] = "1";
                } else {
                    $get_message[$key]["IsSend"] = "0";
                }
            }
        }
        $this->message->update_read($data['receiver_id']);
      
                

        $sender = $this->users->get($SenderID);
        $sender['Birthdate'] = date('d/m/y', strtotime($sender["Birthdate"]));
        $sender['CreatedDate'] = ($sender["CreatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($sender["CreatedAt"])) : '00/00/00');
        $sender['UpdatedDate'] = ($sender["UpdatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($sender["UpdatedAt"])) : '00/00/00');

       

        $receiver = $this->users->get($data['receiver_id']);
        $receiver['Birthdate'] = date('d/m/y', strtotime($receiver["Birthdate"]));
        $receiver['CreatedDate'] = ($receiver["CreatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($receiver["CreatedAt"])) : '00/00/00');
        $receiver['UpdatedDate'] = ($receiver["UpdatedAt"] >= '1970-01-01' ? date('d/m/y H:i:s', strtotime($receiver["UpdatedAt"])) : '00/00/00');

       
        if (!empty($get_message)) {
            $responce["Result"] = 1;
            $responce["Sender"] = $sender;
            $responce["Receiver"] = $receiver;
            $responce['Message'] = $get_message;
            $this->response($responce, 200);
        } else {
            $responce["Result"] = 0;
            $responce['Message'] = "No message found.";
            $this->response($responce, 200);
        }
        $responce["Result"] = 0;
        $responce['Message'] = "Moozer can not connect to server.";
        $this->response($responce, 200);
    }

    function message_history_post() {
        $data = $this->mylib->get_data();
        if (empty($data["SessionID"])) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }
        $User = $this->users->getUserIDbySessionID($data['SessionID']);
        unset($data["SessionID"]);
        if (empty($User)) {
            $responce["Result"] = 0;
            $responce['Message'] = "Invalid User.";
            $this->response($responce, 200);
        }
		
        $userID=$User["UserID"];
        $my_all_message = $this->message->get_message_history($userID);
	
		$tmp=array();
		foreach($my_all_message as $key =>$value){
			if($value["sender_id"]==$userID && empty($tmp[$value["receiver_id"]])){
				$tmp[$value["receiver_id"]]=$value;
			}
			if($value["receiver_id"]==$userID && empty($tmp[$value["sender_id"]])){
				$tmp[$value["sender_id"]]=$value;
			}
			
		}
		
		if (!empty($my_all_message)) {
		
            foreach ($tmp as $key => $value) {
				$tmp[$key]['add_date'] = date('d/m/y', strtotime($value['add_date']));

                if ($value['receiver_id']==$userID ) {
                    $UserID = $this->users->get_by_id($value['sender_id']);
                }
				if ($value['sender_id']==$userID) {
					$UserID = $this->users->get_by_id($value['receiver_id']);
				}
				
                $tmp[$key]["User"] = $UserID;
            }
			
			$tmp2=array();
			foreach ($tmp as $key => $value) {
				$tmp2[]=$value;
			}
			
			$responce["Result"] = 1;
            $responce['Message'] = $tmp2;
            $this->response($responce, 200);
        }
		else {
            $responce["Result"] = 0;
            $responce['Message'] = "No message found.";
            $this->response($responce, 200);
        }
    }

}
