# BI - Data Object

This repository contains the **Data Object** microservice. 

The Label Detector microservice can be found here : 

[Go To Label Detector](https://github.com/menoudyann/BI_LabelDetector)

## Description

This Java microservice offers essential functionalities for Google Cloud Storage, such as file upload/download, existence checks, URL publishing, and deletion operations. It's designed for efficient cloud storage management with easy URI-based interactions.

## Getting Started

### Prerequisites

Here are the prerequisites for finding work on the project.

- IntelliJ IDEA 2023.2.2 (Ultimate Edition)
- Apache Maven 3.9.5 
- Google Cloud Platform account with Vision API V1 enabled 
- Eclipe Temurin version 21.0.1
- jUnit 5.8.1

### Configuration

**Environment File**

The project contains an example of the environment file required for the project. Simply copy it and rename it .env.

```bash
# .env
GOOGLE_APPLICATION_CREDENTIALS="path/to/credentials.json"
GOOGLE_BUCKET_URI=gs://java.gogle.cld.education/

```

## Deployment

To Install dependencies 

```
mvn clean install 
```

To build the development Docker image 

```
docker build -t "dataobject:dev" --target development .
```

 To run the Docker container 

```
docker run --name dataobject-dev -p 8080:8080 dataobject:dev
```

### Production

To build the production Docker image 

```
docker build -t "dataobject:prod" --target production .
```

 To run the Docker container 

```
docker run --name dataobject-prod -p 8080:8080 dataobject:prod
```

## Directory structure

- Tip: try the tree bash command

```
./src
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           ├── GoogleDataObjectImpl.java                //implementation
│   │           ├── GoogleDataObjectImplException.java       
│   │           ├── IDataObject.java                         //interface
│   │           ├── Main.java
│   │           ├── NotEmptyObjectException.java        
│   │           ├── ObjectAlreadyExistsException.java
│   │           └── ObjectNotFoundException.java
│   └── resources
└── test
    └── java
        └── org
            └── example
                ├── GoogleDataObjectImplTest.java            //tests
                └── images
```



## Collaborate

#### How to propose a new feature ?

If you're interested in enhancing this project, you're welcome to:

- **Fork the Project:** You can create a fork of the project on your own GitHub account to work on your changes.
- **Submit Pull Requests:** If you develop new features or improvements, feel free to submit a pull request for integration into the main project.

#### Commit Rules

I use very simple commit rules. The commit message **starts with an infinitive verb and describes the added/deleted content clearly in one sentence**. If your commit requires two sentences because the code added modifies two things, please make two separate commits. This is to improve readability and also simplify versioning.

#### Branches Strategy

By default, there are two branches: main and develop. Main is the branch currently in production, develop is based on the same branch. 

To add a new feature, please create a branch from develop using Gitflow. [To the Gitflow guide](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) 

#### Any question ?

For any questions or further information, please feel free to reach out to me at: yann.menoud@gmail.com.



## License

This project is licensed under the [MIT](https://github.com/menoudyann/BI_DataObject/blob/main/LICENSE) licence.

## Contact

You can contact me by email at the following address: yann.menoud@gmail.com or directly on [Linkedin](https://www.linkedin.com/in/yann-menoud-433780225/).
