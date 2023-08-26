package employee.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class EmployeesController {

	private final LoadBalancerClient LOAD_BALANCER_CLIENT;
	private final RestTemplate REST_TEMPLATE = new RestTemplate();


	public EmployeesController(LoadBalancerClient loadBalancerClient) {
		this.LOAD_BALANCER_CLIENT = loadBalancerClient;
	}


	@GetMapping(value = "/callCrudServiceControllerThroughBonus", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod = "getFallbackMethod")
	public ResponseEntity<String> callCrudServiceController() {

		return new ResponseEntity<>(REST_TEMPLATE.getForObject(getBaseUrl() + "/api/top/it/5", String.class), HttpStatus.OK);

	}


	public ResponseEntity<String> getFallbackMethod() {
		return new ResponseEntity<>("CRUD_SERVICE_APPLICATION is DOWN in Eureka. Check Eureka Instances.....", HttpStatus.BAD_REQUEST);

	}


	@DeleteMapping(value = "/callCrudServiceControllerThroughBonus")
	public void callCrudServiceDeleteAllController() {
		REST_TEMPLATE.delete(getBaseUrl() + "/api/all", String.class);

	}


	public String getBaseUrl() {

		ServiceInstance instance = LOAD_BALANCER_CLIENT.choose("CRUD_SERVICE_APPLICATION");
		return instance.getUri().toString();

	}
}