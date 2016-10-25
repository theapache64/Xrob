<?php

define('SECRET','mySecretServerKey');

$headers = apache_request_headers();

$authKey = isset($headers['Authorization']) ? $headers['Authorization']  : null;
header('Content-Type: application/json');

define ('SITE_ROOT', dirname(__DIR__));

$arr = array();

if($authKey===SECRET){

	//Checking if the request has a file
	if(isset($_FILES['file'])){

		$fileName = $_FILES['file']['name'];
		$tmpName = $_FILES['file']['tmp_name'];

		$target_file = SITE_ROOT. basename($_FILES["file"]["name"]);

		if(move_uploaded_file($tmpName, $target_file)){
			$arr['error'] = true;
			$arr['message'] = "file missing";
			$arr['download_link'] = "http://".$_SERVER['SERVER_NAME'].$target_file;
		}else{
			$arr['error'] = true;
			$arr['message'] = "failed to store the file";
		}

	}else{
		$arr['error'] = true;
		$arr['message'] = "file missing";
	}

}else{
	$arr['error'] = true;
	$arr['message'] = "Unauthorized request: " . $authKey;
}
		
echo json_encode($arr);
