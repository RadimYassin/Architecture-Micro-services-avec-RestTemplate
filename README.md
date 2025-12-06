# Architecture Microservices avec RestTemplate

## 📋 Description

Ce projet démontre une architecture de microservices utilisant **Spring Boot**, **Spring Cloud Netflix Eureka** pour la découverte de services, et **RestTemplate** pour la communication inter-services.

L'application gère une relation entre des **Clients** et des **Voitures**, où chaque client peut posséder plusieurs voitures.

---

## 🏗️ Architecture

Le projet est composé de 4 microservices :

```
┌─────────────────┐
│  Eureka Server  │  (Port 8761)
│  Discovery      │
└────────┬────────┘
         │
    ┌────┴────┐
    │         │
┌───▼──────┐ ┌▼──────────┐
│  Client  │ │    Car    │
│ Service  │ │  Service  │
│ (8081)   │ │  (8082)   │
└──────────┘ └───────────┘
    │              │
    └──────┬───────┘
           │
    ┌──────▼──────┐
    │   Gateway   │  (Port 8888)
    │   Service   │
    └─────────────┘
```

### Services

| Service | Port | Description | Base de données |
|---------|------|-------------|-----------------|
| **Eureka Server** | 8761 | Serveur de découverte de services | - |
| **Client Service** | 8081 | Gestion des clients | H2 (In-Memory) |
| **Car Service** | 8082 | Gestion des voitures | H2 (In-Memory) |
| **Gateway Service** | 8888 | Passerelle API | - |

---

## 🚀 Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Cloud 2023.0.0**
- **Spring Data JPA**
- **H2 Database** (base de données en mémoire)
- **Lombok**
- **Maven**
- **Netflix Eureka** (Service Discovery)
- **RestTemplate** (Communication inter-services)

---

## 📦 Prérequis

- **JDK 17** ou supérieur
- **Maven 3.6+**
- **Git**

---

## ⚙️ Installation et Démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/RadimYassin/Architecture-Micro-services-avec-RestTemplate.git
cd tp20
```

### 2. Compiler tous les services

```bash
# Compiler tous les services
mvn clean install -DskipTests
```

### 3. Démarrer les services dans l'ordre

#### Étape 1 : Démarrer Eureka Server (obligatoire en premier)

```bash
cd eureka-server
mvn spring-boot:run
```

Vérifier que Eureka est démarré : [http://localhost:8761](http://localhost:8761)

#### Étape 2 : Démarrer Client Service

```bash
cd client-service
mvn spring-boot:run
```

#### Étape 3 : Démarrer Car Service

```bash
cd car-service
mvn spring-boot:run
```

#### Étape 4 : Démarrer Gateway Service (optionnel)

```bash
cd gateway-service
mvn spring-boot:run
```

---

## 🧪 Tester l'Application

### Accès aux Consoles H2

- **Client Service H2 Console** : [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
  - JDBC URL : `jdbc:h2:mem:clientservicedb`
  - Username : `sa`
  - Password : *(laisser vide)*

- **Car Service H2 Console** : [http://localhost:8082/h2-console](http://localhost:8082/h2-console)
  - JDBC URL : `jdbc:h2:mem:carservicedb`
  - Username : `sa`
  - Password : *(laisser vide)*

### API Endpoints

#### Client Service (Port 8081)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/clients` | Liste tous les clients |
| GET | `/clients/{id}` | Récupère un client par ID |
| POST | `/clients` | Créer un nouveau client |
| PUT | `/clients/{id}` | Modifier un client |
| DELETE | `/clients/{id}` | Supprimer un client |

**Exemple de requête POST** :
```bash
curl -X POST http://localhost:8081/clients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com"
  }'
```

#### Car Service (Port 8082)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/cars` | Liste toutes les voitures |
| GET | `/cars/{id}` | Récupère une voiture par ID |
| GET | `/cars/client/{clientId}` | Liste les voitures d'un client |
| POST | `/cars` | Créer une nouvelle voiture |
| PUT | `/cars/{id}` | Modifier une voiture |
| DELETE | `/cars/{id}` | Supprimer une voiture |

**Exemple de requête POST** :
```bash
curl -X POST http://localhost:8082/cars \
  -H "Content-Type: application/json" \
  -d '{
    "marque": "Toyota",
    "modele": "Corolla",
    "matricule": "AB-123-CD",
    "clientId": 1
  }'
```

---

## 🔍 Communication Inter-Services

Le **Car Service** utilise **RestTemplate** pour communiquer avec le **Client Service** afin de récupérer les informations du client associé à chaque voiture.

### Exemple de code (RestTemplate)

```java
@Service
public class CarService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Client getClientById(Long clientId) {
        String url = "http://SERVICE-CLIENT/clients/" + clientId;
        return restTemplate.getForObject(url, Client.class);
    }
}
```

---

## 📊 Eureka Dashboard

Accédez au tableau de bord Eureka pour voir tous les services enregistrés :

👉 [http://localhost:8761](http://localhost:8761)

Vous devriez voir :
- `SERVICE-CLIENT` (port 8081)
- `SERVICE-CAR` (port 8082)
- `GATEWAY-SERVICE` (port 8888) *(si démarré)*

---

## 🛠️ Structure du Projet

```
tp20/
├── eureka-server/          # Serveur de découverte Eureka
│   ├── src/
│   └── pom.xml
├── client-service/         # Microservice Client
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── car-service/            # Microservice Voiture
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── gateway-service/        # API Gateway
│   ├── src/
│   └── pom.xml
└── README.md
```

---

## 🐛 Dépannage

### Erreur : "Connection refused" lors de la communication entre services

- Vérifiez que **Eureka Server** est démarré en premier
- Attendez quelques secondes que les services s'enregistrent auprès d'Eureka
- Vérifiez les logs pour voir si les services sont bien enregistrés

### Erreur : Port déjà utilisé

```bash
# Sous Windows, identifier le processus utilisant le port
netstat -ano | findstr :8761

# Tuer le processus
taskkill /PID <PID> /F
```

### Les services ne s'affichent pas dans Eureka

- Vérifiez la configuration dans `application.yml`
- Assurez-vous que `eureka.client.service-url.defaultZone` pointe vers `http://localhost:8761/eureka/`
- Redémarrez le service après modification

---

## 📝 Améliorations Futures

- [ ] Ajouter Spring Cloud Config pour la configuration centralisée
- [ ] Implémenter Feign Client au lieu de RestTemplate
- [ ] Ajouter Spring Cloud Sleuth pour le traçage distribué
- [ ] Implémenter Resilience4j pour la tolérance aux pannes
- [ ] Ajouter des tests unitaires et d'intégration
- [ ] Containeriser les services avec Docker
- [ ] Créer un docker-compose.yml pour démarrer tous les services

---

## 👤 Auteur

**Radim Yassin**

- GitHub: [@RadimYassin](https://github.com/RadimYassin)

---

## 📄 Licence

Ce projet est un projet éducatif réalisé dans le cadre d'un TP sur les architectures microservices.

---

## 📚 Ressources

- [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Microservices Pattern](https://microservices.io/)
