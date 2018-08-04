package com.VodafoneCodeChallenge.CustomerDatabase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.VodafoneCodeChallenge.CustomerDatabase.Exception.BadRequestException;
import com.VodafoneCodeChallenge.CustomerDatabase.Exception.MsisdnExistsException;
import com.VodafoneCodeChallenge.CustomerDatabase.Exception.SubscriberNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@WebMvcTest(value=MobileSubscriberJPAResource.class)
public class MobileSubscriberJPAResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MobileSubscriberRepository repository;

	@Test
	public void addSubscriberSuccessTest() throws Exception {
		String URI = "/";
		
		MobileSubscriber subscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
		String jsonSubscriber = toJson(subscriber);
		
		RequestBuilder builder = MockMvcRequestBuilders.post(URI).content(jsonSubscriber).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(builder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		//assertThat(response.getContentAsString(), equalTo(jsonSubscriber));
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void addSubscriberFailConflictTest() throws Exception {
		String URI = "/";
		MockHttpServletResponse response = null; 
		try {
			MobileSubscriber subscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
			String jsonSubscriber = toJson(subscriber);
			
			RequestBuilder builder = MockMvcRequestBuilders.post(URI).content(jsonSubscriber).contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(builder).andReturn();
			MvcResult result = mockMvc.perform(builder).andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			assertTrue(e instanceof MsisdnExistsException);
			assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		}		
	}
	
	@Test
	public void addSubscriberFailBadRequestTest() throws Exception {
		String URI = "/";
		MockHttpServletResponse response = null; 
		try {
			MobileSubscriber subscriber = new MobileSubscriber(null, 1, 1, MobileServiceType.MOBILE_POSTPAID);
			String jsonSubscriber = toJson(subscriber);
			
			RequestBuilder builder = MockMvcRequestBuilders.post(URI).content(jsonSubscriber).contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(builder).andReturn();
			MvcResult result = mockMvc.perform(builder).andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			assertTrue(e instanceof BadRequestException);
			assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		}		
	}
	
	@Test
	public void getAllMobileNumbersSuccessTest() throws Exception {
		
		String URI = "/";
		
		ArrayList<MobileSubscriber> list = new ArrayList<>();
		MobileSubscriber firstSubscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
		MobileSubscriber secondSubscriber = new MobileSubscriber("35699288664", 2, 2, MobileServiceType.MOBILE_PREPAID);
		list.add(firstSubscriber);
		list.add(secondSubscriber);
		Mockito.when(repository.findAll()).thenReturn(list);
		
		RequestBuilder getRequestBuilder = MockMvcRequestBuilders.get(URI);
		
		MvcResult result = mockMvc.perform(getRequestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String jsonList = toJson(list);
		
		// Test return empty array
		assertThat(response.getContentAsString(), equalTo(jsonList));
		// Test return status 200
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void getAllMobileNumbersEmptyTest() throws Exception {
		
		String URI = "/";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		ArrayList<MobileSubscriber> list = new ArrayList<MobileSubscriber>();
		String jsonList = toJson(list);
		
		// Test return empty array
		assertThat(response.getContentAsString(), equalTo(jsonList));
		// Test return status 200
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void getMobileNumberByIdSuccessTest() throws Exception {
		
		String URI = "/0";
		
		MobileSubscriber subscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(subscriber));
		
		RequestBuilder getRequestBuilder = MockMvcRequestBuilders.get(URI);
		MvcResult result = mockMvc.perform(getRequestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		// Test return empty array
		assertThat(response.getContentAsString(), equalTo(toJson(subscriber)));
		// Test return status 200
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void getMobileNumberByIdFailTest() throws Exception {
		
		String URI = "/1";
		MockHttpServletResponse response = null;
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
			assertTrue(e instanceof SubscriberNotFoundException);
		}		
	}
	
	@Test
	public void deleteSuccessTest() throws Exception {
		String URI = "/1";
		MobileSubscriber subscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(subscriber));		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URI);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
	}
	
	@Test
	public void deleteFailTest() throws Exception {
		String URI = "/1";
		MockHttpServletResponse response = null;
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URI);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
			assertTrue(e instanceof SubscriberNotFoundException);
		}
	}

	@Test
	public void changeServiceTypeSuccessTest() throws Exception {
		String URI = "/1";
		MobileSubscriber subscriber = new MobileSubscriber("35699037433", 1, 1, MobileServiceType.MOBILE_POSTPAID);
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(subscriber));
		MobileSubscriber updatedSubscriber = new MobileSubscriber("35699123456", 3, 3, MobileServiceType.MOBILE_PREPAID);
		subscriber.updateServiceType(updatedSubscriber);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(URI).content(toJson(updatedSubscriber)).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertThat(response.getContentAsString(), equalTo(toJson(subscriber)));
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void changeServiceTypeFailTest() throws Exception {
		String URI = "/1";
		MockHttpServletResponse response = null;
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.put(URI);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			response = result.getResponse();
		} catch (Exception e) {
			assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
			assertTrue(e instanceof SubscriberNotFoundException);
		}
	}
	
	
	private String toJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

}
