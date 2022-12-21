package com.tiagodeveloper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tiagodeveloper.config.LocalStackConfiguration;
import com.tiagodeveloper.entity.Customer;
import com.tiagodeveloper.repository.CustomerRepository;



@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(LocalStackConfiguration.class)
@ContextConfiguration(initializers = LocalStackConfiguration.DataSourceInitializer.class)
class SpringDataRestExampleApplicationTests {

	
    @Autowired
    private MockMvc mockMvc;
    
	@Autowired
	private CustomerRepository customerRepository;
	
	
	@Test
	void mustGetAllCustomers() throws Exception {
		mockMvc.perform(get("/customers")
			.accept("application/hal+json"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Type", "application/hal+json"))
			.andExpect(jsonPath("$._embedded.customers[0].name").value("Tânia Pereira da Silva"));
	}
	
	@Test
	void mustSaveTheCustomer() throws Exception {
		mockMvc.perform(post("/customers")
				.content(asJsonString(new Customer(null, "Tiago", "tiago@gmail.com", true, LocalDate.now())))
				.accept("application/hal+json"))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	void mustEditTheCustomer() throws Exception {
		mockMvc.perform(put("/customers/{name}", "e6f150c7-d7d6-47ac-96f7-8195bfba906e")
				.content(asJsonString(new Customer(null, "Tiago", "tiago@gmail.com", true, LocalDate.now())))
				.accept("application/hal+json")
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Tiago"));
	}
	
	@Test
	void mustPartiallyEditTheClient() throws Exception {
		
		var customer = customerRepository.findById(UUID.fromString("e6f150c7-d7d6-47ac-96f7-8195bfba906e")).orElseThrow();
		customer.setName("Vanessa");
		
		mockMvc.perform(patch("/customers/{name}", customer.getId().toString())
				.content(asJsonString(customer))
				.accept("application/hal+json")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Vanessa"));
	}
	
	@Test
	void mustDeleteTheClient() throws Exception {
		mockMvc.perform(delete("/customers/{name}", "8215bfb7-875a-4712-a1bb-27cefd0c2053")//apartado, pois os testes rodam async e fora de ordem
				.accept("application/hal+json")
				)
		.andDo(print())
		.andExpect(status().isNoContent());
	}
	
	static String asJsonString(final Object obj) {
		var objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());//por default ele não serializa o LocalDate, então tem que adicionar o module para ele passar a serializar
	    try {
	        return objectMapper.writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
