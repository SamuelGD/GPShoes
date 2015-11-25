<?php
	/*** Renvoie Oui si le parcours a été enregistré avec succès, Non sinon ***/

	include("config.php");
	include("fonctions.php");

	if(!empty($_GET['idParcours']) && !empty($_GET['email']) && !empty($_GET['date'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$idParcours = intval($_GET['idParcours']);
		$email = $mysqli->real_escape_string($_GET['email']);
		$date = $mysqli->real_escape_string($_GET['date']);
		$favori = false;
		
		if($_GET['favori'] == "true")
			$favori = true;
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
		
		////// INSERT SI ALENTOURS
		if(!$mysqli->query("INSERT INTO faitUn (date, Client_id, Parcours_idParcours) VALUES('$date', '$idClient', '$idParcours'"))
			$mysqli->query("UPDATE faitUn SET date='$date' WHERE Client_id='$idClient' AND Parcours_idParcours='$idParcours'");
		
		if(!empty($_GET['vitesse'])) {
			$vitesse = floatval($_GET['vitesse']);
			$temps = $mysqli->real_escape_string($_GET['temps']);
			if(!isset($temps)) $temps = "";
			$mysqli->query("UPDATE Vitesse SET vitesse='$vitesse', temps='$temps' WHERE Client_id='$idClient' AND Parcours_idParcours = '$idParcours'");
		}
		
		if($favori)
			$mysqli->query("INSERT INTO Favoris VALUES('$idClient', '$idParcours')");
		
		
		echo "Oui";
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>