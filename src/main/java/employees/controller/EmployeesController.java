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


@RestController
public class EmployeesController {

	@Autowired
	private LoadBalancerClient loadBalancerClient;


	private RestTemplate restTemplate = new RestTemplate();


	@GetMapping(value = "/callCrudServiceControllerThroughBonus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> callCrudServiceController() {

		return new ResponseEntity<>(restTemplate.getForObject(getBaseUrl()+"/rest_api/top/it/5", String.class), HttpStatus.OK);

	}


	@DeleteMapping(value = "/callCrudServiceControllerThroughBonus")
	public void callCrudServiceDeleteAllController() {

		 restTemplate.delete(getBaseUrl()+"/rest_api/all", String.class);

	}


	public String getBaseUrl(){

		ServiceInstance instance=loadBalancerClient.choose("CRUDSERVICEAPPLICATION");

		return instance.getUri().toString();
	}

}
