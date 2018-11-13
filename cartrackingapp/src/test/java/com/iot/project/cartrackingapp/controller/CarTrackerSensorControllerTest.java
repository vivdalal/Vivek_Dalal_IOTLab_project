package com.iot.project.cartrackingapp.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@WebMvcTest(CarTrackerSensorController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class CarTrackerSensorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	
//    .andDo(document("home", responseFields(
//        fieldWithPath("message").description("The welcome message for the user.")
//    ));
	
	@Test
	public void shouldReturnDefaultMessage() throws Exception{
		 this.mockMvc.perform(get("/cartrackerdata/")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("Hello World")))
         .andDo(document("home",responseFields( 
        		 fieldWithPath("message").description("The welcome message for the user."))));
	}
	
	
	
}
