package spring.controller.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.auxiliaryObjects.CreateObjects;
import spring.dto.CarFilterRequestDto;
import spring.dto.CarGiveDto;
import spring.dto.CarImportDto;
import spring.dto.CarRequestDto;
import spring.model.Car;
import spring.service.CarService;
import spring.service.importt.CsvService;

@WebMvcTest(CarRestController.class)
public class CarRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CarService carService;

	@MockBean
	private CsvService csvService;

	@MockBean
	private JwtDecoder jwtDecoder;

	private final CreateObjects createObjects = new CreateObjects();
	private static final String MODEL = "Qweqweqweqwe";

	@Test
	void create_withoutToken_return_401() throws Exception {
		CarRequestDto dto = createObjects.createCarRequestDto();

		mockMvc.perform(post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isUnauthorized());
	}

	@Test
	void create_withJwtWithoutScope_return_403() throws Exception {
		CarRequestDto dto = createObjects.createCarRequestDto();

		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:scope")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isForbidden());
	}

	@Test
	void create_withJwtAndCreateScope_return_201() throws Exception {
		CarRequestDto dto = createObjects.createCarRequestDto();
		CarGiveDto resp = createObjects.createCarGiveDto();

		when(carService.create(any(CarRequestDto.class))).thenReturn(resp);

		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.model").value(MODEL)).andExpect(jsonPath("$.id").exists());
	}

	@Test
	void create_whenModelInvalidTitle_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setModel("qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenModelInvalidSizeMax_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setModel("qweqweqweqweqweqweqweqweqwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenModelInvalidSizeMin_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setModel("");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenModelInvalidYear_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setYear(1959);
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenModelInvalidNumberWords_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setModel("Qwe Qwe Qwe Qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidTitle_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setMakeName("qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidSizeMax_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setMakeName("qweqweqweqweqweqweqweqweqwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidSizeMin_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setMakeName("Qq");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidNumber_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setMakeName("Qwe1");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidNumberWords_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setMakeName("Qwe Qwe Qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidTitle_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setCategoryName("qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidSizeMax_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setCategoryName("qweqweqweqweqweqweqweqweqwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidSizeMin_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setCategoryName("qq");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidNumber_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setCategoryName("Qwe1");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidNumberWords_return400() throws JsonProcessingException, Exception {
		CarRequestDto dtoReq = createObjects.createCarRequestDto();
		dtoReq.setCategoryName("Qwe Qwe Qwe Qwe Qwe");
		mockMvc.perform(post("/api/v1/cars").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void giveBymodel_returns200() throws Exception {
		CarGiveDto resp = createObjects.createCarGiveDto();

		UUID carId = resp.getId();

		when(carService.getById(carId)).thenReturn(resp);

		mockMvc.perform(get("/api/v1/cars/" + carId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(carId.toString()));
	}

	@Test
	void giveAllPageCars_returns200_andPage() throws Exception {
		CarGiveDto dto1 = createObjects.createCarGiveDto();
		CarGiveDto dto2 = createObjects.createCarGiveDto();

		List<CarGiveDto> lists = new ArrayList<CarGiveDto>();
		lists.add(dto1);
		lists.add(dto2);

		Page<CarGiveDto> page = new PageImpl<>(lists, PageRequest.of(0, 10), 2);

		when(carService.getAllPageFilterCar(any(Pageable.class), any(CarFilterRequestDto.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/cars").param("page", "0").param("size", "10").param("makeName", MODEL)
				.param("model", MODEL).param("minYear", "2015").param("maxYear", "2020").param("categoryName", MODEL))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.totalElements").value(2));
	}

	@Test
	void update_withoutToken_returns401() throws Exception {
		CarRequestDto req = createObjects.createCarRequestDto();

		UUID carId = UUID.randomUUID();

		mockMvc.perform(put("/api/v1/cars/" + carId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isUnauthorized());
	}

	@Test
	void update_withJwtWithoutScope_returns403() throws Exception {
		CarRequestDto req = createObjects.createCarRequestDto();
		CarGiveDto resp = createObjects.createCarGiveDto();

		UUID carId = resp.getId();

		when(carService.updateById(eq(carId), any(CarRequestDto.class))).thenReturn(resp);

		mockMvc.perform(
				put("/api/v1/cars/" + carId).with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:update")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isForbidden());
	}

	@Test
	void update_withJwtAndUpdateScope_returns200() throws Exception {
		CarRequestDto req = createObjects.createCarRequestDto();
		CarGiveDto resp = createObjects.createCarGiveDto();

		UUID carId = resp.getId();

		when(carService.updateById(eq(carId), any(CarRequestDto.class))).thenReturn(resp);

		mockMvc.perform(
				put("/api/v1/cars/" + carId).with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:update")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(carId.toString()))
				.andExpect(jsonPath("$.model").value(MODEL));
	}

	@Test
	void delete_withoutToken_returns401() throws Exception {
		UUID carId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/cars/" + carId)).andExpect(status().isUnauthorized());
	}

	@Test
	void delete_withJwtWithoutScope_returns403() throws Exception {
		UUID carId = UUID.randomUUID();

		mockMvc.perform(
				delete("/api/v1/cars/" + carId).with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:delete"))))
				.andExpect(status().isForbidden());
	}

	@Test
	void delete_withDeleteScope_returns204() throws Exception {
		UUID carId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/cars/" + carId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_cars:delete"))))
				.andExpect(status().isNoContent());
	}

	@Test
	void import_isOk() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv",
				("Model,Year,Make,Category\n" + "Camry,2020,Toyota,Sedan\n").getBytes());

		mockMvc.perform(multipart("/api/v1/cars/import/csv").file(file).with(jwt())).andExpect(status().isOk())
				.andExpect(content().string("CSV imported successfully"));

		verify(csvService).importCsv(any(MultipartFile.class), eq(Car.class), eq(CarImportDto.class));

	}

}
