SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

--CREATE SCHEMA IF NOT EXISTS `mfsapis` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
--USE `mfsapis` ;

-- -----------------------------------------------------
-- Table `mfsapis`.`tbl_issuer`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `mfsapis`.`tbl_issuer` ;

-- -----------------------------------------------------
-- Table `mfsapis`.`tbl_role`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `mfsapis`.`tbl_role` ;

CREATE TABLE IF NOT EXISTS `survey`.`tbl_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mfsapis`.`tbl_user`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `mfsapis`.`tbl_user` ;

CREATE TABLE IF NOT EXISTS `survey`.`tbl_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(200) NOT NULL,
  `email` VARCHAR(100) NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tbl_user_tbl_role1_idx` (`role_id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  CONSTRAINT `fk_tbl_user_tbl_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `mfsapis`.`tbl_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
