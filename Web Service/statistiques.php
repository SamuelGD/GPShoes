<?php
	/*** Renvoie les statistiques de performances d'un client pour un parcours donné en json ***/


	include("config.php");
	include("fonctions.php");

	if(!empty($_GET['email']) && !empty($_GET['idParcours'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$email = $mysqli->real_escape_string($_GET['email']);
		$idParcours = intval($_GET['idParcours']);
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
		
		$result = $mysqli->query("SELECT vitesse, temps FROM Vitesse WHERE Client_id='$idClient' AND Parcours_idParcours='$idParcours'");
		
		if(!$result) sortir($mysqli);
		
		$donnees = $result->fetch_assoc();		
		
		$vitesse = $donnees['vitesse'];
		$temps = $donnees['temps'];
		
		$result = $mysqli->query("SELECT longueur FROM Parcours WHERE idParcours='$idParcours'");
		
		if(!$result) sortir($mysqli);
		
		$donnees = $result->fetch_assoc();	

		$longueur = floatval($donnees['longueur']);
		
		
		$result = $mysqli->query("SELECT Poids FROM Contexte WHERE Client_id='$idClient'");
		
		$donnees = $result->fetch_assoc();	
		$poids = $donnees['Poids'];
		$poids = floatval($poids);
		
		$calories = $longueur * $poids;
		
		$retourJson = array("vitesse" => $vitesse, "temps" => $temps, "calories" => $calories, "longueur" => $longueur);
		
		// On affiches les données sérialisées en json
		echo json_encode($retourJson);
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>