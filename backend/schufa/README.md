# SCHUFA Module

## Definition
The following  document describes the SCHUFA Module integration. It covers the data model, the communication mechanism, the REST API, the job scheduling mechanism and
uncovered features.

## Deployment CBI (SCHUFA Server Component)
We have a Bosh Deployment automating the deployment of the CBI. It will be available on DEV, INT and PROD under:
* cbi.itg.immomio.com (both with fake data, from the local store. See module schufa/src/test/resources/*.yml for the request data)
* cbi.stg.immiomio.com (with fake data from a real SCHUFA and sample data in: schufa/src/test/resources/production-acceptance) 
* cbi.htf.immiomio.com (with fake data from a real SCHUFA and sample data in: schufa/src/test/resources/production-acceptance)
* cbi.immomio.com (real endpoint, real data)

## Data Model
For SCHUFA we support three different types of requests:
* Credit Rating
* Identity Check
* Account Number Check

Each request requires an authentication against the SCHUFA API. The customer needs to provide the following two strings:
* TEILNEHMERKENNUNG
* TEILNEHMERKENNWORT

*should be the data entered in the modal for the SCHUFA configuration.

*WARNING*
Currently there is not (known) method to verify the credentials are correct. I am currently waiting on the feedback from SCHUFA regarding a possible solution.

Teilnehmerkennung and Teilnehmerkennwort should look as follows:
* 600/00953
* MIGMIO05

### Database 
The table serving as the backend store for the SCHUFA request is a follows:
```sql

DROP   TYPE IF EXISTS landlord.cbi_action_type CASCADE;
CREATE TYPE           landlord.cbi_action_type
    AS ENUM ('SCHUFA2_ANFRAGE_IDENTITAETS_CHECK', 'SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT',
    'SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT', 'SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK');

CREATE TABLE landlord.schufajob
(
  id          bigint  NOT NULL,
  customer_id bigint  NOT NULL,
  user_id     bigint  NOT NULL,

  credit_rating_request     jsonb,
  credit_rating_response    jsonb,
  credit_rating_result      jsonb,

  account_number_check_request jsonb,
  account_number_check_result jsonb,

  identity_check_request jsonb,
  identity_check_result jsonb,

  state       int NOT NULL,
  type        landlord.cbi_action_type    NOT NULL,

  job_id      bigint  NOT NULL,

  schufa_job_id varchar(255),
  error_message text,

  created     timestamp without time zone,
  updated     timestamp without time zone,

  last_update timestamp without time zone,

  CONSTRAINT schufa_job_pkey PRIMARY KEY (id),
  CONSTRAINT fk_schufa_job_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_schufa_job_02 FOREIGN KEY (user_id)
  REFERENCES propertysearcher."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
```

As one can see, we store all the different request types in the database. The frontend then can differentiate between the information to display based on the value of 
the `type` field. The correlation should be self explanatory. 


### Data Model Credit Rating as JSON Payload
```json
{
    "name": "Johannes",
    "surname": "Hiemer",
    "gender": "m", // m, u, w (auto conversion is done from our userprofile to SCHUFA genders, see SchufaJobDispatcher
    "dateOfBirth": "28.02.1985",
    "placeOfBirth": "Mainz"
    "address": {
        "country": "Deutschland",
        "street": "Friedrichstraße 2",
        "zipCode": "55257",
        "city": "Mainz"
    }
}
```

*ALL FIELDS ARE REQUIRED*

*Note*
This is not the request content that needs to be provided on our REST API. Instead it is enough to send an user with the according userprofile that contains those information.
If the information are not provided the request will fail.

### Data Model Identity Check as JSON Payload
```json
{
    "name": "Johannes",
    "surname": "Hiemer",
    "gender": "m", // m, u, w (auto conversion is done from our userprofile to SCHUFA genders, see SchufaJobDispatcher
    "dateOfBirth": "28.02.1985",
    "placeOfBirth": "Mainz",
    "address": {
        "country": "Deutschland",
        "street": "Friedrichstraße 2",
        "zipCode": "55257",
        "city": "Mainz"
    }
}
```

*ALL FIELDS ARE REQUIRED*

*Note*
This is not the request content that needs to be provided on our REST API. Instead it is enough to send an user with the according userprofile that contains those information.
If the information are not provided the request will fail.

### Data Model Account Check as JSON Payload
```json
{
    "name": "Johannes",
    "surname": "Hiemer",
    "gender": "m", // m, u, w (auto conversion is done from our userprofile to SCHUFA genders, see SchufaJobDispatcher
    "dateOfBirth": "28.02.1985",
    "placeOfBirth": "Mainz",
    "address": {
        "country": "Deutschland",
        "street": "Friedrichstraße 2",
        "zipCode": "55257",
        "city": "Mainz"
    },
    "bankAccount": {
        "bankNumber": "3892398238923",
        "accountNumber": "TRA3923892398" //transfer money to me, if you want
    }
}
```

*ALL FIELDS ARE REQUIRED*

*Note*
This is not the request content that needs to be provided on our REST API. Instead it is enough to send an user with the according userprofile that contains those information.
If the information are not provided the request will fail.

## REST API

### Creating Credentials:
`POST http://localhost:8001/credentials`
```json
{
	"name": "Schufa Test Credentials",
	"customer": "http://localhost:8001/customers/1000016",
	"portal": "SCHUFA",
	"properties": {
		"USERNAME": "600/00953",
		"PASSWORD": "MIGMIO05"
	},
	"encrypted": true
}
```

### Request for SCHUFA INQUIRY
`POST http://localhost:8001/landlordSchufaJobs/dispatch`
```json
{
	"propertySearcherUser": "http://localhost:8001/users/2000003",
	"type": "SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT",
	"customer": "http://localhost:8001/customers/1000016"
}
```

PropertysearchUser: The user we run the SCHUFA inquiry on

Type:
* SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT
* SCHUFA2_ANFRAGE_IDENTITAETS_CHECK
* SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK

Customer: the customer issuing the request (mandatory to retrieve the stored credentials. Also possible through resolution through the token)

### Getting running SCHUFA Jobs
`GET http://localhost:8001/landlordSchufaJobs`


## Open Tasks
* SCHUFA Job Polling
* Data acceptance with SCHUFA for production
* Deployment of production INT/STG/CBI endpoint
* UI Integration and testing

