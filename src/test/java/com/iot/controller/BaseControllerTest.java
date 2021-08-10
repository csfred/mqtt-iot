package com.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseControllerTest  extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeMethod
    public void setUp() {
        //必须用webAppContextSetup方法设置web环境
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }

    @AfterMethod
    public void tearDown() {
    }

    //通用返回结果的封装
    public String getResultCode(MockHttpServletRequestBuilder requestBuilder) {

        try {

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            String result = mvcResult.getResponse().getContentAsString();
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject.getString("code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Test
    public void testTestController() {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test");
            String resultCode = getResultCode(requestBuilder);
            Assert.assertEquals("200", resultCode);
    }
}