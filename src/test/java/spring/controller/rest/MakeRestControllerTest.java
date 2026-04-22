package spring.controller.rest;

import static org.junit.jupiter.api.Assertions.*;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.auxiliaryObjects.CreateObjects;
import spring.dto.MakeGiveDto;
import spring.dto.MakeRequestDto;
import spring.service.MakeService;

@WebMvcTest(MakeRestController.class)
class MakeRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MakeService makeService;

	private final CreateObjects createObjects = new CreateObjects();
	private static final String NAME = "Qweqweqweqwe";

	@Test
	void create_withoutToken_return_401() throws Exception {
		MakeRequestDto dto = createObjects.createMakeRequestDto();

		mockMvc.perform(post("/api/v1/makes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isUnauthorized());
	}

	@Test
	void create_withJwtWithoutScope_return_403() throws Exception {
		MakeRequestDto dto = createObjects.createMakeRequestDto();

		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:scope")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isForbidden());
	}

	@Test
	void create_withJwtAndCreateScope_return_201() throws Exception {
		MakeRequestDto dto = createObjects.createMakeRequestDto();
		MakeGiveDto resp = createObjects.createMakeGiveDto();

		when(makeService.create(any(MakeRequestDto.class))).thenReturn(resp);

		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value(NAME)).andExpect(jsonPath("$.id").exists());
	}

	@Test
	void create_whenMakeNameInvalidTitle_return400() throws JsonProcessingException, Exception {
		MakeRequestDto dtoReq = createObjects.createMakeRequestDto();
		dtoReq.setName("qwe");
		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidSizeMax_return400() throws JsonProcessingException, Exception {
		MakeRequestDto dtoReq = createObjects.createMakeRequestDto();
		dtoReq.setName("qweqweqweqweqweqweqweqweqwe");
		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidSizeMin_return400() throws JsonProcessingException, Exception {
		MakeRequestDto dtoReq = createObjects.createMakeRequestDto();
		dtoReq.setName("Qq");
		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidNumber_return400() throws JsonProcessingException, Exception {
		MakeRequestDto dtoReq = createObjects.createMakeRequestDto();
		dtoReq.setName("Qwe1");
		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_whenMakeNameInvalidNumberWords_return400() throws JsonProcessingException, Exception {
		MakeRequestDto dtoReq = createObjects.createMakeRequestDto();
		dtoReq.setName("Qwe Qwe Qwe");
		mockMvc.perform(post("/api/v1/makes").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:create")))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtoReq)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void giveByName_returns200() throws Exception {
		MakeGiveDto resp = createObjects.createMakeGiveDto();

		UUID makeId = resp.getId();

		when(makeService.getById(makeId)).thenReturn(resp);

		mockMvc.perform(get("/api/v1/makes/" + makeId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(makeId.toString()));
	}

	@Test
	void giveAllPageMakes_returns200_andPage() throws Exception {
		MakeGiveDto dto1 = createObjects.createMakeGiveDto();
		MakeGiveDto dto2 = createObjects.createMakeGiveDto();

		List<MakeGiveDto> lists = new ArrayList<MakeGiveDto>();
		lists.add(dto1);
		lists.add(dto2);

		Page<MakeGiveDto> page = new PageImpl<>(lists, PageRequest.of(0, 10), 2);

		when(makeService.getAllPageMakes(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/makes").param("page", "0").param("size", "10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2)).andExpect(jsonPath("$.content[0].name").value(NAME))
				.andExpect(jsonPath("$.content[1].name").value(NAME));
	}

	@Test
	void update_withoutToken_returns401() throws Exception {
		MakeRequestDto req = createObjects.createMakeRequestDto();

		UUID makeId = UUID.randomUUID();

		mockMvc.perform(put("/api/v1/makes/" + makeId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isUnauthorized());
	}

	@Test
	void update_withJwtWithoutScope_returns403() throws Exception {
		MakeRequestDto req = createObjects.createMakeRequestDto();
		MakeGiveDto resp = createObjects.createMakeGiveDto();

		UUID makeId = resp.getId();

		when(makeService.update(eq(makeId), any(MakeRequestDto.class))).thenReturn(resp);

		mockMvc.perform(
				put("/api/v1/makes/" + makeId).with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:update")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isForbidden());
	}

	@Test
	void update_withJwtAndUpdateScope_returns200() throws Exception {
		MakeRequestDto req = createObjects.createMakeRequestDto();
		MakeGiveDto resp = createObjects.createMakeGiveDto();

		UUID makeId = resp.getId();

		when(makeService.update(eq(makeId), any(MakeRequestDto.class))).thenReturn(resp);

		mockMvc.perform(
				put("/api/v1/makes/" + makeId).with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:update")))
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(makeId.toString()))
				.andExpect(jsonPath("$.name").value(NAME));
	}

	@Test
	void delete_withoutToken_returns401() throws Exception {
		UUID makeId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/makes/" + makeId)).andExpect(status().isUnauthorized());
	}

	@Test
	void delete_withJwtWithoutScope_returns403() throws Exception {
		UUID makeId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/makes/" + makeId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_qwe:delete"))))
				.andExpect(status().isForbidden());
	}

	@Test
	void delete_withDeleteScope_returns204() throws Exception {
		UUID makeId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/makes/" + makeId)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_makes:delete"))))
				.andExpect(status().isNoContent());
	}

}
