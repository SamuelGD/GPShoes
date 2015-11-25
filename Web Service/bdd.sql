-- phpMyAdmin SQL Dump
-- version 4.3.9
-- http://www.phpmyadmin.net
--
-- Client :  sql
-- Généré le :  Lun 20 Avril 2015 à 12:35
-- Version du serveur :  5.5.41-0+wheezy1
-- Version de PHP :  5.4.39-0+deb7u2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `shpouette_shpouette`
--

-- --------------------------------------------------------

--
-- Structure de la table `Clients`
--

CREATE TABLE IF NOT EXISTS `Clients` (
  `id` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Contexte`
--

CREATE TABLE IF NOT EXISTS `Contexte` (
  `Poids` decimal(10,0) DEFAULT NULL,
  `Client_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `faitUn`
--

CREATE TABLE IF NOT EXISTS `faitUn` (
  `Client_id` int(11) NOT NULL,
  `Parcours_idParcours` int(11) NOT NULL,
  `date` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Favoris`
--

CREATE TABLE IF NOT EXISTS `Favoris` (
  `Client_id` int(11) NOT NULL,
  `Parcours_idParcours` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Parcours`
--

CREATE TABLE IF NOT EXISTS `Parcours` (
  `idParcours` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `Notes` float DEFAULT NULL,
  `NbdeNotes` int(11) DEFAULT NULL,
  `longueur` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Point`
--

CREATE TABLE IF NOT EXISTS `Point` (
  `id` int(11) NOT NULL,
  `point` point NOT NULL,
  `Parcours_idParcours` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `Performances`
--

CREATE TABLE IF NOT EXISTS `Performances` (
  `vitesse` float NOT NULL,
  `temps` time NOT NULL,
  `Client_id` int(11) NOT NULL,
  `Parcours_idParcours` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `Clients`
--
ALTER TABLE `Clients`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `Contexte`
--
ALTER TABLE `Contexte`
  ADD PRIMARY KEY (`Client_id`);

--
-- Index pour la table `faitUn`
--
ALTER TABLE `faitUn`
  ADD PRIMARY KEY (`Client_id`,`Parcours_idParcours`), ADD KEY `fk_faitUn_Parcours1_idx` (`Parcours_idParcours`);

--
-- Index pour la table `Favoris`
--
ALTER TABLE `Favoris`
  ADD PRIMARY KEY (`Client_id`,`Parcours_idParcours`), ADD KEY `fk_Historique_Parcours1_idx` (`Parcours_idParcours`);

--
-- Index pour la table `Parcours`
--
ALTER TABLE `Parcours`
  ADD PRIMARY KEY (`idParcours`);

--
-- Index pour la table `Point`
--
ALTER TABLE `Point`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Point_Parcours1_idx` (`Parcours_idParcours`);

--
-- Index pour la table `Performances`
--
ALTER TABLE `Performances`
  ADD PRIMARY KEY (`Client_id`,`Parcours_idParcours`), ADD KEY `fk_Preferences_Client1_idx` (`Client_id`), ADD KEY `fk_Vitesse_Parcours1_idx` (`Parcours_idParcours`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `Clients`
--
ALTER TABLE `Clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT pour la table `Parcours`
--
ALTER TABLE `Parcours`
  MODIFY `idParcours` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=249;
--
-- AUTO_INCREMENT pour la table `Point`
--
ALTER TABLE `Point`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=42;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `Contexte`
--
ALTER TABLE `Contexte`
ADD CONSTRAINT `fk_Contexte_Client1` FOREIGN KEY (`Client_id`) REFERENCES `Clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `faitUn`
--
ALTER TABLE `faitUn`
ADD CONSTRAINT `fk_faitUn_Client1` FOREIGN KEY (`Client_id`) REFERENCES `Clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_faitUn_Parcours1` FOREIGN KEY (`Parcours_idParcours`) REFERENCES `Parcours` (`idParcours`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `Favoris`
--
ALTER TABLE `Favoris`
ADD CONSTRAINT `fk_Historique_Client1` FOREIGN KEY (`Client_id`) REFERENCES `Clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Historique_Parcours1` FOREIGN KEY (`Parcours_idParcours`) REFERENCES `Parcours` (`idParcours`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `Point`
--
ALTER TABLE `Point`
ADD CONSTRAINT `fk_Point_Parcours1` FOREIGN KEY (`Parcours_idParcours`) REFERENCES `Parcours` (`idParcours`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `Performances`
--
ALTER TABLE `Performances`
ADD CONSTRAINT `fk_Preferences_Client1` FOREIGN KEY (`Client_id`) REFERENCES `Clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Vitesse_Parcours1` FOREIGN KEY (`Parcours_idParcours`) REFERENCES `Parcours` (`idParcours`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
