CREATE DATABASE  IF NOT EXISTS `house_manager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `house_manager`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: house_manager
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `apartment_residents`
--

DROP TABLE IF EXISTS `apartment_residents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment_residents` (
  `apartment_id` bigint NOT NULL,
  `person_id` bigint NOT NULL,
  `uses_elevator` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`apartment_id`,`person_id`),
  KEY `fk_ar_person` (`person_id`),
  CONSTRAINT `fk_ar_apt` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ar_person` FOREIGN KEY (`person_id`) REFERENCES `persons` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartment_residents`
--

LOCK TABLES `apartment_residents` WRITE;
/*!40000 ALTER TABLE `apartment_residents` DISABLE KEYS */;
INSERT INTO `apartment_residents` VALUES (1,1,1),(1,2,1),(2,3,1),(3,4,1),(4,1,1),(5,5,1),(6,6,1),(7,7,1),(8,7,1),(8,8,1),(9,9,1),(10,10,1),(11,11,1),(11,12,1),(12,12,1),(13,13,0),(14,14,0),(15,15,1),(16,15,1),(16,16,1),(17,17,1),(18,18,1),(19,19,1),(20,19,1),(20,20,1),(21,21,1),(22,22,1),(23,23,1),(24,23,1),(24,24,1);
/*!40000 ALTER TABLE `apartment_residents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apartments`
--

DROP TABLE IF EXISTS `apartments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `building_id` bigint NOT NULL,
  `floor` int NOT NULL,
  `apt_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `area_m2` decimal(10,2) NOT NULL,
  `owner_person_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_apt` (`building_id`,`apt_number`),
  CONSTRAINT `fk_apt_building` FOREIGN KEY (`building_id`) REFERENCES `buildings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartments`
--

LOCK TABLES `apartments` WRITE;
/*!40000 ALTER TABLE `apartments` DISABLE KEYS */;
INSERT INTO `apartments` VALUES (1,1,1,'1',62.50,1),(2,1,2,'5',71.20,2),(3,1,4,'12',83.00,3),(4,1,6,'20',95.40,4),(5,3,1,'2',58.10,5),(6,3,3,'9',76.00,6),(7,3,5,'17',89.30,7),(8,3,7,'25',102.80,8),(9,5,2,'8',64.00,9),(10,5,4,'18',79.50,10),(11,5,6,'28',93.70,11),(12,5,9,'42',115.20,12),(13,7,1,'3',55.00,13),(14,7,3,'11',72.40,14),(15,7,5,'19',88.60,15),(16,7,8,'31',104.90,16),(17,9,1,'1',49.90,17),(18,9,2,'6',68.80,18),(19,9,3,'11',77.70,19),(20,9,4,'15',84.10,20),(21,12,2,'10',63.30,21),(22,12,4,'26',81.60,22),(23,12,7,'51',97.80,23),(24,12,10,'77',121.40,24);
/*!40000 ALTER TABLE `apartments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `building_fees`
--

