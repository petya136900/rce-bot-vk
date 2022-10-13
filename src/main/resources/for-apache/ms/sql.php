<?php
// SETTINGS
include '../settings.php';
$ms = $endpoint."/mainshelude";
$toolsUrl = $endpoint."/tools";
//
$jsondata = file_get_contents("php://input");
$json = json_decode($jsondata, true);
if(empty($jsondata)) {
	http_response_code(200);
	$options = array(
	  'http' => array(
		'method'  => 'GET',
		'header'=>  "Content-Type: application/json\r\n" .
					"Accept: application/json\r\n"
		)
	);	
	$context  = stream_context_create( $options );	
	if($_GET["type"]=="load") {
		$result = file_get_contents( $ms, false, $context );
	} else if($_GET["type"]=="bs") {
		$result = file_get_contents( $ms."?type=bs", false, $context );
	} else if($_GET["type"]=="grouphash") {
		$result = file_get_contents( $ms."?type=gh", false, $context );
	} else {
		http_response_code(500); // 500
		exit;
	}
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}	
} else if($_GET["type"]=="send") {
	//updatems
	$json["ip"]=$_SERVER['REMOTE_ADDR'];
	$options = array(
	  'http' => array(
		'method'  => 'POST',
		'content' => json_encode( $json ),
		'header'=>  "Content-Type: application/json\r\n" .
					"Accept: application/json\r\n"
		)
	);
	$context  = stream_context_create( $options );
	$result = file_get_contents( $ms."?type=updatems", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}
} else if($_GET["type"]=="gbs") {
	// updatebs
	$json["ip"]=$_SERVER['REMOTE_ADDR'];
	$options = array(
	  'http' => array(
		'method'  => 'POST',
		'content' => json_encode( $json ),
		'header'=>  "Content-Type: application/json\r\n" .
					"Accept: application/json\r\n"
		)
	);
	$context  = stream_context_create( $options );
	$result = file_get_contents( $ms."?type=updatebs", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}	
} else if($_GET["type"]=="bgn") {
	$json["ip"]=$_SERVER['REMOTE_ADDR'];
	$options = array(
	  'http' => array(
		'method'  => 'POST',
		'content' => json_encode( $json ),
		'header'=>  "Content-Type: application/json\r\n" .
					"Accept: application/json\r\n"
		)
	);
	$context  = stream_context_create( $options );
	$result = file_get_contents( $toolsUrl."?type=gbgn", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}	
} else {
	http_response_code(500); // 500
	exit;
}
?>