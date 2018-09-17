<p align="center">
  <img src="/assets/UNiDAYS_Logo.png" />
</p>
<br/>


[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.myunidays/myunidays.svg)](https://oss.sonatype.org/#nexus-search;quick~com.myunidays)
[![Build Status](https://travis-ci.org/MyUNiDAYS/unidays-node.svg?branch=master)](https://travis-ci.org/MyUNiDAYS/unidays-node)

# UNiDAYS Java Library

This is the Java library for integrating with UNiDAYS. This is to be used for coded and codeless integrations. The following documentation provides descriptions of the implementations and examples.

## Contents

[**How to use this code?**](#how-to-use-this-code)

[**Direct Tracking**](#direct-tracking)
- [Parameters](#parameters)
	- [Example Basket](#example-basket)
- [Example Usage](#example-usage)
    - [Get Server URL _(returns url for server to server request)_](#get-server-url)
    - [Get Script URL _(returns url for client to server request)_](#get-script-url)
    - [Tracking Client _(sends server to server request)_](#tracking-client)
    - [Test endpoint](#test-endpoint)
    - [Direct Tracking Details Builder](#direct-tracking-details-builder)

[**Codeless Verification**](#codeless-verification)
- [Codeless API](#codeless-api)
    - [Codeless Url Verifier](#codeless-url-verifier)

[**Contributing**](#contributing)

## How to use this code

- If using pom add

```xml
  <repositories>
    <repository>
      <id>oss-sonatype</id>
      <name>oss-sonatype</name>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>com.myunidays</groupId>
      <artifactId>myunidays</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
```


- If using Gradle add: 

```java
repositories {
    maven {
          url "https://oss.sonatype.org/content/groups/public"
    }
}

dependencies {
    compile "com.myunidays:myunidays:1.0-SNAPSHOT"
}
```

- See the example usage section for the type of call you intend to use. Each of these contains an example.

## Direct Tracking

### Parameters

Here is a description of all available parameters. Which of these you provide are dependent on the agreed contract.

### Mandatory Parameters

| Parameter | Description | Data Type | Example |
|---|---|---|---|
| PartnerId | Your PartnerId as provided by UNiDAYS. If you operate in multiple geographic regions you MAY have a different PartnerId for each region | Base64 Encoded Guid | XaxptFh0sK8Co6pI== |
| TransactionId | A unique ID for the transaction in your system | String | Order123 |
| Currency | The ISO 4217 currency code | String | GBP |

Having **either** Code or MemberID as a parameter is also mandatory:

| Parameter | Description | Data Type | Example |
|---|---|---|---|
| Code | The UNiDAYS discount code used | String | ABC123 |
| MemberId | Only to be provided if you are using a codeless integration |  Base64 Encoded Guid | 0LTio6iVNaKj861RM9azJQ== |

### Additional Parameters

Note any of the following properties to which the value is unknown should be omitted from calls. Which of the following values you provide to us will depend on your agreed contract.

| Parameter | Description | Data Type | Example |
|---|---|---|---|
| OrderTotal | Total monetary amount paid, formatted to 2 decimal places | Decimal | 209.00 |
| ItemsUNiDAYSDiscount | Total monetary amount of UNiDAYS discount applied on gross item value `ItemsGross`, formatted to 2 decimal places | Decimal | 13.00 |
| ItemsTax | Total monetary amount of tax applied to items, formatted to 2 decimal places | Decimal | 34.50
| ShippingGross | Total monetary amount of shipping cost, before any shipping discount or tax applied, formatted to 2 decimal places | Decimal | 5.00 |
| ShippingDiscount | Total monetary amount of shipping discount (UNiDAYS or otherwise) applied to the order, formatted to 2 decimal places | Decimal | 3.00 |
| ItemsGross | Total monetary amount of the items, including tax, before any discounts are applied, formatted to 2 decimal places | Decimal | 230.00 |
| ItemsOtherDiscount | Total monetary amount of all non UNiDAYS discounts applied to `ItemsGross`, formatted to 2 decimal places | Decimal | 10.00 |
| UNiDAYSDiscountPercentage | The UNiDAYS discount applied, as a percentage, formatted to 2 decimal places | Decimal | 10.00 |
| NewCustomer | Is the user a new (vs returning) customer to you? | Boolean | true or false |

### Example Basket

Here is an example basket with the fields relating to UNiDAYS tracking parameters,

| Item | Gross | UNiDAYS Discount | Other Discount | Tax | Net Total | Line Total |
|---|---|---|---|---|---|---|
| Shoes | 100.00 | 0.00 | 0.00 | 16.67 | 83.33 | 100.00 |
| Shirt | 50.00 | 5.00 | 0.00 | 7.50 | 37.50 | 45.00 |
| Jeans | 80.00 | 8.00 | 10.00 | 10.33 | 51.67 | 62.00 |
||||||||
| **Totals** | 230.00 | 13.00 | 10.00 | 34.50 | 172.50 | 207.00 |
||||||||
|||||| Shipping | 5.00 |
|||||| Shipping Discount | 3.00 |
||||||||
|||||| **Order Total** | 209.00 |

## Example Usage

Below are the three options for implementing your integration. These examples cover both coded and codeless integrations (see the live analytics PDF for details) and include all optional parameters. They are intended as a guideline for implementation.

- [Get Server URL _(returns url for server to server request)_](#get-server-url)
- [Get Script URL _(returns url for client to server request)_](#get-script-url)
- [Tracking Client _(sends server to server request)_](#tracking-client)
- [Test endpoint](#test-endpoint)

### Requirements

When using a server-to-server request (using `Get Server URL` or `Tracking Client`) your framework/implementation must support TLS >= 1.2 with SNI.

### Get Server URL

This method returns a URL which you can use to call our API.

It is a mandatory requirement that all server URLs are signed. This means you are required to pass the signing key UNiDAYS provide you with as one of the arguments.The signing key is a Base64 encoded GUID. This endpoint accepts both `GET` and `POST` requests.

#### Making the call

The method to get the URL to make a server-to-server request with is `trackingServerUrl(key)`. To implement this method you first need to use the `DirectTrackingDetailsBuilder` to create a direct tracking object with the properties you want to send across to us. More details about this builder can be found [here](#direct-tracking-details-builder).

Once the object containing the details you need to send us is created, create a Tracking helper, providing those details as an argument `new TrackingHelper(directTrackingDetails)` and call `.trackingServerUrl(signingKey)` where `signingKey` is the key provided to you by UNiDAYS.

#### Return

A URL will be returned, which can then be used to call the API. If successful a response with a status code of 204 No Content will be returned. This will work for both `POST` and `GET` requests.

#### Example

```java
public class Program {
    public static void Main(String... args) {
        // UNiDAYS will provide your partnerId and signingKey. The partnerId GUID needs to be Base64 encoded before passing it to the DirectTrackingDetailsBuilder.
        String partnerId = "somePartnerId";
        String signingKey = "someSigningKey";

        DirectTrackingDetails directTrackingDetails = new DirectTrackingDetailsBuilder(partnerId, "GBP", "the transaction")
                                    .withOrderTotal(new BigDecimal("209.00"))
                                    .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                                    .withCode("a code")
                                    .withItemsTax(new BigDecimal("34.50"))
                                    .withShippingGross(new BigDecimal("5.00"))
                                    .withShippingDiscount(new BigDecimal("3.00"))
                                    .withItemsGross(new BigDecimal("230.00"))
                                    .withItemsOtherDiscount(new BigDecimal("10.00"))
                                    .withUnidaysDiscountPercentage(new BigDecimal("10.00"))
                                    .withNewCustomer(true)
                                    .build();

        URI uri = new TrackingHelper(directTrackingDetails).trackingServerUrl(signingKey);
    }
}
```

### Get Script URL

This is also known as our client-to-server integration. This method returns a URL which can be placed within a script element on your post-payment/order-success page to call the API.

#### Unsigned or Signed

It's an option to create a signed url for your script request. To do this you will need to pass the signing key UNiDAYS provide you with as one of the arguments.

```java
URI uri = new TrackingHelper(directTrackingDetails).trackingScriptUrl(signingKey);
```

Instead of

```java
URI uri = new TrackingHelper(directTrackingDetails).trackingScriptUrl();
```

#### Making the call

The method to get the URL to make a server-to-server request with is `trackingServerUrl()` or  `trackingServerUrl(key)` if you've chosen to have a signed URL returned. To implement this method you first need to use the `DirectTrackingDetailsBuilder` to create a direct tracking object with the properties you want to send across to us. More details about this builder can be found [here](#direct-tracking-details-builder).

Once the object containing the details you need to send us is created, create a Tracking helper, providing those details as an argument `new TrackingHelper(directTrackingDetails)` and call `.trackingScriptUrl()` for an unsigned url or  `.trackingScriptUrl(signingKey)`, where `signingKey` is the key provided to you by UNiDAYS.

#### Return

A script URL will be returned to you. If successful a response with a status code of 200 OK will be returned. This will only work for `GET` requests.

#### Example

```java
public class Program {
    public static void Main(String... args) {
        // UNiDAYS will provide your partnerId. The partnerId GUID needs to be Base64 encoded before passing it to the DirectTrackingDetailsBuilder
        String partnerId = "somePartnerId";

        DirectTrackingDetails directTrackingDetails = new DirectTrackingDetailsBuilder(partnerId, "GBP", "the transaction")
                                    .withOrderTotal(new BigDecimal("209.00"))
                                    .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                                    .withCode("a code")
                                    .withItemsTax(new BigDecimal("34.50"))
                                    .withShippingGross(new BigDecimal("5.00"))
                                    .withShippingDiscount(new BigDecimal("3.00"))
                                    .withItemsGross(new BigDecimal("230.00"))
                                    .withItemsOtherDiscount(new BigDecimal("10.00"))
                                    .withUnidaysDiscountPercentage(new BigDecimal("10.00"))
                                    .withNewCustomer(true)
                                    .build();

        URI uri = new TrackingHelper(directTrackingDetails).trackingServerUrl();
    }
}
```

### Tracking Client

Calls to the tracking client is similar to [get server url](#get-server-url) but rather than returning a URL, UNiDAYS sends the request and returns a response.

It is a mandatory requirement that all codeless client calls are provided with a key, as the requests UNiDAYS send are signed.

#### Making the call

To implement this method you first need to use the `DirectTrackingDetailsBuilder` to create a direct tracking object with the properties you want to send across to us. More details about this builder can be found [here](#direct-tracking-details-builder).

Once the object containing the details you need to send us is created, create an instance of the tracking client, providing those details as an argument, along with the provided signing key `new TrackingClient(directTrackingDetails, signingKey)` and call `.send()`.

#### Return

A HttpResponseMessage is returned. If successful the response should have a status code of 204 No Content.

#### Example

The below example sets up some direct tracking details, calls `.send()` on the client, checks if the status code of the response message is a successful call (2xx) then reads out the content as a string.


```java
public class Program {
    public static void main(String... args) {
        // UNiDAYS will provide your partnerId and signingKey. The partnerId GUID needs to be Base64 encoded before passing it to the DirectTrackingDetailsBuilder
        String partnerId = "somePartnerId";
        String signingKey = "someSigningKey";

        DirectTrackingDetails directTrackingDetails =
            new DirectTrackingDetailsBuilder(partnerId, "GBP", "the transaction id")
                    .withOrderTotal(new BigDecimal("209.00"))
                    .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                    .withCode("a code")
                    .withItemsTax(new BigDecimal("34.50"))
                    .withShippingGross(new BigDecimal("5.00"))
                    .withShippingDiscount(new BigDecimal("3.00"))
                    .withItemsGross(new BigDecimal("230.00"))
                    .withItemsOtherDiscount(new BigDecimal("10.00"))
                    .withUnidaysDiscountPercentage(new BigDecimal("10.00"))
                    .withNewCustomer(true)
                    .build();

       try (TrackingClient client = new TrackingClient(directTrackingDetails, signingKey)) {
           HttpResponse response = client.send().get(10, TimeUnit.SECONDS);

           if (response.getStatusLine().getStatusCode() != 200) {
               // The response body contains a json description of the errors
               response.getEntity().writeTo(System.out);
           }
       } catch (Exception ex) {
           ex.printStackTrace();
       }
    }
}
```

### Test Endpoint

UNiDAYS provide test endpoints for each of the above types of call which are as follows:

- `trackingServerTestUrl(string key)`
- `trackingScriptTestUrl()`
- `trackingScriptTestUrl(string key)`

These methods add an extra parameter (`&Test=True`) to the URL that is returned to you, or sent for you. These endpoints are to be used to test your integration.

### Direct Tracking Details Builder

The purpose of the builder is to make it simple and intuitive when constructing any tracking request to UNiDAYS.

The arguments on the builder are the [mandatory parameters](#mandatory-parameters):

```java
DirectTrackingDetailsBuilder builder = new DirectTrackingDetailsBuilder(partnerId, currency, transactionId);
```

There are then a variety of methods available to build up the information you want to send us which can be chained up per the example. These match up to the [parameters](#parameters) at the top of this document

- `withMemberId(base64 encoded Guid)`
- `withCode(String)`
- `withOrderTotal(BigDecimal)`
- `withItemsUnidaysDiscount(BigDecimal)`
- `withItemsTax(BigDecimal)`
- `withShippingGross(BigDecimal)`
- `withShippingDiscount(BigDecimal)`
- `withItemsGross(BigDecimal)`
- `withItemsOtherDiscount(BigDecimal)`
- `withUnidaysDiscountPercentage(BigDecimal)`
- `withNewCustomer(booelan)`

Only chain the values that you have contractually agreed to provide. It is not a requirement to use every method.

The final call to be chained is `.build()` which creates the object.

#### Example

```java
public class Program {
    public static void main(String... args) {
        DirectTrackingDetails directTrackingDetails =
            new DirectTrackingDetailsBuilder("somePartnerId", "GBP", "the transaction")
                    .withOrderTotal(new BigDecimal("209.00"))
                    .withItemsUnidaysDiscount(new BigDecimal("13.00"))
                    .withCode("a code")
                    .withItemsTax(new BigDecimal("34.50"))
                    .withShippingGross(new BigDecimal("5.00"))
                    .withShippingDiscount(new BigDecimal("3.00"))
                    .withItemsGross(new BigDecimal("230.00"))
                    .withItemsOtherDiscount(new BigDecimal("10.00"))
                    .withUnidaysDiscountPercentage(new BigDecimal("10.00"))
                    .withNewCustomer(true)
                    .build();
    }
}
```

## Codeless Verification

If you have agreed to provide UNiDAYS Members with a codeless experience, alongside direct tracking, you will also need to implement the 'Codeless API' which will assist you with parsing and validating the signed traffic we direct towards your site.

### Codeless API

#### Making the call

First call the CodelessUrlVerifier with the key provided to you by UNiDAYS `new CodelessUrlVerifier(key)`. Then call `VerifyUrlParams(ud_s, ud_t, ud_h)` method with the values for ud_s, ud_t and ud_h as the arguments.

| Parameter | Description | Data Type | Max Length | Example |
|---|---|---|---|---|
| ud_s | UNiDAYS verified student ID | String | 256 chars | Do/faqh330SGgCnn4t3X4g== |
| ud_t | Timestamp for the request | String | 64 bits | 1375349460 |
| ud_h | Hash signature of the other two parameters | Base64 String | GBP | o9Cg3q2eVElZxYlJsEAQ== |

#### Return

If the method successfully validates the hash of the incoming request, a DateTime for the request will be returned; else null will be returned.

#### Example

```java
public class Program {
    public static void main(String... args) {
        // Your key as provided by UNiDAYS
        String signingKey = "someSigningKey";

        // Obtain parameters from the query string. Be sure to URL Decode them
        String ud_s = "Do/faqh330SGgCnn4t3X4g==";
        String ud_t = "1395741712";
        String ud_h = "i38dJdX+XLKuE4F5tv+Knpl5NPtu5zrdsjnqBQliJEJE4NkMmfurVnUaT46WluRYoD1/f5spAqU36YgeTMCNeg==";

        CodelessUrlVerifier verifier = new CodelessUrlVerifier(signingKey);
        Optional<ZonedDateTime> verifiedAt = verifier.verifyUrlParams(ud_s, ud_t, ud_h);
    }
}
```

## Contributing

This project is set up as an open source project. As such, if there are any suggestions that you have for features, for improving the code itself, or you have come across any problems; you can raise them and/or suggest changes in implementation.

If you are interested in contributing to this codebase, please follow the [contributing guidelines](.github/contributing.md). This contains guides on both contributing directly and raising feature requests or bug reports. Please adhere to our [code of conduct](./CODE_OF_CONDUCT.md) when doing any of the above.
