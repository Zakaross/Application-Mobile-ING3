# Application-Mobile-ING3

Ce dépôt contient trois projets d'application Android, chacun ayant un objectif spécifique. Suivez les instructions ci-dessous pour configurer, installer et exécuter les projets.

## 📂 Projets dans ce dépôt

1. **Conversion Nombre en Chiffre** : Une application permettant de convertir un nombre en son équivalent en lettres.
2. **Calculatrice** : Une application de calculatrice permettant d'effectuer des opérations de base.
3. **Galerie d'Images** : Une application permettant de capturer des images, de les sélectionner depuis la galerie et de les gérer dans un répertoire privé.

---

## 🛠 Prérequis

Avant de commencer, assurez-vous que vous avez les outils suivants installés sur votre machine :

- **Android Studio** (dernière version recommandée)
- **SDK Android** (Android 5.0 Lollipop ou plus récent)
- **Java Development Kit (JDK)**

---

## 🚀 Installation

### 1. Cloner le dépôt

Clonez le dépôt avec la commande suivante :

```bash
git clone https://github.com/Zakaross/Application-Mobile-ING3.git
```

### 2. Ouvrir le projet dans Android Studio

- Lancez **Android Studio**.
- Allez dans **File** > **Open** et sélectionnez le répertoire cloné du projet.
- Android Studio chargera automatiquement les fichiers et les dépendances.

### 3. Synchroniser le projet avec Gradle

Si Android Studio ne synchronise pas automatiquement, allez dans **File** > **Sync Project with Gradle Files** pour forcer la synchronisation.

---

## 🎯 Étapes pour exécuter chaque projet

### **1. Conversion Nombre en Chiffre**

1. Ouvrez le projet "Conversion Nombre en Chiffre" dans Android Studio.
2. Exécutez l'application sur un émulateur ou un appareil Android connecté.
3. Utilisez l'application :
   - Entrez un nombre dans le champ de saisie.
   - Cliquez sur le bouton "Convertir" pour voir la conversion en texte.

### **2. Calculatrice**

1. Ouvrez le projet "Calculatrice" dans Android Studio.
2. Exécutez l'application sur un émulateur ou un appareil Android connecté.
3. Utilisez l'application :
   - Cliquez sur les boutons pour entrer des chiffres.
   - Effectuez des opérations (addition, soustraction, multiplication, division).
   - Cliquez sur "Égal" pour obtenir le résultat.

### **3. Galerie d'Images**

1. Ouvrez le projet "Galerie d'Images" dans Android Studio.
2. Exécutez l'application sur un émulateur ou un appareil Android connecté.
3. Utilisez l'application :
   - Capturez une image avec la caméra ou sélectionnez une image depuis la galerie.
   - Les images sélectionnées ou capturées seront affichées et enregistrées dans un répertoire privé de l'application.

---

## 🔑 Permissions nécessaires pour "Galerie d'Images"

L'application "Galerie d'Images" nécessite des permissions pour accéder à la caméra et à la galerie de l'appareil. Android vous demandera d’accepter ces permissions lors de l'exécution.

### **Permissions requises** :
- `android.permission.CAMERA` : pour accéder à la caméra de l'appareil.
- `android.permission.READ_EXTERNAL_STORAGE` : pour accéder aux images et vidéos dans la galerie.
- `android.permission.WRITE_EXTERNAL_STORAGE` : pour enregistrer les images dans un répertoire privé.

Si ces permissions ne sont pas accordées, l'application vous demandera de les accepter.

---

## 🛠 Technologies utilisées

Voici les principales technologies et outils utilisés dans ces projets :

- **Android SDK** : La plateforme de développement officielle pour Android.
- **Kotlin** : Langage de programmation principal pour le développement Android.
- **Android Studio** : IDE recommandé pour développer des applications Android.
- **Jetpack Libraries** :
  - **Room Database** : Pour la gestion des données dans la galerie d'images.
  - **LiveData & ViewModel** : Pour la gestion des données de l'interface utilisateur.
- **Android Permissions API** : Pour gérer les permissions nécessaires à l'accès à la caméra et à la galerie de l'appareil.
- **MediaStore** : Utilisé pour accéder aux fichiers multimédia sur le stockage externe de l'appareil.
- **RecyclerView** : Pour afficher des listes d'images ou de vidéos dans la galerie.

---

## 💡 Problèmes connus et solutions

### **Problème : L'application ne démarre pas sur un appareil réel**
- **Solution** : Assurez-vous que le mode de développement est activé sur votre appareil (Options développeur et débogage USB).

### **Problème : Permissions refusées pour la galerie ou la caméra**
- **Solution** : Vérifiez les permissions dans les paramètres de votre appareil. Vous pouvez réactiver les permissions depuis **Paramètres** > **Applications** > [Nom de l'application] > **Permissions**.

---

## 🤝 Contributions

Les contributions sont les bienvenues ! Si vous souhaitez améliorer ce projet, n'hésitez pas à créer une branche, y apporter vos modifications et soumettre une **Pull Request**.

---

## 📜 Licence

Ce projet est sous licence [MIT](https://opensource.org/licenses/MIT). Vous pouvez l'utiliser, le modifier et le distribuer selon les termes de cette licence.

---

## 📧 Contact

- **Auteur** : ZAKARIA GAMANE
- **Email** : zakariagamane40@gmail.com

---


