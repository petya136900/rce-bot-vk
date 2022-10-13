<?php
// SETTINGS
include '../settings.php';
$url = $endpoint;
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
	if($_GET["type"]=="getteachers") {
		$result = file_get_contents( $url."/teachers"."?type=getall", false, $context );
	} else if($_GET["type"]=="teachershash") {
		$result = file_get_contents( $url."/teachers"."?type=hash", false, $context );
	} else if($_GET["type"]=="getpairs") {
		$result = file_get_contents( $url."/pairs"."?type=getall", false, $context );
	} else if($_GET["type"]=="pairshash") {
		$result = file_get_contents( $url."/pairs"."?type=hash", false, $context );
	} else {
		http_response_code(500); // 500
		exit;
	}
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}	
} else if($_GET["type"]=="addteacher") {
	// add teacher
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
	$result = file_get_contents( $url."/teachers"."?type=add", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}
} else if($_GET["type"]=="updateteacher") {
	// update teacher
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
	$result = file_get_contents( $url."/teachers"."?type=update", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}
} else if($_GET["type"]=="deleteteacher") {
	// delete teacher
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
	$result = file_get_contents( $url."/teachers"."?type=delete", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}
} else if($_GET["type"]=="addpair") {
	// add pair
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
	$result = file_get_contents( $url."/pairs"."?type=add", false, $context );
	if(strlen($result)==0) {
		http_response_code(500); // 500
	} else {
		echo $result;
	}
} else if($_GET["type"]=="updatepair") {
	// delete/update pair
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
	$result = file_get_contents( $url."/pairs"."?type=update", false, $context );
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