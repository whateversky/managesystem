package com.ebay.managesystem;

import com.ebay.managesystem.controller.UserInfoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestUserInfo {
    @Resource
    private UserInfoController userInfoController;
    @Resource
    private MockMvc mockMvc;

    /**
     * 检查不加user-info header
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1111L);
        data.put("endpoint", Arrays.asList("aaa", "bbb", "ccc"));
        ObjectMapper objectMapper = new ObjectMapper();
        String httpUrl = "/admin/addUser";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(httpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        try {
            ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            String contentAsString = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            MatcherAssert.assertThat(contentAsString, Matchers.equalTo("{\"code\":500,\"msg\":\"error parse user info token\",\"data\":null}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * user 不能访问 /admin/addUser
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1111);
        map.put("accountName", "yangting");
        map.put("role", "user");
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfo = new String(Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes()));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 3333);
        data.put("endpoint", Arrays.asList("aaa", "bbb", "ccc"));

        String httpUrl = "/admin/addUser";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(httpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-info", userInfo)
                .content(objectMapper.writeValueAsString(data));
        try {
            ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            String contentAsString = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            MatcherAssert.assertThat(contentAsString, Matchers.equalTo("{\"code\":500,\"msg\":\"the role cannot access the uri\",\"data\":null}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * admin 能访问 /admin/addUser
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 2222);
        map.put("accountName", "jianghui");
        map.put("role", "admin");
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfo = new String(Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes()));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 3333);
        data.put("endpoint", Arrays.asList("aaa", "bbb", "ccc"));

        String httpUrl = "/admin/addUser";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(httpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-info", userInfo)
                .content(objectMapper.writeValueAsString(data));
        try {
            ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            String contentAsString = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            MatcherAssert.assertThat(contentAsString, Matchers.equalTo("{\"code\":200,\"msg\":\"success\",\"data\":null}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 3333 能访问 /user/aaa (aaa资源权限)
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 3333);
        map.put("accountName", "xxxx");
        map.put("role", "user");
        String userInfo = new String(Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes()));

        String httpUrl = "/user/aaa";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(httpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-info", userInfo);
        try {
            ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            String contentAsString = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            MatcherAssert.assertThat(contentAsString, Matchers.equalTo("{\"code\":200,\"msg\":\"success\",\"data\":\"aaa\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 3333 不能访问 /user/ddd (没有ddd资源权限)
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 3333);
        map.put("accountName", "xxxx");
        map.put("role", "user");
        String userInfo = new String(Base64.getEncoder().encode(new ObjectMapper().writeValueAsString(map).getBytes()));

        String httpUrl = "/user/ddd";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(httpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-info", userInfo);
        try {
            ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
            String contentAsString = resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            MatcherAssert.assertThat(contentAsString, Matchers.equalTo("{\"code\":500,\"msg\":\"resource cannot be accessed\",\"data\":null}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
