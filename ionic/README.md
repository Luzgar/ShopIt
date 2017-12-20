# Notre Application

ShopIt est une application cross-platform (iOS, Android, Windows Phone) de gestion de listes de courses (locales ou partagées).


# Installer l'environnement


## Prérequis 

Il faut (si ce n'est déjà fait) installer la dernière version de nodeJS et avoir accès à la commance npm.

```bash
$ npm install -g ionic cordova
```

Pour la partie serveur, il faut que Maven soit installé et donc avoir accès à la commande ```mvn ```.

## Installation

```bash
$ cd ionic
$ npm install
$ cd ../j2e
$ mvn clean package
```


# Lancer l'application

## Ionic
Dans le dossier ionic :

Pour lancer l'application dans une fenêtre du navigateur par défaut

```bash
$ ionic serve
```

Rajouter à l'adresse URL /ionic-lab pour avoir un apercu des différentes versions (iOS, Android, Windows Phone).
Attention, certaines fonctionnalités utilisent des composants/éléments du téléphone physique (caméra, notifications, ...). Utiliser le navigateur n'est donc pas optimal puisque certaines fonctionnalités ne fonctionneront pas

Ainsi, pour lancer sur un emulateur/téléphone physique :

```bash
$ ionic cordova platform add [android|ios|windows]
$ ionic cordova run [android|ios|windows]
```


## J2E



Dans le dossier j2e :

Pour lancer le serveur, il suffit simplement d'éxecuter cette commande dans une console :

```bash
$ mvn tomee:run
```
Les ports utilisés par le serveur sont 8080.



