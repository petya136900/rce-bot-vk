package com.petya136900.rcebot.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.petya136900.rcebot.lifecycle.Logger;
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class SpringServer extends Thread {
	private static Boolean serverStarted=false;
	private String[] args=null;
	private ConfigurableApplicationContext springApp;
	private Integer port;
	public SpringServer(String[] args) {
		this.args=args;
	}
	public void setPort(Integer port) {
		this.port=port;
	}
	public void startServer() {
		if(!SpringServer.serverStarted) {
			SpringServer.serverStarted=true;
			//springApp = SpringApplication.run(SpringServer.class, args);
			start(SpringServer.class).properties("server.port=${other.port:"+this.port+"}").run(args);
		} else {
			Logger.printInfo("Server already started");
		}
	}
	public void stopServer() {
		if(springApp!=null) {
			Logger.printInfo("Stopping server..");
			springApp.stop();
			springApp.close();
		}
	}
	@Override 
	public void run() {
		startServer();
	}
	private static SpringApplicationBuilder start(Class<?>... sources) {
	    return new SpringApplicationBuilder(SpringServer.class)
	        	        .child(sources);
	}
}
