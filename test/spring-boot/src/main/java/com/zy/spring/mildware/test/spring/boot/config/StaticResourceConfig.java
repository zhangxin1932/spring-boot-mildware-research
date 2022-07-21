package com.zy.spring.mildware.test.spring.boot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*配置js,css等*/
        registry.addResourceHandler("/js/**").addResourceLocations("WEB-INF/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("WEB-INF/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("WEB-INF/fonts/");
        // super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /*配置默认访问页:需要在浏览器中输入ip+端口号*/
        registry.addViewController("/").setViewName("forward:/index.jsp");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        // super.addViewControllers(registry);
    }

    @Bean
    public FilterRegistrationBean xssFilter() {
        XssFilter xssFilter = new XssFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(xssFilter);
        filterRegistrationBean.addUrlPatterns("/hello");
        return filterRegistrationBean;
    }

    private static class XssFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            System.out.println("filterChain.doFilter 之前的代码 --> 拦截请求: " + httpServletRequest.getRequestURI());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            // 如果 filterChain.doFilter 之后没有代码, 则等同于只拦截请求, 不拦截响应
            System.out.println("filterChain.doFilter 之后的代码 --> 拦截响应: " + httpServletRequest.getRequestURI());
        }
    }

}
