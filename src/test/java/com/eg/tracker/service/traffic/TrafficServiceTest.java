package com.eg.tracker.service.traffic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.repository.ReactiveDriverRepository;
import com.eg.tracker.service.TrafficServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class TrafficServiceTest {


	@Mock
	private ReactiveDriverRepository driversRepositoryMock;

	@InjectMocks
	private TrafficServiceImpl trafficServiceImpl = new TrafficServiceImpl();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void getTrafficTest() {

//		DriverPosition d1 = new DriverPosition();
//		d1.setName("torrente");
//		DriverPosition d2 = new DriverPosition();
//		d2.setName("paco");
//		DriverPosition d3 = new DriverPosition();
//		d3.setName("julio");
//
//		Flux<Driver> mockDriversFlux = Flux.create(fluxSink -> {
//			fluxSink.next(d1);
//			fluxSink.next(d2);
//			fluxSink.next(d3);
//		});
//
//		when(this.driversRepositoryMock.findAllByType(UserType.DRIVER))
//		.thenReturn(mockDriversFlux);
//
//		Flux<DriverPosition> drivers = this.trafficServiceImpl.getTraffic();
//		drivers.take(6);
//
//		StepVerifier.create(drivers)
//			.expectNext(d1, d2, d3, d1, d2, d3)
//			.thenCancel()
//			.verify();

	}

	@Test
	public void getTrafficWebTest() throws Exception {

		WebClient webClient = WebClient.create("http://localhost:8080/");
		Flux<DriverPosition> drivers = webClient.get()
			.uri("/private/api/v1/traffic")
			.header("Authorization", "bearer b1873613-b839-4e2f-b533-10891c853d45")
			.accept(MediaType.APPLICATION_STREAM_JSON)
			.retrieve()
			.bodyToFlux(DriverPosition.class);

		drivers.subscribe(driver -> {
			log.info(">>>> Driver received {}", driver.getName());
		});

		Thread.sleep(40000);
		drivers.cancelOn(Schedulers.newElastic("toto"));

	}

}
