<?php
	/*** Renvoie la liste des parours favoris pour un client ***/


	include("config.php");
	include("fonctions.php");
	
	if(!empty($_GET['email'])) {
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
		
		$retourJson = array();
		
		$result = $mysqli->query("SELECT Parcours_idParcours FROM Favoris WHERE Client_id='$idClient'");
		if(!$result) sortir($mysqli);
		while($donnees = $result->fetch_assoc()) {
			$idParcours = $donnees['Parcours_idParcours'];
			$result2 = $mysqli->query("SELECT X(point) as latitude, Y(point) as longitude FROM Point WHERE Parcours_idParcours ='$idParcours' ORDER BY id ASC");
			$result3 = $mysqli->query("SELECT date FROM faitUn WHERE Parcours_idParcours='$idParcours'");
			$donnees2 = $result3->fetch_assoc();
			$date = $donnees2['date'];
			
			$flag = true;
			$parcours = "";
			while($point = $result2->fetch_assoc()) {
				if(!$flag) $parcours .= ";";
				$parcours .= $point['latitude'].":".$point['longitude'];
				if($flag) {
					$flag = false;
				}
			}
			
			$result3 = $mysqli->query("SELECT nom, Notes FROM Parcours WHERE idParcours='$idParcours'");
			$donnees2 = $result3->fetch_assoc();
			$nom = utf8_encode($donnees2['nom']);
			$note = $donnees2['Notes'];
			
			$result3 = $mysqli->query("SELECT * FROM Favoris WHERE Client_id='$idClient' AND Parcours_idParcours='$idParcours'");
			if($result3->num_rows >= 1)
				$favori = true;
			else
				$favori = false;
			
			array_push($retourJson, array("id" => $idParcours, "nom" => $nom, "note" => $note, "parcours" => $parcours, "favori" => $favori, "date" => $date));
		}
		
		// On affiches les données sérialisées en json
		echo json_encode($retourJson);
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>