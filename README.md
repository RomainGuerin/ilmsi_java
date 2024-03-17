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
mvn javadoc:javadoc
```
La javadoc sera créée dans le dossier "target/site/apidocs". Pour la consulter, ouvrir le fichier "index.html" dans un navigateur.

## Ouvrir le .jar
Pour ouvrir le fichier .jar du projet :
```bash
java -jar target/Java-1.0-SNAPSHOT.jar 
```

## Auteurs
Romain GUERIN, Nicolas DROESC