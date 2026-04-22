package spring.controller.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.auxiliaryObjects.CreateObjects;
import spring.dto.CategoryGiveDto;
import spring.dto.CategoryRequestDto;
import spring.mapper.CategoryGiveMapper;
import spring.mapper.CategoryRequestMapper;
import spring.service.CategoryService;

@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CategoryService categoryService;

	private final CreateObjects createObjects = new CreateObjects();
	private static final String NAME = "Qweqweqweqwe";

	@Test
	void create_withoutToken_return_401() throws Exception {
		CategoryRequestDto dto = createObjects.createCategoryRequestDto();

		mockMvc.perform(post("/api/v1/categorys").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isUnauthorized());
	}

	@Test
	void create_withJwtWithoutScope_return_403() throws Exception {
		CategoryRequestDto dto = createObjects.createCategoryRequestDto();

		mockMvc.perform(post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:scope")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isForbidden());
	}

	@Test
	void create_withJwtAndCreateScope_return_201() throws Exception {
		CategoryRequestDto dto = createObjects.createCategoryRequestDto();
		CategoryGiveDto resp = createObjects.createCategoryGiveDto();

		when(categoryService.create(any(CategoryRequestDto.class))).thenReturn(resp);

		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value(NAME)).andExpect(jsonPath("$.id").exists());
	}

	@Test
	void create_whenCategoryNameInvalidTitle_return400() throws JsonProcessingException, Exception {
		CategoryGiveDto dtoReq = createObjects.createCategoryGiveDto();
		dtoReq.setName("qwe");
		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidSizeMax_return400() throws JsonProcessingException, Exception {
		CategoryGiveDto dtoReq = createObjects.createCategoryGiveDto();
		dtoReq.setName("Qweqweqweqweqweqweqweqweqwe");
		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidSizeMin_return400() throws JsonProcessingException, Exception {
		CategoryGiveDto dtoReq = createObjects.createCategoryGiveDto();
		dtoReq.setName("Qq");
		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidNumber_return400() throws JsonProcessingException, Exception {
		CategoryGiveDto dtoReq = createObjects.createCategoryGiveDto();
		dtoReq.setName("Qwe1");
		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenCategoryNameInvalidNumberWords_return400() throws JsonProcessingException, Exception {
		CategoryGiveDto dtoReq = createObjects.createCategoryGiveDto();
		dtoReq.setName("Qwe Qwe");
		mockMvc.perform(
				post("/api/v1/categorys").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:create")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void giveByName_returns200() throws Exception {
		CategoryGiveDto resp = createObjects.createCategoryGiveDto();

		UUID categoryId = resp.getId();

		when(categoryService.getById(categoryId)).thenReturn(resp);

		mockMvc.perform(get("/api/v1/categorys/" + categoryId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(categoryId.toString()));
	}

	@Test
	void giveAllPageCategorys_returns200_andPage() throws Exception {
		CategoryGiveDto dto1 = createObjects.createCategoryGiveDto();
		CategoryGiveDto dto2 = createObjects.createCategoryGiveDto();

		List<CategoryGiveDto> lists = new ArrayList<CategoryGiveDto>();
		lists.add(dto1);
		lists.add(dto2);

		Page<CategoryGiveDto> page = new PageImpl<>(lists, PageRequest.of(0, 10), 2);

		when(categoryService.getAllPageCategorys(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/categorys").param("page", "0").param("size", "10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2)).andExpect(jsonPath("$.content[0].name").value(NAME))
				.andExpect(jsonPath("$.content[1].name").value(NAME));
	}

	@Test
	void update_withoutToken_returns401() throws Exception {
		CategoryRequestDto req = createObjects.createCategoryRequestDto();

		UUID categoryId = UUID.randomUUID();

		mockMvc.perform(put("/api/v1/categorys/" + categoryId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isUnauthorized());
	}

	@Test
	void update_withJwtWithoutScope_returns403() throws Exception {
		CategoryRequestDto req = createObjects.createCategoryRequestDto();
		CategoryGiveDto resp = createObjects.createCategoryGiveDto();

		UUID categoryId = resp.getId();

		when(categoryService.update(eq(categoryId), any(CategoryRequestDto.class))).thenReturn(resp);

		mockMvc.perform(put("/api/v1/categorys/" + categoryId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:update")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isForbidden());
	}

	@Test
	void update_withJwtAndUpdateScope_returns200() throws Exception {
		CategoryRequestDto req = createObjects.createCategoryRequestDto();
		CategoryGiveDto resp = createObjects.createCategoryGiveDto();

		UUID categoryId = resp.getId();

		when(categoryService.update(eq(categoryId), any(CategoryRequestDto.class))).thenReturn(resp);

		mockMvc.perform(put("/api/v1/categorys/" + categoryId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:update")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(categoryId.toString()))
				.andExpect(jsonPath("$.name").value(NAME));
	}

	@Test
	void delete_withoutToken_returns401() throws Exception {
		UUID categoryId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/categorys/" + categoryId)).andExpect(status().isUnauthorized());
	}

	@Test
	void delete_withJwtWithoutScope_returns403() throws Exception {
		UUID categoryId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/categorys/" + categoryId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:delete"))))
				.andExpect(status().isForbidden());
	}

	@Test
	void delete_withDeleteScope_returns204() throws Exception {
		UUID categoryId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/categorys/" + categoryId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_categorys:delete"))))
				.andExpect(status().isNoContent());
	}

}
