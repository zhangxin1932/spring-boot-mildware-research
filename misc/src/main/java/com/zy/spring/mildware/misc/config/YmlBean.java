package com.zy.spring.mildware.misc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
//@PropertySource(value = {"classpath:person.properties"}) 另一种配置方式
@ConfigurationProperties(prefix = "person") // 该prefix对应于yaml文件中的person
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 *  @ConfigurationProperties(prefix = "person")默认从全局配置文件中获取值；
 *
 */
public class YmlBean {
    private String lastName;

    private Integer age;

    private Boolean boss;

    private Date birth;

    private Map<String,Object> maps;

    private List<Object> lists;

    private Dog dog;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Component
    @ConfigurationProperties(prefix = "dog") // 该prefix对应于yaml文件中的dog
    public static class Dog {

        private String name;

        private Integer age;

    }
}
