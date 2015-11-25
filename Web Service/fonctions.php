<?php
function sortir($mysqli) {
	printf("Non");
	$mysqli->close();
	die();
}

function chiffrer($password) {
	return md5($salt.$password);
}