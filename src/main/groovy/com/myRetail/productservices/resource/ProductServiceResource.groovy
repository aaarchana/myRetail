package com.myRetail.productservices.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.myRetail.productservices.domain.ProductDetailsResponse
import com.myRetail.productservices.domain.ProductPriceRequest
import com.myRetail.productservices.service.ProductDetailsService
import com.myRetail.productservices.spring.ProductNotFoundException
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController()
@Api( description = "endpoint for ProductPrice details")
@Slf4j
@CompileStatic
class ProductServiceResource {

    Set<String> acceptedClientIds

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    ProductDetailsService productDetailsService


    @RequestMapping(
            path = "/products/{id}",
            method = GET,
            produces = "application/json")
    @ApiOperation(value = "Get ProductPrice details", notes = """ Request details for a given Sku """, response = String)
    @ApiResponses(value = [
            @ApiResponse(code = 200, message = 'OK'),
            @ApiResponse(code = 400, message = 'Bad request'),
            @ApiResponse(code = 500, message = 'Internal Server Error')])

    ResponseEntity<ProductDetailsResponse> getProductDetails (@ApiParam(value = '123456', required = true) @PathVariable("id") Integer id){
        ProductDetailsResponse productDetailsResponse
        try {
            log.info("Request received for skuId: $id")
            productDetailsResponse= productDetailsService.getProductDetails(id)
        }
        catch(Exception e){
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND)
        }

        if (productDetailsResponse.current_price?.value == null){
            return new ResponseEntity<>("Price for the product not found",HttpStatus.NOT_FOUND)
        }
        return new ResponseEntity<ProductDetailsResponse>(productDetailsResponse,HttpStatus.OK)

        }


    @PutMapping(path ="/products/{id}",
    consumes = "application/json",
    produces = "text/plain")
    @ApiResponses(value = [
            @ApiResponse(code = 200, message = 'OK'),
            @ApiResponse(code = 400, message = 'Bad request'),
            @ApiResponse(code = 500, message = 'Internal Server Error')])
    @ApiOperation(value = "Insert  product price", notes =
            """
Product Price.<BR>
<BR><h4>Request</h4>
<pre>
{
  <font color='blue'>"price": 99.99,</font>
  <font color='blue'>"currency_code":"USD",</font>
 
}

</pre>
""")
    ResponseEntity saveProductPrice(@PathVariable("id") Integer id, @RequestHeader("client-id") String clientId, @RequestBody ProductPriceRequest productPriceRequest) {
        if(!(clientId?.toLowerCase() in acceptedClientIds)){
            return new ResponseEntity("invalid client id", HttpStatus.UNAUTHORIZED)
        }
        productPriceRequest.id=id
        if(productPriceRequest.price)
        productDetailsService.saveProductPrice(productPriceRequest)
        return new ResponseEntity("Price for sku:$id updated", HttpStatus.OK)

    }




    @Value('#{${acceptedclientids}}')
    public void setAcceptedClientIds(Set<String> clients){
        acceptedClientIds = clients*.toLowerCase().toSet()
    }

}
