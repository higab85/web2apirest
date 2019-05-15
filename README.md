# [uni] Web2 Project: REST

This project consists of:
- a REST API
- a CLI client
- a web client

Written in java, using netbeans 8 and glassfish 4.1 .

## Setup
Open MySql Server and execute the following commands:

```SQL
CREATE DATABASE competiciones_rest_web2 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
use competiciones_rest_web2;
CREATE TABLE Usuarios (ID INTEGER PRIMARY KEY AUTO_INCREMENT, USERNAME TEXT NOT NULL, PASSWORD TEXT NOT NULL);
CREATE TABLE Tokens (ID INTEGER PRIMARY KEY AUTO_INCREMENT, TOKEN TEXT, EXPIRY DATE, USUARIO_ID INTEGER NOT NULL REFERENCES Usuarios(ID));
CREATE TABLE Competiciones (ID INTEGER PRIMARY KEY AUTO_INCREMENT, NOMBRE TEXT NOT NULL);
CREATE TABLE Deportes (ID INTEGER PRIMARY KEY AUTO_INCREMENT, NOMBRE TEXT NOT NULL, TIPO TEXT NOT NULL, EQUIPOS TEXT NOT NULL, TAMANOEQUIPO TEXT NOT NULL, COMPETICION_ID INTEGER NOT NULL REFERENCES Competiciones(ID));
CREATE TABLE CompeticionesUsuarios (USUARIO_ID INTEGER NOT NULL REFERENCES Usuarios(ID), COMPETICION_ID INTEGER NOT NULL REFERENCES Competiciones(ID));
```