DROP TABLE IF EXISTS `building_fees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building_fees` (
  `building_id` bigint NOT NULL,
  `price_per_m2` decimal(10,2) NOT NULL,
  `add_per_resident_over7_elevator` decimal(10,2) NOT NULL,
  `add_per_pet_common_parts` decimal(10,2) NOT NULL,
  PRIMARY KEY (`building_id`),
  CONSTRAINT `fk_fee_building` FOREIGN KEY (`building_id`) REFERENCES `buildings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `building_fees`
--

LOCK TABLES `building_fees` WRITE;
/*!40000 ALTER TABLE `building_fees` DISABLE KEYS */;
INSERT INTO `building_fees` VALUES (1,0.85,2.50,3.00),(3,0.78,2.00,2.50),(5,0.92,3.00,3.50),(7,0.74,1.80,2.00),(9,0.70,1.50,2.00),(12,0.88,2.80,3.20);
/*!40000 ALTER TABLE `building_fees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buildings`
--

DROP TABLE IF EXISTS `buildings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buildings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `floors` int NOT NULL,
  `apartments_count` int NOT NULL,
  `built_area_m2` decimal(10,2) NOT NULL,
  `common_parts_m2` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_bld_company` (`company_id`),
  KEY `idx_bld_employee` (`employee_id`),
  CONSTRAINT `fk_bld_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bld_employee` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buildings`
--

LOCK TABLES `buildings` WRITE;
/*!40000 ALTER TABLE `buildings` DISABLE KEYS */;
INSERT INTO `buildings` VALUES (1,1,1,'София, ж.к. Младост 1, бл. 12',8,32,2450.00,310.50),(2,1,2,'София, кв. Лозенец, ул. Крум Попов 15',6,18,1320.00,190.00),(3,2,3,'Пловдив, Център, ул. Иван Вазов 44',7,28,2100.00,275.00),(4,2,4,'Пловдив, Кършияка, бул. България 110',9,45,3680.00,420.00),(5,3,5,'Варна, Чайка, бл. 33',10,60,5200.00,610.00),(6,3,6,'Варна, Левски, ул. Мир 7',5,20,1500.00,205.00),(7,4,7,'Бургас, Изгрев, бл. 88',8,40,3100.00,360.00),(8,4,8,'Бургас, Лазур, ул. Демокрация 2',6,24,1750.00,230.00),(9,5,9,'Русе, Център, ул. Александровска 9',4,16,980.00,140.00),(10,5,10,'Стара Загора, Самара 1, бл. 4',9,54,4600.00,540.00),(11,6,11,'София, кв. Манастирски ливади, ул. Луи Айер 6',7,35,2900.00,330.00),(12,6,12,'София, кв. Овча купел, ул. Народен герой 21',12,96,8600.00,920.00);
/*!40000 ALTER TABLE `buildings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `vat_number` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (1,'ГрадДом Сървис ООД','BG112233445','+359887101010','contact@graddom.bg'),(2,'СмартБилдинг Мениджмънт ЕООД','BG556677889','+359887202020','office@smartbuilding.bg'),(3,'Комфорт Хоумс АД','BG998877665','+359887303030','info@comforthomes.bg'),(4,'ЮниХаус Мениджмънт ООД','BG443322110','+359887404040','support@unihouse.bg'),(5,'ПримаДом ЕООД','BG667788990','+359887505050','admin@primadom.bg'),(6,'ЕкоХаус ЕАД','BG987654321','+359888333444','info@ecohouse.bg');
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `first_name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_emp_company` (`company_id`),
  CONSTRAINT `fk_emp_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,1,'Иван','Петров','+359888100101','ivan.petrov@graddom.bg',1),(2,1,'Мария','Илиева','+359888100102','maria.ilieva@graddom.bg',1),(3,2,'Георги','Станев','+359888200201','georgi.stanev@smartbuilding.bg',1),(4,2,'Елица','Николова','+359888200202','elitsa.nikolova@smartbuilding.bg',1),(5,3,'Димитър','Колев','+359888300301','dimitar.kolev@comforthomes.bg',1),(6,3,'Надя','Георгиева','+359888300302','nadya.georgieva@comforthomes.bg',1),(7,4,'Петър','Тодоров','+359888400401','petar.todorov@unihouse.bg',1),(8,4,'Силвия','Василева','+359888400402','silvia.vasileva@unihouse.bg',0),(9,5,'Кристиян','Димов','+359888500501','kristiyan.dimov@primadom.bg',1),(10,5,'Ралица','Бонева','+359888500502','ralitsa.boneva@primadom.bg',1),(11,6,'Виктор','Младенов','+359888600601','viktor.mladenov@ecohouse.bg',1),(12,6,'Антония','Павлова','+359888600602','antonia.pavlova@ecohouse.bg',1);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `building_id` bigint NOT NULL,
  `apartment_id` bigint NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `paid_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pay_apartment` (`apartment_id`),
  KEY `idx_pay_company` (`company_id`),
  KEY `idx_pay_building` (`building_id`),
  KEY `idx_pay_employee` (`employee_id`),
  KEY `idx_pay_paid_at` (`paid_at`),
  CONSTRAINT `fk_pay_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pay_building` FOREIGN KEY (`building_id`) REFERENCES `buildings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pay_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pay_employee` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,1,1,1,58.40,'2025-11-05 10:12:00'),(2,1,1,1,2,66.10,'2025-11-06 18:43:00'),(3,1,1,1,3,79.30,'2025-12-02 09:05:00'),(4,1,1,1,4,92.70,'2025-12-10 20:15:00'),(5,2,3,3,5,52.90,'2025-11-03 12:00:00'),(6,2,3,3,6,68.40,'2025-11-12 16:22:00'),(7,2,3,3,7,80.80,'2025-12-01 11:18:00'),(8,2,3,3,8,95.60,'2025-12-08 08:47:00'),(9,3,5,5,9,60.10,'2025-11-08 14:30:00'),(10,3,5,5,10,74.50,'2025-11-16 09:55:00'),(11,3,5,5,11,88.90,'2025-12-04 19:02:00'),(12,3,5,5,12,112.30,'2025-12-19 10:40:00'),(13,4,7,7,13,49.80,'2025-11-04 08:20:00'),(14,4,7,7,14,64.20,'2025-11-20 17:05:00'),(15,4,7,7,15,78.70,'2025-12-05 13:10:00'),(16,4,7,7,16,93.40,'2025-12-21 21:00:00'),(17,5,9,9,17,44.50,'2025-11-02 10:00:00'),(18,5,9,9,18,61.00,'2025-11-15 15:15:00'),(19,5,9,9,19,70.30,'2025-12-03 09:30:00'),(20,5,9,9,20,76.90,'2025-12-18 18:45:00'),(21,6,12,12,21,59.90,'2025-11-09 11:11:00'),(22,6,12,12,22,77.20,'2025-11-25 16:40:00'),(23,6,12,12,23,92.60,'2025-12-06 08:05:00'),(24,6,12,12,24,118.80,'2025-12-23 20:20:00'),(32,1,1,4,1,58.13,'2025-12-26 13:38:59');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persons`
--

DROP TABLE IF EXISTS `persons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bg_0900_ai_ci NOT NULL,
  `last_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bg_0900_ai_ci NOT NULL,
  `birth_date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persons`
--

LOCK TABLES `persons` WRITE;
/*!40000 ALTER TABLE `persons` DISABLE KEYS */;
INSERT INTO `persons` VALUES (1,'Никола','Иванов','1986-02-14'),(2,'Десислава','Иванова','1988-07-03'),(3,'Асен','Георгиев','1979-11-22'),(4,'Виолета','Георгиева','1982-05-10'),(5,'Светлин','Дончев','1991-09-18'),(6,'Рая','Дончева','1993-12-01'),(7,'Мартин','Стоянов','1985-03-27'),(8,'Елена','Стоянова','1987-06-19'),(9,'Борис','Попов','1976-01-08'),(10,'Невена','Попова','1978-04-25'),(11,'Иво','Костов','1990-10-02'),(12,'Калина','Костова','1992-08-16'),(13,'Галя','Рангелова','1969-02-09'),(14,'Теодор','Рангелов','1967-09-30'),(15,'Симеон','Петров','1995-01-12'),(16,'Йоана','Петрова','1997-05-04'),(17,'Кирил','Славов','1983-12-12'),(18,'Диана','Славова','1984-03-15'),(19,'Пламен','Ангелов','1974-06-28'),(20,'Милена','Ангелова','1975-11-05'),(21,'Веселин','Драганов','1989-04-08'),(22,'Анна','Драганова','1991-01-21'),(23,'Стоян','Митев','1980-08-30'),(24,'Мартина','Митева','1982-02-17');
/*!40000 ALTER TABLE `persons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pets`
--

DROP TABLE IF EXISTS `pets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apartment_id` bigint NOT NULL,
  `name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uses_common_parts` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_pet_apt` (`apartment_id`),
  CONSTRAINT `fk_pet_apt` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pets`
--

LOCK TABLES `pets` WRITE;
/*!40000 ALTER TABLE `pets` DISABLE KEYS */;
INSERT INTO `pets` VALUES (1,4,'Мици','котка',0),(2,3,'Бари','куче',1),(3,5,'Коко','папагал',0),(4,7,'Роки','куче',1),(5,11,'Сноу','котка',0),(6,16,'Оскар','куче',1),(7,18,'Локи','котка',0),(8,24,'Нала','котка',0);
/*!40000 ALTER TABLE `pets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-04 22:31:20
