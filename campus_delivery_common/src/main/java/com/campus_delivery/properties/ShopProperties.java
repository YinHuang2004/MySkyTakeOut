package com.campus_delivery.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "campus_delivery.shop")
@Data
public class ShopProperties {
    private String address;
}
