package com.eg.tracker.amqp;

import java.util.Calendar;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eg.tracker.conf.AMQPConfig;
import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.Position;
import com.eg.tracker.service.DriverService;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("mock-traffic")
public class MockTrafficGenerator {

	@Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private DriverService driverService;

    @Scheduled(cron = "0 0/1 7-20 * * *")
    public void mockTraffic() throws Exception {
    	log.info("Generating mock traffic");
    	int count = 100;

    	List<Driver> drivers = this.driverService.findDrivers();

    	for (int i = 0; i < count; i++) {
    		int d = ThreadLocalRandom.current().nextInt(drivers.size());
    		Driver driver = drivers.get(d);
    		driver = this.driverService.getDriver(driver.getId());
    		Position p = driver.getLastPosition();

    		double v = Double.valueOf(p.getLatitude()) + ThreadLocalRandom.current().nextDouble(-0.0100, +0.0100);
    		p.setLatitude(String.valueOf(v));
    		v = Double.valueOf(p.getLongitude()) + ThreadLocalRandom.current().nextDouble(-0.0100, +0.0100);
    		p.setLongitude(String.valueOf(v));
    		p.setTime(Calendar.getInstance().getTime());

    		DriverPosition dp = new DriverPosition();
    		dp.setId(driver.getId());
    		dp.setLatitude(p.getLatitude());
    		dp.setLongitude(p.getLongitude());
    		dp.setTime(Calendar.getInstance().getTime());
    		dp.setName(driver.getName());
    		dp.setPlate(driver.getPlate());

    		byte[] obj = SerializationUtils.serialize(dp);
    		this.rabbitTemplate.convertAndSend(AMQPConfig.topicExchangeName, "teg.traffic", obj);

    		//this.driverService.setLastPosition(driver, p);
    		Thread.sleep(500);
    	}
    }

}
