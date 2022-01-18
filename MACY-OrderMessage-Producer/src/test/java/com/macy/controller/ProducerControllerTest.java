package com.macy.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.macy.dto.FulFillmentOrder;
import com.macy.dto.OrderJsonDto;
import com.macy.dto.OrderStatus;

@WebMvcTest(ProducerController.class)
public class ProducerControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	@Qualifier("jsonTemplate")
	private RabbitTemplate rabbitJsonTemplate;

	@MockBean
	@Qualifier("xmlTemplate")
	private RabbitTemplate rabbitXmlTemplate;

	private static ObjectMapper jsonMapper = new ObjectMapper();
	private XmlMapper xmlMapper = new XmlMapper();

	@Test
	public void testJsonOrder() throws Exception {

		OrderJsonDto jsonDto = new OrderJsonDto(1, "", "", "DELL i5", "", 2.4, 4.9, 5.9, 2.3, "", "", 1, "JSON", "");
		String json = jsonMapper.writeValueAsString(jsonDto);

		mvc.perform(MockMvcRequestBuilders.post("/macy/producer/order/json").content(json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
	}

	@Test
	public void testXmlOrder() throws Exception {

		FulFillmentOrder xmlOrder = new FulFillmentOrder(1, "Digital", "SAM123", OrderStatus.CREATED, null, null, 0,
				null, 0, 0, null, null, null);
		String xml = xmlMapper.writeValueAsString(xmlOrder);

		mvc.perform(MockMvcRequestBuilders.post("/macy/producer/order/xml").content(xml)
				.contentType(MediaType.APPLICATION_XML_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
	}
}
