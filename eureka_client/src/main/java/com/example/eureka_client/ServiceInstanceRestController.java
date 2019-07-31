package com.example.eureka_client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/client")
class ServiceInstanceRestController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WypClient wypClient;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getBooksI(){
        return wypClient.getBooks();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookI(@PathVariable Long id){
        return wypClient.getBook(id);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBookI(@RequestBody BookDTO bookDto){
        return wypClient.createBook(bookDto);
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClient eurekaClient;

    @RequestMapping("/service-instances/{applicationName}")
    public String serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        Application application
                = eurekaClient.getApplication(applicationName);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();

        HttpEntity<?> entity = new HttpEntity<>(createHeaders("admin","admin"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://"+hostname+":"+port+"/books");
        ResponseEntity<String> exchange = this.restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
        return exchange.getBody().toString();

    }

    HttpHeaders createHeaders(String username, String password){
        HttpHeaders authorization = new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        authorization.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return  authorization;
    }

}