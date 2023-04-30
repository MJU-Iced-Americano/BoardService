package com.mju.Board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mju.Board.presentation.dto.qnadto.QnARegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BoardApplicationTests {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testRegisterQnA() throws Exception {
		byte[] imageContent = Files.readAllBytes(Paths.get("C:/Users/DaEunYeo/Pictures/Screenshots/스크린샷_20221222_012024.png"));
		// Multipart 데이터 생성
		MockMultipartFile image = new MockMultipartFile(
				"image",
				"test-image.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				imageContent
		);

		// HTTP 요청 생성
		QnARegisterDto qnARegisterDto = new QnARegisterDto();
		qnARegisterDto.setQuestionTitle("test question title");
		qnARegisterDto.setQuestionContent("test question content");
		ObjectMapper objectMapper = new ObjectMapper();
		String dtoJson = objectMapper.writeValueAsString(qnARegisterDto);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/registerQnA")
				.file(image)
				.content(dtoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA);


	}

}
