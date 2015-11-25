<?php
	/*** Renvoie Oui si l'utilisateur a été créé avec succès, Non sinon ***/

	include("config.php");
	include("fonctions.php");
	
	if(isset($_GET['email']) && !empty($_GET['email']) && isset($_GET['password']) && !empty($_GET['password']) && !empty($_GET['poids'])) {
		$mysqli = new mysqli($BDD_hote, $BDD_utilisateur, $BDD_password, $BDD_nom);
		if(mysqli_connect_errno()) {
			printf("Echec de la connexion à la base de donnée : %s<br />", mysqli_connect_error());
			die();
		}
		
		$email = $mysqli->real_escape_string($_GET['email']);
		$password = $mysqli->real_escape_string(chiffrer($_GET['password']));
		$poids = intval($_GET['poids']);
		
		$resultat = $mysqli->query("SELECT * FROM Clients WHERE email='$email'");
		if(!$resultat)
			sortir($mysqli);
		
		if($resultat->num_rows >= 1)
			sortir($mysqli);
		
		if(!$mysqli->query("INSERT INTO Clients (email, password) VALUES('$email', '$password')"))
			sortir($mysqli);
		
		$result = $mysqli->query("SELECT id FROM Clients WHERE email='$email'");
		if(!$result) sortir($mysqli);
		$donnees = $result->fetch_assoc();
		$idClient = $donnees['id'];
		
		if(!$mysqli->query("INSERT INTO Contexte (Poids, Client_id) VALUES('$poids', '$idClient')"))
			sortir($mysqli);
		
		
		echo "Oui";
		
		$mysqli->close();
	}
	else {
		echo "Non";
	}


?>