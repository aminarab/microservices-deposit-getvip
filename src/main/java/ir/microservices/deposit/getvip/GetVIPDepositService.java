package ir.microservices.deposit.getvip;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@SpringBootApplication
@RestController

@EnableCircuitBreaker

@EnableHystrix
@EnableHystrixDashboard

public class GetVIPDepositService {

	@Bean
	public RestTemplate cbRestTemplate(){
		return new RestTemplate();
	}
	
	@Autowired
	private RestTemplate cbRestTemplate;
	
	@RequestMapping("/getVIPDeposits")
	@HystrixCommand(fallbackMethod = "failOver")
	/*
	 * https://github.com/Netflix/Hystrix/wiki/Configuration
	 */
//	@HystrixCommand(fallbackMethod = "failOver" , commandProperties ={
//		@HystrixProperty(name="fallback.enabled" , value= "false")
//	})
	public List<String> getVIPDeposits(){
		List<String> vipDeposits = new ArrayList<>();
		List<String> list = this.cbRestTemplate.getForObject("http://localhost:8888/getDepositNumbers", List.class);
		for (int i = 0; i < list.size(); i++) {
			if(i % 2 == 0 ){
				vipDeposits.add(list.get(i));
			}
		}
		return vipDeposits;
	}
	
	public List<String> failOver(){
		List<String> arrayList = new ArrayList<>();
		arrayList.add("my deposit account" );
		arrayList.add("your deposit account");
		return arrayList;
	}


	public static void main(String[] args) {
		SpringApplication.run(GetVIPDepositService.class, args);
	}
}
