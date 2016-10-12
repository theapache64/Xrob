<?php

define('BASE_SERVER_URL','http://192.168.0.101:8080/v1');

$headers = apache_request_headers();
$authKey = $headers['authorization'];

$victimResp = file_get_contents(BASE_SERVER_URL."/isVictimExist?api_key=".$authKey);

$fileName = $_FILES['data']['name'];

