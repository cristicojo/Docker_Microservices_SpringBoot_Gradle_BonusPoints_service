package employees.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RestController
public class EmployeesController {

	@Autowired
	private LoadBalancerClient loadBalancerClient;


	private final RestTemplate REST_TEMPLATE = new RestTemplate();


	@GetMapping(value = "/callCrudServiceControllerThroughBonus", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod = "getFallbackMethod")
	public ResponseEntity<String> callCrudServiceController() {

		return new ResponseEntity<>(REST_TEMPLATE.getForObject(getBaseUrl() + "/rest_api/top/it/5", String.class), HttpStatus.OK);

	}


	public ResponseEntity<String> getFallbackMethod() {

		return new ResponseEntity<>("CRUD_SERVICE_APPLICATION is DOWN in Eureka. Check Eureka Instances.....", HttpStatus.BAD_REQUEST);

	}


	@DeleteMapping(value = "/callCrudServiceControllerThroughBonus")
	public void callCrudServiceDeleteAllController() {

		REST_TEMPLATE.delete(getBaseUrl() + "/rest_api/all", String.class);

	}


	public String getBaseUrl() {

		ServiceInstance instance = loadBalancerClient.choose("CRUD_SERVICE_APPLICATION");

		return instance.getUri().toString();
	}

}
