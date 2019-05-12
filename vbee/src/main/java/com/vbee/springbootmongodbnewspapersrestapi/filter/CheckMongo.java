package com.vbee.springbootmongodbnewspapersrestapi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

import com.vbee.springbootmongodbnewspapersrestapi.Ultils;

@Component
public class CheckMongo implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(req, resp);
		// TODO Auto-generated method stub
//		if (Ultils.ping("localhost", 27017, 300)) {
//			chain.doFilter(req, resp);
//		} else {
//			try {
//				Process process = Runtime.getRuntime().exec(new String[] { "service", "mongod", "start" });
//				process.waitFor();
//				long start = System.currentTimeMillis();
//				while (System.currentTimeMillis() - start < 30000) {
//					if (Ultils.ping("localhost", 27017, 300)) {
//						chain.doFilter(req, resp);
//						return;
//					} else {
//						Thread.sleep(10);
//					}
//				}
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
