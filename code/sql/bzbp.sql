-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: bzbp
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `id` int(11) NOT NULL DEFAULT '0',
  `title` varchar(45) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `publisher` varchar(45) DEFAULT NULL,
  `date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'嫌疑人X的献身','东野圭吾',34,'新华','2012-01-01 00:00:00'),(2,'一个叫欧微的男人','巴克曼',3300,'新华','2012-01-01 00:00:00'),(3,'巨人的陨落','福莱特',5100,'新华','2012-01-01 00:00:00'),(4,'流浪苍穹','郝静芳',4444,'新华','2012-01-01 00:00:00'),(5,'日本当代四大小说','多人',7787,'译林','2012-01-01 00:00:00'),(6,'长安十二时辰','马伯庸',4400,'译林','2012-01-01 00:00:00'),(7,'复仇者的秘密','周山亚',2300,'译林','2012-01-01 00:00:00'),(16,'12','3',1200,'12','2012-01-01 00:00:00');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderitems`
--

DROP TABLE IF EXISTS `orderitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderitems` (
  `id` int(11) NOT NULL DEFAULT '0',
  `orderid` int(11) DEFAULT NULL,
  `bookid` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `orderitemscol` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitems`
--

LOCK TABLES `orderitems` WRITE;
/*!40000 ALTER TABLE `orderitems` DISABLE KEYS */;
INSERT INTO `orderitems` VALUES (64,3,4,1,100,NULL),(67,3,5,1,8888,NULL),(68,1,1,5,36,NULL),(72,18,1,1,36,NULL),(73,19,3,1,5100,NULL),(74,19,5,1,8888,NULL),(75,20,5,1,8888,NULL),(76,22,1,1,36,NULL),(77,22,3,1,5100,NULL),(78,23,1,1,36,NULL),(79,23,5,1,8888,NULL),(80,23,5,1,8888,NULL),(81,24,1,1,36,NULL),(82,24,2,1,3300,NULL),(83,24,5,5,8888,NULL),(85,34,6,1,4400,NULL),(86,35,1,1,36,NULL),(87,37,1,1,36,NULL),(88,37,5,1,7787,NULL),(89,38,1,3,36,NULL),(90,38,2,1,3300,NULL),(91,40,1,1,36,NULL),(92,40,5,1,7787,NULL);
/*!40000 ALTER TABLE `orderitems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,'1000-01-01 00:00:00'),(3,3,NULL),(6,5,'2017-05-28 00:00:00'),(7,7,'2017-05-29 00:00:00'),(18,5,'2017-05-28 00:00:00'),(19,5,'2017-05-28 00:00:00'),(20,5,'2017-05-28 00:00:00'),(22,5,'2017-05-28 00:00:00'),(23,5,'2017-05-28 00:00:00'),(24,5,'2017-05-28 00:00:00'),(31,1,'2017-06-01 00:00:00'),(34,5,'2017-06-01 00:00:00'),(35,5,'2017-06-01 00:00:00'),(36,5,'2017-06-01 00:00:00'),(37,5,'2017-06-01 00:00:00'),(38,5,'2017-06-01 00:00:00'),(39,1,'2017-06-01 00:00:00'),(40,5,'2017-06-05 00:00:00'),(41,5,'2017-06-05 00:00:00'),(42,5,'2017-06-05 00:00:00'),(43,5,'2017-06-05 00:00:00'),(44,5,'2017-06-05 00:00:00'),(45,5,'2017-06-05 00:00:00'),(46,5,'2017-06-05 00:00:00'),(48,5,'2017-06-05 00:00:00'),(49,5,'2017-06-05 00:00:00'),(50,5,'2017-06-05 00:00:00'),(51,5,'2017-06-05 00:00:00'),(52,5,'2017-06-05 00:00:00'),(53,5,'2017-06-05 00:00:00'),(54,5,'2017-06-05 00:00:00'),(55,5,'2017-06-05 00:00:00'),(56,5,'2017-06-05 00:00:00'),(58,5,'2017-06-08 00:00:00'),(59,5,'2017-06-08 00:00:00'),(60,5,'2017-06-08 00:00:00'),(61,5,'2017-06-11 00:00:00'),(62,5,'2017-06-11 00:00:00');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posto`
--

DROP TABLE IF EXISTS `posto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posto` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  `path_local` varchar(45) DEFAULT NULL,
  `path_server` varchar(45) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `date` double DEFAULT NULL,
  `belong_rid` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posto`
--

LOCK TABLES `posto` WRITE;
/*!40000 ALTER TABLE `posto` DISABLE KEYS */;
INSERT INTO `posto` VALUES (1,NULL,NULL,'',NULL,NULL,NULL,NULL,1499446460462,-1),(2,NULL,NULL,'',NULL,NULL,NULL,NULL,1499446605508,-1);
/*!40000 ALTER TABLE `posto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL DEFAULT '0',
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'123','123',NULL),(2,'1','2133','administrator'),(3,'xiaoming','1332','user'),(5,'1','1','user'),(7,'123213','23123','user'),(9,'34234','22133',NULL),(16,'asd','asd',NULL),(39,'qwewwew','qwewwew',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'bzbp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-10 10:34:48
