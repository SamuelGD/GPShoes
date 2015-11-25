<?php
	/*** Renvoie Oui si la connexion a été établie, Non sinon ***/

	include("config.php");
	include("fonctions.php");
	
	if(isset($_GET['email']) && !empty($_GET['email']) && isset($_GET['password']) && !empty($_GET['password'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$email = $mysqli->real_escape_string($_GET['email']);
		$password = $mysqli->real_escape_string(chiffrer($_GET['password']));
		
		$resultat = $mysqli->query("SELECT * FROM Clients WHERE email='$email' AND password='$password'");
		if(!$resultat)
			sortir($mysqli);
		
		if($resultat->num_rows >= 1)
			echo "Oui";
		else
			echo "Non";
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>