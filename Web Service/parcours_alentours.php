<?php
	/*** Renvoie la liste des parours favoris pour un client ***/

	$rayonRecherche = 2; // 2 km par défaut
	$rayonTerre = 6371.009;

	include("config.php");
	include("fonctions.php");
	
	if(!empty($_GET['lat']) && !empty($_GET['lng']) && !empty($_GET['email'])) {
		if(!empty($_GET['rayon']))
			$rayonRecherche = floatval($_GET['rayon']);
		
		
		
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
		
		$lat = floatval($_GET['lat']);
		$lng = floatval($_GET['lng']);
	

		$result = $mysqli->query("SELECT Parcours_idParcours FROM Point WHERE (6371.009*ABS(ACOS(SIN(".$lat."* PI() / 180)*SIN(X(point)* PI() / 180) + COS(".$lat."* PI() / 180)*cos(X(point)* PI() / 180)*COS((".$lng." - Y(point))* PI() / 180))) <= '$rayonRecherche') GROUP BY Parcours_idParcours");
		//$result = $mysqli->query("SELECT Parcours_idParcours FROM Point WHERE ((ACOS(SIN($lat * PI() / 180) * SIN(X(point) * PI() / 180) + COS($lat * PI() / 180) * COS(X(point) * PI() / 180) * COS(($lon – Y(point)) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) <= '$rayonRecherche') GROUP BY Parcours_idParcours");
	
		//(A, B) = R * arccos (sin (lata) sin (LATB) + cos (lata) cos (LATB) cos (Lona-lonB)) 
		
		$retourJson = array();
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