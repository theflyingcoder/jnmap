#jNmap

Simple port scanning application based on nmap. This project was done as part of an interview process.
--------------------------------------

View at: [http://jnmap.herokuapp.com/](http://jnmap.herokuapp.com/)


Features
--------------------------------------
- Mutiple, concurrent scan capability
- Accummulative results displayed in UI
- Historical report from prior scans
- REST API (scan and historical report)
- Build with Java 8
- Deployable as a Heroku application

Limitation
--------------------------------------
- UI only displays port and service information.
- More information can be harvested from nmap output if needed.

Depedencies
--------------------------------------
- Maven
- Spark Java Web Framework
- Apache Commons (Lang, Validator)
- Google gson
- MySQL Connector
- JUnit
- Mockito + PowerMockito
- AngularJS
- Bootstrap CSS

REST API
--------------------------------------
**Scan Submission**
----
  Submits a nmap scan for a given target and returns results including port#, protocol, state and service information.

* **URL**

  /scan/:targets

* **Method:**

  `POST`
  
*  **URL Params**

   **Required:**
 
   `targets=[ipv4|ipv6|domain],[ipv4|ipv6|domain],...`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
    
    ```javascript
   [
  {
    "id": 292,
    "target": "domain.com",
    "targetStatus": "up",
    "elapsedSecs": 4.57,
    "createTime": "Sep 1, 2015 3:52:34 AM",
    "result": {
      "ports": [
        {
          "port": 80,
          "state": "open",
          "protocol": "tcp",
          "service": "http"
        },
        {
          "port": 443,
          "state": "open",
          "protocol": "tcp",
          "service": "https"
        }
      ]
    },
    "outputs": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE nmaprun>...<\/nmaprun>\n",
    "errors": ""
  }
] 
    ```
 
* **Error Response:**

  * **Code:** 403 Forbidden <br />
    **Content:** `{"error":"Scan failed: invalid target(s)"}`

* **Sample Call:**

  ```javascript
    $.ajax({
      url: "/scan/google.com,intel.com,github.com",
      dataType: "json",
      type : "GET",
      success : function(r) {
        console.log(r);
      }
    });
  ```

**History Retrieval**
----
  Retrieves historical scans including to target status, elapsed scan time, scan create time, port#s, port states and services.

* **URL**

  /scan/:targets

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `targets=[ipv4|ipv6|domain],[ipv4|ipv6|domain],...`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:**
    
    ```javascript
[
  {
    "id": 52,
    "target": "192.168.0.1",
    "targetStatus": "up",
    "elapsedSecs": 0,
    "createTime": "Aug 31, 2015 5:40:02 PM",
    "result": {
      "ports": [
        {
          "port": 53,
          "state": "open",
          "service": "domain"
        },
        {
          "port": 80,
          "state": "open",
          "service": "http"
        },
        {
          "port": 139,
          "state": "open",
          "service": "netbios-ssn"
        },
        {
          "port": 445,
          "state": "open",
          "service": "microsoft-ds"
        }
      ]
    }
  },
  {
    "id": 312,
    "target": "192.168.0.1",
    "targetStatus": "up",
    "elapsedSecs": 0,
    "createTime": "Aug 31, 2015 9:05:36 PM",
    "result": {
      "ports": [
        {
          "port": 515,
          "state": "open",
          "service": "printer"
        },
        {
          "port": 0,
          "state": "filtered"
        },
        {
          "port": 53,
          "state": "open",
          "service": "domain"
        },
        {
          "port": 80,
          "state": "open",
          "service": "http"
        },
        {
          "port": 139,
          "state": "open",
          "service": "netbios-ssn"
        },
        {
          "port": 445,
          "state": "open",
          "service": "microsoft-ds"
        }
      ]
    }
  },...]
    ```
 
* **Error Response:**

  * **Code:** 403 Forbidden <br />
    **Content:** `{"error":"Scan failed: invalid target(s)"}`

* **Sample Call:**

  ```javascript
    $.ajax({
      url: "/scan/192.168.0.1",
      dataType: "json",
      type : "GET",
      success : function(r) {
        console.log(r);
      }
    });
  ```