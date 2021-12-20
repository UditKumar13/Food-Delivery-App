
CREATE DATABASE `food_ordering` 

USE food_ordering ; 

CREATE TABLE `customer` (
  `USERNAME` VARCHAR(30) NOT NULL,
  `GENDER` VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PASSWRD` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ADDRESS` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `EMAIL` VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`EMAIL`)
)

CREATE TABLE `restaurant` (
  `res_Id` VARCHAR(20) NOT NULL,
  `res_Name` VARCHAR(30) NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `address` VARCHAR(30) NOT NULL,
  `contact` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`res_Id`)
)


CREATE TABLE `menu` (
  `menu_Id` VARCHAR(10) NOT NULL,
  `res_Id` VARCHAR(30) NOT NULL,
  `res_Name` VARCHAR(30) NOT NULL,
  `item_Name` VARCHAR(30) NOT NULL,
  `price` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`menu_Id`),
  KEY `id` (`res_Id`),
  CONSTRAINT `id` FOREIGN KEY (`res_Id`) REFERENCES `restaurant` (`res_Id`)
)

CREATE TABLE `cart` (
  `user_Email` VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `res_Name` VARCHAR(50) NOT NULL,
  `item_Name` VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `quantity` INT NOT NULL,
  `price` INT NOT NULL
)

CREATE TABLE `wishlist` (
  `user_Email` VARCHAR(30) NOT NULL,
  `Res_Name` VARCHAR(50) NOT NULL,
  `item_Name` VARCHAR(30) NOT NULL,
  `price` INT NOT NULL
)


