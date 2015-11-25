<?php
	/*** Renvoie Oui si le parcours a été enregistré avec succès, Non sinon ***/

	include("config.php");
	include("fonctions.php");

	if(!empty($_POST['nomParcours']) && !empty($_POST['note']) && !empty($_POST['email']) && !empty($_POST['date']) && !empty($_POST['parcours'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$nomParcours = $mysqli->real_escape_string($_POST['nomParcours']);
		$note = intval($_POST['note']);
		$email = $mysqli->real_escape_string($_POST['email']);
		$date = $mysqli->real_escape_string($_POST['date']);
		$parcours = $mysqli->real_escape_string($_POST['parcours']);
		$favori = false;
		
		if($_POST['favori'] == "true")
			$favori = true;
		
		if(isset($_POST['longueur']))
			$longueur = intval($_POST['longueur']);
		else
			$longueur = 0;
		
		if(isset($_POST['temps']))
			$temps = $mysqli->real_escape_string($_POST['temps']);
		else
			$temps = "";
		
		
		$sql = "INSERT INTO Parcours (nom, Notes, NbdeNotes, longueur) VALUES('$nomParcours', '$note', '1', '$longueur')";
		$retour = $mysqli->query($sql);
		if(!$retour)
			sortir($mysqli);
		
		if ($retour === TRUE)
			$idParcours = $mysqli->insert_id;
		else sortir($mysqli);
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
		
		$mysqli->query("INSERT INTO faitUn VALUES('$idClient', '$idParcours', '$date')");
		
		if(!empty($_POST['vitesse'])) {
			$vitesse = floatval($_POST['vitesse']);
			$mysqli->query("INSERT INTO Vitesse (vitesse, Client_id, temps, Parcours_idParcours) VALUES('$vitesse', '$idClient', '$temps', '$idParcours')");
		}
		
		
		
		if($favori)
			$mysqli->query("INSERT INTO Favoris VALUES('$idClient', '$idParcours')");
		
		// Format de parcours:  "lat1:lgt1;lat2:lgt2;...;latn:lgtn"
		$tok1 = strtok($parcours, ";");
		while ($tok1 !== false) {
			list($lat, $lgt) = sscanf($tok1, "%f:%f");
			
			$lat = $mysqli->real_escape_string($lat);
			$lgt = $mysqli->real_escape_string($lgt);
			
			$mysqli->query("INSERT INTO Point(point, Parcours_idParcours) VALUES(GeomFromText('POINT($lat $lgt)'),'$idParcours')");
			
			$tok1 = strtok(";");
		}
		
		echo "Oui";
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>