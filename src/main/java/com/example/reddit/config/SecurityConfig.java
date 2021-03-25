package com.example.reddit.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@AllArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    //用户想要个性化的用户认证逻辑，就需要自己写一个自定义的配置类，适配到 spring security 中：
    //这个配置类定义Spring security如何将自定义的user对象存放，然后在登录认证的时候与它进行认证
    //AuthenticationManagerBuilder这个类就是给我们用各种不同的方式注入用户信息
    //以及指定Spring security用何种方式去认证这个信息（内存、数据库等等）
    //userDetailsService(T userDetailsService)：根据传入的自定义UserDetailsService添加身份验证，
    //自定义UserDetailsService就是实现spring security 下
    //org.springframework.security.core.userdetails包下的UserDetailsService接口

    //具体authentication manager的解析
    // https://www.cnblogs.com/felordcn/p/13370489.html


    //configureGlobal makes the AuthenticationManager available to the entire application
    // (i.e. other WebSecurityConfigurerAdapter instances, method security, etc)
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    //**显式注入authentication manager**
    //否则会报错：
    //Field authenticationManager in cn.springcloud.book.OAuthConfiguration required
    // a bean of type 'org.springframework.security.authentication.AuthenticationManager' that could not be found.
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    //redirect to different pages based on different authorization status
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/subreddit")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/**")
                .permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated();
//        httpSecurity.addFilterBefore(jwtAuthenticationFilter,
//                UsernamePasswordAuthenticationFilter.class);
    }




}
