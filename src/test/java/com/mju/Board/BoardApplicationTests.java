package com.mju.Board;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.repository.FAQBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	FAQBoardRepository faqBoardRepository;

	@Test
	void contextLoads() {
		FAQBoard faqBoard = new FAQBoard("제목.","내용.", true);
		faqBoardRepository.save(faqBoard);
//		List<?> list = faqBoardRepository.findAll();
//		System.out.print(list);

	}

}
