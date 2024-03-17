# Java

## Projet .jar
Pour créer le fichier .jar pour le projet :
```bash
mvn clean package
```
Cette commande créera un dossier "target", qui contient le fichier .jar du projet.

## Javadoc pour le projet
Pour créer le javadoc pour le projet :
```bash
javadoc -d docs -sourcepath src/main/java -subpackages .
```
Cette commande créera un dossier "docs", qui contient le javadoc du projet.

## Ouvrir le .jar
Pour ouvrir le fichier .jar du projet :
```bash
java -jar target/Java-1.0-SNAPSHOT.jar 
```

## Auteurs
Romain GUERIN, Nicolas DROESC