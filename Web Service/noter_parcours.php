<?php
	/*** Renvoie Oui si le parcours a été mis à jour avec succès, Non sinon ***/

	include("config.php");
	include("fonctions.php");
	
	if(!empty($_GET['idParcours']) && !empty($_GET['note']) && !empty($_GET['email'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$idParcours = floatval($_GET['idParcours']);
		$note = floatval($_GET['note']);

		$email = $mysqli->real_escape_string($_GET['email']);
		$favori = false;
		if($_GET['favori'] == "true")
			$favori = true;
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];

		if($favori)
			$mysqli->query("INSERT INTO Favoris VALUES('$idClient', '$idParcours')");
		
		
		if(($note >= 1) && ($note <= 5)) {
			$result = $mysqli->query("SELECT Notes, NbdeNotes FROM Parcours WHERE idParcours='$idParcours'");
			if(!$result) sortir($mysqli);
			$donnees = $result->fetch_assoc();
			$exNote = $donnees['Notes'];
			$nb = $donnees['NbdeNotes'];
		
			$nouvelle_note = ($exNote * $nb + $note) / ($nb + 1);
			$nb = $nb + 1;
		
			$mysqli->query("UPDATE Parcours SET Notes='$nouvelle_note', NbdeNotes='$nb' WHERE idParcours='$idParcours'");
		}
		
		echo "Oui";
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>