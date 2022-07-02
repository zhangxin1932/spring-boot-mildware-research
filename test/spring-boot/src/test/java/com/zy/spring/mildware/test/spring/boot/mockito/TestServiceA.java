package com.zy.spring.mildware.test.spring.boot.mockito;

import com.zy.spring.mildware.test.spring.boot.service.ServiceA;
import com.zy.spring.mildware.test.spring.boot.service.ServiceB;
import lombok.extern.slf4j.Slf4j;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Slf4j
public class TestServiceA {

    @Mock
    private ServiceB serviceB;

    @InjectMocks
    private ServiceA serviceA;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHiA() {
        BDDMockito.when(serviceB.hi(BDDMockito.anyString())).thenReturn("123");
        log.info(serviceA.hi("456"));
    }

}
