<?php
include 'settings.php';

$vkPath = "vk";
$vkEndpointPath = $endpoint."/".$vkPath;

$errorMsg = '<body style="font-family: -apple-system,BlinkMacSystemFont,Roboto,Open Sans,Helvetica Neue,sans-serif;">Access Forbidden</br>Go <a href="ms">here [Main Shelude]</a></br>or <a href="teachers">here [Teachers]</a></body>';
$jsondata = file_get_contents("php://input");
$json = json_decode($jsondata, true);
if(!empty($json)!=0) {
	//echo print_r($json, true);
	$options = array(
	  'http' => array(
		'method'  => 'POST',
		'content' => json_encode( $json ),
		'header'=>  "Content-Type: application/json\r\n" .
					"Accept: application/json\r\n"
		)
	);
	$context  = stream_context_create( $options );
	$result = file_get_contents( $vkEndpointPath, false, $context );
	echo $result;
} else {
	echo $errorMsg;
}
?>