<?php
	/*** Renvoie la liste des parours favoris pour un client ***/


	include("config.php");
	include("fonctions.php");
	
	if(!empty($_GET['favori']) && !empty($_GET['idParcours']) && !empty($_GET['email'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		$email = $mysqli->real_escape_string($_GET['email']);
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
				
		$idParcours = intval($_GET['idParcours']);
		if($_GET['favori'] == "false")
			$mysqli->query("DELETE FROM Favoris WHERE Client_id='$idClient' AND Parcours_idParcours='$idParcours'");
		else if ($_GET['favori'] == "true")
			$mysqli->query("INSERT INTO Favoris VALUES('$idClient', '$idParcours')");
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>