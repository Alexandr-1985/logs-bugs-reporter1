//3
//надо построитьапликационный контекст (внутренний) только для теста, ГДЕ Я БУДУ ИМИТИРОВАТЬ CONTROLLER, WEB SERVER, WEB CLIENT, чтобы 
//проверить, действительно ли мой DTO РОХОДИТ ПО иНТЕРНЕТУ. 

package telran.logs.bugs.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Date;

import javax.validation.Valid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//у меня нет spring boot aplication, нет метода main поэтому это не апликация, - поэтому создаем другие анатации
@ExtendWith(SpringExtension.class)
// анотация чтобы попасть в апликоционный контекст
@AutoConfigureMockMvc
//нам нужно создать rest controller и  запустить 
@WebMvcTest(LogDtoTest.TestController.class)
//имитация апликационнного контекста для ТЕСТОВ
@ContextConfiguration(classes = LogDtoTest.TestController.class)

public class LogDtoTest {
//"" - пустая строка
// внутренний тест, нис=xего не связано с реальным контекстом
	public static @RestController class TestController {
		// мы проверяем как этот JSON который будет получен из dto будет передан и
		// получен
		// new LogDto сравнивает то что в ()
		static LogDto logDtoExp = new LogDto(new Date(), LogType.NO_EXCEPTION, "artifact", 0, "");

		// @Valid говорит что spring будет проверять мой logDto в соответствии с
		@PostMapping("/")
		void testPost(@RequestBody @Valid LogDto logDto) {
//анотация что есть в LogDto
			assertEquals(logDtoExp, logDto);
		}
	}

//когда мы будем запускать тест нам нужен MockMvc
//Object Mapper - сопостовитель объектов	
	ObjectMapper mapper = new ObjectMapper();

//Mvc - Maket Web Server
	// макет который дает сервер,
	// spring поместит апликационный контекст
//тк этот mock дает Spring то мы делаем @Autowired
	@Autowired
	MockMvc mock;

	// теперь метод тест запускаем через mocMVC, посылаем и говорим какие тесты
	// возможны
	// TestController.logDtoExp.dateTime = null;
	// 200 возврат ошибки
	// делаем post запрос. добавляем контент(медиаТайп)
	@Test
	void testPostRun() throws JsonProcessingException, Exception {
		assertEquals(200,
				mock.perform(post("/").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(TestController.logDtoExp))).andReturn().getResponse()
						.getStatus()); // контент который мы получаем из logDtoExp который я предаю
	}
}
