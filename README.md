# CENMIRE Scala REST Service

CERMINE is a Java library and a web service ([cermine.ceon.pl](http://cermine.ceon.pl/)) for extracting metadata and content from PDF files containing academic publications.

Read more detail at ([https://github.com/CeON/CERMINE]( https://github.com/CeON/CERMINE)

This project is a REST service implementation for CERMINE written in scala.

## Environment

- Java Development Kit (8.0 or above)
- Sbt (1.x)

## Dockerize

Use `sbt-native-packager` to deploy

```
sbt docker:publishLocal
```


## API documentation

### Extract PDF file

**URL** : `/pdf/extract`

**Method** : `POST`

**Content-Type**: `application/pdf`

**Auth required** : NO

### Success Response

**Code** : `200 OK`

**Content example**

```json
[{
    "title": "Stressful life events are not associated with the development of dementia",
    "journalTitle": "International Psychogeriatrics",
    "journalISSN": "",
    "publisher": "",
    "doi": "10.1017/S1041610213001804",
    "urn": "",
    "abtractText": "Background: The impact of stressful life events as a risk factor of dementia diseases is inconclusive. We sought to determine whether stressful negative life events are associated with incidental dementia in a populationbased study with long-term follow-up.",
    "authors": [
        {
            "name": "Anna Sundström",
            "givenNames": "",
            "surname": "",
            "emails": [
                "anna.sundstrom@psy.umu.se"
            ],
            "affiliations": [
                "0Centre for Population Studies/Ageing and Living Conditions, and Department of Psychology, Umeå University, Umeå, Sweden"
            ]
        },
        {
            "name": "Michael Rönnlund",
            "givenNames": "",
            "surname": "",
            "emails": [],
            "affiliations": [
                "3Department of Psychology, Umeå University, Umeå, Sweden"
            ]
        },
        {
            "name": "Rolf Adolfsson",
            "givenNames": "",
            "surname": "",
            "emails": [],
            "affiliations": [
                "1Department of Clinical Sciences, Division of Psychiatry, Umeå University, Umeå, Sweden"
            ]
        },
        {
            "name": "Lars-Göran Nilsson",
            "givenNames": "",
            "surname": "",
            "emails": [],
            "affiliations": [
                "2Department of Psychology, Stockholm University, and Stockholm Brain Institute, Stockholm, Sweden"
            ]
        }
    ],
    "editors": [],
    "keywords": [
        "dementia",
        "Alzheimer's disease",
        "life events",
        "stress",
        "risk factor",
        "longitudinal"
    ],
    "volume": "26",
    "issue": "",
    "publishDate": "2014",
    "receivedDate": null,
    "revisedDate": null,
    "acceptedDate": null,
    "fPage": "147",
    "lPage": "154",
    "references": [
        {
            "title": "",
            "journalTitle": "World Alzheimer Report",
            "publisher": "",
            "publisherLocation": "",
            "abstractText": "Alzheimer's Disease International. (2009)",
            "year": "2009",
            "lPage": "",
            "fPage": "",
            "volume": "",
            "issue": "",
            "authors": [
                {
                    "name": "",
                    "givenNames": "",
                    "surname": "",
                    "emails": [],
                    "affiliations": []
                }
            ]
        }
    ]
}]
```

### Error Response

**Condition** : Internal server error

**Code** : `500`

**Content** :

```json
{
    "message": "Internal server error"
}
```
