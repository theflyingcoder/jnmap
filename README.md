jNmap - Simple port scanning application based on nmap. This project was done as part of an interview process.
--------------------------------------

View at: [http://jnmap.herokuapp.com/](http://jnmap.herokuapp.com/)


Features
--------------------------------------
- Mutiple, concurrent scan capability
- Accummulative results displayed in UI
- Historical report from prior scans
- REST API
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
 
   `targets=[ipv4|ipv6|domain]`

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
      url: "/scan/google.com,intel.com,localhost",
      dataType: "json",
      type : "POST",
      success : function(r) {
        console.log(r);
      }
    });
  ```
