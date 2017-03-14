# Rest0 Service

## About

The Location Service used by the Horizon GO platform exposes one operation only, it informs a client if the customer is "In Home".

A client is considered "in Home" when the IP address of his device has a MAC address that corresponds with the MAC address of his settop box/settop boxes.

To completely test the Location Service, one needs the **IpInvent Service** and the **Provisioning Service**.
* Retrieval of the MAC address is established by doing a **first call** to the IpInvent service.
* Retrieval of the MAC addresses of the customer's settop box/boxes is established by doing a **second call** to the Provisioning Service and retrieving the MODEM_HFC_MAC_ADDRESS property of each TV_RESOURCE_STB for this customer.

## Running the project

### Running locally
```sh
mvn spring-boot:run
```

### Testing locally
The rest service can be easily tested with Postman.

FYI : the StbReference tag is optional.
A more complete design can be found at https://bugtracker.inet.telenet.be/confluence/display/YB/Location+Service.

Example of a ResolveInHomeStatusRequest request:
```sh
<ResolveInHomeStatusRequest>
    <IpAddress>10.25.45.241</IpAddress>
    <CustomerId>74574385-3f42-3248-375737328429</CustomerId>
    <StbReference>
        <Platform>HORIZON</Platform>
        <Identifier>984395-EOS1-43665475676</Identifier>
    </StbReference>
</ResolveInHomeStatusRequest>
```
Example of a ResolveInHomeStatusResponse response:
```sh
<ResolveInHomeStatusResponse>
    <InHomeStatus>true</InHomeStatus>
</ResolveInHomeStatusResponse>
```
