<?php

/**
 *  Set Global Validation Rule.
 */
$config = array(
    /*   Login Rule   */
    'login' => array(
        array(
            'field' => 'Email',
            'label' => 'Email',
            'rules' => 'required'
        ),
        array(
            'field' => 'Password',
            'label' => 'Password',
            'rules' => 'required'
        ),
    ),
    /* Registration Rule  */
    'register' => array(
        array(
            'field' => 'Username',
            'label' => 'Username',
            'rules' => '',
            'errors' => array(
                'is_unique' => '%s already exists',
            ),
        ),
        array(
            'field' => 'Email',
            'label' => 'Email',
            'rules' => 'required|is_unique[users.Email]',
            'errors' => array(
                'is_unique' => '%s already exists',
            ),
        ),
        array(
            'field' => 'Password',
            'label' => 'Password',
            'rules' => 'required|min_length[6]',
        ),
        array(
            'field' => 'Birthdate',
            'label' => 'Birthdate',
            'rules' => 'required'
        ),
        array(
            'field' => 'Gender',
            'label' => 'Gender',
            'rules' => 'required'
        ),
    )
);

