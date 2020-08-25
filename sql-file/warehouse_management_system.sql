-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: warehouse_management_system
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `storage_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK31ughgxwjqslxrggmf20knt4a` (`storage_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (1,'2020-08-26',5,10,1),(2,'2020-08-26',10,5,3);
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `available_capacity` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `total_capacity` int DEFAULT NULL,
  `storage_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcf4ot45b63oj5swy79ipxixmt` (`storage_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,200,'Books',5,200,3),(2,300,'Pen',5,300,3),(3,400,'Pencil',10,500,3),(4,400,'Board',10,400,3),(5,600,'Bench',5,600,3),(6,200,'Char',5,200,1),(7,600,'Table',2,600,1),(8,500,'Black Round',5,500,1),(9,650,'Book Table',10,650,1),(10,120,'Info desk',3,120,1),(11,500,'Potato',2,500,2),(12,1000,'Rice',5,1000,2),(13,300,'Onion',5,500,2),(14,500,'Table',10,500,8);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_request`
--

DROP TABLE IF EXISTS `product_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `capacity` int DEFAULT NULL,
  `cost` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1k1mqwqffwotki2qq2s76qf50` (`product_id`),
  KEY `FK861kjy6r5qi0eqw7rlifa0fwv` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_request`
--

LOCK TABLES `product_request` WRITE;
/*!40000 ALTER TABLE `product_request` DISABLE KEYS */;
INSERT INTO `product_request` VALUES (1,20,200,'2020-08-28',2,'checkout',1,2),(2,20,0,'2020-08-26',0,'pending',9,2),(3,200,0,'2020-08-26',0,'pending',12,2),(4,100,0,'2020-08-19',0,'accepted',3,2),(5,50,1350,'2020-08-26',6,'checkout',2,2),(6,100,0,'2020-08-26',0,'pending',11,2),(7,200,0,'2020-08-22',0,'accepted',13,2);
/*!40000 ALTER TABLE `product_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `message` text,
  `storage_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqf781caf8doilvvyiy2c4tl4v` (`storage_id`),
  KEY `FKiyf57dy48lyiftdrf7y87rnxi` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,'2020-08-26','Very good storage.Thanks to the authority again.',8,2),(2,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',6,2),(3,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',3,2),(4,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',7,2),(5,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',5,2),(6,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',8,2),(7,'2020-08-26','Very good storage.Thanks to the authority again.Very good storage.Thanks to the authority again.',2,2);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage`
--

DROP TABLE IF EXISTS `storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `available_capacity` int DEFAULT NULL,
  `is_active` int DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `total_capacity` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnqrjocdfnotsd4e1gjuevdi5v` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage`
--

LOCK TABLES `storage` WRITE;
/*!40000 ALTER TABLE `storage` DISABLE KEYS */;
INSERT INTO `storage` VALUES (1,2930,1,NULL,'Probortok',NULL,'Sufian Storage',5000,1),(2,2000,1,NULL,'Muradpur',NULL,'Naba Find',4000,1),(3,4000,1,NULL,'Muradpur',NULL,'Leading Inc',6000,1),(4,6000,0,NULL,'Kotowali',NULL,'Sheba Ltd',6000,1),(5,8000,1,NULL,'Chokbajar',NULL,'My Storage',8000,1),(6,5000,1,NULL,'Farmgate',NULL,'Fah St 1',5000,3),(7,5000,1,NULL,'Banani',NULL,'Trust Cor',5000,3),(8,4000,1,NULL,'Kolabagan',NULL,'Best Stg',4500,3);
/*!40000 ALTER TABLE `storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'sufian@gmail.com','$2a$10$u6xpw0UEnNgMwtZbwXFLD.A4iYluAJHRJUgrSrOMUU9MMcXDObhbu','01876639192','ROLE_OWNER','sufian'),(2,'rahim@gmail.com','$2a$10$/N6nOz2qPyJtF99DodsCSesf5DtxegAv/lKg1t6uYr1sUJwFaEgwi','01876639192','ROLE_USER','rahim'),(3,'fahim@gmail.com','$2a$10$Mhv41MkmpVD7U5.c5OffFeUxyXAN3drt9nrI5g1HP4kl2qcMK5pp6','01876639192','ROLE_OWNER','fahim'),(4,'kamal@gmail.com','$2a$10$x.oC1hXUz8ZtgT5CAzlV.eIM980ydDnOIRxllRef5zNEdSyONwZ4O','01876639192','ROLE_USER','kamal'),(5,'iqbal@gmail.com','$2a$10$.B5Zn04QV/36S8m3Ut0KEe51ySxU1hR41msjVZxSUZrVebVwMVto.','01876639192','ROLE_USER','iqbal'),(6,'shuvo@gmail.com','$2a$10$ro9MXjVcDyDM.EYEY09wWO1tirz1INBe.z75ShPxWTMjhNqd1QkHi','01876639192','ROLE_USER','shuvo');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-26  1:17:19
