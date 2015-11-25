<?php
	/*** Renvoie Oui si le parcours a été enregistré avec succès, Non sinon ***/

	include("config.php");
	include("fonctions.php");

	if(!empty($_GET['email']) && !empty($_GET['poids'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$poids = intval($_GET['poids']);

		$email = $mysqli->real_escape_string($_GET['email']);
				
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
		
		$mysqli->query("UPDATE Contexte SET Poids='$poids' WHERE Client_id='$idClient'");
		
		echo "Oui";
		
		$mysqli->close();
	}
	else if(!empty($_GET['email'])) {
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
			
		$result = $mysqli->query("SELECT Poids FROM Contexte WHERE Client_id='$idClient'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		
		
		echo intval($donnees['Poids']);
		
	}
	else {
		echo "Non";
	}


?>