package com.example.mongo.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component("tenantProvider")
public class TenantProvider {

	private static final String forwardedTenantId = "X-TENANT-ID";
	private static final String defaultTenant = "Defaul";

	private static ThreadLocal<String> tenantId = new ThreadLocal<>();

//	@Context
//	HttpServletRequest request;
	@Bean
	public static TenantProvider GetTenantProvider() {
		return new TenantProvider();
	}

	public String getTenantId() {
		System.out.println(tenantId);
		return tenantId.get() != null ? tenantId.get() : defaultTenant;
	}

	public static void parseTenantIdFromRequest(HttpServletRequest request) {
		tenantId.set(request.getHeader(forwardedTenantId));
//		tenantId.set(Optional.ofNullable(request.getHeader(forwardedTenantId))
//				.map(TenantProvider::extractTenantIdentifier).orElse(defaultTenant));
	}

	public static void clearTenantId() {
		tenantId.remove();
	}
}